package main.java.com.alexx666.acs.configuration.impl;

import main.java.com.alexx666.acs.configuration.Mode;
import main.java.com.alexx666.acs.db.dto.config.ExternalProcess;

public class Updater extends Mode {

	@Override
	public void run() {
		processManager.createAll(true);
	}

	@Override
	public void setExternalProcess() {
		String oinkmasterCommand = "oinkmaster -o " + settings.getSuricata().getRules() + " -q -s";	
		
		ExternalProcess oinkmaster = new ExternalProcess("Oinkmaster", "oinkmaster", oinkmasterCommand);
		
		processManager.set(oinkmaster);
	}

	@Override
	public void manageIO() {
		// TODO Auto-generated method stub
	}
}
