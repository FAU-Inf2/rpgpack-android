package de.fau.cs.mad.rpgpack;

import de.fau.cs.mad.rpgpack.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ThumbnailLoader {
	/**
	 * Loads the icon from the given Path. Uses the dimensions set in the resources integer array "thumbnail_dimensions".
	 * Returns null if iconPath is empty or when an error occured.
	 * Taken from http://blog-emildesign.rhcloud.com/?p=590 07.11.2014 
	 * @param iconPath The path for the icon file.
	 * @param context The context for accessing resources.
	 * @return Null if there was an error or if no icon is set. The icon as a {@link Bitmap}.
	 */
	static public Bitmap loadThumbnail(final String iconPath, final Context context) {
		if(iconPath == null || context == null) {
			return null;
		}
		if(iconPath.isEmpty()) {
			return null;
		}
		final int[] thumbnail_dimens = context.getResources().getIntArray(R.array.thumbnail_dimensions);
		final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(iconPath, options);

	    // Calculate inSampleSize, Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    options.inPreferredConfig = Bitmap.Config.RGB_565;
	    int inSampleSize = 1;

	    if (height > thumbnail_dimens[1]) 
	    {
	        inSampleSize = Math.round((float)height / (float)thumbnail_dimens[1]);
	    }
	    int expectedWidth = width / inSampleSize;

	    if (expectedWidth > thumbnail_dimens[0]) 
	    {
	        //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
	        inSampleSize = Math.round((float)width / (float)thumbnail_dimens[0]);
	    }

	    options.inSampleSize = inSampleSize;

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;

	    return BitmapFactory.decodeFile(iconPath, options);
	}
}
