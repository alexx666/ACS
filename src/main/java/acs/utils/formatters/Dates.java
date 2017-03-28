package main.java.acs.utils.formatters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 
 * @author alexx666
 *
 */
public class Dates {
		
	public static String formatToGMT(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
		sdf.setTimeZone(TimeZone.getTimeZone("")); 
		return sdf.format(date);
	}
	
    public static String getYearAsString(final Date date, final String format) {
        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
    
    
    public static Date addNMinutesToTime(Date date, int minutesToAdd) {
		Calendar startTime = Calendar.getInstance();
		startTime.setTime(date);
		startTime.add(Calendar.MINUTE, minutesToAdd);
		return startTime.getTime();
	}
}
