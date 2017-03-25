package main.java.acs.utils.handlers;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.acs.configuration.ACSConfiguration;
import main.java.acs.data.dao.ProfileDao;
import main.java.acs.data.dao.SnapshotDao;
import main.java.acs.data.entities.Anomaly;
import main.java.acs.data.entities.Statistics;
import main.java.acs.utils.process.ExternalProcess;
import main.java.acs.utils.process.ProcessManager;

/**
 * 
 * @author alexx666
 *
 */
public class AlertObserver {
	
	private static final Logger LOGGER = Logger.getLogger("ACS");
	private static final SimpleFormatter FORMATTER = new SimpleFormatter();
	
	private AlertSubject subject;
	private ProfileDao profileDao;
	private SnapshotDao snapshotDao;
	private Statistics profile;
	private Statistics snapshot;
	
	public AlertObserver(AlertSubject subject) {
		this.subject = subject;
		this.subject.addObserver(this);
		
		profileDao = new ProfileDao();
		snapshotDao = new SnapshotDao();
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
