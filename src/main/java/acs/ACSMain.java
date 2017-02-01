package main.java.acs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import main.java.acs.dao.ProfileDao;
import main.java.acs.dao.SnapshotDao;
import main.java.acs.entities.Alert;
import main.java.acs.entities.Anomaly;
import main.java.acs.entities.Statistics;
import main.java.acs.utils.ProcessManager;

public class ACSMain {
	
	private static final String FIFO = "/var/log/suricata/fast.pipe"; //TODO crear pipe en vez de meterlo a pelo
	private static final String DB = "cxtracker"; //TODO get from YAML file
	private static final String USER = "cxtracker"; //TODO get from YAML file
	private static final String PASS = "cxtracker"; //TODO get from YAML file
	private static final Thread mainThread = Thread.currentThread();
	private static volatile boolean running = true;
	private static volatile ProcessManager pm = new ProcessManager();
	private static Object lock = new Object();
	//private static ACSConfiguration config;

	public ACSMain() {}

	public static void main(String[] args) {
		Options options = new Options();
		CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        ACSMain acs = new ACSMain();
		
		options.addOption("m", "monitor", false, "Run the network monitoring tools (Suricata and PRADS) and calculate the network anomaly based on the NIDS alerts.");
		options.addOption("p", "profiler", false, "Run Cxtracker to make a profile of the networks traffic.");
		options.addOption("u", "update", false, "Use Oinkmaster to update Suricata rules.");
		
		//config = new ACSConfiguration("acs.yml");
		//config.init();
		
		try {
			CommandLine cmd = parser.parse(options, args);
			
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {					
					try {
						System.out.println();
						pm.stopAll(true);
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
			else if (cmd.hasOption("p")) {	
				pm.setProcesses(ExternalProcess.PROFILE2DB,	ExternalProcess.CXTRACKER);
				pm.startAll(true);
				System.out.println("[acs] Profiling network traffic...");
				while(running);
			}	
			else if (cmd.hasOption("u")) {
				pm.setProcesses(ExternalProcess.OINKMASTER);
				pm.startAll(true);
			}
		}catch (ParseException e) {
			System.out.println(e.getMessage());
            formatter.printHelp("acs [option]", options);
		}
	}
	
	private void runAlertHandler() {
		pm.setProcesses(ExternalProcess.SURICATA, ExternalProcess.SNAPSHOT2DB, ExternalProcess.PRADS);
		pm.startAll(true);
		
		ProfileDao profileDao = new ProfileDao(DB, USER, PASS);
		SnapshotDao snapshotDao = new SnapshotDao(DB, USER, PASS);
		Statistics profile = profileDao.getFullProfile();
		Statistics snapshot = profile;
		
		System.out.println("[acs] Connecting to Suricata...");
		try {
			BufferedReader in = new BufferedReader(new FileReader(FIFO));
			System.out.println("[acs] Listening for alerts...");
			synchronized (lock) {
				while (running) {
					String line;
					if ((line = in.readLine()) != null) {
						pm.stop(ExternalProcess.PRADS, false);
						Alert alert = new Alert(line);
						System.out.println();
						System.out.print("[acs] Alert recieved: " + alert.getMessage() + " at: [UTC] " + alert.getDate());
						
						if (profileDao.isProfileDataEnough()) {	profile = profileDao.getProfile(); }
						else System.out.print("[P]");
						if (snapshotDao.isSnapshotReady(alert.getDate())) { snapshot = snapshotDao.getSnapshot(alert.getDate()); }
						else System.out.print("[S]"); 
					
						Anomaly anomaly = new Anomaly(profile, snapshot);
						System.out.print(" ---> Network ANOMALY of: " + Math.round(anomaly.getAnomaly()) + "/100");
		
						pm.start(ExternalProcess.PRADS, false);
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