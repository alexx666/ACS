package main.java.acs.entities;

import java.util.Calendar;

public class Alert {

	private String line;
	
	public Alert(String line) {	this.line = line; }

	public String getMessage() {
		String[] components = line.split("[**]"); //TODO mejorar
		return components[2];
	}
	
	public String getDate() {
		String time = main.java.acs.utils.general.Dates.getTimestampFrom(line);
		Calendar cal = main.java.acs.utils.general.Dates.toCal(time);
		return main.java.acs.utils.general.Dates.addNHoursToTime(cal, -2);
	}
}
