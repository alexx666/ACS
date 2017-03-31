package com.alexx666.acs.data.dao.factory.impl;

import com.alexx666.acs.data.dao.ProfileDAO;
import com.alexx666.acs.data.dao.SnapshotDAO;
import com.alexx666.acs.data.dao.connection.impl.JDBCConnectionPool;
import com.alexx666.acs.data.dao.factory.DAOFactory;
import com.alexx666.acs.data.dao.impl.mysql.MySQLProfileDAO;
import com.alexx666.acs.data.dao.impl.mysql.MySQLSnapshotDAO;
import com.mysql.jdbc.Connection;

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
