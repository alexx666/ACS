package main.java.acs.data.entities;

import java.util.Calendar;

public class Alert {

	private String line;
	
	public Alert(String line) {	this.line = line; }

	public String getMessage() {
		String[] components = line.split("[**]"); //TODO mejorar
		return components[2];
	}
	
	public String getDate() {
		String time = main.java.acs.utils.calc.Dates.getTimestampFrom(line);
		Calendar cal = main.java.acs.utils.calc.Dates.toCal(time);
		return main.java.acs.utils.calc.Dates.addNHoursToTime(cal, -2);
	}
}
