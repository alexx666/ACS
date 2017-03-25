package main.java.acs;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import main.java.acs.configuration.ACSConfiguration;

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
        options.addOption("c", "clean", false, "Clear all existing snapshot data from DB.");
        options.addOption("h", "help", false, "Show this message.");
	}
	
	public void start(String[] args) {
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("f") && cmd.hasOption("m")) {
				ACSConfiguration.getInstance().setSettingsFromFile(cmd.getOptionValue("f"));
				ACSConfiguration.getInstance().prepareLogFiles();
				ACSConfiguration.getInstance().prepareDatabase(cmd.hasOption("c"));
				ACSConfiguration.getInstance().setExternalProcessCommands();
				ACSConfiguration.getInstance().getConfiguration(cmd.getOptionValue("m")).run();
			}else{
				formatter.printHelp("acs [option]", options);
			}
		}catch (ParseException e) {
			formatter.printHelp("acs [option]", options);
		}catch (NullPointerException e) {
			System.out.println("[acs] Error: Invalid mode!");
		}
	}
	
	public static void main(String[] args) { (new ACS()).start(args); }
}