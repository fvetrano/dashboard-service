package it.tim.dashboard.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Aggregateoffer {
	
	@ApiModelProperty(notes = "Tipo di Bundle" , example = "LOV: DATI|SMS|VOCE", required=true, position = 1)
	private String type;

	@ApiModelProperty(notes = "Valore eroso dal Bundle" , example = "5.4", position = 2)
	private String value;
    
	@ApiModelProperty(notes = "Valore totale del Bundle" , example = "10", position = 3)
	private String total;
    
	@ApiModelProperty(notes = "Misura del Bundle" , example = "LOV: MIN|SMS|GB", position = 4)
	private String measure;
    
	@ApiModelProperty(notes = "Percentuale di erosione del Bundle" , example = "18", position = 5)
	private long percent;
    
	@ApiModelProperty(notes = "Percentuale minima di erosione sotto la quale ingrigire le torte" , example = "5", position = 6)
	private long min_percent;

	@ApiModelProperty(notes = "Indica se il Bundle è illimitato" , example = "false", position = 7)
	private boolean unlimited;
    
	@ApiModelProperty(notes = "Immagine da visualizzare" , example = "http://.../cornetta.jpeg", position = 8)
	private String imageLink;
	
	
	@JsonIgnore
    private String mindescription;
	
}
