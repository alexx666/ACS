package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class ProcessManager {
	
	private static final String bash = "/bin/sh";
	private static final String c = "-c";
			
	public static void start(Commands[] cmds, boolean verbose) {						
		for (Commands cmd : cmds) {
			if(cmd.isProcess() && verbose) {
				if (!isActive(cmd)) {
					System.out.println("[acs] Starting: " + cmd.getName());
				}else{
					System.out.println("[acs]  " + cmd.getName() + " is already running");
				}
			}
			if(cmd.isProcess() && !isActive(cmd) || !cmd.isProcess()) {
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
	}
	
	public static void stop(Commands[] cmds, boolean verbose) {
		for (Commands cmd : cmds) {
			if(cmd.isProcess() && verbose) {
				if(isActive(cmd)){
					System.out.println("[acs] Stopping: " + cmd.getName());
					List<String> commands = new ArrayList<String>();
					commands.add("sudo");
					commands.add("-S");
					commands.add("killall");
					commands.add(cmd.getName());
					execute(commands, false);
				}else{
					System.out.println("[acs] " + cmd.getName() + "  was not running") ;
				}
			}
		}
	}
	
	//TODO
	private static boolean isActive(Commands process) {
		List<String> commands = new ArrayList<String>();
		commands.add("/bin/sh");
		commands.add("-c");
		commands.add("ps -e | grep "+ process.getName());
		
		String output = execute(commands, false);
		
		if (output != null && output.contains(process.getName())) {
			return true;
		}else{
			return false;
		}
	}
	
	private static String execute(List<String> commands, boolean print) {

		try {
	        ProcessBuilder probuilder = new ProcessBuilder(commands);
	        Process process;
			
			process = probuilder.start();

	        OutputStream os = process.getOutputStream();
	        PrintWriter pw = new PrintWriter(os);
	        pw.println("n\"\"DL3");
	        pw.flush();
	        
	        InputStream is = process.getInputStream();
	        InputStreamReader isr = new InputStreamReader(is);
	        BufferedReader br = new BufferedReader(isr);
	        String line;
	        String output = "";
	        while ((line = br.readLine()) != null) {
	        	if (print) {
		            System.out.println("	" + line);
	        	}
	            output += line;
	        }
	        
	        br.close();
	        
			process.waitFor();
			
			return output;
			
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return null;
        }
	}
}
