package com.alexx666.acs.configuration.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import com.alexx666.acs.configuration.Configuration;
import com.alexx666.acs.configuration.ConfigurationFactory;
import com.alexx666.acs.db.dto.alerts.impl.SuricataAlert;
import com.alexx666.acs.db.dto.config.ExternalProcess;
import com.alexx666.acs.manager.impl.ProcessManager;
import com.alexx666.acs.observer.impl.AlertObserver;
import com.alexx666.acs.subject.Subject;
import com.alexx666.acs.subject.impl.AlertSubject;

public class Monitor extends Configuration {
	
	private Subject alertSubject;
	
	public Monitor() {
		super();
		this.alertSubject = new AlertSubject();
		new AlertObserver(alertSubject);
	}
	
	@Override
	public void run() {
								
		processManager.createAll(true);
		
		try {
			File inputFile = new File(ConfigurationFactory.getInstance().getSettings().getSuricata().getFast());
			
			System.out.println("[acs] Connecting to Suricata...");
			while (inputFile.length()!=0);
			
			FileReader fr = new FileReader(inputFile);
			BufferedReader in = new BufferedReader(fr);
			
			System.out.println("[acs] Listening for alerts...");
			synchronized (lock) {
				while (running) {
					String line;
					ProcessManager.create(processManager.get()[2], false);
					if ((line = in.readLine()) != null) {
						ProcessManager.destroy(processManager.get()[2], false);
						alertSubject.setAlert(new SuricataAlert(line));
					}
					lock.wait(10); //TODO Optimize wait time
				}
			}
			in.close();
		}catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
		}finally{
			ProcessManager.destroy(processManager.get()[2], false);
		}
	}

	@Override
	public void manageIO() {
		String dumpfile = ConfigurationFactory.getInstance().getSettings().getOutputs().getFile() + 
				"/acs_" + Calendar.getInstance().getTimeInMillis() + ".log";

		//Paths		
		Path outputFile = Paths.get(dumpfile);
		Path outputDir = Paths.get(ConfigurationFactory.getInstance().getSettings().getOutputs().getFile());
		
		Path prads = Paths.get(ConfigurationFactory.getInstance().getSettings().getTrackers().getLogs() + "/prads");
		Path pradsEth = Paths.get(prads + ConfigurationFactory.getInstance().getSettings().getTrackers().getInet());
		Path pradsSessions = Paths.get(pradsEth + "/sessions");
		Path pradsFailed = Paths.get(pradsSessions + "/failed");
		
		//Tracker Directories
		fm.set(prads, pradsEth, pradsSessions, pradsFailed, outputDir);
		fm.createAll(true);
		
		//Output File
		fm.set(outputFile);
		fm.createAll(false);
	}

	@Override
	public void setExternalProcesses() {
		String suricataCommand = "suricata -c " 
				+ ConfigurationFactory.getInstance().getSettings().getSuricata().getYaml() + " --pcap=" 
				+ ConfigurationFactory.getInstance().getSettings().getSuricata().getInet() + " -D";
		String snapshotCommand = "snapshot2db.pl " 
				+ "--dir " + ConfigurationFactory.getInstance().getSettings().getTrackers().getLogs() + "/prads/" 
				+ ConfigurationFactory.getInstance().getSettings().getTrackers().getInet() + "/sessions/ --daemon";
		String pradsCommand = "prads -i " 
				+ ConfigurationFactory.getInstance().getSettings().getTrackers().getInet() + " -L "
				+ ConfigurationFactory.getInstance().getSettings().getTrackers().getLogs() + "/prads/"
				+ ConfigurationFactory.getInstance().getSettings().getTrackers().getInet() + "/sessions/ -D";
				
		ExternalProcess suricata = new ExternalProcess("Suricata", "Suricata-Main", suricataCommand);
		ExternalProcess snapshot = new ExternalProcess("Snapshot2DB", "snapshot2db.pl", snapshotCommand);
		ExternalProcess prads = new ExternalProcess("PRADS", "prads", pradsCommand);
		
		processManager.set(suricata, snapshot, prads);
	}
}
