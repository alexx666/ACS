package main.java.acs.configuration;

import main.java.acs.utils.process.ExternalProcess;

public class ProfileConfiguration extends Configuration {

	@Override
	public void run() {
		getPm().setProcesses(ExternalProcess.PROFILE2DB, ExternalProcess.CXTRACKER);
		getPm().startAll(true);
		System.out.println("[acs] Profiling network traffic...");
		while(running);
	}
}
