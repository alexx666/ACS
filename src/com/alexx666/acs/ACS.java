package com.alexx666.acs;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.alexx666.acs.runtime.modes.Mode;
import com.alexx666.acs.runtime.modes.ModeFactory;

/**
 * 
 * @author alexx666
 *
 */
public class ACS {
	
	private CommandLineParser parser;
	private HelpFormatter formatter;
	private Options options;

	public ACS() {
		parser = new DefaultParser();
		formatter = new HelpFormatter();
        options = new Options();
		
        options.addOption("m", "mode", true, "Select between 'monitor', 'profiler' and 'updater'.");	
        options.addOption("f", "file", true, "Path to acs.yml configuration file.");
        options.addOption("h", "help", false, "Show this message.");
	}
	
	public void start(String[] args) {
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("f") && cmd.hasOption("m")) {
				Mode rm = ModeFactory.getConfiguration(cmd.getOptionValue("m"));
				rm.setSettingsFromFile(cmd.getOptionValue("f"));
				rm.manageIO();
				rm.setExternalProcess();
				rm.run();
			}else{
				formatter.printHelp("acs [option]", options);
			}
		}catch (ParseException | NullPointerException e) {
			e.printStackTrace();
			formatter.printHelp("acs [option]", options);
		}
	}
	
	public static void main(String[] args) { (new ACS()).start(args); }
}