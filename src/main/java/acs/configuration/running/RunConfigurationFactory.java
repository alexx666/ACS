package main.java.acs.configuration.running;

public class RunConfigurationFactory {
	
	public RunConfiguration getConfiguration(String mode) throws NullPointerException {
		switch (mode) {
		case "monitor": return new Monitor();
		case "profiler": return new Profile();
		case "updater": return new Update();
		default: throw new NullPointerException();
		}
	}
}
