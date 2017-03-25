package main.java.acs.data.dao.factory.impl;

import com.mysql.jdbc.Connection;

import main.java.acs.data.dao.ProfileDAO;
import main.java.acs.data.dao.SnapshotDAO;
import main.java.acs.data.dao.connection.impl.JDBCConnectionPool;
import main.java.acs.data.dao.factory.DAOFactory;
import main.java.acs.data.dao.impl.MySQLProfileDAO;
import main.java.acs.data.dao.impl.MySQLSnapshotDAO;

public class MySQLDAOFactory extends DAOFactory {
	
	public static Connection createConnection() {
		return JDBCConnectionPool.getInstance().checkOut();
	}

	@Override
	public ProfileDAO getProfileDAO() {
		return new MySQLProfileDAO();
	}

	@Override
	public SnapshotDAO getSnapshotDAO() {
		return new MySQLSnapshotDAO();
	}

}
