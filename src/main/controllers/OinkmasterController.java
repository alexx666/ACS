package main.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.utils.ProcessManager;

public class OinkmasterController extends ProcessManager {

	private static final String OINKMASTER = "sudo -S oinkmaster -o /etc/suricata/rules -s";

	public OinkmasterController() {
		List<String> pnames = new ArrayList<String>();
		Map<String, String> calls = new HashMap<>();

		pnames.add("oinkmaster");

		calls.put("oinkmaster", OINKMASTER);

		setPnames(pnames);
		setCalls(calls);
	}
}
