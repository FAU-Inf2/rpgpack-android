package de.fau.cs.mad.gamekobold;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

public class MainMenu extends AbstractThreeButtonMenu {
	// toast for managing double press to exit feature
	private Toast leaveToast = null;

	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// create toast for managing double press to exit feature
		leaveToast = Toast.makeText(this, getString(R.string.leave_toast),Toast.LENGTH_SHORT);

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
		setButton1MainText(getString(R.string.menu_your_games));
		setButton1DescriptionText("Beschreibung");
		
		// button 2
		setButton2MainText(getString(R.string.menu_your_characters));
		setButton2DescriptionText("Beschreibung");

		// button 3
		setButton3MainText(getString(R.string.menu_your_templates));
		setButton3DescriptionText("Beschreibung");

		// set the colors for the gradients
		setButton1Color(gradient1);
		setButton2Color(gradient2);
		setButton3Color(gradient3);
	}

	public void onBackPressed() {
		if (!leaveToast.getView().isShown()) {
			leaveToast.show();
		} else {
			super.onBackPressed();
		}
	}

	/**
	 * This gets called when button 1 is clicked.
	 */
	@Override
	protected void button1Action() {
		Intent intent = new Intent(MainMenu.this, GameMenu.class);
		startActivity(intent);
	}

	/**
	 * This gets called when button 2 is clicked.
	 */
	@Override
	protected void button2Action() {
		Intent intent = new Intent(MainMenu.this, CharacterMenu.class);
		startActivity(intent);
	}

	/**
	 * This gets called when button 3 is clicked.
	 */
	@Override
	protected void button3Action() {
		Intent intent = new Intent(MainMenu.this, TemplateMenu.class);
		startActivity(intent);
	}
}
