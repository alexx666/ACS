package com.alexx666.acs.runtime.modes;

import com.alexx666.acs.runtime.modes.impl.Monitor;
import com.alexx666.acs.runtime.modes.impl.Profiler;
import com.alexx666.acs.runtime.modes.impl.Updater;

public class ModeFactory {
	
	public static Mode getConfiguration(String mode) throws NullPointerException {
		switch (mode) {
		case "monitor": return new Monitor();
		case "profiler": return new Profiler();
		case "updater": return new Updater();
		default: throw new NullPointerException();
		}
	}
}