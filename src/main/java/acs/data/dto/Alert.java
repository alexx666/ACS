package main.java.acs.data.dto;

import java.util.Calendar;

import main.java.acs.utils.formatters.Dates;

/**
 * 
 * @author alexx666
 *
 */
public class Alert {

	private String line;
	
	public Alert(String line) {	this.line = line; }

	public String getMessage() {
		String[] components = line.split("[**]"); //TODO mejorar
		return components[2];
	}
	
	public String getTimestamp() {
		String time = Dates.getTimestampFrom(line);
		Calendar cal = Dates.getCalendarFrom(time);
		return Dates.addNHoursToTime(cal, -2);
	}
	
	public String getHour() {
		return Dates.getTimestampFrom(line);
	}
}
