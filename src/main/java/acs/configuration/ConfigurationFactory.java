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
		
        options.addOption("m", "monitor", false, "Run the network monitoring tools (Suricata and PRADS) and calculate the network anomaly based on the NIDS alerts.");
		options.addOption("p", "profiler", false, "Run Cxtracker to make a profile of the networks traffic.");
		options.addOption("u", "update", false, "Use Oinkmaster to update Suricata rules.");
	}
	
	public Configuration getConfiguration(String [] args) throws NullPointerException {
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.getOptions().length != 1) { 
				formatter.printHelp("acs [option]", options);
				throw new NullPointerException();
			}else if (cmd.hasOption("m")) {	
				return new MonitorConfiguration();
			}else if (cmd.hasOption("p")) {	
				return new ProfileConfiguration();
			}else if (cmd.hasOption("u")) {
				return new UpdateConfiguration();
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
