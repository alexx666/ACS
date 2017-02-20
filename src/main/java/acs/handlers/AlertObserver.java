package main.java.acs.handlers;

import main.java.acs.db.dao.ProfileDao;
import main.java.acs.db.dao.SnapshotDao;
import main.java.acs.entities.Anomaly;
import main.java.acs.entities.Statistics;

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
		System.out.println();
		System.out.print("[acs] Alert recieved: " 
				+ subject.getAlert().getMessage() 
				+ " at: [UTC] " + subject.getAlert().getDate());
		
		if (profileDao.isProfileDataEnough()) {	
			profile = profileDao.getProfile(); 
		}else System.out.print("[P]");
		
		if (snapshotDao.isSnapshotReady(subject.getAlert().getDate())) { 
			snapshot = snapshotDao.getSnapshot(subject.getAlert().getDate()); 
		}else System.out.print("[S]"); 
	
		System.out.print(" ---> Network ANOMALY of: " 
				+ Math.round((new Anomaly(profile, snapshot)).getAnomaly()) 
				+ "/100");
	}
}
