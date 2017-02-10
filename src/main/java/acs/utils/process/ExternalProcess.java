package main.java.acs.utils.process;

/**
 * This enumeration defines a runnable command needed for ACS and its components to work
 * 
 * Format of the commands:
 * 
 * 		COMMAND (name - String, command - String)
 * 		
 **/
public enum ExternalProcess {
	
	PROFILE2DB (new Command("profile2DB","profile2db.pl","profile2db.pl eth2 --dir /var/log/cxtracker/eth2/sessions/ --daemon")),
	SNAPSHOT2DB (new Command("Snapshot2DB", "snapshot2db.pl", "snapshot2db.pl eth2 --dir /var/log/prads/eth2/sessions/ --daemon")),
	CXTRACKER (new Command("Cxtracker", "cxtracker", "cxtracker -i eth2 -d /var/log/cxtracker/eth2/sessions/ -D")),
	PRADS (new Command("PRADS", "prads", "prads -i eth2 -L /var/log/prads/eth2/sessions/ -x -O -D")),
	OINKMASTER (new Command("Oinkmaster", "oinkmaster", "oinkmaster -o /etc/suricata/rules -q -s")),
	SURICATA (new Command("Suricata", "Suricata-Main", "suricata -c /etc/suricata/suricata.yaml --pcap=eth1 -D"));
	
	private Command command;
	
	private ExternalProcess(Command command) {
		this.command = command;
	}
	
	public String getAlias() { return command.getAlias(); }
    public String getName() { return command.getName(); }
    public String getCommand() { return command.getCommand(); }
    
    public void setCommand(String newCommand) { command.setCommand(newCommand); }
}