package main.java.acs.data.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.mysql.jdbc.Connection;

import main.java.acs.data.connection.JDBCConnectionPool;
import main.java.acs.data.entities.Flow;
import main.java.acs.data.entities.Statistics;

/**
 * 
 * @author alexx666
 *
 */
public class ProfileDao {
	
	private static Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	
	private JDBCConnectionPool jdbcpool;
			
	public ProfileDao() {
		this.jdbcpool = JDBCConnectionPool.getInstance();
	}

	public Statistics getProfile() { 
		
		try {
			Statistics profile = null;

			Connection connection = jdbcpool.checkOut();
			
			Date now = new Date();
			
			String startTime = main.java.acs.utils.calc.Dates.addNMinutesToTime(Calendar.getInstance(), -5);
			String endTime = main.java.acs.utils.calc.Dates.addNMinutesToTime(Calendar.getInstance(), 5);
			String dayOfWeek = main.java.acs.utils.calc.Dates.toString(now, "EEEE");
			String query = "select INET_NTOA(src_ip), INET_NTOA(dst_ip), src_port, dst_port, src_pkts, dst_pkts, src_bytes, dst_bytes, ip_proto, duration from session_nidslinux_VirtualBox_" + dayOfWeek + " where date_format(start_time, '%H:%i:%s') between '"+startTime+"' and '"+endTime+"';";
			
			Statement stmt = connection.createStatement();
		    if (stmt.execute(query)) {
		    	
		    	List<Flow> cons = new ArrayList<Flow>();
		    	ResultSet rs = stmt.getResultSet();
		        
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
		        rs.close();
		    }
		    stmt.close();
		    jdbcpool.checkIn(connection);
		    return profile;
		}catch (SQLException ex){ 
			LOGGER.info("SQLException: " + ex.getMessage()); 
			return null;
		}
	}
	
	public Statistics getFullProfile() {
		try {
			Statistics profile = null;
			Connection connection = jdbcpool.checkOut();
			Date now = new Date();
			String dayOfWeek = main.java.acs.utils.calc.Dates.toString(now, "EEEE");
			String query = "select INET_NTOA(src_ip), INET_NTOA(dst_ip), src_port, dst_port, src_pkts, dst_pkts, src_bytes, dst_bytes, ip_proto, duration from session_nidslinux_VirtualBox_" + dayOfWeek;
			Statement stmt = connection.createStatement();
		    if (stmt.execute(query)) {
		    	
		    	List<Flow> cons = new ArrayList<Flow>();
		    	ResultSet rs = stmt.getResultSet();
		        
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
		        rs.close();
		    }
		    stmt.close();
		    jdbcpool.checkIn(connection);
		    return profile;
		}catch (SQLException ex){ 
			LOGGER.info("SQLException: " + ex.getMessage());
			return null;
		}
	}
	
	public boolean isProfileDataEnough() {
		try {
			Connection connection = jdbcpool.checkOut();
			
			Date now = new Date();
			String startTime = main.java.acs.utils.calc.Dates.addNMinutesToTime(Calendar.getInstance(), -5);
			String endTime = main.java.acs.utils.calc.Dates.addNMinutesToTime(Calendar.getInstance(), 5);
			String dayOfWeek = main.java.acs.utils.calc.Dates.toString(now, "EEEE");
			String query = "select count(*) from session_nidslinux_VirtualBox_" + dayOfWeek + " where date_format(start_time, '%H:%i:%s') between '"+startTime+"' and '"+endTime+"';";
		    Statement stmt = connection.createStatement();
		    if (stmt.execute(query)) {
		    	ResultSet rs = stmt.getResultSet();
		    	rs.next();
		    	if (rs.getInt("count(*)") > 0) {
		    		rs.close();
		    		stmt.close();
		    		jdbcpool.checkIn(connection);
		    		return true;
		    	}else{
		    		rs.close();
		    		stmt.close();
		    		jdbcpool.checkIn(connection);
		    		return false;
		    	}
		    }
		    stmt.close();
    		jdbcpool.checkIn(connection);
		    return false;
		}catch (SQLException ex){ 
			LOGGER.warning(ex.getMessage());
			return false;
		}
	}
}
