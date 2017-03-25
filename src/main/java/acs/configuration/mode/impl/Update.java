package main.java.acs.configuration.mode.impl;

import main.java.acs.configuration.mode.RunningMode;
import main.java.acs.utils.process.ExternalProcess;

public class Update extends RunningMode {

	@Override
	public void run() {
		processManager.setProcesses(ExternalProcess.OINKMASTER);
		processManager.startAll(true);
	}
}
