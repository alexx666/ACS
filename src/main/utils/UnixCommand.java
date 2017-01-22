package main.utils;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class UnixCommand {
	
	private static Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());
	
	private StringBuffer output;
	
	public UnixCommand() {}
	
	public StringBuffer getOutput() { return output; }
		
	public void execute(List<String> commands, boolean print, boolean out) {
		try {			
			ProcessBuilder pb = new ProcessBuilder(commands);
			pb.redirectErrorStream(true);
			Process p = pb.start();
			
			StreamHandler stream = new StreamHandler(p.getInputStream(), p.getOutputStream(), "n\"\"DL3", this, print, out);
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
		}catch(IOException e) { 
			LOGGER.info("Exception: " + e.getMessage());
		}
	}
}
