package main.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import main.entities.Flow;
import main.entities.Statistics;

public class ProfileDao {
	
	private static Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	
	private Connection connection;
	private Statement stmt = null;
	private ResultSet rs = null;
	private Statistics profile = null;
		
	public ProfileDao(Connection connection) {
		this.connection = connection;
	}

	public Statistics getProfile() { 
		Date now = new Date();
		String startTime = main.utils.Dates.addNMinutesToTime(Calendar.getInstance(), -5);
		String endTime = main.utils.Dates.addNMinutesToTime(Calendar.getInstance(), 5);
		String dayOfWeek = main.utils.Dates.toString(now, "EEEE");
		String query = "select INET_NTOA(src_ip), INET_NTOA(dst_ip), src_port, dst_port, src_pkts, dst_pkts, src_bytes, dst_bytes, ip_proto, duration from session_nidslinux_VirtualBox_" + dayOfWeek + " where date_format(start_time, '%H:%i:%s') between '"+startTime+"' and '"+endTime+"';";
		try {
		    stmt = connection.createStatement();
		    if (stmt.execute(query)) {
		    	
		    	List<Flow> cons = new ArrayList<Flow>();
		    	rs = stmt.getResultSet();
		        
		        while(!rs.isLast()) {
		        	rs.next();
					
					String src_ip = rs.getString("INET_NTOA(src_ip)");
			        String dst_ip = rs.getString("INET_NTOA(dst_ip)");
			        String src_port = rs.getString("src_port");
			        String dst_port = rs.getString("dst_port");
			        int src_pkts = rs.getInt("src_pkts");
			        int dst_pkts = rs.getInt("dst_pkts");
			        int src_bytes = rs.getInt("src_bytes");
			        int dst_bytes = rs.getInt("dst_bytes");
			        int ip_proto = rs.getInt("ip_proto");
			        int duration = rs.getInt("duration");
			        
					Flow f = new Flow(src_ip, dst_ip, src_port, dst_port, src_pkts, dst_pkts, src_bytes, dst_bytes, ip_proto, duration);
					
					cons.add(f);
		        }
		        
		        profile = new Statistics(cons);
		    }
		}catch (SQLException ex){ 
			LOGGER.info("SQLException: " + ex.getMessage()); 
		}finally {
		    if (rs != null) { 
		    	try { 
		    		rs.close(); 
		    	} catch (SQLException sqlEx) {/*ignore*/}
		        rs = null;
		    }
		    if (stmt != null) {
		    	try { 
		    		stmt.close(); 
		    	} catch (SQLException sqlEx) {/*ignore*/}
		        stmt = null;
		    }
		}
		return profile;
	}
	
	public Statistics getFullProfile() {
		Date now = new Date();
		String dayOfWeek = main.utils.Dates.toString(now, "EEEE");
		String query = "select INET_NTOA(src_ip), INET_NTOA(dst_ip), src_port, dst_port, src_pkts, dst_pkts, src_bytes, dst_bytes, ip_proto, duration from session_nidslinux_VirtualBox_" + dayOfWeek;
		try {
		    stmt = connection.createStatement();
		    if (stmt.execute(query)) {
		    	
		    	List<Flow> cons = new ArrayList<Flow>();
		    	rs = stmt.getResultSet();
		        
		        while(!rs.isLast()) {
		        	rs.next();
					
					String src_ip = rs.getString("INET_NTOA(src_ip)");
			        String dst_ip = rs.getString("INET_NTOA(dst_ip)");
			        String src_port = rs.getString("src_port");
			        String dst_port = rs.getString("dst_port");
			        int src_pkts = rs.getInt("src_pkts");
			        int dst_pkts = rs.getInt("dst_pkts");
			        int src_bytes = rs.getInt("src_bytes");
			        int dst_bytes = rs.getInt("dst_bytes");
			        int ip_proto = rs.getInt("ip_proto");
			        int duration = rs.getInt("duration");
			        
					Flow f = new Flow(src_ip, dst_ip, src_port, dst_port, src_pkts, dst_pkts, src_bytes, dst_bytes, ip_proto, duration);
					
					cons.add(f);
		        }
		        
		        profile = new Statistics(cons);
		    }
		}catch (SQLException ex){ 
			LOGGER.info("SQLException: " + ex.getMessage()); 
		}finally {
		    if (rs != null) { 
		    	try { 
		    		rs.close(); 
		    	} catch (SQLException sqlEx) {/*ignore*/}
		        rs = null;
		    }
		    if (stmt != null) {
		    	try { 
		    		stmt.close(); 
		    	} catch (SQLException sqlEx) {/*ignore*/}
		        stmt = null;
		    }
		}
		return profile;
	}
	
	public boolean isProfileDataEnough() {
		Date now = new Date();
		String startTime = main.utils.Dates.addNMinutesToTime(Calendar.getInstance(), -5);
		String endTime = main.utils.Dates.addNMinutesToTime(Calendar.getInstance(), 5);
		String dayOfWeek = main.utils.Dates.toString(now, "EEEE");
		String query = "select count(*) from session_nidslinux_VirtualBox_" + dayOfWeek + " where date_format(start_time, '%H:%i:%s') between '"+startTime+"' and '"+endTime+"';";
		try {
		    stmt = connection.createStatement();
		    if (stmt.execute(query)) {
		    	rs = stmt.getResultSet();
		    	rs.next();
		    	if (rs.getInt("count(*)") > 0) {
			    	return true;
		    	}else{
		    		return false;
		    	}
		    }else{
		    	return false;
		    }
		}catch (SQLException ex){ 
			LOGGER.warning(ex.getMessage());
			return false;
		}finally{
			if (stmt != null) { 
		    	try { 
		    		stmt.close(); 
		    	} catch (SQLException sqlEx) { 
		    		LOGGER.warning(sqlEx.getMessage()); 
		    	} 
		    	stmt = null; 
		    }
		}
	}
}
