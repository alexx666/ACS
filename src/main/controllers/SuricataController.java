package main.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.utils.ProcessManager;

public class SuricataController extends ProcessManager {

	private static final String SURICATA = "sudo -S suricata -c /etc/suricata/suricata.yaml --pcap=eth1 -D";
	private static final String BARNYARD2 = "sudo -S barnyard2 -c /etc/suricata/barnyard2.conf -d /var/log/suricata -f unified2.alert -w /var/log/suricata/suricata.waldo -D";
	
	public SuricataController() {
		List<String> pnames = new ArrayList<String>();
		Map<String,String> calls = new HashMap<>();
		
		pnames.add("Suricata-Main");
		pnames.add("barnyard2");
		
		calls.put("Suricata-Main", SURICATA);
		calls.put("barnyard2", BARNYARD2);
		
		setPnames(pnames);
		setCalls(calls);
	}
}
