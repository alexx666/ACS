package main.java.com.alexx666.acs.configuration;

import main.java.com.alexx666.acs.configuration.impl.Monitor;
import main.java.com.alexx666.acs.configuration.impl.Profiler;
import main.java.com.alexx666.acs.configuration.impl.Updater;
import main.java.com.alexx666.acs.error.InvalidModeException;

public class ModeFactory {
	
	public static Mode getConfiguration(String mode) throws InvalidModeException {
		switch (mode) {
		case "monitor": return new Monitor();
		case "profiler": return new Profiler();
		case "updater": return new Updater();
		default: throw new InvalidModeException();
		}
	}
}