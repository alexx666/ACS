package com.alexx666.acs.db.factory;

import com.alexx666.acs.db.dao.profile.ProfileDAO;
import com.alexx666.acs.db.dao.snapshot.SnapshotDAO;
import com.alexx666.acs.db.factory.impl.MySQLDAOFactory;

public abstract class DAOFactory {
		
	public abstract ProfileDAO getProfileDAO();
	public abstract SnapshotDAO getSnapshotDAO();
	
	public static DAOFactory getDAOFactory(String dataSource) throws NullPointerException {
		switch(dataSource) {
		case "mysql": return new MySQLDAOFactory();
		default: throw new NullPointerException();
		}
	}
}
