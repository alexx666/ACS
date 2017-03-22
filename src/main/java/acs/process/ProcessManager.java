package main.java.acs.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessManager {
	
	private ExternalProcess [] processes;
	
	public ExternalProcess[] getProcesses() { return processes;	}
	public ProcessManager() { this.processes = new ExternalProcess[]{}; };
	
	public void setProcesses(ExternalProcess...processes) {	this.processes = processes; }
	
	public static void start(ExternalProcess process, boolean verbose) {
		if(!isActive(process)) {
			if (verbose) System.out.println("[acs] Starting: " + process.getName());
			List<String> commands = new ArrayList<String>();
			commands.add("/bin/sh");
			commands.add("-c");
			commands.add(process.getCommand());
			runProcess(commands, verbose);
		}else if(verbose) System.out.println("[acs]  " + process.getName() + " is already running");
	}
	
	public static void stop(ExternalProcess process, boolean verbose) {
		if(isActive(process)) {
			if(verbose) System.out.println("[acs] Stopping: " + process.getName());
			List<String> commands = new ArrayList<String>();
			commands.add("sudo");
			commands.add("-S");
			commands.add("killall");
			commands.add(process.getName());
			runProcess(commands, verbose);
		}else if(verbose) System.out.println("[acs] " + process.getName() + " is not running");
	}

	public void startAll(boolean verbose) {
		if(processes.length != 0) {
			if (verbose) System.out.println("[acs] Inicializing tools...");
			for (ExternalProcess process : processes) {
				if(!isActive(process)) {
					if (verbose) System.out.println("[acs] Starting: " + process.getName());
					List<String> commands = new ArrayList<String>();
					commands.add("/bin/sh");
					commands.add("-c");
					commands.add(process.getCommand());
					runProcess(commands, verbose);
				}else if(verbose) System.out.println("[acs]  " + process.getName() + " is already running");
			}
		}
	}
	
	public void stopAll(boolean verbose) {
		if(processes.length != 0) {
			for (ExternalProcess process : processes) {
				if(isActive(process)) {
					if(verbose) System.out.println("[acs] Stopping: " + process.getName());
					List<String> commands = new ArrayList<String>();
					commands.add("sudo");
					commands.add("-S");
					commands.add("killall");
					commands.add(process.getName());
					runProcess(commands, verbose);
				}else if(verbose) System.out.println("[acs] " + process.getName() + "  was not running");
			}
		}
	}
	
	private static boolean isActive(ExternalProcess process) {
		List<String> commands = new ArrayList<String>();
		commands.add("/bin/sh");
		commands.add("-c");
		commands.add("ps -e | grep "+ process.getName());
		
		String output = runProcess(commands, false);
		if (output != null && output.contains(process.getName())) {
			return true;
		}else{
			return false;
		}
	}
	
	public static String runProcess(List<String> commands, boolean print) {
		try {
	        ProcessBuilder probuilder = new ProcessBuilder(commands);
	        probuilder.redirectErrorStream(true);
	        Process process = probuilder.start();
	        
	        InputStream is = process.getInputStream();
	        InputStreamReader isr = new InputStreamReader(is);
	        BufferedReader br = new BufferedReader(isr);
	        
	        String output = "";
	        String line;
	        while ((line = br.readLine()) != null) {
	        	output += line + "\n";
	        	if (print) {
		            System.out.println("  " + line);
	        	}
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