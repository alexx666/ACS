package main.java.com.alexx666.acs.db.dto.config;

public class ExternalProcess {

	private String alias;
	private String name;
	private String command;
	
	public ExternalProcess(String alias, String name, String command) {
		this.name = name;
		this.alias = alias;
		this.command = command;
	}
	
	public String getAlias() { return alias; }
    public String getName() { return name; }
    public String getCommand() { return command; }

	public void setAlias(String alias) { this.alias = alias; }
	public void setName(String name) { this.name = name; }
	public void setCommand(String command) { this.command = command; }    
}
