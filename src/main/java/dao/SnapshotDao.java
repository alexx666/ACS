package main.java.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.java.entities.Flow;
import main.java.entities.Statistics;

public class SnapshotDao {
	
	private static Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private static String DB = "cxtracker";
	private static String USER = "cxtracker";
	private static String PASS = "cxtracker";
			
	public SnapshotDao() {}

	public Statistics getSnapshot(String time) {
		try {

			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/"+DB+"?user="+USER+"&password="+PASS);
	
			String query = "select INET_NTOA(src_ip), INET_NTOA(dst_ip), src_port, dst_port, src_pkts, dst_pkts, src_bytes, dst_bytes, ip_proto, duration from snapshot_session_nidslinux_VirtualBox where end_time >= '" + time + "';";
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
	    		stmt.close();
	    		connection.close();
				return new Statistics(cons);
		    }else{
	    		stmt.close();
	    		connection.close();
		    	return null;
		    }
		}catch (SQLException ex){ 
			LOGGER.warning("SQLException: " + ex.getMessage()); 
			return null;
		}
	}
	
	public boolean isSnapshotReady(String time) {
		try {

			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/"+DB+"?user="+USER+"&password="+PASS);
	
			String query = "select count(*) from snapshot_session_nidslinux_VirtualBox where end_time >= '" + time + "';";
		    Statement stmt = connection.createStatement();
		    if (stmt.execute(query)) {
		    	ResultSet rs = stmt.getResultSet();
		    	rs.next();
		    	if (rs.getInt("count(*)") > 0) {
		    		rs.close();
		    		stmt.close();
		    		connection.close();
			    	return true;
		    	}else{
		    		rs.close();
		    		stmt.close();
		    		connection.close();
		    		return false;
		    	}
		    }else{
	    		stmt.close();
	    		connection.close();
		    	return false;
		    }
		}catch (SQLException ex){ 
			LOGGER.warning(ex.getMessage());
			return false;
		}
	}
}
