package it.tim.dashboard.domain;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Consistenza {

    String numLinea;
    String sourceSystem;
    String tiidOwner;
    StatoLinea statoLinea;
    List<Offertum> offerta;
}
