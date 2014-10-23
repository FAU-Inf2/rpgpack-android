package de.fau.cs.mad.rpgpack.filebrowser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileCopyUtility {
	/**
	 * 
	 * @param from
	 * @param to
	 * @param overwriteIfExists
	 * @throws IOException
	 * @throws FileWouldOverwriteException
	 */
	public static void copyFile(File from, File to, boolean overwriteIfExists) throws IOException, FileWouldOverwriteException {
		if(from == null || to == null) { 
			return;
		}
		// check if from == to
		if(from.getAbsolutePath().equals(to.getAbsolutePath())) {
			return;
		}
		// check if we would overwrite a file
		if(!overwriteIfExists && to.exists()) {
			throw new FileWouldOverwriteException(to);
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(from);
			out = new FileOutputStream(to);
			byte[] buffer = new byte[1024];
			int len;
			while((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		}
		finally {
			in.close();
			out.close();
		}
	}
}
