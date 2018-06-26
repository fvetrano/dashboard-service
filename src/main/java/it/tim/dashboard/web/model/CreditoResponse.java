package it.tim.dashboard.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditoResponse {

    private String status;
    private String numLinea;
    private String credito;
    private String bonus1;
    private String dataUltimoAggiornamento;

}
