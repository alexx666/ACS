package main.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Logger;


public class StreamHandler extends Task implements RunnableTask {
	
	private static Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	
	private boolean sudoIsRequested;
	private InputStream inputStream;
	private String adminPassword;
	private PrintWriter printWriter;
	private StringBuffer outputBuffer;
	private Object lock;
	private boolean print;
	private boolean out;

	public StreamHandler(InputStream inputStream, OutputStream outputStream, String adminPassword, Object lock, boolean print, boolean out) {
	    this.inputStream = inputStream;
	    this.printWriter = new PrintWriter(outputStream);
	    this.adminPassword = adminPassword;
	    this.sudoIsRequested = true;
	    this.outputBuffer = new StringBuffer();
	    this.lock = lock;
	    this.print = print;
	    this.out = out;
	}
	
	public StringBuffer getOutputBuffer() { return outputBuffer; }

	public void run() {
		if (out) {
			sync(lock);
		}else{
			async();
		}
	}

	@Override
	public void sync(Object lock) {
		if (sudoIsRequested) {
			printWriter.println(adminPassword);
			printWriter.flush();
		}
	    BufferedReader bufferedReader = null;
	    try {
	    	bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	    	String line = null;
	    	while ((line = bufferedReader.readLine()) != null) {
	    		if (print) {
		    		System.out.println("     "+line);
	    		}
	    		outputBuffer.append(line + "\n");
	    	}
	    }catch (Throwable e) { 
	    	System.out.println(e.getMessage());
	    }finally{
	    	try { 
	    		bufferedReader.close(); 
	    	}catch (IOException ex) {
	    		LOGGER.info("SQLException: " + ex.getMessage());
	    	}
	    	synchronized (lock) { 
	    		lock.notify();
	    	}
	    }
	}

	@Override
	public void async() {
		if (sudoIsRequested) {
			printWriter.println(adminPassword);
			printWriter.flush();
		}
	}
}
