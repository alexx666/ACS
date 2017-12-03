package main.java.com.alexx666.acs.error;

public class InvalidModeException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidModeException() { super(); }
	public InvalidModeException(String message) { super(message); }
	public InvalidModeException(String message, Throwable cause) { super(message, cause); }
	public InvalidModeException(Throwable cause) { super(cause); }
	
}
