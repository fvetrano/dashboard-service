package it.tim.dashboard.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.tim.dashboard.aspects.Loggable;
import it.tim.dashboard.domain.Credit;
import it.tim.dashboard.integration.client.SDPClientiClient;
import it.tim.dashboard.transformer.CreditoTransformer;
import it.tim.dashboard.web.interceptor.TIMContext;
import it.tim.dashboard.web.model.CreditoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
@Slf4j
public class CreditoController {

    private static final Pattern PHONE_NUMBER = Pattern.compile("^3[0-9]{8,10}");

    private final SDPClientiClient sdpClientiClient;
    private final TIMContext timContext;

    @Autowired
    public CreditoController(SDPClientiClient sdpClientiClient, TIMContext timContext) {
        this.sdpClientiClient = sdpClientiClient;
        this.timContext = timContext;
    }

    @Loggable
    @SuppressWarnings("unused")
    @RequestMapping(method = RequestMethod.GET, value = "/credito/{numLinea}")
    @ApiOperation(value = "Retrieve the credit amount fot a given line", response = CreditoResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Generic Error"),
            @ApiResponse(code = 400, message = "Bad Input Parameters")
    })
    public ResponseEntity getCredito(@PathVariable String numLinea,
    		@RequestHeader(value = "businessID", required = false) String xBusinessId,    		
			@RequestHeader(value = "messageID", required = false) String xMessageID,    		
			@RequestHeader(value = "transactionID", required = false) String xTransactionID,    		
			@RequestHeader(value = "channel", required = false) String xChannel,    		
			@RequestHeader(value = "sourceSystem", required = false) String xSourceSystem,    		
			@RequestHeader(value = "sessionID", required = false) String xSessionID
    		)
    {
        if(!PHONE_NUMBER.matcher(numLinea).matches()) {
            Map<String, String> body = new HashMap<>();
            body.put("errorCode","RIC002");
            body.put("errorMessage","Parametri della richiesta mancanti o errati");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        ResponseEntity<Credit> response = sdpClientiClient.getCredito(timContext.getCfPiva(), numLinea);
        log.debug("sdp getCredito response status code: %s", response.getStatusCodeValue());

        if (!response.getStatusCode().is2xxSuccessful()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Credit credit = response.getBody();
        log.debug("sdp getCredito response body: %s", credit);

        return ResponseEntity.ok(CreditoTransformer.transform(credit,numLinea));


    }


}
