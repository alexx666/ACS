package main.java.acs.configuration.run.impl;

import main.java.acs.configuration.run.RunConfiguration;
import main.java.acs.utils.process.ExternalProcess;

public class Update extends RunConfiguration {

	@Override
	public void run() {
		processManager.setProcesses(ExternalProcess.OINKMASTER);
		processManager.startAll(true);
	}
}
