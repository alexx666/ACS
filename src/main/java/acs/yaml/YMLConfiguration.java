package main.java.acs.yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;

import main.java.acs.yaml.entity.Tools;
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
			//TODO set ExternalProcess Commands
		} catch (FileNotFoundException e) {
			System.out.println("[acs] Error: File not found!");
		} catch (YamlException e) {
			System.out.println("[acs] Error: Invalid file, please review the parameters in: " + filePath);
		}
	}
}
