package main.java.entities;

import java.util.Calendar;

public class Alert {

	private String line;
	
	public Alert(String line) {	this.line = line; }

	public String getMessage() {
		String[] components = line.split("[**]"); //TODO mejorar
		return components[2];
	}
	
	public String getDate() {
		String time = main.java.utils.Dates.getTimestampFrom(line);
		Calendar cal = main.java.utils.Dates.toCal(time);
		return main.java.utils.Dates.addNHoursToTime(cal, -2);
	}
}
