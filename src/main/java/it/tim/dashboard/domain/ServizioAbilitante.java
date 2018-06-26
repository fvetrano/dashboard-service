
package it.tim.dashboard.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ServizioAbilitante {

    String id;
    String subtype;
    String name;
    String ambito;
    String statoCommerciale;
    String tipoPagamento;
    String metodoPagamento;
    String dataAttivazione;
    String dataCessazione;
    String dataPrimaAttivazione;
    Double creditoResiduo;
    String codiceCOS;
    String codiceCOW;
    String codiceCOR;
    String codiceControllo;
    String codiceOLO;


}
