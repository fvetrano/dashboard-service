package it.tim.dashboard.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Credit {

    private Double credito;
    private Double bonus1;
    private String dataAggiornamento;

}
