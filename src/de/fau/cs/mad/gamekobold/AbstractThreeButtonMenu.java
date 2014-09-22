package de.fau.cs.mad.gamekobold;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class AbstractThreeButtonMenu extends Activity implements OnClickListener{
	private LinearLayout row1 = null, row2 = null, row3 = null;
	private GradientDrawable row1Gradient = null, row2Gradient = null, row3Gradient = null;
	private TextView row1Main = null, row2Main = null, row3Main = null;
	private TextView row1Description = null, row2Description = null, row3Description = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.main_actionbar);
		setContentView(R.layout.menu_with_3_buttons);

		// get the three rows
		row1 = (LinearLayout)findViewById(R.id.row1);
		row2 = (LinearLayout)findViewById(R.id.row2);
		row3 = (LinearLayout)findViewById(R.id.row3);

		// set their on click listener
		row1.setOnClickListener(this);
		row2.setOnClickListener(this);
		row3.setOnClickListener(this);

		// get the three image views
		ImageView row1IV = (ImageView) row1.findViewById(R.id.row1_imageView);
		ImageView row2IV = (ImageView) row2.findViewById(R.id.row2_imageView);
		ImageView row3IV = (ImageView) row3.findViewById(R.id.row3_imageView);

		// create the 3 gradient drawables with standard colors of black and white and add them to
		// the image views
		int[] standardColors = {
			Color.BLACK,
			Color.WHITE
		};
		row1Gradient = new GradientDrawable(Orientation.TOP_BOTTOM, standardColors);
		row1IV.setImageDrawable(row1Gradient);
		row2Gradient = new GradientDrawable(Orientation.TOP_BOTTOM, standardColors);
		row2IV.setImageDrawable(row2Gradient);
		row3Gradient = new GradientDrawable(Orientation.TOP_BOTTOM, standardColors);
		row3IV.setImageDrawable(row3Gradient);

		// get all the text views
		row1Main = (TextView) row1.findViewById(R.id.row1_textView_main);
		row1Description = (TextView) row1.findViewById(R.id.row1_textView_description);
		row2Main = (TextView) row2.findViewById(R.id.row2_textView_main);
		row2Description = (TextView) row2.findViewById(R.id.row2_textView_description);
		row3Main = (TextView) row3.findViewById(R.id.row3_textView_main);
		row3Description = (TextView) row3.findViewById(R.id.row3_textView_description);

		// underline the main texts
		row1Main.setPaintFlags(row1Main.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		row2Main.setPaintFlags(row1Main.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		row3Main.setPaintFlags(row1Main.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
	}

	/**
	 * Sets the colors for the provided {@link GradientDrawable}. Calls {@link GradientDrawable#setColor(int)} on API level < 16.
	 * For API level >= 16 {@link GradientDrawable#setColors(int[])} is called.
	 * @param colors The colors to use. Uses colors[0] on API < 16.
	 * @param drawable The drawable for which to set the color.
	 */
	@SuppressLint("NewApi")
	private void setColors(int [] colors, GradientDrawable drawable) {
		// check sdl level
		if(android.os.Build.VERSION.SDK_INT >= 16) {
			drawable.setColors(colors);			
		}
		else {
			drawable.setColor(colors[0]);
		}	
	}

	public void setButton1Color(int [] colors) {
		setColors(colors, row1Gradient);
	}

	public void setButton2Color(int [] colors) {
		setColors(colors, row2Gradient);	
	}

	public void setButton3Color(int [] colors) {
		setColors(colors, row3Gradient);		
	}
	
	public void setButton1MainText(String text) {
		row1Main.setText(text);
	}
	
	public void setButton2MainText(String text) {
		row2Main.setText(text);
	}
	
	public void setButton3MainText(String text) {
		row3Main.setText(text);
	}
	
	public void setButton1DescriptionText(String text) {
		row1Description.setText(text);
	}

	public void setButton2DescriptionText(String text) {
		row2Description.setText(text);
	}
	
	public void setButton3DescriptionText(String text) {
		row3Description.setText(text);
	}
	
	@Override
	public void onClick(View v) {
		if(v == row1) {
			Log.d("MainMenu", "clicked row1");
			button1Action();
		}
		else if(v == row2) {
			Log.d("MainMenu", "clicked row2");
			button2Action();
		}
		else if(v == row3) {
			Log.d("MainMenu", "clicked row3");
			button3Action();
		}
	}
	/**
	 * This gets called when button 1 is clicked.
	 */
	protected abstract void button1Action();
	/**
	 * This gets called when button 2 is clicked.
	 */
	protected abstract void button2Action();
	/**
	 * This gets called when button 3 is clicked.
	 */
	protected abstract void button3Action();
}
