package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import main.dao.ProfileDao;
import main.dao.SnapshotDao;
import main.entities.Alert;
import main.entities.Anomaly;
import main.entities.Statistics;
import main.utils.ProcessManager;

public class ACS {
	
	private static final String FIFO = "/var/log/suricata/fast.pipe"; //TODO crear pipe en vez de meterlo a pelo
	private static final Thread mainThread = Thread.currentThread();
	
	private static Logger LOGGER = Logger.getLogger(mainThread.getStackTrace()[0].getClassName());
	private static ExternalProcess [] up = {ExternalProcess.OINKMASTER};
	private static ExternalProcess [] pp = {ExternalProcess.PROFILE2DB, ExternalProcess.CXTRACKER};
	private static ExternalProcess [] mp = {ExternalProcess.SURICATA, ExternalProcess.SNAPSHOT2DB, ExternalProcess.PRADS};
	
	static volatile boolean running = true;

	public ACS() {}

	public static void main(String[] args) {
		
		ACS acs = new ACS();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				running = false;
				ProcessManager.stop(mp, true);				
				try {mainThread.join();} 
				catch (InterruptedException e) { e.printStackTrace(); }
			}
		});
		
		acs.manage(Options.NIDS, true);
	}

	private void manage(Options option, boolean verbose) {
		switch (option){
		case NIDS:
			System.out.println("[acs] Inicializing tools...");
			ProcessManager.start(mp, verbose);
			handleAlerts();
			break;
		case PROFILES:
			System.out.println("[acs] Inicializing tools...");
			ProcessManager.start(pp, verbose);
			System.out.println("[acs] Profiling...");
			break;
		case RULES:
			System.out.println("[acs] Inicializing update...");
			ProcessManager.start(up, verbose);
			System.out.println("[acs] Update finnished.");
			break;
		}
	}
	
	private void handleAlerts() {
		ProfileDao profileDao = new ProfileDao();
		SnapshotDao snapshotDao = new SnapshotDao();
		Statistics profile = profileDao.getFullProfile();
		Statistics snapshot = profileDao.getFullProfile();
		ExternalProcess [] prads = {ExternalProcess.PRADS};
		
		System.out.println("[acs] Listening...");
		try {
			BufferedReader in = new BufferedReader(new FileReader(FIFO));
			while (running) {
				String line;
				if ((line = in.readLine()) != null) {
					
					ProcessManager.stop(prads, false);
					
					Alert alert = new Alert(line);
					
					System.out.println();
					System.out.print("[acs] Alert recieved: " + alert.getMessage() + " at: [UTC] " + alert.getDate());

					if (profileDao.isProfileDataEnough()) {	profile = profileDao.getProfile(); }
					else{ System.out.print("[P]"); }
					if (snapshotDao.isSnapshotReady(alert.getDate())) { snapshot = snapshotDao.getSnapshot(alert.getDate()); }
					else{ System.out.print("[S]"); }
				
					System.out.print(" ---> Network ANOMALY of: " + Math.round((new Anomaly(profile, snapshot)).getAnomaly()) + "/100");
	
					ProcessManager.start(prads, false);
				}
			}
			in.close();
		}catch (IOException ex) {
			LOGGER.warning(ex.getMessage());
		}
	}
}