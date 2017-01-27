package main.java.acs;

/**
 * This enumeration defines a runnable command needed for ACS and its components to work
 * 
 * Format of the commands:
 * 
 * 		COMMAND (name - String, command - String)
 * 		
 **/
public enum ExternalProcess {
	
	PROFILE2DB ("profile2db.pl", "profile2db.pl eth2 --dir /var/log/cxtracker/eth2/sessions/ --daemon"),
	CXTRACKER ("cxtracker", "cxtracker -i eth2 -d /var/log/cxtracker/eth2/sessions/ -D"),
	OINKMASTER ("oinkmaster", "oinkmaster -o /etc/suricata/rules -s"),
	SNAPSHOT2DB ("snapshot2db.pl", "snapshot2db.pl eth2 --dir /var/log/prads/eth2/sessions/ --daemon"),
	PRADS ("prads", "prads -i eth2 -L /var/log/prads/eth2/sessions/ -x -O -D"),
	SURICATA ("Suricata-Main", "suricata -c /etc/suricata/suricata.yaml --pcap=eth1 -D");
	
    private final String name;   
    private final String command;
    
    ExternalProcess(String name, String command) {
        this.name = name;
        this.command = command;
    }
    
    public String getName() { return name; }
    public String getCommand() { return command; }
}