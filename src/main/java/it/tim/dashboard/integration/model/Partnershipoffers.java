package it.tim.dashboard.integration.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Partnershipoffers {
	private String fixedLine;
	private List<Psoffer> psoffer;

}