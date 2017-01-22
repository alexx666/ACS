package main.utils;

import java.util.ArrayList;
import java.util.List;

import main.Commands;

public abstract class ProcessManager {
	
	protected static final String bash = "/bin/sh";
	protected static final String c = "-c";
			
	public static void start(Commands...cmds) {	
						
		for (Commands cmd : cmds) {
			if ((cmd.isProcess() && !isActive(cmd)) || !cmd.isProcess()) {
				if (cmd.isProcess()) {
					System.out.println("[acs] Starting ---> " + cmd.getName());
				}else{
					System.out.println("[acs] " + cmd.getName());
				}
				List<String> commands = new ArrayList<String>();
				commands.add(bash);
				commands.add(c);
				commands.add(cmd.getCommand());
				UnixCommand uc = new UnixCommand();
				uc.execute(commands, cmd.shouldPrint(),cmd.waitoutput());
			}
		}
	}
	
	public static void stop(Commands...cmds) {
		for (Commands cmd : cmds) {
			if (cmd.isProcess() && isActive(cmd)) { 
				System.out.println("[acs] Stopping ---> " + cmd.getName());
				List<String> commands = new ArrayList<String>();
				commands.add("sudo");
				commands.add("-S");
				commands.add("killall");
				commands.add(cmd.getName());
				UnixCommand uc = new UnixCommand();
				uc.execute(commands,false,true); 
			}
		}
	}
	
	private static boolean isActive(Commands process) {
		List<String> commands = new ArrayList<String>();
		commands.add("/bin/sh");
		commands.add("-c");
		commands.add("ps -e | grep "+ process.getName());
		UnixCommand uc = new UnixCommand();
		uc.execute(commands,false,true);
		if (uc.getOutput().toString().contentEquals("")) {
			return false;
		}else{
			return true;
		}
	}
	
	//no print version
	//TODO remove
	public static void silentStart(Commands...cmds) {	
		
		for (Commands cmd : cmds) {
			if ((cmd.isProcess() && !isActive(cmd)) || !cmd.isProcess()) {
				List<String> commands = new ArrayList<String>();
				commands.add(bash);
				commands.add(c);
				commands.add(cmd.getCommand());
				UnixCommand uc = new UnixCommand();
				uc.execute(commands, cmd.shouldPrint(),cmd.waitoutput());
			}
		}
	}
	
	//no print version 
	//TODO remove
	public static void silentStop(Commands...cmds) {
		for (Commands cmd : cmds) {
			if (cmd.isProcess() && isActive(cmd)) { 
				List<String> commands = new ArrayList<String>();
				commands.add("sudo");
				commands.add("-S");
				commands.add("killall");
				commands.add(cmd.getName());
				UnixCommand uc = new UnixCommand();
				uc.execute(commands,false,true); 
			}
		}
	}
}
