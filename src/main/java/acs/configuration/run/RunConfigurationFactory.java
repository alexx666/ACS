package main.java.acs.configuration.run;

import main.java.acs.configuration.run.impl.Monitor;
import main.java.acs.configuration.run.impl.Profile;
import main.java.acs.configuration.run.impl.Update;

public class RunConfigurationFactory {
	
	private RunConfigurationFactory() {}
	
	private static class Static {
		private static final RunConfigurationFactory INSTANCE = new RunConfigurationFactory();
	}
	
	public static RunConfigurationFactory getInstance() {
		return Static.INSTANCE;
	}
	
	public RunConfiguration getConfiguration(String mode) throws NullPointerException {
		switch (mode) {
		case "monitor": return new Monitor();
		case "profiler": return new Profile();
		case "updater": return new Update();
		default: throw new NullPointerException();
		}
	}
}
