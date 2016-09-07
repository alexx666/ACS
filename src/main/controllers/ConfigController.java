package main.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.utils.ProcessManager;

public class ConfigController extends ProcessManager {

	private static final String FIND = "locate 'barnyard2.conf' && locate 'suricata.yaml' && locate 'oinkmaster.conf'";
	private static final String V = "suricata -V && oinkmaster -V && barnyard2 -V";

	public ConfigController() {}

	public void showToolVersions() {
		List<String> pnames = new ArrayList<String>();
		Map<String, String> calls = new HashMap<>();

		pnames.add("tools");
		calls.put("tools", V);

		setPnames(pnames);
		setCalls(calls);
	}

	public void showFileLocations() {
		List<String> pnames = new ArrayList<String>();
		Map<String, String> calls = new HashMap<>();

		pnames.add("config_files");
		calls.put("config_files", FIND);

		setPnames(pnames);
		setCalls(calls);
	}
}
