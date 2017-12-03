package main.java.com.alexx666.acs.db.factory.impl;

import com.mysql.jdbc.Connection;

import main.java.com.alexx666.acs.db.connection.impl.JDBCConnectionPool;
import main.java.com.alexx666.acs.db.dao.profile.ProfileDAO;
import main.java.com.alexx666.acs.db.dao.profile.impl.MySQLProfileDAO;
import main.java.com.alexx666.acs.db.dao.snapshot.SnapshotDAO;
import main.java.com.alexx666.acs.db.dao.snapshot.impl.MySQLSnapshotDAO;
import main.java.com.alexx666.acs.db.factory.DAOFactory;

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
