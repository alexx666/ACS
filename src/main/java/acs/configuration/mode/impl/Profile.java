package main.java.acs.configuration.mode.impl;

import main.java.acs.configuration.mode.RunningMode;
import main.java.acs.utils.process.ExternalProcess;

public class Profile extends RunningMode {

	@Override
	public void run() {
		processManager.setProcesses(ExternalProcess.PROFILE2DB, ExternalProcess.CXTRACKER);
		processManager.startAll(true);
		System.out.println("[acs] Profiling network traffic...");
		while(running);
	}
}
