package main.java.acs.configuration.running;

import main.java.acs.process.ExternalProcess;

public class Update extends RunConfiguration {

	@Override
	public void run() {
		getPm().setProcesses(ExternalProcess.OINKMASTER);
		getPm().startAll(true);
	}
}
