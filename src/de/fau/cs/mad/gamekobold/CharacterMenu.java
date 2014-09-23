package de.fau.cs.mad.gamekobold;

import de.fau.cs.mad.gamekobold.characterbrowser.CharacterBrowserActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.TemplateBrowserActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class CharacterMenu extends AbstractThreeButtonMenu {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final int startColor = getResources().getColor(R.color.menu_characters_start_color);
		final int endColor = getResources().getColor(R.color.menu_characters_end_color);
		final int[] midColors = getMidGradientColors(startColor, endColor);
		setTitle(R.string.menu_your_characters);
		// gradient colors
		// button 1 gradient color
		int[] gradient1 = {
			// start color
			startColor,
			// end color
			midColors[0]
		};
		// button 2 gradient color
		int[] gradient2 = {
			// start color
			midColors[0],
			// end color
			midColors[1]
		};
		// button 3 gradient color
		int[] gradient3 = {
			// start color
			midColors[1],
			// end color
			endColor
		};

		// Set the texts
		// button 1
		setButton1MainText(getString(R.string.menu_new_character));
		setButton1DescriptionText(getString(R.string.menu_new_character_description));
		
		// button 2
		setButton2MainText(getString(R.string.menu_edit_characters));
		setButton2DescriptionText(getString(R.string.menu_edit_characters_description));

		// button 3
		//TODO right text
		setButton3MainText(getString(R.string.menu_alljoyn));
		setButton3DescriptionText(getString(R.string.menu_alljoyn_description));

		// set the colors for the gradients
		setButton1Color(gradient1);
		setButton2Color(gradient2);
		setButton3Color(gradient3);
	}

	@Override
	protected void button1Action() {
		// create new character
		Intent i = new Intent(CharacterMenu.this,
				TemplateBrowserActivity.class);
		//TODO: translate
		Toast.makeText(CharacterMenu.this, "Please pick a template", Toast.LENGTH_LONG).show();
		i.putExtra(TemplateBrowserActivity.CREATE_CHAR_DIRECT, true);
		startActivity(i);
	}

	@Override
	protected void button2Action() {
		// edit character
		Intent i = new Intent(CharacterMenu.this,
				CharacterBrowserActivity.class);
		startActivity(i);
	}

	@Override
	protected void button3Action() {
		Toast.makeText(CharacterMenu.this, "Alljoyn", Toast.LENGTH_LONG).show();
	}
}
