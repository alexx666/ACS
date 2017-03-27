package main.java.acs.modes.process;

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
	
	PROFILE2DB ("profile2DB","profile2db.pl", ""),
	SNAPSHOT2DB ("Snapshot2DB", "snapshot2db.pl", ""),
	CXTRACKER ("Cxtracker", "cxtracker", ""),
	PRADS ("PRADS", "prads", ""),
	OINKMASTER ("Oinkmaster", "oinkmaster", ""),
	SURICATA ("Suricata", "Suricata-Main", "");
	
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