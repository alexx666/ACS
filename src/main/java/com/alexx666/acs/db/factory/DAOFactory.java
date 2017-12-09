package com.alexx666.acs.db.factory;

import com.alexx666.acs.db.dao.profile.ProfileDAO;
import com.alexx666.acs.db.dao.snapshot.SnapshotDAO;
import com.alexx666.acs.db.factory.impl.MySQLDAOFactory;

public abstract class DAOFactory {
		
	private static final MySQLDAOFactory MYSQL_DAO_FACTORY =  new MySQLDAOFactory(); 
	
	public static DAOFactory getDAOFactory(String dataSource) throws NullPointerException {
		switch(dataSource) {
		case "mysql": return MYSQL_DAO_FACTORY;
		default: throw new NullPointerException();
		}
	}
	
	public abstract ProfileDAO getProfileDAO();
	public abstract SnapshotDAO getSnapshotDAO();
}
