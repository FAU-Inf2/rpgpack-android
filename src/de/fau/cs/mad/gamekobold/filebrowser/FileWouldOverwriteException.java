package de.fau.cs.mad.gamekobold.filebrowser;

import java.io.File;

public class FileWouldOverwriteException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -856950931646349532L;
	
	public FileWouldOverwriteException(File target) {
		super("The following file would be overwritten:"+target.getAbsolutePath());
	}
}
