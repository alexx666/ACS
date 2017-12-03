package com.alexx666.acs.configuration.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import com.alexx666.acs.configuration.Mode;
import com.alexx666.acs.db.connection.impl.JDBCConnectionPool;
import com.alexx666.acs.db.dto.alerts.impl.SuricataAlert;
import com.alexx666.acs.db.dto.config.ExternalProcess;
import com.alexx666.acs.manager.impl.FileManager;
import com.alexx666.acs.manager.impl.ProcessManager;
import com.alexx666.acs.observer.impl.AlertObserver;
import com.alexx666.acs.subject.impl.AlertSubject;

public class Monitor extends Mode {
	
	private String dumpfile;
	
	@Override
	public void run() {
		
		JDBCConnectionPool.getInstance().setDsn("jdbc:mysql://localhost/" + settings.getTrackers().getDb());
		JDBCConnectionPool.getInstance().setUsr(settings.getTrackers().getUser());
		JDBCConnectionPool.getInstance().setPwd(settings.getTrackers().getPass());
			
		AlertSubject as = new AlertSubject();
		new AlertObserver(as, processManager.get()[2], dumpfile, settings.getOutputs().shouldAppend(), settings.getTrackers().getType());
						
		processManager.createAll(true);
		
		try {
			File inputFile = new File(settings.getSuricata().getFast());
			
			System.out.println("[acs] Connecting to Suricata...");
			while (inputFile.length()!=0);
			
			FileReader fr = new FileReader(inputFile);
			BufferedReader in = new BufferedReader(fr);
			
			System.out.println("[acs] Listening for alerts...");
			synchronized (lock) {
				while (running) {
					String line;
					if ((line = in.readLine()) != null) {
						as.setAlert(new SuricataAlert(line));
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
	public void setExternalProcess() {
		String suricataCommand = "suricata -c " 
				+ settings.getSuricata().getYaml() + " --pcap=" 
				+ settings.getSuricata().getInet() + " -D";
		String snapshotCommand = "snapshot2db.pl " 
				+ "--dir " + settings.getTrackers().getLogs() + "/prads/" 
				+ settings.getTrackers().getInet() + "/sessions/ --daemon";
		String pradsCommand = "prads -i " 
				+ settings.getTrackers().getInet() + " -L "
				+ settings.getTrackers().getLogs() + "/prads/"
				+ settings.getTrackers().getInet() + "/sessions/ -D";
				
		ExternalProcess suricata = new ExternalProcess("Suricata", "Suricata-Main", suricataCommand);
		ExternalProcess snapshot = new ExternalProcess("Snapshot2DB", "snapshot2db.pl", snapshotCommand);
		ExternalProcess prads = new ExternalProcess("PRADS", "prads", pradsCommand);
		
		processManager.set(suricata, snapshot, prads);
	}

	@Override
	public void manageIO() {
		dumpfile = settings.getOutputs().getFile() + "/acs_" + Calendar.getInstance().getTimeInMillis() + ".log";

		//Paths		
		Path outputFile = Paths.get(dumpfile);
		Path outputDir = Paths.get(settings.getOutputs().getFile());
		Path prads = Paths.get(settings.getTrackers().getLogs() + "/prads");
		Path eth = Paths.get(settings.getTrackers().getLogs() + "/prads/" + settings.getTrackers().getInet());
		Path sessions = Paths.get(settings.getTrackers().getLogs() + "/prads/" + settings.getTrackers().getInet() + "/sessions");
		Path failed = Paths.get(settings.getTrackers().getLogs() + "/prads/" + settings.getTrackers().getInet() + "/sessions/failed");

		//Tracker Directories
		FileManager fm = new FileManager();
		fm.set(prads, eth, sessions, failed, outputDir);
		fm.createAll(true);
		
		//Output File
		fm.set(outputFile);
		fm.createAll(false);
	}
}
