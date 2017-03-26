package main.java.acs.data.dto;

import java.util.Calendar;

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
	
	public String getDate() {
		String time = main.java.acs.utils.Dates.getTimestampFrom(line);
		Calendar cal = main.java.acs.utils.Dates.toCal(time);
		return main.java.acs.utils.Dates.addNHoursToTime(cal, -2);
	}
}
