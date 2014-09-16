package de.fau.cs.mad.gamekobold.templatestore;

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

}
