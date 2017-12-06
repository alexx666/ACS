package com.alexx666.acs.db.dao.snapshot.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alexx666.acs.db.connection.impl.JDBCConnectionPool;
import com.alexx666.acs.db.dao.snapshot.SnapshotDAO;
import com.alexx666.acs.db.dto.traffic.Flow;
import com.alexx666.acs.db.dto.traffic.Statistics;
import com.alexx666.acs.db.factory.impl.MySQLDAOFactory;
import com.alexx666.acs.util.DateFormatters;

/**
 * 
 * @author alexx666
 *
 */
public class MySQLSnapshotDAO implements SnapshotDAO {
				
	@Override
	public Statistics getSnapshot(Date time) {
		Connection connection = MySQLDAOFactory.createConnection();
		Statistics result = null;
		try {	
			String query = "select INET_NTOA(src_ip), INET_NTOA(dst_ip), src_port, dst_port, src_pkts, dst_pkts, src_bytes, dst_bytes, ip_proto, duration from snapshot_session_nidslinux_VirtualBox where end_time >= '" + DateFormatters.formatToGMT(time) + "';";
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
	    		result = new Statistics(cons);
		    }
    		stmt.close();
		}catch (SQLException ex){ 
			System.out.println("[acs] Error extracting snapshot!");
		}finally{
    		JDBCConnectionPool.getInstance().checkIn(connection);
		}
		return result;
	}
	
	@Override
	public boolean isSnapshotReady(Date time) {
		Connection connection = MySQLDAOFactory.createConnection();	
		boolean result = false;
		try {
			String query = "select count(*) from snapshot_session_nidslinux_VirtualBox where end_time >= '" + DateFormatters.formatToGMT(time) + "';";
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
			System.out.println("[acs] Error extracting snapshot!");
		}finally{
    		JDBCConnectionPool.getInstance().checkIn(connection);
		}
		return result;
	}
}