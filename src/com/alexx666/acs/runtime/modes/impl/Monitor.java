package com.alexx666.acs.runtime.modes.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alexx666.acs.data.dao.connection.impl.JDBCConnectionPool;
import com.alexx666.acs.data.dto.Alert;
import com.alexx666.acs.data.dto.ExternalProcess;
import com.alexx666.acs.runtime.handlers.AlertObserver;
import com.alexx666.acs.runtime.handlers.AlertSubject;
import com.alexx666.acs.runtime.managers.impl.ProcessManager;
import com.alexx666.acs.runtime.modes.Mode;

public class Monitor extends Mode {
	
	@Override
	public void run() {
		
		JDBCConnectionPool.getInstance().setDsn("jdbc:mysql://localhost/" + settings.getTrackers().getDb());
		JDBCConnectionPool.getInstance().setUsr(settings.getTrackers().getUser());
		JDBCConnectionPool.getInstance().setPwd(settings.getTrackers().getPass());
		
		AlertSubject as = new AlertSubject();
		new AlertObserver(as, processManager.get()[2], settings.getOutputs().getFile(), settings.getOutputs().shouldAppend(), settings.getTrackers().getType());
						
		processManager.createAll();
		
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
		/*Path path = Paths.get(PATH);
		if(!Files.exists(path)){
			try {
				File dir = Files.createDirectory(path).toFile();
				dir.setExecutable(true, false);
				dir.setReadable(true, false);
				dir.setWritable(true, false);
			}catch(IOException e) {
				e.printStackTrace();
			}
		}*/
		String fifoName = settings.getOutputs().getFile();
		List<String> commands = new ArrayList<String>();
		commands.add("/bin/sh");
		commands.add("-c");
		commands.add("sudo touch " + fifoName + " && sudo chmod a+rw "+ fifoName);
		ProcessManager.runProcess(commands, false);
	}
}
