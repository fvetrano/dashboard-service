package it.tim.dashboard.exception;

import lombok.Data;

@Data
public class SubsystemException extends RuntimeException {

	private final String causeException;
	private final String causeMsg;
	private final String subsystem;

	public SubsystemException(String subsystem, String causeException, String causeMsg) {
		super();
		this.subsystem = subsystem;
		this.causeException = causeException;
		this.causeMsg = causeMsg;
	}

}
