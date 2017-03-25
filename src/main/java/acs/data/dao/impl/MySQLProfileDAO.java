package main.java.acs.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.Connection;

import main.java.acs.data.dao.ProfileDAO;
import main.java.acs.data.dao.connection.impl.JDBCConnectionPool;
import main.java.acs.data.dao.factory.impl.MySQLDAOFactory;
import main.java.acs.data.dto.Flow;
import main.java.acs.data.dto.Statistics;
import main.java.acs.utils.calc.Dates;

/**
 * 
 * @author alexx666
 *
 */
public class MySQLProfileDAO implements ProfileDAO {
					
	@Override
	public Statistics getProfile() { 
		Connection connection = MySQLDAOFactory.createConnection();
		Statistics profile = null;
		
		try {		
			Date now = new Date();
			
			String startTime = Dates.addNMinutesToTime(Calendar.getInstance(), -5);
			String endTime = Dates.addNMinutesToTime(Calendar.getInstance(), 5);
			String dayOfWeek = Dates.toString(now, "EEEE");
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
		        rs.close();
		        profile = new Statistics(cons);
		    }
		    stmt.close();
		}catch (SQLException ex){ 
			System.out.println("SQLException: " + ex.getMessage()); 
		}finally{
    		JDBCConnectionPool.getInstance().checkIn(connection);
		}
		return profile;
	}
	
	@Override
	public Statistics getFullProfile() {
		Connection connection = MySQLDAOFactory.createConnection();
		Statistics profile = null;
		
		try {
			Date now = new Date();
			String dayOfWeek = Dates.toString(now, "EEEE");
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
		        rs.close();
		        profile = new Statistics(cons);
		    }
		    stmt.close();
		}catch (SQLException ex){ 
			System.out.println("SQLException: " + ex.getMessage());
		}finally{
    		JDBCConnectionPool.getInstance().checkIn(connection);
		}
	    return profile;
	}
	
	@Override
	public boolean isProfileDataEnough() {
		Connection connection = MySQLDAOFactory.createConnection();
		boolean result = false;
		try {	
			Date now = new Date();
			String startTime = Dates.addNMinutesToTime(Calendar.getInstance(), -5);
			String endTime = Dates.addNMinutesToTime(Calendar.getInstance(), 5);
			String dayOfWeek = Dates.toString(now, "EEEE");
			String query = "select count(*) from session_nidslinux_VirtualBox_" + dayOfWeek + " where date_format(start_time, '%H:%i:%s') between '"+startTime+"' and '"+endTime+"';";
		    Statement stmt = connection.createStatement();
		    if (stmt.execute(query)) {
		    	ResultSet rs = stmt.getResultSet();
		    	rs.next();
		    	if (rs.getInt("count(*)") > 0) {
		    		result = true;
		    	}
	    		rs.close();
		    }
		    stmt.close();
		}catch (SQLException ex){ 
			System.out.println(ex.getMessage());
		}finally{
    		JDBCConnectionPool.getInstance().checkIn(connection);
		}
		return result;
	}
}