package it.tim.dashboard.integration.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Discount {
	private List<StepElement> stepElement;
	private long finalCost;

	
}