package main.java.acs.modes.handlers;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.java.acs.data.dao.ProfileDAO;
import main.java.acs.data.dao.SnapshotDAO;
import main.java.acs.data.dao.factory.DAOFactory;
import main.java.acs.data.dto.Alert;
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
	private Alert oldAlert;
	private Anomaly oldAnomaly;
	private ProfileDAO profileDao;
	private SnapshotDAO snapshotDao; 
	private Statistics profile;
	private Statistics snapshot;
	
	public AlertObserver(AlertSubject subject, String dumpFile, boolean append, String dataSource) {
		this.subject = subject;
		this.subject.addObserver(this);
		this.profileDao = DAOFactory.getDAOFactory(dataSource).getProfileDAO();
		this.snapshotDao= DAOFactory.getDAOFactory(dataSource).getSnapshotDAO();
		this.profile = profileDao.getFullProfile(new Date());
		this.snapshot = profile;
		this.oldAnomaly = new Anomaly(profile, snapshot);
		
		try {
			FileHandler fh = new FileHandler(dumpFile, append);
			fh.setFormatter(FORMATTER);
			LOGGER.addHandler(fh);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		if (oldAlert == null || subject.getAlert().getHour().toString() != oldAlert.getHour().toString()) {
			ProcessManager.stop(ExternalProcess.PRADS, false);
			
			if (profileDao.isProfileDataEnough(subject.getAlert().getHour())) {	
				profile = profileDao.getProfile(subject.getAlert().getHour()); 
			}
			if (snapshotDao.isSnapshotReady(subject.getAlert().getHour())) { 
				snapshot = snapshotDao.getSnapshot(subject.getAlert().getHour()); 
			} //TODO Attempt threading both profile and snapshot extraction
				
			oldAnomaly = new Anomaly(profile, snapshot);
								
			ProcessManager.start(ExternalProcess.PRADS, false);
			
		}
		
		LOGGER.warning(subject.getAlert().getMessage() 
					+ " ---> Network ANOMALY of: " 
					+ Math.round(oldAnomaly.getAnomaly()) 
					+ "/100");
		
		oldAlert = subject.getAlert();
	}
}
