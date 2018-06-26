package it.tim.dashboard.integration.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SDPOffersResponse {
	private Opscdata opscdata;
	private Activeoffers activeoffers;
	private Activableoffers activableoffers;
	private Partnershipoffers partnershipoffers;
	private PsDetailArray psDetailArray;
	private Multideviceoffers multideviceoffers;
	private String result;
	private String subsystem;
	private String operationType;
	private String resultDescription;

}