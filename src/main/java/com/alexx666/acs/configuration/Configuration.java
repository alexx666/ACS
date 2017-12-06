package com.alexx666.acs.configuration;

import com.alexx666.acs.manager.impl.FileManager;
import com.alexx666.acs.manager.impl.ProcessManager;

public abstract class Configuration {
		
	protected volatile ProcessManager processManager = new ProcessManager();
	protected volatile boolean running = true;
	
	protected Object lock = new Object();
	
	protected FileManager fm = new FileManager();
	
	private final Thread mainThread = Thread.currentThread();	
	
	public Configuration() { 
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
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
		}));
		
		setExternalProcesses();
		manageIO();
	}
	
	public abstract void setExternalProcesses();
	public abstract void manageIO();
	public abstract void run();
}