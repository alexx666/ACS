package com.alexx666.acs.data.dao.impl.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alexx666.acs.data.dao.ProfileDAO;
import com.alexx666.acs.data.dao.connection.impl.JDBCConnectionPool;
import com.alexx666.acs.data.dao.factory.impl.MySQLDAOFactory;
import com.alexx666.acs.data.dto.traffic.Flow;
import com.alexx666.acs.data.dto.traffic.Statistics;
import com.alexx666.acs.utils.formatters.Dates;
import com.mysql.jdbc.Connection;

/**
 * 
 * @author alexx666
 *
 */
public class MySQLProfileDAO implements ProfileDAO {
					
	@Override
	public Statistics getProfile(Date hour) { 
		Connection connection = MySQLDAOFactory.createConnection();
		Statistics profile = null;
		
		try {										
			String startTime = Dates.formatToGMT(Dates.addNMinutesToTime(hour, -5));
			String endTime = Dates.formatToGMT(Dates.addNMinutesToTime(hour, 5));
			String dayOfWeek = Dates.getYearAsString(hour, "EEEE");
						
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
			System.out.println("[acs] Error: Unable to obtain profile data."); 
		}finally{
    		JDBCConnectionPool.getInstance().checkIn(connection);
		}
		return profile;
	}
	
	@Override
	public Statistics getFullProfile(Date hour) {
		Connection connection = MySQLDAOFactory.createConnection();
		Statistics profile = null;
		
		try {
			String dayOfWeek = Dates.getYearAsString(hour, "EEEE");
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
			System.out.println("[acs] Error: No profile data avaliable for today. Gather data by running 'profiler' mode.");
		}finally{
    		JDBCConnectionPool.getInstance().checkIn(connection);
		}
	    return profile;
	}
	
	@Override
	public boolean isProfileDataEnough(Date hour) {
		Connection connection = MySQLDAOFactory.createConnection();
		boolean result = false;
		try {			
			String startTime = Dates.formatToGMT(Dates.addNMinutesToTime(hour, -5));
			String endTime = Dates.formatToGMT(Dates.addNMinutesToTime(hour, 5));
			String dayOfWeek = Dates.getYearAsString(hour, "EEEE");
									
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
			System.out.println("[acs] Error: Unable to obtain profile data.");
		}finally{
    		JDBCConnectionPool.getInstance().checkIn(connection);
		}
		return result;
	}
}