package it.tim.dashboard.web.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(Include.NON_NULL)
public class OfferResponse {

	@ApiModelProperty(notes = "Outcome Result" , example = "OK", required = true, position = 1)
	private String status;
	
	@ApiModelProperty(notes = "Tipo di risposta. LOV: AGGREGATO|OFFERTA|PROFILO|OFFERTA_SOSPESA|SOSPESA_IN_CONSISTENZA" , example = "AGGREGATO", required = true, position = 2)
	private String type;
	
	@ApiModelProperty(notes = "Titolo della vista aggregata" , example = "Panoramica", required = true, position = 3)
	private String title;
	
	@ApiModelProperty(notes = "Descrizione della vista aggregata" , example = "Hai a disposizione .... ", required = true, position = 4)
	private String description;
	
	@ApiModelProperty(notes = "Numero di giorni dal rinnovo" , example = "10", required = true, position = 5)
	private String scadenzaGG;
	
	@ApiModelProperty(notes = "Descrizione dettagliata. Presente solo nei casi in cui non sono presenti aggregateoffers o detailinfo" , example = "10", required = false, position = 6)
	private String detailDescription;
	
	@ApiModelProperty(notes = "Action associata al tap sulla view" , required = false, position = 7)
	private Action action;

	@ApiModelProperty(notes = "Informazioni relative ai bundle aggregati" , required = false, position = 8)
	private List<Aggregateoffer> aggregateoffers;

	@ApiModelProperty(notes = "Informazioni relative al profilo/offerta singola" , required = false, position = 9)
	private List<Detailinfo> detailinfo;
	
	@ApiModelProperty(notes = "CardAction associata ad un bottone" , required = false, position = 10)
	private CardAction cardAction;

}
