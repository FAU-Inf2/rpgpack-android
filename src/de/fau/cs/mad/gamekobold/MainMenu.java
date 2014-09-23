package de.fau.cs.mad.gamekobold;

import android.annotation.SuppressLint;
import android.content.Intent;
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
			getResources().getColor(R.color.menu_games_start_color),
			// end color
			getResources().getColor(R.color.menu_games_end_color)
		};
		// button 2 gradient color
		int[] gradient2 = {
			// start color
			getResources().getColor(R.color.menu_characters_start_color),
			// end color
			getResources().getColor(R.color.menu_characters_end_color)
		};
		// button 3 gradient color
		int[] gradient3 = {
			// start color
			getResources().getColor(R.color.menu_templates_start_color),
			// end color
			getResources().getColor(R.color.menu_templates_end_color)
		};

		// Set the texts
		// button 1
		setButton1MainText(getString(R.string.menu_your_games));
		setButton1DescriptionText(getString(R.string.menu_your_games_description));
		
		// button 2
		setButton2MainText(getString(R.string.menu_your_characters));
		setButton2DescriptionText(getString(R.string.menu_your_characters_description));

		// button 3
		setButton3MainText(getString(R.string.menu_your_templates));
		setButton3DescriptionText(getString(R.string.menu_your_templates_description));

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
		// Go to Game sub menu
		Intent intent = new Intent(MainMenu.this, GameMenu.class);
		startActivity(intent);
	}

	/**
	 * This gets called when button 2 is clicked.
	 */
	@Override
	protected void button2Action() {
		// Go to Character sub menu
		Intent intent = new Intent(MainMenu.this, CharacterMenu.class);
		startActivity(intent);
	}

	/**
	 * This gets called when button 3 is clicked.
	 */
	@Override
	protected void button3Action() {
		// Go to template sub menu
		Intent intent = new Intent(MainMenu.this, TemplateMenu.class);
		startActivity(intent);
	}
}
