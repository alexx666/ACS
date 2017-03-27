package main.java.acs.modes.run.impl;

import main.java.acs.modes.process.ExternalProcess;
import main.java.acs.modes.run.RunningMode;

public class Profiler extends RunningMode {

	@Override
	public void run() {
		processManager.setProcesses(ExternalProcess.PROFILE2DB, ExternalProcess.CXTRACKER);
		processManager.startAll(true);
		System.out.println("[acs] Profiling network traffic...");
		while(running);
	}

	@Override
	public void setExternalProcessCommands() {		
		String profileCommand = "profile2db.pl " 
				+ "--dir " + settings.getTrackers().getLogs() + "/cxtracker/" 
				+ settings.getTrackers().getInet() + "/sessions/ --daemon";
		String cxtrackerCommand = "cxtracker -i " 
				+ settings.getTrackers().getInet() + " -d "
				+ settings.getTrackers().getLogs() + "/cxtracker/"
				+ settings.getTrackers().getInet() + "/sessions/ -D";
		
		ExternalProcess.PROFILE2DB.setCommand(profileCommand);
		ExternalProcess.CXTRACKER.setCommand(cxtrackerCommand);
	}

	@Override
	public void createOutputFile() {
		// TODO Auto-generated method stub
	}
}
