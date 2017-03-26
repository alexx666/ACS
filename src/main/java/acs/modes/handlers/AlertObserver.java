package main.java.acs.modes.handlers;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.acs.data.dao.ProfileDAO;
import main.java.acs.data.dao.SnapshotDAO;
import main.java.acs.data.dao.factory.DAOFactory;
import main.java.acs.data.dto.Anomaly;
import main.java.acs.data.dto.Statistics;
import main.java.acs.modes.process.ExternalProcess;
import main.java.acs.modes.process.ProcessManager;

/**
 * 
 * @author alexx666
 *
 */
public class AlertObserver {
	
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private static final SimpleFormatter FORMATTER = new SimpleFormatter();
	
	private AlertSubject subject;
	private ProfileDAO profileDao;
	private SnapshotDAO snapshotDao; 
	private Statistics profile;
	private Statistics snapshot;
	
	public AlertObserver(AlertSubject subject, String dumpFile, String dataSource) {
		this.subject = subject;
		this.subject.addObserver(this);
		this.profileDao = DAOFactory.getDAOFactory(dataSource).getProfileDAO();
		this.snapshotDao= DAOFactory.getDAOFactory(dataSource).getSnapshotDAO();
		
		profile = profileDao.getFullProfile();
		snapshot = profile;
		
		try {
			FileHandler fh = new FileHandler(dumpFile, true);
			fh.setFormatter(FORMATTER);
			LOGGER.addHandler(fh);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		ProcessManager.stop(ExternalProcess.PRADS, false);
		
		if (profileDao.isProfileDataEnough()) {	profile = profileDao.getProfile(); }
		if (snapshotDao.isSnapshotReady(subject.getAlert().getDate())) { 
			snapshot = snapshotDao.getSnapshot(subject.getAlert().getDate()); 
		} //TODO Attempt threading both profile and snapshot extraction
				
		LOGGER.warning(subject.getAlert().getMessage() 
				+ " ---> Network ANOMALY of: " 
				+ Math.round((new Anomaly(profile, snapshot)).getAnomaly()) 
				+ "/100");
			
		ProcessManager.start(ExternalProcess.PRADS, false);
	}
}
