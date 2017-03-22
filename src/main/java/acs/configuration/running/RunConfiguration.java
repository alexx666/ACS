package main.java.acs.configuration.running;

import main.java.acs.process.ProcessManager;

public abstract class RunConfiguration {
	
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
				System.out.println();
			}catch (InterruptedException e) { e.printStackTrace(); }					
		}
	});
	
	public RunConfiguration() {
		Runtime.getRuntime().addShutdownHook(shutdownhook);
	}
	
	public abstract void run();
}