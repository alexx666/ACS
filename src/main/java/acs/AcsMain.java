package main.java.acs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import main.java.acs.dao.ProfileDao;
import main.java.acs.dao.SnapshotDao;
import main.java.acs.entities.Alert;
import main.java.acs.entities.Anomaly;
import main.java.acs.entities.Statistics;
import main.java.acs.utils.ProcessManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class AcsMain {
	
	private static final String FIFO = "/var/log/suricata/fast.pipe"; //TODO crear pipe en vez de meterlo a pelo
	private static final Thread mainThread = Thread.currentThread();
	private static volatile ProcessManager pm = new ProcessManager();
	private static volatile boolean running = true;
	private static Object lock = new Object();

	public AcsMain() {}

	public static void main(String[] args) {
		
		Options options = new Options();
		CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        AcsMain acs = new AcsMain();
		
		options.addOption("m", "monitor", false, "Run the network monitoring tools (Suricata and PRADS) and calculate the network anomaly based on the NIDS alerts.");
		options.addOption("p", "profiler", false, "Run Cxtracker to make a profile of the networks traffic.");
		options.addOption("u", "update", false, "Use Oinkmaster to update Suricata rules.");
		
		try {
			CommandLine cmd = parser.parse(options, args);
			
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {					
					try {
						System.out.println();
						pm.setProcesses(ExternalProcess.SURICATA, ExternalProcess.SNAPSHOT2DB, ExternalProcess.PRADS);
						pm.stop(true);
						synchronized (lock) {
							running = false;
							lock.notifyAll();
						}
						mainThread.join();
					}catch (InterruptedException e) { e.printStackTrace(); }					
				}
			}));
			
			if (cmd.getOptions().length != 1) { formatter.printHelp("acs [option]", options); }
			else if (cmd.hasOption("m")) {	acs.runAlertHandler(); }
			else if (cmd.hasOption("p")) {	acs.runProfiler(); }	
			else if (cmd.hasOption("u")) { acs.runUpdater(); }
		} catch (ParseException e) {
			System.out.println(e.getMessage());
            formatter.printHelp("run-command [option]", options);
		}
	}
	
	private void runUpdater() {
		pm.setProcesses(ExternalProcess.OINKMASTER);
		pm.start(true);
	}
	
	private void runProfiler() {
		pm.setProcesses(ExternalProcess.PROFILE2DB,	ExternalProcess.CXTRACKER);
		pm.start(true);
		System.out.println("[acs] Profiling network traffic...");
		while(running);
		pm.stop(true);
	}
	
	private void runAlertHandler() {
		pm.setProcesses(ExternalProcess.SURICATA, ExternalProcess.SNAPSHOT2DB, ExternalProcess.PRADS);
		pm.start(true);
		
		ProfileDao profileDao = new ProfileDao();
		SnapshotDao snapshotDao = new SnapshotDao();
		
		System.out.println("[acs] Connecting to Suricata...");
		try {
			BufferedReader in = new BufferedReader(new FileReader(FIFO));
			System.out.println("[acs] Listening for alerts...");
			
			pm.setProcesses(ExternalProcess.PRADS);
			synchronized (lock) {
				while (running) {
					String line;
					if ((line = in.readLine()) != null) {
						pm.stop(false);
						
						Statistics profile = null;
						Statistics snapshot = null;
						
						Alert alert = new Alert(line);
						
						System.out.println();
						System.out.print("[acs] Alert recieved: " + alert.getMessage() + " at: [UTC] " + alert.getDate());
						
						if (profileDao.isProfileDataEnough()) {	
							profile = profileDao.getProfile(); 
						}else{ 
							System.out.print("[P]"); 
							profile = profileDao.getFullProfile();
						}
						
						if (snapshotDao.isSnapshotReady(alert.getDate())) { 
							snapshot = snapshotDao.getSnapshot(alert.getDate()); 
						}else{ 
							System.out.print("[S]"); 
							snapshot = profileDao.getFullProfile();
						}
					
						Anomaly anomaly = new Anomaly(profile, snapshot);
						
						System.out.print(" ---> Network ANOMALY of: " + Math.round(anomaly.getAnomaly()) + "/100");
		
						pm.start(false);
					}
					lock.wait(200);
				}
			}
			in.close();
		}catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}