package main.java.acs.configuration.yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;

import main.java.acs.process.ExternalProcess;
import net.sourceforge.yamlbeans.YamlException;
import net.sourceforge.yamlbeans.YamlReader;

public class YMLConfiguration {
	
	private static YMLConfiguration instance = null;
	
	private Tools tools;
	
	private YMLConfiguration() {}
	
	public static YMLConfiguration getInstance() {
		if (instance == null) {	
			instance = new YMLConfiguration(); 
		}
		return instance;
	}
		
	public Tools getTools() { return tools; }

	public void runWithFile(String filePath) {
		try {
			YamlReader reader = new YamlReader(new FileReader(filePath));
			Tools tools = reader.read(Tools.class);
			this.tools = tools;
			this.setExternalProcessCommands();
		} catch (FileNotFoundException e) {
			System.out.println("[acs] Error: File not found!");
		} catch (YamlException e) {
			System.out.println("[acs] Error: Invalid file, please review the parameters in: " + filePath);
		}
	}
	
	private void setExternalProcessCommands() {
		String suricataCommand = "suricata -c " + tools.suricata.yaml + " --pcap=" + tools.suricata.inet + " -D";
		
		String profileCommand = "profile2db.pl " + tools.trackers.inet + " --dir "
				+ tools.trackers.logs + "/cxtracker/" 
				+ tools.trackers.inet + "/sessions --daemon";
		
		String snapshotCommand = "snapshot2db.pl " + tools.trackers.inet + " --dir "
				+ tools.trackers.logs + "/prads/" 
				+ tools.trackers.inet + "/sessions --daemon";
		
		String cxtrackerCommand = "cxtracker -i " + tools.trackers.inet + " -d "
				+ tools.trackers.logs + "/cxtracker/"
				+ tools.trackers.inet + "/sessions -D";
		
		String pradsCommand = "prads -i " + tools.trackers.inet + " -d "
				+ tools.trackers.logs + "/prads/"
				+ tools.trackers.inet + "/sessions -x -O -D";
		
		String oinkmasterCommand = "oinkmaster -o " + tools.suricata.rules + " -q -s";
		
		ExternalProcess.SURICATA.setCommand(suricataCommand);
		ExternalProcess.PROFILE2DB.setCommand(profileCommand);
		ExternalProcess.SNAPSHOT2DB.setCommand(snapshotCommand);
		ExternalProcess.CXTRACKER.setCommand(cxtrackerCommand);
		ExternalProcess.PRADS.setCommand(pradsCommand);
		ExternalProcess.OINKMASTER.setCommand(oinkmasterCommand);
	}
}
