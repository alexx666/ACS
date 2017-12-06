package com.alexx666.acs.db.connection.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.alexx666.acs.configuration.ConfigurationFactory;
import com.alexx666.acs.db.connection.ObjectPool;

/**
 * 
 * 
 *
 */
public class JDBCConnectionPool extends ObjectPool<Connection> {
	
	private String dsn;
	private String usr;
	private String pwd;
	  
	private JDBCConnectionPool() { 
		super();
		this.dsn = "jdbc:mysql://localhost/" + ConfigurationFactory.getInstance().getSettings().getTrackers().getDb();
		this.usr = ConfigurationFactory.getInstance().getSettings().getTrackers().getUser();
		this.pwd = ConfigurationFactory.getInstance().getSettings().getTrackers().getPass();
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