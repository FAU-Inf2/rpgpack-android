package de.fau.cs.mad.gamekobold.filebrowser;

import java.io.File;

public class FileTargetIsSourceException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5601635809083163355L;

	public FileTargetIsSourceException(File file) {
		super("Target file equals source file:"+file.getAbsolutePath());
	}
}
