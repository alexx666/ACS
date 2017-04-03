package com.alexx666.acs.runtime.modes;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.alexx666.acs.data.dto.Settings;
import com.alexx666.acs.runtime.managers.impl.ProcessManager;

import net.sourceforge.yamlbeans.YamlException;
import net.sourceforge.yamlbeans.YamlReader;

public abstract class Mode {
	
	protected Settings settings;
	
	protected volatile boolean running = true;
	protected volatile ProcessManager processManager = new ProcessManager();
	protected Object lock = new Object();
	private final Thread mainThread = Thread.currentThread();	
	private final Thread shutdownhook = new Thread(new Runnable() {
		@Override
		public void run() {					
			try {
				processManager.destroyAll(false);
				synchronized (lock) {
					running = false;
					lock.notifyAll();
				}
				mainThread.join();
			}catch (InterruptedException e) {
				e.printStackTrace(); 
			}					
		}
	});
	
	public Mode() {
		Runtime.getRuntime().addShutdownHook(shutdownhook);
	}
	
	public void setSettingsFromFile(String filePath) throws FileNotFoundException, YamlException {
		YamlReader reader = new YamlReader(new FileReader(filePath));
		this.settings = reader.read(Settings.class);
		manageIO();
		setExternalProcess();
	}
	
	protected abstract void setExternalProcess();
	protected abstract void manageIO();
	public abstract void run();
}