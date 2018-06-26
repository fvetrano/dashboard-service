package it.tim.dashboard.integration.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import it.tim.dashboard.integration.model.SDPOffersResponse;
import lombok.extern.slf4j.Slf4j;

@FeignClient(
        name = "Offerte",
        url = "${integration.sdp.offersbasepath}", //Come definrlo?
        fallbackFactory = SDPOffersClient.FallBackFactory.class

)
public interface SDPOffersClient {

    @GetMapping("/clienti/{rifCliente}/consistenze/{numLinea}/dashboard")
    ResponseEntity<SDPOffersResponse> getOffers(@PathVariable(name = "rifCliente") String rifCliente,
    		@PathVariable(name = "numLinea") String numLinea,
    		@RequestParam(value= "operazione") String operazione,
    		@RequestParam(value= "subSystem") String subSystem,
    		@RequestHeader HttpHeaders headers);


    @Component
    @Slf4j
    class FallBackFactory implements feign.hystrix.FallbackFactory<SDPOffersClient> {

        @Override

        public SDPOffersClient create(Throwable error) {

            return (rifCliente, numLinea, operazione, subSystem, headers) -> {
                log.error("unable to perform request", error);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            };
        }
    }



}


