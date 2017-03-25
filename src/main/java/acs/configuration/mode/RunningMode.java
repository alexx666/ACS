package main.java.acs.configuration.mode;

import main.java.acs.utils.process.ProcessManager;

public abstract class RunningMode {
	
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
	
	public abstract void run();
}