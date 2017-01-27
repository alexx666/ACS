package main.java.acs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

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

public class Acs {
	
	private static final String FIFO = "/var/log/suricata/fast.pipe"; //TODO crear pipe en vez de meterlo a pelo
	private static final Thread mainThread = Thread.currentThread();
	private static Logger LOGGER = Logger.getLogger(mainThread.getStackTrace()[0].getClassName());
	private static ProcessManager pm = new ProcessManager();
	private static volatile boolean running = true;

	public Acs() {}

	public static void main(String[] args) {
		
		Options options = new Options();
		CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
		Acs acs = new Acs();
		
		options.addOption("m", "monitor", false, "Calculate network anomaly based on the NIDS alerts.");
		options.addOption("p", "profile", false, "Create network profiles using the connection tracker.");
		options.addOption("u", "update", false, "Run Oinkmaster to update NIDS rules.");
		
		try {
			CommandLine cmd = parser.parse(options, args);
			
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println();
					running = false;
					try {mainThread.join();} 
					catch (InterruptedException e) { e.printStackTrace(); }					
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

		System.out.println("[acs] Listening for Suricata alerts...");
		try {
			BufferedReader in = new BufferedReader(new FileReader(FIFO));
			pm.setProcesses(ExternalProcess.PRADS);
			while (running) {
				String line;
				if ((line = in.readLine()) != null) {
					pm.stop(false);
					
					Alert alert = new Alert(line);
					
					System.out.println();
					System.out.print("[acs] Alert type: " + alert.getMessage() + " at: [UTC] " + alert.getDate());
					
					Statistics profile = profileDao.getFullProfile(); 
					Statistics snapshot = profile;
					
					if (profileDao.isProfileDataEnough()) {	
						profile = profileDao.getProfile(); 
					}else{ 
						System.out.print("[P]"); 
					}
					
					if (snapshotDao.isSnapshotReady(alert.getDate())) { 
						snapshot = snapshotDao.getSnapshot(alert.getDate()); 
					}else{ 
						System.out.print("[S]"); 
					}
				
					Anomaly anomaly = new Anomaly(profile, snapshot);
					
					System.out.print(" > ANOMALY: " + Math.round(anomaly.getAnomaly()) + "/100");
	
					pm.start(false);
				}
			}
			in.close();
		}catch (IOException ex) {
			LOGGER.warning(ex.getMessage());
		}finally{
			pm.setProcesses(ExternalProcess.SURICATA, ExternalProcess.SNAPSHOT2DB, ExternalProcess.PRADS);
			pm.stop(true);
		}
	}
}