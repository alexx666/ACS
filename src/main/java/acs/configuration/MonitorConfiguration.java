package main.java.acs.configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import main.java.acs.dao.ProfileDao;
import main.java.acs.dao.SnapshotDao;
import main.java.acs.entities.Alert;
import main.java.acs.entities.Anomaly;
import main.java.acs.entities.Statistics;
import main.java.acs.utils.process.ExternalProcess;

public class MonitorConfiguration extends Configuration {
	
	@Override
	public void run() {
		getPm().setProcesses(ExternalProcess.SURICATA, ExternalProcess.SNAPSHOT2DB, ExternalProcess.PRADS);
		getPm().startAll(true);
		
		ProfileDao profileDao = new ProfileDao("cxtracker", "cxtracker", "cxtracker");
		SnapshotDao snapshotDao = new SnapshotDao("cxtracker", "cxtracker", "cxtracker");
		Statistics profile = profileDao.getFullProfile();
		Statistics snapshot = profile;
		
		System.out.println("[acs] Connecting to Suricata...");
		try {
			BufferedReader in = new BufferedReader(new FileReader("/var/log/suricata/fast.pipe"));
			System.out.println("[acs] Listening for alerts...");
			synchronized (getLock()) {
				while (running) {
					String line;
					if ((line = in.readLine()) != null) {
						getPm().stop(ExternalProcess.PRADS, false);
						Alert alert = new Alert(line);
						System.out.println();
						System.out.print("[acs] Alert recieved: " + alert.getMessage() + " at: [UTC] " + alert.getDate());
						
						if (profileDao.isProfileDataEnough()) {	profile = profileDao.getProfile(); }
						else System.out.print("[P]");
						if (snapshotDao.isSnapshotReady(alert.getDate())) { snapshot = snapshotDao.getSnapshot(alert.getDate()); }
						else System.out.print("[S]"); 
					
						Anomaly anomaly = new Anomaly(profile, snapshot);
						System.out.print(" ---> Network ANOMALY of: " + Math.round(anomaly.getAnomaly()) + "/100");
		
						getPm().start(ExternalProcess.PRADS, false);
					}
					getLock().wait(200);
				}
			}
			in.close();
		}catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}
