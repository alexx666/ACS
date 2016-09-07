package main.utils;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;



public class UnixShell {
	
	private static Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	
	private List<String> commands;
	private StreamHandler stream;
	private StringBuffer output;
		
	public UnixShell(List<String> commands) {
		this.commands = commands;
	}
	
	public StringBuffer getOutput() {
		return output;
	}

	public void execute(boolean print, boolean out) {
		output = new StringBuffer();
		try {
			ProcessBuilder pb = new ProcessBuilder(commands);
			pb.redirectErrorStream(true);
			Process p = pb.start();
			
			stream = new StreamHandler(p.getInputStream(), p.getOutputStream(), "n\"\"DL3", this, print, out);
			stream.start();
		
			if (out) {
				synchronized (this) { 
					try { 
						this.wait(); 
					} catch (InterruptedException e) {
						LOGGER.info(e.getMessage());
					}
				}
				output = stream.getOutputBuffer();
			}
		}catch(IOException ex) { 
			LOGGER.info("Exception: " + ex.getMessage());
		}
	}
}
