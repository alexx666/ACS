package main.java.acs.utils.handlers;

import main.java.acs.data.dao.ProfileDao;
import main.java.acs.data.dao.SnapshotDao;
import main.java.acs.data.entities.Anomaly;
import main.java.acs.data.entities.Statistics;
import main.java.acs.utils.process.ExternalProcess;
import main.java.acs.utils.process.ProcessManager;

public class AlertObserver {
	
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
	}

	public void update() {
		
		ProcessManager.stop(ExternalProcess.PRADS, false);
		
		if (profileDao.isProfileDataEnough()) {	profile = profileDao.getProfile(); }
		if (snapshotDao.isSnapshotReady(subject.getAlert().getDate())) { 
			snapshot = snapshotDao.getSnapshot(subject.getAlert().getDate()); 
		} 
		
		System.out.println("[acs] Alert recieved: " 
				+ subject.getAlert().getMessage() 
				+ " at: [UTC] " + subject.getAlert().getDate()
				+ " ---> Network ANOMALY of: " 
				+ Math.round((new Anomaly(profile, snapshot)).getAnomaly()) 
				+ "/100");
			
		ProcessManager.start(ExternalProcess.PRADS, false);
	}
}
