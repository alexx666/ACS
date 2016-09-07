package main.utils;

public abstract class Task implements Runnable {
	
	private Thread t;
	
	@Override
	public void run() {	/* code to run */ }

	public void start() {
		if (t == null || !t.isAlive()) {
			t = new Thread(this);
			t.start();	
		}
	}
	
	public void sleep(long time) {
		if (t != null && t.isAlive()) {
			try { Thread.sleep(time); } catch (InterruptedException e) {/*ignored*/}
		}
	}
}
