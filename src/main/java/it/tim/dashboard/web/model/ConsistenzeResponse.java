package it.tim.dashboard.web.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsistenzeResponse {

    private String status;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String tiid;
    private List<Sim> sim;

    @Data
    @Builder
    public static class Sim{
        String msisdn;
        String type;
        String ambito;
    }
}
