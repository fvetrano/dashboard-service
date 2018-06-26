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
public class Action {
	
	@ApiModelProperty(notes = "URL della funzionalità da richiamare" , example = "/internal/lamialinea", position = 1)
	private String path;
    
	@ApiModelProperty(notes = "Eventuale label da visualizzare" , position = 2)
	private String label;
}
