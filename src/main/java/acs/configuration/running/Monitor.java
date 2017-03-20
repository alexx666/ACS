package main.java.acs.configuration.running;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import main.java.acs.entities.Alert;
import main.java.acs.handlers.AlertObserver;
import main.java.acs.handlers.AlertSubject;
import main.java.acs.io.Fifo;
import main.java.acs.process.ExternalProcess;

public class Monitor extends RunConfiguration {
	
	@Override
	public void run() {
		AlertSubject as = new AlertSubject();
		new AlertObserver(as);
		
		Fifo.createFifoPipe();
				
		processManager.setProcesses(ExternalProcess.SURICATA, ExternalProcess.SNAPSHOT2DB, ExternalProcess.PRADS);
		processManager.startAll(true);
		
		System.out.println("[acs] Connecting to Suricata...");
		try {
			FileReader fr = new FileReader(Fifo.getFifoPipe());
			BufferedReader in = new BufferedReader(fr);
			System.out.println("[acs] Listening for alerts...");
			synchronized (lock) {
				while (running) {
					String line;
					if ((line = in.readLine()) != null) {
						processManager.stop(ExternalProcess.PRADS, false);
						as.setAlert(new Alert(line));
						processManager.start(ExternalProcess.PRADS, false);
					}
					lock.wait(5);
				}
			}
			in.close();
			Fifo.removeFifoPipe();
			processManager.stopAll(false);
		}catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
		}
	}
}
