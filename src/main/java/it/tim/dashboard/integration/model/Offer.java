package it.tim.dashboard.integration.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Offer {
	private String offerRestartable;
	private String fixedLine;
	private String offerPaymentInUse;
	private String fT_FlagAggregation;
	private String offerPaymentConfigured;
	private String offerActivationSubsys;
	private String initiativeCode;
	private Discount discount;
	private String balanceInfo;
	private Section section;
	private String flagCSRec;
	private long accumulatedDiscount;
	private String speed;
	private String fT_FlagMusic;
	private String offerType;
	private String gtf;
	private String fT_FlagChat;
	private String sellingType;
	private String offerLastRestartDateRequest;
	private String partnership;
	private String offerState;
	private String offerActivationDate;
	private String offerRestarted;
	private String rAC;
	private String offerCode;
	private String prodId;
	private String fT_FlagFriends;
	private String limitDate;
	private String fT_FlagSocial;

}