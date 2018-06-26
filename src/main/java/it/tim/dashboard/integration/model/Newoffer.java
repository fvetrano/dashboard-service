package it.tim.dashboard.integration.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Newoffer {
	private String allowedPaymentMode;
	private String sellingType;
	private String offerCodeActivable;
	private String sellingEndDate;
	private String sellingStartDate;
	private String flagCSRec;

}