package main.java.acs.configuration;

import main.java.acs.utils.ProcessManager;

public abstract class Configuration {
	
	protected volatile boolean running = true;
	
	private Object lock = new Object();
	private volatile ProcessManager pm = new ProcessManager();
	private final Thread mainThread = Thread.currentThread();	
	private final Thread shutdownhook = new Thread(new Runnable() {
		@Override
		public void run() {					
			try {
				System.out.println();
				pm.stopAll(true);
				synchronized (lock) {
					running = false;
					lock.notifyAll();
				}
				mainThread.join();
			}catch (InterruptedException e) { e.printStackTrace(); }					
		}
	});
	
	public Configuration() {
		Runtime.getRuntime().addShutdownHook(shutdownhook);
	}
	
	public ProcessManager getPm() { return pm; }
	public Object getLock() { return lock; }
	
	public abstract void run();
}