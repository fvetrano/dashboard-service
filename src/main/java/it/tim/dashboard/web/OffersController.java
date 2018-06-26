package it.tim.dashboard.web;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.tim.dashboard.aspects.Loggable;
import it.tim.dashboard.exception.BadRequestException;
import it.tim.dashboard.exception.NotAuthorizedException;
import it.tim.dashboard.exception.ServiceException;
import it.tim.dashboard.service.OffersService;
import it.tim.dashboard.web.interceptor.TIMContext;
import it.tim.dashboard.web.model.OfferResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class OffersController {

    private static final Pattern PHONE_NUMBER = Pattern.compile("^3[0-9]{8,10}");

   
    private final TIMContext timContext;
    private OffersService offerService;

    @Autowired
    public OffersController(OffersService offerService, TIMContext timContext) {
        this.offerService = offerService;
        this.timContext = timContext;
        
    }

    @Loggable
    @SuppressWarnings("unused")
    @RequestMapping(method = RequestMethod.GET, value = "/consistenze/{numLinea}/offerte/aggregato")
    @ApiOperation(value = "Retrieve active offers fot a given line and ", response = OfferResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Generic Error"),
            @ApiResponse(code = 400, message = "Bad Input Parameters")
    })
    public ResponseEntity getOffers(@PathVariable String numLinea, 
    		                        @RequestHeader HttpHeaders headers,
    		                        @RequestHeader(value = "businessID", required = false) String xBusinessId,    		
    		            			@RequestHeader(value = "messageID", required = false) String xMessageID,    		
    		            			@RequestHeader(value = "transactionID", required = false) String xTransactionID,    		
    		            			@RequestHeader(value = "channel", required = false) String xChannel,    		
    		            			@RequestHeader(value = "sourceSystem", required = false) String xSourceSystem,    		
    		            			@RequestHeader(value = "sessionID", required = false) String xSessionID
    		            			) throws ServiceException, NotAuthorizedException, BadRequestException {
        
    	if(!PHONE_NUMBER.matcher(numLinea).matches()) {
            Map<String, String> body = new HashMap<>();
            body.put("errorCode","RIC002");
            body.put("errorMessage","Parametri della richiesta mancanti o errati");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
        }

        
        
    	ResponseEntity<OfferResponse> response = offerService.getOffers(timContext.getCfPiva(), numLinea, headers);
        log.debug("service getOffers response status code: %s", response.getStatusCodeValue());

        if (!response.getStatusCode().is2xxSuccessful()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        OfferResponse offer= response.getBody();
        log.debug("body: %s", offer);

        return ResponseEntity.ok(offer);


    }


}
