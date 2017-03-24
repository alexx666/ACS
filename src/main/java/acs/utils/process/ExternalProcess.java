package main.java.acs.utils.process;

/**
 * This enumeration defines a runnable command needed for ACS and its components to work
 * 
 * @author alexx666
 * 
 * Format of the commands:
 * 
 * 		COMMAND (alias - String, name - String, command - String)
 * 		
 **/
public enum ExternalProcess {
	
	PROFILE2DB ("profile2DB","profile2db.pl", "profile2db.pl eth2 --dir /var/log/cxtracker/eth2/sessions/ --daemon"),
	SNAPSHOT2DB ("Snapshot2DB", "snapshot2db.pl", "snapshot2db.pl eth2 --dir /var/log/prads/eth2/sessions/ --daemon"),
	CXTRACKER ("Cxtracker", "cxtracker", "cxtracker -i eth2 -d /var/log/cxtracker/eth2/sessions/ -D"),
	PRADS ("PRADS", "prads", "prads -i eth2 -L /var/log/prads/eth2/sessions/ -x -O -D"),
	OINKMASTER ("Oinkmaster", "oinkmaster", "oinkmaster -o /etc/suricata/rules -q -s"),
	SURICATA ("Suricata", "Suricata-Main", "suricata -c /etc/suricata/suricata.yaml --pcap=eth1 -D");
	
	private String alias;
	private String name;
	private String command;
	
	private ExternalProcess(String alias, String name, String command) {
		this.name = name;
		this.alias = alias;
		this.command = command;
	}
	
	public String getAlias() { return alias; }
    public String getName() { return name; }
    public String getCommand() { return command; }
    
    public void setCommand(String newCommand) { command = newCommand; }
}