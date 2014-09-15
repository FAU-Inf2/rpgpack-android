package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.ThumbnailLoader;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CharacterSelectAdapter extends ArrayAdapter<CharacterSheet> {
	CharacterSheet[] sheets;
	Context context;
	
	public CharacterSelectAdapter(Context context, int resource,
			CharacterSheet[] objects) {
		super(context, resource, objects);
        Log.d("CharacterSelectAdapter", "constructor");
		this.sheets = objects;
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {   // Ordinary view in Spinner, we use android.R.layout.simple_spinner_item
        Log.d("CharacterSelectAdapter", "getView");

		final Bitmap icon = ThumbnailLoader.loadThumbnail(sheets[position].getIconPath(),
				SlideoutNavigationActivity.getAc());
		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View wholeView = li.inflate(R.layout.character_spinner_row, parent, false);
		ImageView theImage = (ImageView) wholeView.findViewById(R.id.spinnerImage);
		ImageView characterImage = new ImageView(SlideoutNavigationActivity.getAc());

//		LayoutParams params = (LayoutParams) characterImage.getLayoutParams();
//		params.width = 20;
//		// existing height is ok as is, no need to edit it
//		characterImage.setLayoutParams(params);
		characterImage.setAdjustViewBounds(true);
//		characterImage.setScaleType(ScaleType.FIT_XY);
        Drawable drawable = null;
		if(icon != null){
//			characterImage = scaleImage(icon);
			drawable = new BitmapDrawable(context.getResources(), icon);
//			characterImage.setImageBitmap(icon);
			characterImage.setImageDrawable(drawable);
			theImage.setImageDrawable(drawable);
		}
		else{
			String uri = "@drawable/character_icon";
			int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
			drawable = context.getResources().getDrawable(imageResource);
//			characterImage.setImageDrawable(drawable);
			characterImage.setImageResource(imageResource);
			theImage.setImageResource(imageResource);
		}

//		LinearLayout.LayoutParams vp = 
//		        new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
//		LayoutParams params = (LayoutParams) characterImage.getLayoutParams();
//        Log.d("CharacterSelectAdapter", "dimens: width==" + params.width + "; height==" + params.height);
//        drawable.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//		int width = drawable.getMeasuredWidth();
//		int height = drawable.getMeasuredHeight();
//        Log.d("CharacterSelectAdapter", "MEASURED dimens: width==" + width + "; height==" + height);

//		LinearLayout.LayoutParams vp = 
//		        new LinearLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
//		characterImage.setLayoutParams(vp);

        
//        BitmapDrawable bd=(BitmapDrawable) drawable;
//        int height=bd.getBitmap().getHeight();
//        int width=bd.getBitmap().getWidth();
//        Log.d("CharacterSelectAdapter", "MEASURED dimens: width==" + width + "; height==" + height);
//        characterImage.setMaxWidth(width);
        
        int actionBarHeight = -1;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(
            		tv.data,context.getResources().getDisplayMetrics());
        }
        Log.d("CharacterSelectAdapter", "actionBarHeight == " + actionBarHeight);
        
//        LinearLayout ll = new LinearLayout(context);
//        ll.setLayoutParams(new LinearLayout.LayoutParams(actionBarHeight, actionBarHeight));
//        ll.addView(characterImage);
//        
//		return ll;
//        return characterImage;
        return wholeView;
//        return super.getView(position, convertView, parent);   
		
		//test
//		TextView text = new TextView(context);
//		text.setText("a");
//		return text;
    }
	
	@Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {   // This view starts when we click the spinner.
        Log.d("CharacterSelectAdapter", "getDropDownView");
//		return super.getDropDownView(position, convertView, parent); 
        TextView tv = new TextView(context);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
 	           context.getResources().getDimension(R.dimen.text_large));
        tv.setText(sheets[position].getName());
        return tv;
    }
	
	private ImageView scaleImage(Bitmap bitmap)
	{
	    // Get current dimensions AND the desired bounding box
	    int width = bitmap.getWidth();
	    int height = bitmap.getHeight();
	    int bounding = dpToPx(250);
	    Log.i("Test", "original width = " + Integer.toString(width));
	    Log.i("Test", "original height = " + Integer.toString(height));
	    Log.i("Test", "bounding = " + Integer.toString(bounding));

	    // Determine how much to scale: the dimension requiring less scaling is
	    // closer to the its side. This way the image always stays inside your
	    // bounding box AND either x/y axis touches it.  
	    float xScale = ((float) bounding) / width;
	    float yScale = ((float) bounding) / height;
	    float scale = (xScale <= yScale) ? xScale : yScale;
	    Log.i("Test", "xScale = " + Float.toString(xScale));
	    Log.i("Test", "yScale = " + Float.toString(yScale));
	    Log.i("Test", "scale = " + Float.toString(scale));

	    // Create a matrix for the scaling and add the scaling data
	    Matrix matrix = new Matrix();
	    matrix.postScale(scale, scale);

	    // Create a new bitmap and convert it to a format understood by the ImageView 
	    Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	    width = scaledBitmap.getWidth(); // re-use
	    height = scaledBitmap.getHeight(); // re-use
	    BitmapDrawable result = new BitmapDrawable(scaledBitmap);
	    Log.i("Test", "scaled width = " + Integer.toString(width));
	    Log.i("Test", "scaled height = " + Integer.toString(height));

	    // Apply the scaled bitmap
	    ImageView view = new ImageView(context);
	    view.setImageDrawable(result);

	    // Now change ImageView's dimensions to match the scaled image
	    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams(); 
	    params.width = width;
	    params.height = height;
	    view.setLayoutParams(params);

	    Log.i("Test", "done");
	    return view;
	}

	private int dpToPx(int dp)
	{
	    float density = context.getResources().getDisplayMetrics().density;
	    return Math.round((float)dp * density);
	}

}
