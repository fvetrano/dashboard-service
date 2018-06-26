package it.tim.dashboard.integration.proxy;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import it.tim.dashboard.integration.client.SDPOffersClient;
import it.tim.dashboard.integration.model.SDPOffersResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
class SDPProxyOffersDelegate {

	private SDPOffersClient sdpOffersClient;

	@Autowired
	SDPProxyOffersDelegate(SDPOffersClient sdpOffersClient) {
		this.sdpOffersClient = sdpOffersClient;
	}

	@HystrixCommand(fallbackMethod = "reliableOfferInfo")
	ResponseEntity<SDPOffersResponse> getOffers(String rifCliente, String numLinea, HttpHeaders headers) {
		log.debug("SDPProxyDelegate.getOffers (" + rifCliente + ")");
		updateHeaders(headers);
		return sdpOffersClient.getOffers(rifCliente, numLinea, "FTLight", "VAS_916WEB", headers);
	}
	
	  //FALLBACK
    @SuppressWarnings("unused")
    ResponseEntity<SDPOffersResponse> reliableOfferInfo(String rifCliente, String numLinea, HttpHeaders headers, Throwable throwable) {
        return ProxyTemplate.getFallbackResponse(throwable);
    }
    
    private void updateHeaders(HttpHeaders headers) {
    	
    	Date now = new Date(System.currentTimeMillis());
    	
    	headers.set("MessageID", generateIdHexadecimal(24));
    	headers.set("SourceSystem", "CBE");
    	headers.set("interactionDate-Date", getDate(now));
    	headers.set("interactionDate-Time", getTime(now));

    	headers.remove("sessionjwt");

    	log.debug("OUTPUT HEADERS: " + headers);
    }
    
    
    
	private static String getTime(Date d){
		SimpleDateFormat sdfTime = new SimpleDateFormat ( "HH:mm:ss.SSS" );
		return sdfTime.format(d);
	}

	private static String getDate(Date d){
		SimpleDateFormat sdfDate = new SimpleDateFormat ( "yyyy-MM-dd" );
		return sdfDate.format(d);
	}

	
    public static String generateIdHexadecimal(int numchars) {
    	
    	Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }

    	
    	return sb.toString().substring(0, numchars);
    }
    
}