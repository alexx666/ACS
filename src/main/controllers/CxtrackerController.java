package main.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.utils.ProcessManager;

public class CxtrackerController extends ProcessManager {
	
	private static final String CXTRACKER2DB = "sudo -S cxtracker2db.pl eth2 --dir /var/log/cxtracker/eth2/sessions/ --daemon";
	private static final String CXTRACKER = "sudo -S cxtracker -i eth2 -d /var/log/cxtracker/eth2/sessions/ -D";
	
	public CxtrackerController() {
		List<String> pnames = new ArrayList<String>();
		Map<String, String> calls = new HashMap<>();

		pnames.add("cxtracker2db.pl");
		pnames.add("cxtracker");

		calls.put("cxtracker2db.pl", CXTRACKER2DB);
		calls.put("cxtracker", CXTRACKER);

		setPnames(pnames);
		setCalls(calls);
	}

}
