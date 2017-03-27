package main.java.acs.modes.run.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.java.acs.data.dao.connection.impl.JDBCConnectionPool;
import main.java.acs.data.dto.Alert;
import main.java.acs.modes.handlers.AlertObserver;
import main.java.acs.modes.handlers.AlertSubject;
import main.java.acs.modes.process.ExternalProcess;
import main.java.acs.modes.process.ProcessManager;
import main.java.acs.modes.run.RunningMode;

public class Monitor extends RunningMode {
	
	@Override
	public void run() {
		
		JDBCConnectionPool.getInstance().setDsn("jdbc:mysql://localhost/" + settings.getTrackers().getDb());
		JDBCConnectionPool.getInstance().setUsr(settings.getTrackers().getUser());
		JDBCConnectionPool.getInstance().setPwd(settings.getTrackers().getPass());
		
		AlertSubject as = new AlertSubject();
		new AlertObserver(as, settings.getOutputs().getFile(), settings.getOutputs().shouldAppend(), settings.getTrackers().getType());
						
		processManager.setProcesses(ExternalProcess.SURICATA, ExternalProcess.SNAPSHOT2DB, ExternalProcess.PRADS);
		processManager.startAll(true);
		
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
						as.setAlert(new Alert(line));
					}
					lock.wait(10); //TODO Optimize wait time
				}
			}
			in.close();
		}catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
		}finally{
			ProcessManager.stop(ExternalProcess.PRADS, false);
		}
	}

	@Override
	public void setExternalProcessCommands() {
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
		
		ExternalProcess.SURICATA.setCommand(suricataCommand);
		ExternalProcess.SNAPSHOT2DB.setCommand(snapshotCommand);
		ExternalProcess.PRADS.setCommand(pradsCommand);
	}

	@Override
	public void createOutputFile() {
		String fifoName = settings.getOutputs().getFile();
		List<String> commands = new ArrayList<String>();
		commands.add("/bin/sh");
		commands.add("-c");
		commands.add("sudo touch " + fifoName + " && sudo chmod a+rw "+ fifoName);
		ProcessManager.runProcess(commands, false);
	}
}
