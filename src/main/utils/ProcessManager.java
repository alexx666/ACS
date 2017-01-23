package main.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import main.Commands;

public abstract class ProcessManager {
	
	protected static final String bash = "/bin/sh";
	protected static final String c = "-c";
			
	public static void start(Commands...cmds) {	
						
		for (Commands cmd : cmds) {
			System.out.println("[acs] Starting: " + cmd.getName());
			List<String> commands = new ArrayList<String>();
			commands.add(bash);
			commands.add(c);
			commands.add(cmd.getCommand());
			if(cmd.isProcess()) {
				execute(commands, false); 
			}else{
				execute(commands, true); 
			}
		}
	}
	
	public static void stop(Commands...cmds) {
		for (Commands cmd : cmds) {
			System.out.println("[acs] Stopping: " + cmd.getName());
			List<String> commands = new ArrayList<String>();
			commands.add("sudo");
			commands.add("-S");
			commands.add("killall");
			commands.add(cmd.getName());
			execute(commands, false); 
		}
	}
	
	//TODO
	/*
	private static boolean isActive(Commands process) {
		List<String> commands = new ArrayList<String>();
		commands.add("/bin/sh");
		commands.add("-c");
		commands.add("ps -e | grep "+ process.getName());
		
		if (execute(commands) != null) {
			return false;
		}else{
			return true;
		}
	}
	*/
	
	private static String execute(List<String> commands, boolean print) {
		
        String output = null;

		try {
	        ProcessBuilder probuilder = new ProcessBuilder(commands);
	        Process process;
			
			process = probuilder.start();

	        OutputStream out = process.getOutputStream();
	        PrintWriter pw = new PrintWriter(out);
	        pw.println("n\"\"DL3");
	        pw.flush();
	        
	        InputStream is = process.getInputStream();
	        InputStreamReader isr = new InputStreamReader(is);
	        BufferedReader br = new BufferedReader(isr);
	        String line;
	        while ((line = br.readLine()) != null) {
	        	if (print) {
		            System.out.println("	" + line);
	        	}
	            //output += line + " \n";
	        }
			process.waitFor();
			
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
		
		return output;
	}
}
