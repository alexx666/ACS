package main.utils;

import java.util.ArrayList;
import java.util.List;

import main.Processes;

public abstract class ProcessManager {
	
	protected static final String bash = "/bin/sh";
	protected static final String c = "-c";
			
	public static void start(Processes...processes ) {	
		List<String> commands = new ArrayList<String>();
		
		String main = "";
		
		for (Processes process : processes) {
			if (!isActive(process.getName())) {		
				if (process.execSolo()) {	
					List<String> exCommand = new ArrayList<String>();
					exCommand.add(bash);
					exCommand.add(c);
					exCommand.add(process.getCommand());
					(new UnixCommand(exCommand)).execute(process.shouldPrint(),process.waitoutput());
					System.out.println("[acs-#] " + process.getName() + " started");
				}else{
					System.out.println("[acs-#] starting: " + process.getName());
					if (!main.equals("")) { 
						main += "&& " + process.getCommand() + " "; 
					}else{ 
						main += process.getCommand() + " "; 
					}
				}
			}
		}
		
		if (!main.equals("")) {
			commands.add(bash);
			commands.add(c);
			commands.add(main);
			System.out.println();
			(new UnixCommand(commands)).execute(true,true);
		}
	}
	
	public static void stop(Processes...processes) {
		for (Processes process : processes) {
			if (isActive(process.getName())) { 
				killAll(process.getName()); 
				System.out.println("[acs-#] " + process.getName() + " stoped.");
			}
		}
	}
		
	private static void killAll(String name) {
		List<String> commands = new ArrayList<String>();
		commands.add("sudo");
		commands.add("-S");
		commands.add("killall");
		commands.add(name);
		(new UnixCommand(commands)).execute(false,true);
	}
	
	private static boolean isActive(String pname) {
		List<String> commands = new ArrayList<String>();
		commands.add("/bin/sh");
		commands.add("-c");
		commands.add("ps -e | grep "+ pname);
		UnixCommand us = new UnixCommand(commands);
		us.execute(false,true);
		if (us.getOutput().toString().contentEquals("")) {
			return false;
		}else{
			return true;
		}
	}
}
