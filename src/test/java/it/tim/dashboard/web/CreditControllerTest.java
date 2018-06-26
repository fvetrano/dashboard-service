package it.tim.dashboard.web;

import it.tim.dashboard.domain.Credit;
import it.tim.dashboard.integration.client.SDPClientiClient;
import it.tim.dashboard.web.interceptor.TIMContext;
import it.tim.dashboard.web.interceptor.TIMHeadersInputInterceptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest({CreditoController.class,TIMHeadersInputInterceptor.class})
public class CreditControllerTest {

    @MockBean
    SDPClientiClient client;

    @MockBean
    TIMContext timContext;

    @Autowired
    MockMvc mockMvc;

    final static String sessionJWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImlzcyI6Imh0dHBzOi8vZHQtcy1hcGlndzAxLnRlbGV" +
            "jb21pdGFsaWEubG9jYWw6ODQ0MyJ9.ew0KCSJ1c2VyQWNjb3VudCI6ICJwcmltZTFAdGltLml0IiwNCgkiY2ZfcGl2YSI6ICJa" +
            "Sk9SSkE2MkQyMUwyMTlQIiwNCgkiZGNhQ29vY2tpZSI6ICJaamRqWldRNFlXVXRORFV3TVMwME1HTmxMV0psWkdNdFlURXhabVZt" +
            "T1RWbFpESm1YMTlmUkVOQlZWUklYMEZWVkVoZlEwOVBTMGxGWDE5ZkxuUnBiUzVwZEE9PSIsDQoJImFjY291bnRUeXBlIjogIkFDQ0" +
            "9VTlRfVU5JQ08iDQp9.t0dJFeFFF5v2FHZkI7y2ALqg4iAGav2_XSqFYzIFpOk";

    @Test
    public void whenJWTSessionTokenIsMissing400IsReturned() throws Exception{
        when(client.getCredito(anyString(),anyString())).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        ResultActions response = mockMvc.perform(get("/api/credito/123"));
        response.andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void whenJWTSessionIsMalformed400IsReturned() throws Exception{
        when(client.getCredito(anyString(),anyString())).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        ResultActions response = mockMvc.perform(get("/api/credito/123").header("sessionJWT","amalformedtoken"));
        response.andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void whenSDPClientReturnNon2xxStatus500IsReturned() throws Exception{


        when(client.getCredito(anyString(),anyString())).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        ResultActions response = mockMvc.perform(get("/api/credito/3400000001")
                .header("sessionJWT",sessionJWT));
        response.andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @Test
    public void whenCreditoIsReturnedFromSDPClientResponseIsCorret() throws Exception{

        Credit sdpCredit = Credit.builder()
                .credito(10.0)
                .bonus1(9.9)
                .dataAggiornamento("2020-12-10T23:59:59-04:00Z")
                .build();

        when(client.getCredito(anyString(),anyString())).thenReturn(ResponseEntity.ok(sdpCredit));

        ResultActions response = mockMvc.perform(get("/api/credito/3400000001").header("sessionJWT",sessionJWT));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.credito").value("10,00"))
                .andExpect(jsonPath("$.bonus1").value("9,90"))
                .andExpect(jsonPath("$.dataUltimoAggiornamento").value("2020-12-10T23:59:59-04:00Z"));



    }

    @Test
    public void whenWrongLineIsPassedResponseIsBadRequest() throws Exception{

        ResultActions response = mockMvc.perform(get("/api/credito/123").header("sessionJWT",sessionJWT));

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.errorCode").value("RIC002"))
                .andExpect(jsonPath("$.errorMessage").value("Parametri della richiesta mancanti o errati"));



    }

}