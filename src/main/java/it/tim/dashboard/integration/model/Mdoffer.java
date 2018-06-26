package it.tim.dashboard.integration.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Mdoffer {

	private List<MdSlaveList> mdSlaveList;
	private String mdMaster;
	private String mdofferCode;

	

}