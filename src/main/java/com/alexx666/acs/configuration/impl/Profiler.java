package com.alexx666.acs.configuration.impl;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.alexx666.acs.configuration.Mode;
import com.alexx666.acs.db.dto.config.ExternalProcess;
import com.alexx666.acs.manager.impl.FileManager;

public class Profiler extends Mode {

	@Override
	public void run() {
		processManager.createAll(true);
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
		//Paths
		Path cxtracker = Paths.get(settings.getTrackers().getLogs() + "/cxtracker");
		Path eth = Paths.get(settings.getTrackers().getLogs() + "/cxtracker/" + settings.getTrackers().getInet());
		Path sessions = Paths.get(settings.getTrackers().getLogs() + "/cxtracker/" + settings.getTrackers().getInet() + "/sessions");
		Path failed = Paths.get(settings.getTrackers().getLogs() + "/cxtracker/" + settings.getTrackers().getInet() + "/sessions/failed");

		//Tracker Directories
		FileManager dm = new FileManager();
		dm.set(cxtracker, eth, sessions, failed);
		dm.createAll(true);
	}
}
