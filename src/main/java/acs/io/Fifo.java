package main.java.acs.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import main.java.acs.configuration.yaml.YMLConfiguration;
import main.java.acs.process.ProcessManager;

public class Fifo {
	
	public static void createFifoPipe() {
		String fifoName = YMLConfiguration.getInstance().getTools().suricata.pipe;
		List<String> commands = new ArrayList<String>();
		commands.add("/bin/sh");
		commands.add("-c");
		commands.add("sudo mkfifo " + fifoName + " && sudo chmod a+rw "+ fifoName);
		ProcessManager.runProcess(commands, false);  
	}

	public static File getFifoPipe() {
	    return new File(YMLConfiguration.getInstance().getTools().suricata.pipe);
	}

	public static void removeFifoPipe() {
		getFifoPipe().delete();
	}
}