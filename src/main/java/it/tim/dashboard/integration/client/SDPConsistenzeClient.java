package it.tim.dashboard.integration.client;

import it.tim.dashboard.domain.Consistenza;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "Consistenze",
        url = "${integration.sdp.consistenzebasepath}",
        fallbackFactory = SDPConsistenzeClient.FallBackFactory.class

)
public interface SDPConsistenzeClient {

    @GetMapping("/clienti/{rifCliente}/consistenze")
    ResponseEntity<List<Consistenza>> getConsistenze(@PathVariable(name = "rifCliente") String rifCliente);


    @Component
    @Slf4j
    class FallBackFactory implements feign.hystrix.FallbackFactory<SDPConsistenzeClient> {

        @Override

        public SDPConsistenzeClient create(Throwable error) {

            return rifCliente -> {
                log.error("unable to perform request", error);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            };
        }
    }



}


