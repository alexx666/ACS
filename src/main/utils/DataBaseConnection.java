package main.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DataBaseConnection {
	
	private static Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	
	/* Variables */
	private Connection conn;
	private String connection;
	
	/**
	 * Constructor
	 * @param db you want to connect to
	 * @param user of the database 
	 * @param password for the user
	 */
	public DataBaseConnection(String db, String user, String password) {
		this.connection = "jdbc:mysql://localhost/" + db + "?" + "user=" + user + "&password=" + password;
	}
	
	/**
	 * Connects to the db
	 * @return the connection so it can be passed to a different method for queries
	 * @throws SQLException 
	 */
	public void connect() {
		try {
			conn = DriverManager.getConnection(connection);
		} catch (SQLException e) {
			LOGGER.info("SQLException: " + e.getMessage());
		}
	}
	
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			LOGGER.info("SQLException: " + e.getMessage());
		}
	}

	public Connection getConn() {
		return conn;
	}
}
