package de.fau.cs.mad.gamekobold;

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
	 * @throws IOException
	 */
	public static void copyFile(File from, File to) throws IOException {
		if(from == null || to == null) { 
			return;
		}
		// check if from == to
		if(from.getAbsolutePath().equals(to.getAbsolutePath())) {
			return;
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
