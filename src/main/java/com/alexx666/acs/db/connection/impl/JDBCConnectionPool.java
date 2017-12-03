package main.java.com.alexx666.acs.db.connection.impl;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

import main.java.com.alexx666.acs.db.connection.ObjectPool;

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
	}
	
	private static class Static {
		private static final JDBCConnectionPool INSTANTE = new JDBCConnectionPool();
	}
	  
	public static JDBCConnectionPool getInstance() {
		return Static.INSTANTE;
	}

	public void setDsn(String dsn) { this.dsn = dsn; }
	public void setUsr(String usr) { this.usr = usr; }
	public void setPwd(String pwd) { this.pwd = pwd; }

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