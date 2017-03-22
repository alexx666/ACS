package main.java.acs.configuration.running;

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
