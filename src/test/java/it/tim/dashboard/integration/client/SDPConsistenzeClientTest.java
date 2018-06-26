package it.tim.dashboard.integration.client;

import it.tim.dashboard.domain.Consistenza;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.*;

public class SDPConsistenzeClientTest {

    @Test
    public void FallBackFactoryShouldReturnFallBackClassInstance(){
        SDPConsistenzeClient fallback = new SDPConsistenzeClient.FallBackFactory().create(new RuntimeException());
        assertNotNull(fallback);
        assertTrue(fallback instanceof SDPConsistenzeClient);
    }

    @Test
    public void GetCreditoFallBackMethodShouldReturnInternalServerError(){
        SDPConsistenzeClient fallback = new SDPConsistenzeClient.FallBackFactory().create(new RuntimeException());
        ResponseEntity<List<Consistenza>> response = fallback.getConsistenze("any");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

}