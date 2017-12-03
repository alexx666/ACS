package com.alexx666.acs.db.dto.alerts.impl;

import java.util.Calendar;
import java.util.Date;

import com.alexx666.acs.db.dto.alerts.Alert;

/**
 * 
 * @author alexx666
 *
 */
public class SuricataAlert extends Alert {
	
	public SuricataAlert(String line) { super(line); }

	public String getMessage() {
		String[] components = line.split("[**]"); //TODO improve
		return components[2];
	}
	
	public Date getHour() {
		String [] elements = line.split(" ");
		String [] timeComp = elements[0].split("-");
		String timestamp = timeComp[1].substring(0, 8);
		String [] comps = timestamp.split(":");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(comps[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(comps[1]));
		cal.set(Calendar.SECOND, Integer.parseInt(comps[2]));
		return cal.getTime();
	}
}
