package com.alexx666.acs.manager.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.alexx666.acs.manager.Manager;

public class FileManager extends Manager<Path> {
		
	@Override
	public void createAll(boolean areDirs) {
		for (Path file : things) {
			create(file, areDirs);
		}
	}
	
	@Override
	public void destroyAll(boolean areDirs) {
		for (Path directory : things) {
			directory.toFile().delete();
		}
	}
	
	public static void create(Path path, boolean isDir) {
		if(!Files.exists(path)){
			try {
				File file = null;
				if (isDir) {
					file = Files.createDirectory(path).toFile();
				}else{
					file = Files.createFile(path).toFile();
				}
				file.setReadable(true, false);
				file.setWritable(true, false);
				file.setExecutable(false, true);
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}	
}
