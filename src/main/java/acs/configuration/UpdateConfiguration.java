package main.java.acs.configuration;

import main.java.acs.utils.process.ExternalProcess;

public class UpdateConfiguration extends Configuration {

	public UpdateConfiguration() {
		//TODO
	}

	@Override
	public void run() {
		getPm().setProcesses(ExternalProcess.OINKMASTER);
		getPm().startAll(true);
	}
}
