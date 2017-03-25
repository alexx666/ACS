package main.java.acs.configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import main.java.acs.configuration.mode.RunningMode;
import main.java.acs.configuration.mode.impl.Monitor;
import main.java.acs.configuration.mode.impl.Profile;
import main.java.acs.configuration.mode.impl.Update;
import main.java.acs.configuration.settings.Settings;
import main.java.acs.utils.process.ExternalProcess;
import main.java.acs.utils.process.ProcessManager;
import net.sourceforge.yamlbeans.YamlException;
import net.sourceforge.yamlbeans.YamlReader;

public class ACSConfiguration {
	
	private Settings settings;
	
	private ACSConfiguration() {}
	
	private static class Static {
		private static final ACSConfiguration INSTANCE = new ACSConfiguration();
	}
	
	public static ACSConfiguration getInstance() {
		return Static.INSTANCE;
	}
	
	public void setSettingsFromFile(String filePath) {
		try {
			YamlReader reader = new YamlReader(new FileReader(filePath));
			Settings settings = reader.read(Settings.class);
			this.settings = settings;
		} catch (FileNotFoundException e) {
			System.out.println("[acs] Error: File not found!");
		} catch (YamlException e) {
			System.out.println("[acs] Error: Invalid file, please review the parameters in: " + filePath);
		}
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	public void prepareLogFiles() {
		createOutputFile(); //TODO
	}
	
	public void prepareDatabase(boolean clean) {
		//TODO
	}
	
	public void setExternalProcessCommands() {
		String suricataCommand = "suricata -c " + settings.suricata.yaml + " --pcap=" + settings.suricata.inet + " -D";
		
		String profileCommand = "profile2db.pl " + settings.trackers.inet + " --dir "
				+ settings.trackers.logs + "/cxtracker/" 
				+ settings.trackers.inet + "/sessions --daemon";
		
		String snapshotCommand = "snapshot2db.pl " + settings.trackers.inet + " --dir "
				+ settings.trackers.logs + "/prads/" 
				+ settings.trackers.inet + "/sessions --daemon";
		
		String cxtrackerCommand = "cxtracker -i " + settings.trackers.inet + " -d "
				+ settings.trackers.logs + "/cxtracker/"
				+ settings.trackers.inet + "/sessions -D";
		
		String pradsCommand = "prads -i " + settings.trackers.inet + " -d "
				+ settings.trackers.logs + "/prads/"
				+ settings.trackers.inet + "/sessions -x -O -D";
		
		String oinkmasterCommand = "oinkmaster -o " + settings.suricata.rules + " -q -s";
		
		ExternalProcess.SURICATA.setCommand(suricataCommand);
		ExternalProcess.PROFILE2DB.setCommand(profileCommand);
		ExternalProcess.SNAPSHOT2DB.setCommand(snapshotCommand);
		ExternalProcess.CXTRACKER.setCommand(cxtrackerCommand);
		ExternalProcess.PRADS.setCommand(pradsCommand);
		ExternalProcess.OINKMASTER.setCommand(oinkmasterCommand);
	}
	
	public RunningMode getConfiguration(String mode) throws NullPointerException {
		switch (mode) {
		case "monitor": return new Monitor();
		case "profiler": return new Profile();
		case "updater": return new Update();
		default: throw new NullPointerException();
		}
	}
	
	private void createOutputFile() {
		String fifoName = settings.outputs.file;
		List<String> commands = new ArrayList<String>();
		commands.add("/bin/sh");
		commands.add("-c");
		commands.add("sudo touch " + fifoName + " && sudo chmod a+rw "+ fifoName);
		ProcessManager.runProcess(commands, false);
	}
}