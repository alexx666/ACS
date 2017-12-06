package com.alexx666.acs.db.factory.impl;

import java.sql.Connection;

import com.alexx666.acs.db.connection.impl.JDBCConnectionPool;
import com.alexx666.acs.db.dao.profile.ProfileDAO;
import com.alexx666.acs.db.dao.profile.impl.MySQLProfileDAO;
import com.alexx666.acs.db.dao.snapshot.SnapshotDAO;
import com.alexx666.acs.db.dao.snapshot.impl.MySQLSnapshotDAO;
import com.alexx666.acs.db.factory.DAOFactory;

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
