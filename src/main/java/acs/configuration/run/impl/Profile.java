package main.java.acs.configuration.run.impl;

import main.java.acs.configuration.run.RunConfiguration;
import main.java.acs.utils.process.ExternalProcess;

public class Profile extends RunConfiguration {

	@Override
	public void run() {
		processManager.setProcesses(ExternalProcess.PROFILE2DB, ExternalProcess.CXTRACKER);
		processManager.startAll(true);
		System.out.println("[acs] Profiling network traffic...");
		while(running);
	}
}
