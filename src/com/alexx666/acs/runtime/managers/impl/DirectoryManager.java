package com.alexx666.acs.runtime.managers.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.alexx666.acs.runtime.managers.Manager;

public class DirectoryManager extends Manager<Path> {
	
	public void createAll(String permissions) {
		for (Path directory : things) {
			create(directory, permissions);
		}
	}
	
	public void destroyAll(String permissions) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void createAll() {
		createAll("");
	}

	@Override
	public void destroyAll() {
		destroyAll("");
	}
	
	public static void create(Path path, String permissions) {
		if(!Files.exists(path)){
			try {
				Files.createDirectory(path).toFile();
				/*dir.setExecutable(true, false);
				dir.setReadable(true, false);
				dir.setWritable(true, false);*/
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
