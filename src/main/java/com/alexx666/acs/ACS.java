package com.alexx666.acs;

import java.io.FileNotFoundException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.alexx666.acs.configuration.Mode;
import com.alexx666.acs.configuration.ModeFactory;
import com.alexx666.acs.error.InvalidModeException;
import net.sourceforge.yamlbeans.YamlException;

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
				rm.run();
			}else{
				System.out.println("[acs] WARNING: Please indicate config file and a mode of execution. Options 'f' and 'm' are mendatory.");
				formatter.printHelp("acs [option]", options);
			}
		}catch (ParseException e) {
			System.out.println("[acs] Error parsing input variables.");
			formatter.printHelp("acs [option]", options);
		}catch (FileNotFoundException e) {
			System.out.println("[acs] Error: File not found!");
			formatter.printHelp("acs [option]", options);
		}catch (YamlException e) {
			System.out.println("[acs] Error while reading file!");
			formatter.printHelp("acs [option]", options);
		}catch (InvalidModeException e) {
			System.out.println("[acs] Error: Invalid mode!");
			formatter.printHelp("acs [option]", options);
		}catch (NullPointerException e) {
			System.out.println("[acs] Error: Unable to calculate anomaly.");			
		} 
	}
	
	public static void main(String[] args) { (new ACS()).start(args); }
}