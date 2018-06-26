package it.tim.dashboard.integration.client;

import it.tim.dashboard.integration.model.SDPOffersResponse;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;
import org.springframework.http.HttpHeaders;
public class SDPOffersClientTest {



    @Test
    public void FallBackFactoryShouldReturnFallBackClassInstance(){
    	SDPOffersClient fallback = new SDPOffersClient.FallBackFactory().create(new RuntimeException());
        assertNotNull(fallback);
        assertTrue(fallback instanceof SDPOffersClient);
    }

    @Test
    public void GetCreditoFallBackMethodShouldReturnInternalServerError(){
    	SDPOffersClient fallback = new SDPOffersClient.FallBackFactory().create(new RuntimeException());
        ResponseEntity<SDPOffersResponse> response = fallback.getOffers("any", "any", "any", "any", new HttpHeaders());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }


}