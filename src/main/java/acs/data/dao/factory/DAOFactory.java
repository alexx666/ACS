package main.java.acs.data.dao.factory;

import main.java.acs.data.dao.ProfileDAO;
import main.java.acs.data.dao.SnapshotDAO;
import main.java.acs.data.dao.factory.impl.MySQLDAOFactory;

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
