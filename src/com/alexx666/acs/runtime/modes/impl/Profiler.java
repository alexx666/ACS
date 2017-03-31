package com.alexx666.acs.runtime.modes.impl;

import com.alexx666.acs.data.dto.ExternalProcess;
import com.alexx666.acs.runtime.modes.Mode;

public class Profiler extends Mode {

	@Override
	public void run() {
		processManager.createAll();
		System.out.println("[acs] Profiling network traffic...");
		while(running);
	}

	@Override
	public void setExternalProcess() {		
		String profileCommand = "profile2db.pl " 
				+ "--dir " + settings.getTrackers().getLogs() + "/cxtracker/" 
				+ settings.getTrackers().getInet() + "/sessions/ --daemon";
		String cxtrackerCommand = "cxtracker -i " 
				+ settings.getTrackers().getInet() + " -d "
				+ settings.getTrackers().getLogs() + "/cxtracker/"
				+ settings.getTrackers().getInet() + "/sessions/ -D";
		
		ExternalProcess profile2db = new ExternalProcess("Profile2DB","profile2db.pl", profileCommand);
		ExternalProcess cxtracker = new ExternalProcess("Cxtracker", "cxtracker", cxtrackerCommand);
		
		processManager.set(profile2db, cxtracker);
	}

	@Override
	public void manageIO() {
		// TODO Auto-generated method stub
	}
}
