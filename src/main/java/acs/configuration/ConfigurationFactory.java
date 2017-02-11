package main.java.acs.configuration;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ConfigurationFactory {
	
	private Options options;
	private CommandLineParser parser;
	private HelpFormatter formatter;
	
	public ConfigurationFactory() {
		parser = new DefaultParser();
        formatter = new HelpFormatter();
		options = new Options();
		
        options.addOption("m", "mode", true, "Select between monitor, profiler and updater.");	
        options.addOption("f", "file", true, "Path to acs.yml configuration file.");
	}
	
	public Configuration getConfiguration(String [] args) throws NullPointerException {
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("f")) {
				YAML.setParamsFromFile(cmd.getOptionValue("f"));
			}
			if (cmd.hasOption("m")) {	
				switch (cmd.getOptionValue("m")) {
				case "monitor": return new MonitorConfiguration();
				case "profiler": return new ProfileConfiguration();
				case "updater": return new UpdateConfiguration();
				default: 
					formatter.printHelp("acs [option]", options);
					throw new NullPointerException();
				}
			}else{
				formatter.printHelp("acs [option]", options);
				throw new NullPointerException();
			}
		} catch (ParseException e) {
			formatter.printHelp("acs [option]", options);
			throw new NullPointerException();
		}
	}
}
