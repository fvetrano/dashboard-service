package it.tim.dashboard.integration.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Section {
	private String sectionCycleCounter;
	private String quantity;
	private String rolloverNumber;
	private long initBasketValue;
	private String maxRolloverNumber;
	private String fT_FlagAggregation;
	private String renewalType;
	private String usageStartDate;
	private String maxBundleValue;
	private String sectionId;
	private String fT_NoLimitBasket;
	private String usageEndDate;
	private String fT_BasketType;
	private String fT_NextUsageDate;
	private String basketType;
	private String fT_StartErosionValue;
	private String usageEndSubPeriodDate;
	private String lastSyncDateBasket;
	private String fT_Direttrice;
	private String fT_StopAccumulationValue;
	private String fT_AggregationType;
	private String fT_TimeInterval;
	private String usageStartSubPeriodDate;
	private String fT_BasketConsumeType;


}