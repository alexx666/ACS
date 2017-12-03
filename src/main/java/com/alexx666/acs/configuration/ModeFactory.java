package com.alexx666.acs.configuration;

import com.alexx666.acs.configuration.impl.Monitor;
import com.alexx666.acs.configuration.impl.Profiler;
import com.alexx666.acs.configuration.impl.Updater;
import com.alexx666.acs.error.InvalidModeException;

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