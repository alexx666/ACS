package main.java.acs.configuration.run.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import main.java.acs.configuration.run.RunConfiguration;
import main.java.acs.configuration.yaml.YMLConfiguration;
import main.java.acs.data.entities.Alert;
import main.java.acs.utils.handlers.AlertObserver;
import main.java.acs.utils.handlers.AlertSubject;
import main.java.acs.utils.process.ExternalProcess;
import main.java.acs.utils.process.ProcessManager;

public class Monitor extends RunConfiguration {
	
	@Override
	public void run() {
		AlertSubject as = new AlertSubject();
		new AlertObserver(as);
						
		processManager.setProcesses(ExternalProcess.SURICATA, ExternalProcess.SNAPSHOT2DB, ExternalProcess.PRADS);
		processManager.startAll(true);
		
		try {
			File inputFile = new File(YMLConfiguration.getInstance().getTools().suricata.fast);
			
			System.out.println("[acs] Connecting to Suricata...");
			while (inputFile.length()!=0);
			
			FileReader fr = new FileReader(inputFile);
			BufferedReader in = new BufferedReader(fr);
			
			System.out.println("[acs] Listening for alerts...");
			synchronized (lock) {
				while (running) {
					String line;
					if ((line = in.readLine()) != null) {
						as.setAlert(new Alert(line));
					}
					lock.wait(10);
				}
			}
			in.close();
		}catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
		}finally{
			ProcessManager.stop(ExternalProcess.PRADS, false);
		}
	}
}
