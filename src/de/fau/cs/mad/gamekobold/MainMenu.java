package de.fau.cs.mad.gamekobold;

import de.fau.cs.mad.gamekobold.characterbrowser.CharacterBrowserActivity;
import de.fau.cs.mad.gamekobold.game.GameBrowserActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.TemplateBrowserActivity;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

public class MainMenu extends Activity implements OnClickListener{
	private LinearLayout row1 = null, row2 = null, row3 = null;

	// toast for managing double press to exit feature
	private Toast leaveToast = null;
	
	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.main_actionbar);
		setContentView(R.layout.menu_with_3_buttons);
		
		// create toast for managing double press to exit feature
		leaveToast = Toast.makeText(this, getString(R.string.leave_toast),Toast.LENGTH_SHORT);

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
		GradientDrawable row1Gradient = new GradientDrawable(Orientation.TOP_BOTTOM, standardColors);
		row1IV.setImageDrawable(row1Gradient);
		GradientDrawable row2Gradient = new GradientDrawable(Orientation.TOP_BOTTOM, standardColors);
		row2IV.setImageDrawable(row2Gradient);
		GradientDrawable row3Gradient = new GradientDrawable(Orientation.TOP_BOTTOM, standardColors);
		row3IV.setImageDrawable(row3Gradient);

		// get all the text views
		TextView row1Main = (TextView) row1.findViewById(R.id.row1_textView_main);
		TextView row1Description = (TextView) row1.findViewById(R.id.row1_textView_description);
		TextView row2Main = (TextView) row2.findViewById(R.id.row2_textView_main);
		TextView row2Description = (TextView) row2.findViewById(R.id.row2_textView_description);
		TextView row3Main = (TextView) row3.findViewById(R.id.row3_textView_main);
		TextView row3Description = (TextView) row3.findViewById(R.id.row3_textView_description);

		// underline the main texts
		row1Main.setPaintFlags(row1Main.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		row2Main.setPaintFlags(row1Main.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		row3Main.setPaintFlags(row1Main.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		
		// START EDITING HERE
		
		// gradient colors
		// button 1 gradient color
		int[] gradient1 = {
			// start color
			Color.rgb(255, 0, 0),
			// end color
			Color.rgb(100, 0, 0)
		};
		// button 2 gradient color
		int[] gradient2 = {
			// start color
			Color.rgb(0, 255, 0),
			// end color
			Color.rgb(0, 100, 0)
		};
		// button 3 gradient color
		int[] gradient3 = {
			// start color
			Color.rgb(0, 0, 255),
			// end color
			Color.rgb(0, 0, 100)
		};

		// Set the texts
		// button 1
		row1Main.setText("Game Browser");
		row1Description.setText("foo");
		// button 2
		row2Main.setText("Character Browser");
		row2Description.setText("foo");
		// button 3
		row3Main.setText("Template Browser");
		row3Description.setText("foo");
		
		// STOP EDITING HERE
		
		// set the colors for the gradients
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
		// check sdl level
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

	public void onBackPressed() {
		if (!leaveToast.getView().isShown()) {
			leaveToast.show();
		} else {
			super.onBackPressed();
		}
	}
	
	// START EDITING HERE
	
	/**
	 * This gets called when button 1 is clicked.
	 */
	private void button1Action() {
		Intent intent = new Intent(MainMenu.this, GameBrowserActivity.class);
		startActivity(intent);
	}

	/**
	 * This gets called when button 2 is clicked.
	 */
	private void button2Action() {
		Intent intent = new Intent(MainMenu.this, CharacterBrowserActivity.class);
		startActivity(intent);
	}
	
	/**
	 * This gets called when button 3 is clicked.
	 */
	private void button3Action() {
		Intent intent = new Intent(MainMenu.this, TemplateBrowserActivity.class);
		startActivity(intent);
	}
	
	// STOP EDITING HERE
}
