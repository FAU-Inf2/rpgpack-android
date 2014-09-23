package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.ThumbnailLoader;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
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
		// Log.d("CharacterSelectAdapter", "constructor");
		this.sheets = objects;
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Log.d("CharacterSelectAdapter", "getView");
		final Bitmap icon = ThumbnailLoader.loadThumbnail(
				sheets[position].getIconPath(),
				SlideoutNavigationActivity.getAc());
		LayoutInflater li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View wholeView = li.inflate(R.layout.character_spinner_row, parent,
				false);
		ImageView theImage = (ImageView) wholeView
				.findViewById(R.id.spinnerImage);
		if (icon != null) {
			Drawable drawable = new BitmapDrawable(context.getResources(), icon);
			theImage.setImageDrawable(drawable);
		} else {
			String uri = "@drawable/group_white";
			int imageResource = context.getResources().getIdentifier(uri, null,
					context.getPackageName());
			theImage.setImageResource(imageResource);

		}
		// theImage.setAdjustViewBounds(true);
		// theImage.setScaleType(ScaleType.FIT_CENTER);

		// margin comes from parent to we have to subtract that
		Window window = SlideoutNavigationActivity.getAc().getWindow();
		View v = window.getDecorView();
		int resId = SlideoutNavigationActivity.getAc().getResources()
				.getIdentifier("action_bar_container", "id", "android");
		View actionBarView = v.findViewById(resId);
		LinearLayout actionBarParent = (LinearLayout) actionBarView.getParent();
		ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) actionBarParent
				.getLayoutParams();
		// Log.d("CharacterSelectAdapter", "actionBarHeight method == " +
		// getActionBarHeight());
		// Log.d("CharacterSelectAdapter", "actionBar padding top == " +
		// actionBarView.getPaddingTop());
		// Log.d("CharacterSelectAdapter", "actionBar padding bot == " +
		// actionBarView.getPaddingBottom());
		// Log.d("CharacterSelectAdapter", "actionBar margin top == " +
		// pxToDp(lp.topMargin));
		// Log.d("CharacterSelectAdapter", "actionBar margin bottom == " +
		// pxToDp(lp.bottomMargin));

		scaleImage(
				theImage,
				(getActionBarHeight() - pxToDp(lp.topMargin) - pxToDp(lp.bottomMargin)));
		return theImage;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) { // This
																					// view
																					// starts
																					// when
																					// we
																					// click
																					// the
																					// spinner.
	// Log.d("CharacterSelectAdapter", "getDropDownView");
	// return super.getDropDownView(position, convertView, parent);
		TextView tv = new TextView(context);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources()
				.getDimension(R.dimen.text_large));
		tv.setText(sheets[position].getName());
		return tv;
	}

	private void scaleImage(ImageView view, int boundBoxInDp) {
		// Get the ImageView and its bitmap
		Drawable drawing = view.getDrawable();
		Bitmap bitmap = ((BitmapDrawable) drawing).getBitmap();

		// Get current dimensions
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		// Determine how much to scale: the dimension requiring less scaling is
		// closer to the its side. This way the image always stays inside your
		// bounding box AND either x/y axis touches it.
		float xScale = ((float) boundBoxInDp) / width;
		float yScale = ((float) boundBoxInDp) / height;
		float scale = (xScale <= yScale) ? xScale : yScale;

		// Create a matrix for the scaling and add the scaling data
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		// Create a new bitmap and convert it to a format understood by the
		// ImageView
		Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		BitmapDrawable result = new BitmapDrawable(scaledBitmap);
		width = scaledBitmap.getWidth();
		height = scaledBitmap.getHeight();

		// Apply the scaled bitmap
		view.setImageDrawable(result);

		// Now change ImageView's dimensions to match the scaled image
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
				.getLayoutParams();
		params.width = width;
		params.height = height;
		view.setLayoutParams(params);
	}

	// private int dpToPx(int dp)
	// {
	// float density =
	// context.getApplicationContext().getResources().getDisplayMetrics().density;
	// return Math.round((float)dp * density);
	// }

	private int pxToDp(int px) {
		DisplayMetrics displayMetrics = getContext().getResources()
				.getDisplayMetrics();
		int dp = Math.round(px
				/ (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return dp;
	}

	public int getActionBarHeight() {
		int actionBarHeight = 0;
		TypedValue tv = new TypedValue();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (SlideoutNavigationActivity.getAc().getTheme()
					.resolveAttribute(android.R.attr.actionBarSize, tv, true))
				actionBarHeight = TypedValue.complexToDimensionPixelSize(
						tv.data, SlideoutNavigationActivity.getAc()
								.getResources().getDisplayMetrics());
		} else {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
					SlideoutNavigationActivity.getAc().getResources()
							.getDisplayMetrics());
		}
		return actionBarHeight;
		// commented out 2 other ways to get action bar dimensions
		// method 1
		// final TypedArray styledAttributes =
		// getContext().getTheme().obtainStyledAttributes(
		// new int[] { android.R.attr.actionBarSize });
		// actionBarHeight = (int) styledAttributes.getDimension(0, 0);
		// styledAttributes.recycle();
		// method 2
		// Window window =
		// SlideoutNavigationActivity.theActiveActivity.getWindow();
		// View v = window.getDecorView();
		// int resId = SlideoutNavigationActivity.theActiveActivity.
		// getResources().getIdentifier("action_bar_container", "id",
		// "android");

	}

}
