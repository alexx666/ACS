package main.entities;

import java.util.Calendar;

public class Alert {

	private String line;
	
	public Alert(String line) {
		this.line = line;
	}

	public String getMessage() {
		String[] components = line.split("[**]");
		//TODO complete
		return components[2];
	}
	
	public String getDate() {
		String time = main.utils.Dates.getTimestampFrom(line);
		Calendar cal = main.utils.Dates.toCal(time);
		return main.utils.Dates.addNHoursToTime(cal, -2);
	}
	
}
