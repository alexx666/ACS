package main.java.acs.configuration.running;

import main.java.acs.process.ExternalProcess;

public class Profile extends RunConfiguration {

	@Override
	public void run() {
		processManager.setProcesses(ExternalProcess.PROFILE2DB, ExternalProcess.CXTRACKER);
		processManager.startAll(true);
		System.out.println("[acs] Profiling network traffic...");
		while(running);
	}
}