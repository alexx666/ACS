package main.utils;

public abstract class Task implements Runnable {
	
	private Thread thread;
	private Object lock;
	private boolean synced;
	
	public Task() {
		this.thread = null;
		this.synced = false;
		this.lock = null;
	}
	
	public Task(boolean synced, Object lock) {
		this.thread = null;
		this.synced = synced;
		this.lock = lock;
	}
	
	public abstract void sync(Object lock);
	public abstract void async();
	public abstract void stop();

	public void start() {
		if (thread == null || !thread.isAlive()) {
			thread = new Thread(this);
			thread.start();	
		}
	}
	
	public void sleep(long time) {
		if (thread != null && thread.isAlive()) {
			try { Thread.sleep(time); } 
			catch (InterruptedException e) { e.printStackTrace(); }
		}
	}
	
	@Override
	public void run() {	
		if (synced) { sync(lock);}
		else { async();	}
	}
}
