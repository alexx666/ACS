package main.java.acs.utils.process;

public class Command {

	private String alias;
	private String name;
	private String command;
	
	public Command (String alias, String name, String command) {
		this.alias = alias;
		this.name = name;
		this.command = command;
	}

	public String getName() { return name; }
	public String getAlias() { return alias; }
	public String getCommand() { return command; }
	public void setCommand(String newCommand) { this.command = newCommand; }
}
