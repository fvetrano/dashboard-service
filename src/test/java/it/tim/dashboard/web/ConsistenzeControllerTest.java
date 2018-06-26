package it.tim.dashboard.web;


import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;

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

import it.tim.dashboard.domain.Consistenza;
import it.tim.dashboard.domain.Offertum;
import it.tim.dashboard.domain.StatoLinea;
import it.tim.dashboard.integration.client.SDPConsistenzeClient;
import it.tim.dashboard.web.interceptor.TIMContext;
import it.tim.dashboard.web.interceptor.TIMHeadersInputInterceptor;

@RunWith(SpringRunner.class)
@WebMvcTest({ConsistenzeController.class,TIMHeadersInputInterceptor.class})
public class ConsistenzeControllerTest {

    @MockBean
    SDPConsistenzeClient client;

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
        when(client.getConsistenze(anyString())).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        ResultActions response = mockMvc.perform(get("/api/consistenze/"));
        response.andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void whenJWTSessionIsMalformed400IsReturned() throws Exception{
        when(client.getConsistenze(anyString())).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        ResultActions response = mockMvc.perform(get("/api/consistenze/").header("sessionJWT","amalformedtoken"));
        response.andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void whenSDPClientReturnNon2xxStatus500IsReturned() throws Exception{
        when(client.getConsistenze(anyString())).thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        ResultActions response = mockMvc.perform(get("/api/consistenze/")
                .header("sessionJWT",sessionJWT));
        response.andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @Test
    public void whenAmbitoIsNotPresent() throws Exception{

        Consistenza consistenza = Consistenza.builder()
                .numLinea("123")
                .tiidOwner("123")
                .sourceSystem("MSS")
                .statoLinea(StatoLinea.builder().descrizione("ATTIVO").build())
                .build();

        when(client.getConsistenze(anyString())).thenReturn(ResponseEntity.ok(Collections.singletonList(consistenza)));

        ResultActions response = mockMvc.perform(get("/api/consistenze/").header("sessionJWT",sessionJWT));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.tiid").value("123"))
                .andExpect(jsonPath("$.sim[0].msisdn").value("123"))
                .andExpect(jsonPath("$.sim[0].ambito").value("N.A."))
                .andExpect(jsonPath("$.sim[0].type").value("PP"));

    }

    @Test
    public void whenConsistenzeAreReturnedFromSDPClientResponseIsCorrect() throws Exception{

        StatoLinea statoLinea = StatoLinea.builder().descrizione("ATTIVO").build();

        Consistenza consistenza = Consistenza.builder()
                .numLinea("123")
                .tiidOwner("123")
                .sourceSystem("MSS")
                .statoLinea(statoLinea)
                .offerta(Arrays.asList(Offertum.builder().ambito("Fisso").build(),
                        Offertum.builder().ambito("Mobile").build()))
                .build();

        when(client.getConsistenze(anyString())).thenReturn(ResponseEntity.ok(Collections.singletonList(consistenza)));

        ResultActions response = mockMvc.perform(get("/api/consistenze/").header("sessionJWT",sessionJWT));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.tiid").value("123"))
                .andExpect(jsonPath("$.sim[0].msisdn").value("123"))
                .andExpect(jsonPath("$.sim[0].ambito").value("Fisso"))
                .andExpect(jsonPath("$.sim[0].type").value("PP"));

    }


    @Test
    public void whenConsistenzeAreReturnedFromSDPThenOnlyActiveLinesAreInClientResponse() throws Exception{

        StatoLinea activeLine = StatoLinea.builder().descrizione("ATTIVO").build();
        StatoLinea notActiveLine = StatoLinea.builder().descrizione("anythingelse").build();

        Consistenza consistenzaActive = Consistenza.builder()
                .numLinea("123")
                .tiidOwner("123")
                .sourceSystem("MSS")
                .statoLinea(activeLine)
                .build();

        Consistenza consistenzaNotActive = Consistenza.builder()
                .numLinea("456")
                .tiidOwner("123")
                .sourceSystem("MSS")
                .statoLinea(notActiveLine)
                .build();

        when(client.getConsistenze(anyString())).thenReturn(ResponseEntity.ok(Arrays.asList(consistenzaActive, consistenzaNotActive)));

        ResultActions response = mockMvc.perform(get("/api/consistenze/").header("sessionJWT",sessionJWT));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.tiid").value("123"))
                .andExpect(jsonPath("$.sim", hasSize(1)))
                .andExpect(jsonPath("$.sim[0].msisdn").value("123"))
                .andExpect(jsonPath("$.sim[0].type").value("PP"));

    }

    @Test
    public void whenConsistenzeAreReturnedWithNoActiveLinesFromSDPThenInClientResponseIsEmpty() throws Exception{

        Consistenza consistenzaActive = Consistenza.builder()
                .numLinea("123")
                .sourceSystem("MSS")
                .statoLinea(null)
                .build();

        when(client.getConsistenze(anyString())).thenReturn(ResponseEntity.ok(Arrays.asList(consistenzaActive)));

        ResultActions response = mockMvc.perform(get("/api/consistenze/").header("sessionJWT",sessionJWT));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.sim", hasSize(0)));

    }

    @Test
    public void whenConsistenzeAreReturnedWithActiveLinesAndEmptyDescriptionFromSDPThenInClientResponseIsEmpty() throws Exception{

        Consistenza consistenzaActive = Consistenza.builder()
                .numLinea("123")
                .sourceSystem("MSS")
                .statoLinea(StatoLinea.builder().descrizione("").build())
                .build();

        when(client.getConsistenze(anyString())).thenReturn(ResponseEntity.ok(Arrays.asList(consistenzaActive)));

        ResultActions response = mockMvc.perform(get("/api/consistenze/").header("sessionJWT",sessionJWT));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.sim", hasSize(0)));

    }

    @Test
    public void whenConsistenzeAreReturnedWithActiveLinesAndNullDescriptionFromSDPThenInClientResponseIsEmpty() throws Exception{

        Consistenza consistenzaActive = Consistenza.builder()
                .numLinea("123")
                .sourceSystem("MSS")
                .statoLinea(StatoLinea.builder().descrizione(null).build())
                .build();

        when(client.getConsistenze(anyString())).thenReturn(ResponseEntity.ok(Arrays.asList(consistenzaActive)));

        ResultActions response = mockMvc.perform(get("/api/consistenze/").header("sessionJWT",sessionJWT));

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.sim", hasSize(0)));

    }

}