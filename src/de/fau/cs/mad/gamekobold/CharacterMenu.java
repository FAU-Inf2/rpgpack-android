package de.fau.cs.mad.gamekobold;

import android.graphics.Color;
import android.os.Bundle;

public class CharacterMenu extends AbstractThreeButtonMenu {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getString(R.string.menu_your_characters));
		// gradient colors
		// button 1 gradient color
		int[] gradient1 = {
			// start color
			Color.rgb(0, 255, 0),
			// end color
			Color.rgb(0, 203, 0)
		};
		// button 2 gradient color
		int[] gradient2 = {
			// start color
			Color.rgb(0, 203, 0),
			// end color
			Color.rgb(0, 151, 0)
		};
		// button 3 gradient color
		int[] gradient3 = {
			// start color
			Color.rgb(0, 151, 0),
			// end color
			Color.rgb(0, 100, 0)
		};

		// Set the texts
		// button 1
		setButton1MainText(getString(R.string.menu_new_character));
		setButton1DescriptionText("Beschreibung");
		
		// button 2
		setButton2MainText(getString(R.string.menu_edit_characters));
		setButton2DescriptionText("Beschreibung");

		// button 3
		setButton3MainText(getString(R.string.menu_alljoyn));
		setButton3DescriptionText("Beschreibung");

		// set the colors for the gradients
		setButton1Color(gradient1);
		setButton2Color(gradient2);
		setButton3Color(gradient3);
	}

	@Override
	protected void button1Action() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void button2Action() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void button3Action() {
		// TODO Auto-generated method stub
		
	}
}
