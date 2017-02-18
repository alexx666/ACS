package main.java.acs.handlers;

import main.java.acs.dao.ProfileDao;
import main.java.acs.dao.SnapshotDao;
import main.java.acs.entities.Anomaly;
import main.java.acs.entities.Statistics;

public class AlertObserver implements Observer {
	
	private AlertSubject subject;
	
	private ProfileDao profileDao;
	private SnapshotDao snapshotDao;
	private Statistics profile;
	private Statistics snapshot;
	
	public AlertObserver(AlertSubject subject) {
		this.subject = subject;
		this.subject.addObserver(this);
		
		profileDao = new ProfileDao("cxtracker", "cxtracker", "cxtracker");
		snapshotDao = new SnapshotDao("cxtracker", "cxtracker", "cxtracker");
		profile = profileDao.getFullProfile();
		snapshot = profile;
	}

	@Override
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
