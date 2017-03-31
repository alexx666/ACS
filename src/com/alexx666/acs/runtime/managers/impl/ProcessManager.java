package com.alexx666.acs.runtime.managers.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.alexx666.acs.data.dto.ExternalProcess;
import com.alexx666.acs.runtime.managers.Manager;

public class ProcessManager extends Manager<ExternalProcess> {
		
	public ProcessManager() { this.things = new ExternalProcess[]{}; };
	
	public static void create(ExternalProcess process, boolean verbose) {
		if(!isActive(process)) {
			if (verbose) System.out.println("[acs] Starting: " + process.getAlias());
			List<String> commands = new ArrayList<String>();
			commands.add("/bin/sh");
			commands.add("-c");
			commands.add(process.getCommand());
			runProcess(commands, verbose);
		}else if(verbose) System.out.println("[acs]  " + process.getAlias() + " is already running");
	}
	
	public static void destroy(ExternalProcess process, boolean verbose) {
		if(isActive(process)) {
			if(verbose) System.out.println("[acs] Stopping: " + process.getAlias());
			List<String> commands = new ArrayList<String>();
			commands.add("sudo");
			commands.add("-S");
			commands.add("killall");
			commands.add(process.getName());
			runProcess(commands, verbose);
		}else if(verbose) System.out.println("[acs] " + process.getAlias() + " is not running");
	}

	public void createAll(boolean verbose) {
		if(things.length != 0) {
			System.out.println("[acs] Inicializing tools...");
			for (ExternalProcess process : things) {
				if(!isActive(process)) {
					create(process, verbose);
				}else if(verbose) System.out.println("[acs]  " + process.getAlias() + " is already running");
			}
		}
	}
	
	public void destroyAll(boolean verbose) {
		if(things.length != 0) {
			for (ExternalProcess process : things) {
				if(isActive(process)) {
					destroy(process, verbose);
				}else if(verbose) System.out.println("[acs] " + process.getAlias() + "  was not running");
			}
		}
	}
	
	@Override
	public void createAll() {
		createAll(false);
	}

	@Override
	public void destroyAll() {
		destroyAll(false);
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