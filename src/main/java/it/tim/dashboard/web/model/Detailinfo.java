package it.tim.dashboard.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(Include.NON_EMPTY)
public class Detailinfo {
	
	@ApiModelProperty(notes = "Tipo di dettaglio" , example = "LOV: DATI|SMS|VOCE", required=true, position = 1)
	private String type;
	
	@ApiModelProperty(notes = "Descrizione del costo del tipo di consumo" , example = "28 cent", required=true, position = 1)
	private String value;
	
	@ApiModelProperty(notes = "Immagine da visualizzare" , example = "http://.../letter.jpeg", position = 8)
	private String imageLink;
	

}
