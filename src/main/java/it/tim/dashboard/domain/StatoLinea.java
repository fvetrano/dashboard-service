package it.tim.dashboard.domain;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StatoLinea {

    public static final String LINE_ACTIVE = "ATTIVO";

    String descrizione;

}
