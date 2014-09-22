package de.fau.cs.mad.gamekobold;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainMenu extends Activity implements OnClickListener{
	private LinearLayout row1 = null, row2 = null, row3 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.main_actionbar);
		setContentView(R.layout.menu_with_3_buttons);

		row1 = (LinearLayout)findViewById(R.id.row1);
		row2 = (LinearLayout)findViewById(R.id.row2);
		row3 = (LinearLayout)findViewById(R.id.row3);

		row1.setOnClickListener(this);
		row2.setOnClickListener(this);
		row3.setOnClickListener(this);

		ImageView row1IV = (ImageView) row1.findViewById(R.id.ImageView02);
		ImageView row2IV = (ImageView) row2.findViewById(R.id.ImageView01);
		ImageView row3IV = (ImageView) row3.findViewById(R.id.imageView1);

		int[] standardColors = {
			Color.BLACK,
			Color.WHITE
		};
		GradientDrawable row1Gradient = new GradientDrawable(Orientation.TOP_BOTTOM, standardColors);
		row1IV.setImageDrawable(row1Gradient);
		GradientDrawable row2Gradient = new GradientDrawable(Orientation.TOP_BOTTOM, standardColors);
		row2IV.setImageDrawable(row2Gradient);
		GradientDrawable row3Gradient = new GradientDrawable(Orientation.TOP_BOTTOM, standardColors);
		row3IV.setImageDrawable(row3Gradient);
		// Edit your colors here
		// button 1
		int[] gradient1 = {
			// start color
			Color.rgb(255, 0, 0),
			// end color
			Color.rgb(100, 0, 0)
		};
		// button 2
		int[] gradient2 = {
			// start color
			Color.rgb(0, 255, 0),
			// end color
			Color.rgb(0, 100, 0)
		};
		// button 3
		int[] gradient3 = {
			// start color
			Color.rgb(0, 0, 255),
			// end color
			Color.rgb(0, 0, 100)
		};
		// Stop editing here
		setColors(gradient1, row1Gradient);
		setColors(gradient2, row2Gradient);
		setColors(gradient3, row3Gradient);
	}

	/**
	 * Sets the colors for the provided {@link GradientDrawable}. Calls {@link GradientDrawable#setColor(int)} on API level < 16.
	 * For API level >= 16 {@link GradientDrawable#setColors(int[])} is called.
	 * @param colors The colors to use. Uses colors[0] on API < 16.
	 * @param drawable The drawable for which to set the color.
	 */
	@SuppressLint("NewApi")
	private void setColors(int [] colors, GradientDrawable drawable) {
		if(android.os.Build.VERSION.SDK_INT >= 16) {
			drawable.setColors(colors);			
		}
		else {
			drawable.setColor(colors[0]);
		}	
	}

	@Override
	public void onClick(View v) {
		if(v == row1) {
			// button 1 action
			Log.d("MainMenu", "clicked row1");
		}
		else if(v == row2) {
			// button 2 action
			Log.d("MainMenu", "clicked row2");
		}
		else if(v == row3) {
			// button 3 action
			Log.d("MainMenu", "clicked row3");			
		}
	}
}
