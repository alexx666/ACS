package com.alexx666.acs.runtime.modes.impl;

import com.alexx666.acs.data.dto.ExternalProcess;
import com.alexx666.acs.runtime.modes.Mode;

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
