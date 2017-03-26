package main.java.acs.modes.run;

import java.io.FileNotFoundException;
import java.io.FileReader;

import main.java.acs.data.dto.Settings;
import main.java.acs.modes.process.ProcessManager;
import net.sourceforge.yamlbeans.YamlException;
import net.sourceforge.yamlbeans.YamlReader;

public abstract class RunningMode {
	
	protected Settings settings;
	
	protected volatile boolean running = true;
	protected volatile ProcessManager processManager = new ProcessManager();
	protected Object lock = new Object();
	private final Thread mainThread = Thread.currentThread();	
	private final Thread shutdownhook = new Thread(new Runnable() {
		@Override
		public void run() {					
			try {
				processManager.stopAll(false);
				synchronized (lock) {
					running = false;
					lock.notifyAll();
				}
				mainThread.join();
			}catch (InterruptedException e) { e.printStackTrace(); }					
		}
	});
	
	public RunningMode() {
		Runtime.getRuntime().addShutdownHook(shutdownhook);
	}
	
	public void setSettingsFromFile(String filePath) {
		try {
			YamlReader reader = new YamlReader(new FileReader(filePath));
			this.settings = reader.read(Settings.class);
		} catch (FileNotFoundException | YamlException e) {
			System.out.println("[acs] Error: Invalid file!");
		}
	}

	public abstract void setExternalProcessCommands();
	public abstract void createOutputFile();
	public abstract void run();
}