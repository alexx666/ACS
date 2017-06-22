package com.alexx666.acs.data.dto.alerts;

import java.util.Date;

public abstract class Alert {
	
	protected String line;
	
	public Alert(String line) {	this.line = line; }

	public abstract String getMessage();
	public abstract Date getHour();
}
