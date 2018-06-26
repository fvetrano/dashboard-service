package it.tim.dashboard.integration.client;

import it.tim.dashboard.domain.Credit;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;

public class SDPClientiClientTest {



    @Test
    public void FallBackFactoryShouldReturnFallBackClassInstance(){
        SDPClientiClient fallback = new SDPClientiClient.FallBackFactory().create(new RuntimeException());
        assertNotNull(fallback);
        assertTrue(fallback instanceof SDPClientiClient);
    }

    @Test
    public void GetCreditoFallBackMethodShouldReturnInternalServerError(){
        SDPClientiClient fallback = new SDPClientiClient.FallBackFactory().create(new RuntimeException());
        ResponseEntity<Credit> response = fallback.getCredito("any", "any");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }


}