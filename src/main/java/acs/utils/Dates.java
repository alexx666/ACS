package main.java.acs.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Dates {
	
//	TODO retocar todos estos metodos
	@SuppressWarnings("deprecation")
	public static String getTimestampFrom(String line) {	
		Date today;
	    String output;
	    SimpleDateFormat formatter;
		
		String [] elements = line.split(" ");
		String [] timeComp = elements[0].split("-");
		String timestamp = timeComp[1].substring(0, 8);
	    String [] tcomp = timestamp.split(":");
	    
	    formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
	    today = new Date();
	    today.setHours(Integer.parseInt(tcomp[0]));
	    today.setMinutes(Integer.parseInt(tcomp[1]));
	    today.setSeconds(Integer.parseInt(tcomp[2]));
	    output = formatter.format(today);
	    	    
		return output;
	}
	
	public static Calendar toCal(String time) {
		String [] comps = time.split(":");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(comps[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(comps[1]));
		cal.set(Calendar.SECOND, Integer.parseInt(comps[2]));
		return cal;
	}
	
    public static String toString(final Date date, final String format) {
        return toString(date, format, "UTC");
    }

    private static String toString(final Date date, final String format, final String timezone) {
        final TimeZone tz = TimeZone.getTimeZone(timezone);
        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(tz);
        return formatter.format(date);
    }
    
    public static String addNSecondsToTime(Calendar date, int secondsToAdd) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar startTime = date;
		startTime.add(Calendar.SECOND, secondsToAdd);
		String dateStr = df.format(startTime.getTime());
		return dateStr;
	}
    
    public static String addNMinutesToTime(Calendar date, int minutesToAdd) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		Calendar startTime = date;
		startTime.add(Calendar.MINUTE, minutesToAdd);
		String dateStr = df.format(startTime.getTime());
		return dateStr;
	}
    
    public static String addNHoursToTime(Calendar date, int hoursToAdd) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar startTime = date;
		startTime.add(Calendar.HOUR, hoursToAdd);
		String dateStr = df.format(startTime.getTime());
		return dateStr;
	}
}
