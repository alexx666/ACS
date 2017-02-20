package main.java.acs.db.connection;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

import main.java.acs.configuration.yaml.YMLConfiguration;

public class JDBCConnectionPool extends ObjectPool<Connection> {
	
	private static JDBCConnectionPool instance = null;
	
	private static final String dsn = "jdbc:mysql://localhost/" + YMLConfiguration.getInstance().getTools().trackers.db;
	private static final String usr = YMLConfiguration.getInstance().getTools().trackers.user;
	private static final String pwd = YMLConfiguration.getInstance().getTools().trackers.pass;
	  
	private JDBCConnectionPool() { 
		super(); 
	}
	  
	public static JDBCConnectionPool getInstance() {
		if (instance == null) {	
			instance = new JDBCConnectionPool(); 
		}
		return instance;
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