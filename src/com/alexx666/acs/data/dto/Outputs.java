package com.alexx666.acs.data.dto;

public class Outputs {
	private String file;
	private String append;

	public String getAppend() {
		return append;
	}

	public void setAppend(String append) {
		this.append = append;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	public boolean shouldAppend() {
		return (append == "yes" ? true : false);
	}
}
