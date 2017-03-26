package main.java.acs.modes.run.impl;

import main.java.acs.modes.process.ExternalProcess;
import main.java.acs.modes.run.RunningMode;

public class Updater extends RunningMode {

	@Override
	public void run() {
		processManager.setProcesses(ExternalProcess.OINKMASTER);
		processManager.startAll(true);
	}

	@Override
	public void setExternalProcessCommands() {
		String oinkmasterCommand = "oinkmaster -o " + settings.getSuricata().getRules() + " -q -s";	
		ExternalProcess.OINKMASTER.setCommand(oinkmasterCommand);
	}

	@Override
	public void createOutputFile() {
		// TODO Auto-generated method stub
	}
}
