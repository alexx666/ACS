package main.java.acs.utils.handlers;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.acs.configuration.ACSConfiguration;
import main.java.acs.data.dao.ProfileDAO;
import main.java.acs.data.dao.SnapshotDAO;
import main.java.acs.data.dao.factory.DAOFactory;
import main.java.acs.data.dto.Anomaly;
import main.java.acs.data.dto.Statistics;
import main.java.acs.utils.process.ExternalProcess;
import main.java.acs.utils.process.ProcessManager;

/**
 * 
 * @author alexx666
 *
 */
public class AlertObserver {
	
	private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	private static final SimpleFormatter FORMATTER = new SimpleFormatter();
	private static final String dataSource = ACSConfiguration.getInstance().getSettings().trackers.type;
	private static final ProfileDAO profileDao = DAOFactory.getDAOFactory(dataSource).getProfileDAO();
	private static final SnapshotDAO snapshotDao = DAOFactory.getDAOFactory(dataSource).getSnapshotDAO();
	
	private AlertSubject subject;
	private Statistics profile;
	private Statistics snapshot;
	
	public AlertObserver(AlertSubject subject) {
		this.subject = subject;
		this.subject.addObserver(this);
		
		profile = profileDao.getFullProfile();
		snapshot = profile;
		
		try {
			FileHandler fh = new FileHandler(ACSConfiguration.getInstance().getSettings().outputs.file, true);
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
