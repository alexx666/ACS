package com.alexx666.acs.configuration.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import com.alexx666.acs.configuration.Configuration;
import com.alexx666.acs.configuration.ConfigurationFactory;
import com.alexx666.acs.db.dto.config.ExternalProcess;
import com.alexx666.acs.manager.impl.FileManager;

public class Profiler extends Configuration {

	@Override
	public void run() {
		processManager.createAll(true);
		System.out.println("[acs] Profiling network traffic...");
		while(running);
	}

	@Override
	public void setExternalProcesses() {		
		String profileCommand = "profile2db.pl " 
				+ "--dir " + ConfigurationFactory.getInstance().getSettings().getTrackers().getLogs() + "/cxtracker/" 
				+ ConfigurationFactory.getInstance().getSettings().getTrackers().getInet() + "/sessions/ --daemon";
		String cxtrackerCommand = "cxtracker -i " 
				+ ConfigurationFactory.getInstance().getSettings().getTrackers().getInet() + " -d "
				+ ConfigurationFactory.getInstance().getSettings().getTrackers().getLogs() + "/cxtracker/"
				+ ConfigurationFactory.getInstance().getSettings().getTrackers().getInet() + "/sessions/ -D";
		
		ExternalProcess profile2db = new ExternalProcess("Profile2DB","profile2db.pl", profileCommand);
		ExternalProcess cxtracker = new ExternalProcess("Cxtracker", "cxtracker", cxtrackerCommand);
		
		processManager.set(profile2db, cxtracker);
	}

	@Override
	public void manageIO() {
		String dumpfile = ConfigurationFactory.getInstance().getSettings().getOutputs().getFile() + 
				"/acs_" + Calendar.getInstance().getTimeInMillis() + ".log";

		//Paths		
		Path outputFile = Paths.get(dumpfile);
		Path outputDir = Paths.get(ConfigurationFactory.getInstance().getSettings().getOutputs().getFile());
		Path cxtracker = Paths.get(ConfigurationFactory.getInstance().getSettings().getTrackers().getLogs() + "/cxtracker");
		Path cxtrackerEth = Paths.get(cxtracker + ConfigurationFactory.getInstance().getSettings().getTrackers().getInet());
		Path cxtrackerSessions = Paths.get(cxtrackerEth + "/sessions");
		Path cxtrackerFailed = Paths.get(cxtrackerSessions + "/failed");
		
		//Tracker Directories
		FileManager fm = new FileManager();
		fm.set(cxtracker, cxtrackerEth, cxtrackerSessions, cxtrackerFailed, outputDir);
		fm.createAll(true);
		
		//Output File
		fm.set(outputFile);
		fm.createAll(false);
	}
}
