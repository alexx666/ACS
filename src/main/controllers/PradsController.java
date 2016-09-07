package main.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.utils.ProcessManager;

public class PradsController extends ProcessManager {

	private static final String SNAPSHOT2DB = "sudo -S snapshot2db.pl eth2 --dir /var/log/prads/eth2/sessions/ --daemon";
	private static final String PRADS = "sudo -S prads -i eth2 -L /var/log/prads/eth2/sessions/ -q -D";

	public PradsController() {
		List<String> pnames = new ArrayList<String>();
		Map<String, String> calls = new HashMap<>();

		pnames.add("snapshot2db.pl");
		pnames.add("prads");

		calls.put("snapshot2db.pl", SNAPSHOT2DB);
		calls.put("prads", PRADS);

		setPnames(pnames);
		setCalls(calls);
	}
	
	public static void startPrads() {
		start(PRADS);
	}
	
	public static void stopPrads() {
		stop("prads");
	}
}
