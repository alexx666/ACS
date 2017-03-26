package main.java.acs.modes.run;

import main.java.acs.modes.run.impl.Monitor;
import main.java.acs.modes.run.impl.Profiler;
import main.java.acs.modes.run.impl.Updater;

public class RunningModeFactory {
	
	public static RunningMode getConfiguration(String mode) throws NullPointerException {
		switch (mode) {
		case "monitor": return new Monitor();
		case "profiler": return new Profiler();
		case "updater": return new Updater();
		default: throw new NullPointerException();
		}
	}
}