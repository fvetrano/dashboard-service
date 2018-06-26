package it.tim.dashboard.transformer;

import it.tim.dashboard.domain.Consistenza;
import it.tim.dashboard.domain.Offertum;
import it.tim.dashboard.web.model.ConsistenzeResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

@Slf4j
public class ConsistenzeTransformer {

    private ConsistenzeTransformer() {
        //empty constructor
    }

    public enum SimType{

        PP("PP"),
        ABB("ABB"),
        UNKNOWN("unknown");

        private final String value;

        SimType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static ConsistenzeResponse transform(List<Consistenza> consistenze){

        List<ConsistenzeResponse.Sim> sims = new ArrayList<>();
        
        String tiidOwner = null;
        for (Consistenza consistenza : consistenze){

        	if(consistenza.getTiidOwner()!=null && !consistenza.getTiidOwner().isEmpty())
        			tiidOwner = consistenza.getTiidOwner();
        	
        	log.debug("tiidOwner -----> "+tiidOwner);
        	
            Optional<Offertum> first = consistenza.getOfferta() != null ?
                    consistenza.getOfferta().stream().findFirst()
                    : Optional.empty();

            sims.add(ConsistenzeResponse.Sim.builder()
                    .msisdn(consistenza.getNumLinea())
                    .type(getSimType(consistenza.getSourceSystem()).value)
                    .ambito(first.isPresent() ? first.get().getAmbito() : "N.A.")
                    .build());
        }

        return ConsistenzeResponse.builder()
                .status("OK")
                .tiid(tiidOwner)
                .sim(sims)
                .build();
    }

    private static SimType getSimType(String sourceSystem){

        if (isNotEmpty(sourceSystem)){
            if (sourceSystem.equalsIgnoreCase("MSP")) return SimType.PP;
            if (sourceSystem.equalsIgnoreCase("MSC")) return SimType.ABB;
            if (sourceSystem.equalsIgnoreCase("MSS")) return SimType.PP;
        }

        return SimType.UNKNOWN;

    }



}
