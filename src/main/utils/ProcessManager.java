package main.utils;

import java.util.ArrayList;
import java.util.List;

import main.Commands;

public abstract class ProcessManager {
	
	protected static final String bash = "/bin/sh";
	protected static final String c = "-c";
			
	public static void start(Commands...processes) {	
						
		for (Commands process : processes) {
			if ((process.isProcess() && !isActive(process)) || !process.isProcess()) {
				if (process.isProcess()) {
					System.out.println("[acs-#] Starting ---> " + process.getName());
				}else{
					System.out.println("[acs-#] " + process.getName());
				}
				List<String> exCommand = new ArrayList<String>();
				exCommand.add(bash);
				exCommand.add(c);
				exCommand.add(process.getCommand());
				UnixCommand uc = new UnixCommand();
				uc.execute(exCommand, process.shouldPrint(),process.waitoutput());
			}
		}
	}
	
	public static void stop(Commands...processes) {
		for (Commands process : processes) {
			if (process.isProcess() && isActive(process)) { 
				System.out.println("[acs-#] Stopping ---> " + process.getName());
				killAll(process); 
			}
		}
	}
		
	private static void killAll(Commands process) {
		List<String> commands = new ArrayList<String>();
		commands.add("sudo");
		commands.add("-S");
		commands.add("killall");
		commands.add(process.getName());
		UnixCommand uc = new UnixCommand();
		uc.execute(commands,false,true);
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
}
