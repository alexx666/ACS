package com.alexx666.acs.configuration.impl;

import com.alexx666.acs.configuration.Configuration;
import com.alexx666.acs.configuration.ConfigurationFactory;
import com.alexx666.acs.db.dto.config.ExternalProcess;

public class Updater extends Configuration {

	@Override
	public void run() {
		processManager.createAll(true);
	}

	@Override
	public void setExternalProcesses() {
		String oinkmasterCommand = "oinkmaster -o " + 
				ConfigurationFactory.getInstance().getSettings().getSuricata().getRules() + " -q -s";	
		
		ExternalProcess oinkmaster = new ExternalProcess("Oinkmaster", "oinkmaster", oinkmasterCommand);
		
		processManager.set(oinkmaster);
	}

	@Override
	public void manageIO() {
		// TODO Auto-generated method stub
	}
}
