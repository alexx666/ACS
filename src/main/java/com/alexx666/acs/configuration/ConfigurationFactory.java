package com.alexx666.acs.configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.alexx666.acs.configuration.impl.Monitor;
import com.alexx666.acs.configuration.impl.Profiler;
import com.alexx666.acs.configuration.impl.Updater;
import com.alexx666.acs.db.dto.config.Settings;
import com.alexx666.acs.error.InvalidModeException;
import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

public class ConfigurationFactory {
	
	private Settings settings;
	
	private ConfigurationFactory() {}
	
	private static class Static {
		private static final ConfigurationFactory INSTANTE = new ConfigurationFactory();
	}
	  
	public static ConfigurationFactory getInstance() {
		return Static.INSTANTE;
	}
	
	public static Configuration getConfiguration(String mode) throws InvalidModeException {
		switch (mode) {
		case "monitor": return new Monitor();
		case "profiler": return new Profiler();
		case "updater": return new Updater();
		default: throw new InvalidModeException();
		}
	}
	
	public void setSettingsFromFile(String filePath) throws FileNotFoundException, YamlException {
		YamlReader reader = new YamlReader(new FileReader(filePath));
		this.settings = reader.read(Settings.class);
	}
	
	public Settings getSettings() {
		return this.settings;
	}
	
	public void run(Configuration mode) {
		mode.run();
	}

}
