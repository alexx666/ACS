package main.java.acs.configuration;

/**
 * This enumeration defines a runnable command needed for ACS and its components to work
 * 
 * Format of the commands:
 * 
 * 		COMMAND (name - String, command - String)
 * 		
 **/
public enum ExternalProcess {
	
	PROFILE2DB ("profile2DB", "profile2db.pl", "profile2db.pl eth2 --dir /var/log/cxtracker/eth2/sessions/ --daemon"),
	SNAPSHOT2DB ("Snapshot2DB", "snapshot2db.pl", "snapshot2db.pl eth2 --dir /var/log/prads/eth2/sessions/ --daemon"),
	CXTRACKER ("Cxtracker", "cxtracker", "cxtracker -i eth2 -d /var/log/cxtracker/eth2/sessions/ -D"),
	PRADS ("PRADS", "prads", "prads -i eth2 -L /var/log/prads/eth2/sessions/ -x -O -D"),
	OINKMASTER ("Oinkmaster", "oinkmaster", "oinkmaster -o /etc/suricata/rules -q -s"),
	SURICATA ("Suricata", "Suricata-Main", "suricata -c /etc/suricata/suricata.yaml --pcap=eth1 -D");
	
	private final String alias;
    private final String name;   
    private final String command;
    
    ExternalProcess(String alias, String name, String command) {
    	this.alias = alias;
        this.name = name;
        this.command = command;
    }
    
    public String getAlias() { return alias; }
    public String getName() { return name; }
    public String getCommand() { return command; }
}