package it.tim.dashboard.integration.client;

import it.tim.dashboard.domain.Credit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "Clienti",
        url = "${integration.sdp.clientibasepath}",
        fallbackFactory = SDPClientiClient.FallBackFactory.class

)
public interface SDPClientiClient {

    @GetMapping("/clienti/{rifCliente}/consistenze/{numLinea}/credito")
    ResponseEntity<Credit> getCredito(@PathVariable(name = "rifCliente") String rifCliente, @PathVariable(name = "numLinea") String numLinea);


    @Component
    @Slf4j
    class FallBackFactory implements feign.hystrix.FallbackFactory<SDPClientiClient> {

        @Override

        public SDPClientiClient create(Throwable error) {

            return (rifCliente, numLinea) -> {
                log.error("unable to perform request", error);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            };
        }
    }



}


