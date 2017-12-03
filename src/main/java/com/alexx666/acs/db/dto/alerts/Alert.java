package main.java.com.alexx666.acs.db.dto.alerts;

import java.util.Date;

public abstract class Alert {
	
	protected String line;
	
	public Alert(String line) {	this.line = line; }

	public abstract String getMessage();
	public abstract Date getHour();
}
