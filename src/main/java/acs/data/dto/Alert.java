package main.java.acs.data.dto;

import java.util.Calendar;
import java.util.Date;

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
