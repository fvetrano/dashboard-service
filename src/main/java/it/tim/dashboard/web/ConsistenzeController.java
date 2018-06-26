package it.tim.dashboard.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.tim.dashboard.aspects.Loggable;
import it.tim.dashboard.domain.Consistenza;
import it.tim.dashboard.domain.StatoLinea;
import it.tim.dashboard.integration.client.SDPConsistenzeClient;
import it.tim.dashboard.transformer.ConsistenzeTransformer;
import it.tim.dashboard.web.interceptor.TIMContext;
import it.tim.dashboard.web.model.ConsistenzeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Slf4j
public class ConsistenzeController {

    private final SDPConsistenzeClient sdpConsistenzeClient;
    private final TIMContext timContext;

    @Autowired
    public ConsistenzeController(SDPConsistenzeClient sdpConsistenzeClient, TIMContext timContext) {
        this.sdpConsistenzeClient = sdpConsistenzeClient;
        this.timContext = timContext;
    }

    @SuppressWarnings("unused")
    @ApiOperation(value = "Retrieve the consistencies", response = ConsistenzeResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Generic Error"),
            @ApiResponse(code = 400, message = "Bad Input Parameters")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/consistenze")
    @Loggable
    public ResponseEntity<ConsistenzeResponse> getConsistenze(
    		@RequestHeader(value = "businessID", required = false) String xBusinessId,    		
			@RequestHeader(value = "messageID", required = false) String xMessageID,    		
			@RequestHeader(value = "transactionID", required = false) String xTransactionID,    		
			@RequestHeader(value = "channel", required = false) String xChannel,    		
			@RequestHeader(value = "sourceSystem", required = false) String xSourceSystem,    		
			@RequestHeader(value = "sessionID", required = false) String xSessionID){
        ResponseEntity<List<Consistenza>> response = sdpConsistenzeClient.getConsistenze(timContext.getCfPiva());      
        
        log.debug("sdp getConsistenze response status code: "+ response.getStatusCodeValue());

        if (!response.getStatusCode().is2xxSuccessful()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        List<Consistenza> consistenze = response.getBody();

        log.debug("sdp getConsistenze response body: "+ consistenze.stream().map(Consistenza::toString).collect(Collectors.joining(",", "{", "}")));
        
        List<Consistenza> consistenzeAttive = consistenze.stream().filter(c -> 
        			c.getStatoLinea() != null && ! StringUtils.isEmpty(c.getStatoLinea().getDescrizione())
        		&& 	c.getStatoLinea().getDescrizione().equalsIgnoreCase(StatoLinea.LINE_ACTIVE)).collect(Collectors.toList());

        log.debug("sdp filter consistenze: "+ consistenzeAttive.stream().map(Consistenza::toString).collect(Collectors.joining(",", "{", "}")));

        ConsistenzeResponse obtainedConsistencies = ConsistenzeTransformer.transform(consistenzeAttive);

        return ResponseEntity.ok(obtainedConsistencies);

    }



}
