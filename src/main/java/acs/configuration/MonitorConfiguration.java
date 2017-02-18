package main.java.acs.configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import main.java.acs.entities.Alert;
import main.java.acs.handlers.AlertObserver;
import main.java.acs.handlers.AlertSubject;
import main.java.acs.utils.process.ExternalProcess;

public class MonitorConfiguration extends Configuration {
	
	@Override
	public void run() {
		AlertSubject as = new AlertSubject();
		new AlertObserver(as);
				
		getPm().setProcesses(ExternalProcess.SURICATA, ExternalProcess.SNAPSHOT2DB, ExternalProcess.PRADS);
		getPm().startAll(true);
		
		System.out.println("[acs] Connecting to Suricata...");
		try {
			BufferedReader in = new BufferedReader(new FileReader("/var/log/suricata/fast.pipe"));
			System.out.println("[acs] Listening for alerts...");
			synchronized (getLock()) {
				while (running) {
					String line;
					if ((line = in.readLine()) != null) {
						getPm().stop(ExternalProcess.PRADS, false);
						
						as.setAlert(new Alert(line));
						
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
