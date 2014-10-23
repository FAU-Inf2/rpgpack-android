package de.fau.cs.mad.rpgpack.templatestore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;

public class Helper {
	
	static String getSizeName(Context context) {
	    int screenLayout = context.getResources().getConfiguration().screenLayout;
	    screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

	    switch (screenLayout) {
	    case Configuration.SCREENLAYOUT_SIZE_SMALL:
	        return "small";
	    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
	        return "normal";
	    case Configuration.SCREENLAYOUT_SIZE_LARGE:
	        return "large";
	    case 4: // Configuration.SCREENLAYOUT_SIZE_XLARGE is API >= 9
	        return "xlarge";
	    default:
	        return "undefined";
	    }
	}
	
	static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/** checks if template was already downloaded before 
	 * @throws IOException **/
	public static boolean templateExists(int id, Context context, TemplateStoreMainActivity activity) throws IOException {
		
		File file = new File(context.getExternalFilesDir(null), "downloaded.txt");
		if( ! file.exists()) {
			file.createNewFile();
			return false;	
		}
		
	    BufferedReader in = new BufferedReader(new FileReader(file.getAbsolutePath()));
	    String str=null;
	    Boolean exists = false;
	    while((str = in.readLine()) != null){
	        if(str.trim().contentEquals(Integer.valueOf(id).toString()))
	        	exists = true;
	    }
		in.close();
		return exists;
	}
	
	/** adds id of downloaded template to file **/
	public static void addIdToFile(int id, Context context) throws IOException {
		
		File file = new File(context.getExternalFilesDir(null), "downloaded.txt");
		if( ! file.exists() ) {
			file.createNewFile();
		}
		
		BufferedWriter output = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true));
		output.append(Integer.valueOf(id).toString());
		output.newLine();
		output.close();
		
	}

	public static String readTemplate(String filePath) throws IOException {
		File f = new File(filePath);
		if( ! f.canRead())
			throw new IOException();
			
		BufferedReader in = new BufferedReader(new FileReader(filePath));
		String template = "";
		String line;
		while( (line = in.readLine()) != null) {
			template += line;
		}
		in.close();
		return template;
	}
}
