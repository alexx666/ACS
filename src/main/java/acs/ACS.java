package main.java.acs;

import main.java.acs.configuration.Configuration;
import main.java.acs.configuration.ConfigurationFactory;

public class ACS {

	public static void main(String[] args) {
		ConfigurationFactory cf = new ConfigurationFactory();
		
		try {
			Configuration config = cf.getConfiguration(args);
			config.run();
		}catch (NullPointerException e) {}
	}
}