package main.java.acs.configuration;

public class ConfigurationFactory {
	
	public Configuration getConfiguration(String mode) throws NullPointerException {
		switch (mode) {
		case "monitor": return new MonitorConfiguration();
		case "profiler": return new ProfileConfiguration();
		case "updater": return new UpdateConfiguration();
		default: throw new NullPointerException();
		}
	}
}
