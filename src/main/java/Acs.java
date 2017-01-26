package main.java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import main.java.dao.ProfileDao;
import main.java.dao.SnapshotDao;
import main.java.entities.Alert;
import main.java.entities.Anomaly;
import main.java.entities.Statistics;
import main.java.utils.ProcessManager;

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
		
		options.addOption("m", "monitor", false, "Run the network monitoring tools (Suricata and PRADS) and calculate the network anomaly based on the NIDS alerts.");
		options.addOption("p", "profiler", false, "Run Cxtracker to make a profile of the networks traffic.");
		//options.addOption("u", "update", false, "Use Oinkmaster to update Suricata rules.");
		
		try {
			CommandLine cmd = parser.parse(options, args);
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					System.out.println();
					running = false;
					pm.stop(true);
					try {mainThread.join();} 
					catch (InterruptedException e) { e.printStackTrace(); }
				}
			});
			
			if (cmd.getOptions().length != 1) { formatter.printHelp("run-command [option]", options); }
			else if (cmd.hasOption("m")) {	acs.handleAlerts(); }
			else if (cmd.hasOption("p")) {	acs.runProfiler(); }			
		} catch (ParseException e) {
			System.out.println(e.getMessage());
            formatter.printHelp("run-command [option]", options);
		}
	}
	
	private void runProfiler() {
		pm.setProcesses(ExternalProcess.PROFILE2DB,	ExternalProcess.CXTRACKER);
		pm.start(true);
		while(running);
	}
	
	private void handleAlerts() {		
		ProfileDao profileDao = new ProfileDao();
		SnapshotDao snapshotDao = new SnapshotDao();
		Statistics profile = profileDao.getFullProfile();
		Statistics snapshot = profileDao.getFullProfile();
		
		pm.setProcesses(ExternalProcess.SURICATA, ExternalProcess.SNAPSHOT2DB, ExternalProcess.PRADS);
		pm.start(true);
		System.out.println("[acs] Listening...");
		try {
			BufferedReader in = new BufferedReader(new FileReader(FIFO));
			pm.setProcesses(ExternalProcess.PRADS);
			while (running) {
				String line;
				if ((line = in.readLine()) != null) {
					pm.stop(false);
					
					Alert alert = new Alert(line);
					
					System.out.println();
					System.out.print("[acs] Alert recieved: " + alert.getMessage() + " at: [UTC] " + alert.getDate());

					if (profileDao.isProfileDataEnough()) {	profile = profileDao.getProfile(); }
					else{ System.out.print("[P]"); }
					
					if (snapshotDao.isSnapshotReady(alert.getDate())) { snapshot = snapshotDao.getSnapshot(alert.getDate()); }
					else{ System.out.print("[S]"); }
				
					Anomaly anomaly = new Anomaly(profile, snapshot);
					
					System.out.print(" ---> Network ANOMALY of: " + Math.round(anomaly.getAnomaly()) + "/100");
	
					pm.start(false);
				}
			}
			in.close();
			pm.setProcesses(ExternalProcess.SURICATA, ExternalProcess.SNAPSHOT2DB, ExternalProcess.PRADS);
		}catch (IOException ex) {
			LOGGER.warning(ex.getMessage());
		}
	}
}