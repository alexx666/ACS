package main.controllers;

import java.util.ArrayList;
import java.util.List;

import main.utils.UnixCommand;

public class ConfigController {
	
	protected static final String bash = "/bin/sh";
	protected static final String c = "-c";
	private static final String FIND = "locate 'barnyard2.conf' && locate 'suricata.yaml' && locate 'oinkmaster.conf'";
	private static final String VERSION = "suricata -V && oinkmaster -V && barnyard2 -V";

	public ConfigController() {}

	public void showToolVersions() {
		List<String> exCommand = new ArrayList<String>();
		exCommand.add("/bin/sh");
		exCommand.add("-c");
		exCommand.add(VERSION);
		(new UnixCommand(exCommand)).execute(true,true);
	}

	public void showFileLocations() {
		List<String> exCommand = new ArrayList<String>();
		exCommand.add(bash);
		exCommand.add(c);
		exCommand.add(FIND);
		(new UnixCommand(exCommand)).execute(true,true);
	}
}
