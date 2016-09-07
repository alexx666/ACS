package main.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ProcessManager {
	
	protected static final String bash = "/bin/sh";
	protected static final String c = "-c";
	
	private List<String> pnames;
	private Map<String, String> calls;
	
	public void setPnames(List<String> pnames) {
		this.pnames = pnames;
	}

	public void setCalls(Map<String, String> calls) {
		this.calls = calls;
	}

	public void start() {	
		List<String> commands = new ArrayList<String>();
		
		String main = "";
		
		for (String name : pnames) {
			
			if (name == "barnyard2") {
				if (!isActive("barnyard2")) {
					List<String> barnyard = new ArrayList<String>();
					barnyard.add(bash);
					barnyard.add(c);
					barnyard.add(calls.get("barnyard2"));
					(new UnixShell(barnyard)).execute(false,false);
					System.out.println("[acs-#] barnyard2 started");
				}
			}else{
				if (!isActive(name)) { 
					if (!main.equals("")) { 
						main += "&& " + calls.get(name) + " "; 
						System.out.println("[acs-#] starting: " + name);
					}else{ 
						main += calls.get(name) + " "; 
						System.out.println("[acs-#] starting: " + name);
					}
				}
			}			
		}
		
		if (!main.equals("")) {
			commands.add(bash);
			commands.add(c);
			commands.add(main);
			System.out.println();
			(new UnixShell(commands)).execute(true,true);
		}
	}
	
	public void stop() {
		for (String name : pnames) {
			if (isActive(name)) { 
				killAll(name); 
				System.out.println("[acs-#] " + name + " stoped.");
			}
		}
	}
	
	public static void start(String command) {
		List<String> commands = new ArrayList<String>();
		commands.add(bash);
		commands.add(c);
		commands.add(command);
		System.out.println();
		(new UnixShell(commands)).execute(false,true);
	}
	
	public static void stop(String name) {
		if (isActive(name)) { 
			killAll(name); 
		}
	}
		
	public static void killAll(String name) {
		List<String> commands = new ArrayList<String>();
		commands.add("sudo");
		commands.add("-S");
		commands.add("killall");
		commands.add(name);
		(new UnixShell(commands)).execute(false,true);
	}
	
	public static boolean isActive(String pname) {
		List<String> commands = new ArrayList<String>();
		commands.add("/bin/sh");
		commands.add("-c");
		commands.add("ps -e | grep "+ pname);
		UnixShell us = new UnixShell(commands);
		us.execute(false,true);
		if (us.getOutput().toString().contentEquals("")) {
			return false;
		}else{
			return true;
		}
	}
}
