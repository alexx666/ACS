package main.java.acs.configuration;

public class ProfileConfiguration extends Configuration {
	
	public ProfileConfiguration() {
		//TODO
	}

	@Override
	public void run() {
		getPm().setProcesses(ExternalProcess.PROFILE2DB, ExternalProcess.CXTRACKER);
		getPm().startAll(true);
		System.out.println("[acs] Profiling network traffic...");
		while(running);
	}
}
