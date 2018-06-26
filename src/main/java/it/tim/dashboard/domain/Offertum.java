
package it.tim.dashboard.domain;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Offertum {

    String id;
    String subtype;
    String name;
    String offerId;
    String offerIdWeb;
    String ambito;
    String dataAttivazione;
    String dataCessazione;
    String statoCommerciale;
    String tipoPagamento;
    String metodoPagamento;
    String codiceContratto;
    List<ServizioAbilitante> servizioAbilitante;

}
