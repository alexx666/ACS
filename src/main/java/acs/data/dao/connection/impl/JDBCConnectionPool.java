package main.java.acs.data.dao.connection.impl;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

import main.java.acs.configuration.ACSConfiguration;
import main.java.acs.data.dao.connection.ObjectPool;

/**
 * 
 * 
 *
 */
public class JDBCConnectionPool extends ObjectPool<Connection> {
		
	private static final String dsn = "jdbc:mysql://localhost/" + ACSConfiguration.getInstance().getSettings().trackers.db;
	private static final String usr = ACSConfiguration.getInstance().getSettings().trackers.user;
	private static final String pwd = ACSConfiguration.getInstance().getSettings().trackers.pass;
	  
	private JDBCConnectionPool() { 
		super(); 
	}
	
	private static class Static {
		private static final JDBCConnectionPool INSTANTE = new JDBCConnectionPool();
	}
	  
	public static JDBCConnectionPool getInstance() {
		return Static.INSTANTE;
	}

	@Override
	protected Connection create() {
		try {
			return (Connection) (DriverManager.getConnection(dsn, usr, pwd));
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return (null);
	    }
	}

	@Override
	public void expire(Connection o) {
		try {
			((Connection) o).close();
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
	}

	@Override
	public boolean validate(Connection o) {
		try {
			return (!((Connection) o).isClosed());
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return (false);
	    }
	}
}