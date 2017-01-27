package main.java.acs.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import main.java.acs.ExternalProcess;

public class ProcessManager {
	
	private static final String bash = "/bin/sh";
	private static final String c = "-c";
	
	private ExternalProcess [] processes;
	
	public ExternalProcess[] getProcesses() { return processes;	}
	public ProcessManager() { this.processes = new ExternalProcess[]{}; };
	
	public void setProcesses(ExternalProcess...processes) {	this.processes = processes; }

	public void start(boolean verbose) {
		if(processes.length != 0) {
			if (verbose) System.out.println("[acs] Inicializing tools...");
			for (ExternalProcess process : processes) {
				if(!isActive(process)) {
					if (verbose) System.out.println("[acs] Starting: " + process.getName());
					List<String> commands = new ArrayList<String>();
					commands.add(bash);
					commands.add(c);
					commands.add(process.getCommand());
					runProcess(commands, verbose);
				}else if(verbose) System.out.println("[acs]  " + process.getName() + " is already running");
			}
		}
	}
	
	public void stop(boolean verbose) {
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
	
	private boolean isActive(ExternalProcess process) {
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
	
	private String runProcess(List<String> commands, boolean print) {
		try {
	        ProcessBuilder probuilder = new ProcessBuilder(commands);
	        Process process;
			
			process = probuilder.start();

			PrintWriter pw = new PrintWriter(process.getOutputStream());
			/* TODO meter la contraseña por consola */
	        pw.println("n\"\"DL3"); 
	        pw.flush();
	        
	        InputStream is = process.getInputStream();
	        InputStreamReader isr = new InputStreamReader(is);
	        BufferedReader br = new BufferedReader(isr);
	        
	        String output = "";
	        String line;
	        while ((line = br.readLine()) != null) {
	        	output += line + "\n";
	        	if (print) {
		            System.out.println("	" + line);
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