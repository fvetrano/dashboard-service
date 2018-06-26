package it.tim.dashboard.integration.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Psoffer {
	private String allowedPaymentMode;
	private Partnerships partnerships;
	private String offerCodeActivable;
	private String checkResult;

}