package de.fau.cs.mad.rpgpack.toolbox;

import java.io.File;

import de.fau.cs.mad.rpgpack.R;
import de.fau.cs.mad.rpgpack.AbstractThreeButtonMenu;
import de.fau.cs.mad.rpgpack.jackson.CharacterSheet;
import de.fau.cs.mad.rpgpack.jackson.JacksonInterface;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MenuActivity extends AbstractThreeButtonMenu {
		
		private CharacterSheet[] characterSheets;
		public static String EXTRA_CHARACTER_ABS_PATH = "EXTRA_CHARACTER_ABS_PATH";
		public static String INFLATE_CHARACTER_NUMBER = "INFLATE_CHARACTER_NUMBER";

		@SuppressLint("ShowToast")
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			this.setTitle(R.string.action_tools);
			Intent intent = getIntent();
			final String[] characterAbsPaths = intent
					.getStringArrayExtra(EXTRA_CHARACTER_ABS_PATH);
			if (savedInstanceState != null) {
				characterSheets = (CharacterSheet[]) savedInstanceState
						.getParcelableArray("characterSheets");
			} else {
				characterSheets = new CharacterSheet[characterAbsPaths.length];
				if (characterAbsPaths != null) {
					try {
						int index = 0;
						for (String onePath : characterAbsPaths) {
							characterSheets[index++] = JacksonInterface
									.loadCharacterSheet(new File(onePath), false);
						}

					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
			
			final int startColor = getResources().getColor(R.color.menu_toolbox_start_color);
			final int endColor = getResources().getColor(R.color.menu_toolbox_end_color);
			final int[] midColors = getMidGradientColors(startColor, endColor);
			setTitle(R.string.toolbox_title);
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
			setButton1MainText(getString(R.string.open_dice));
			setButton1DescriptionText(getString(R.string.desc_dice));
			
			// button 2
			setButton2MainText(getString(R.string.open_tactical));
			setButton2DescriptionText(getString(R.string.desc_tactical));

			// button 3
			setButton3MainText(getString(R.string.open_timer));
			setButton3DescriptionText(getString(R.string.desc_timer));

			// set the colors for the gradients
			setButton1Color(gradient1);
			setButton2Color(gradient2);
			setButton3Color(gradient3);
		}

		/**
		 * This gets called when button 1 is clicked.
		 */
		@Override
		protected void button1Action() {
			// Go to dice tool
			Intent intent = new Intent(MenuActivity.this,
					DiceActivity.class);
			startActivity(intent);
		}

		/**
		 * This gets called when button 2 is clicked.
		 */
		@Override
		protected void button2Action() {
			// Go to tactical map tool
			Intent intent = MapActivity.createIntentForStarting(this, characterSheets);	
			startActivity(intent);
		}

		/**
		 * This gets called when button 3 is clicked.
		 */
		@Override
		protected void button3Action() {
			// Go to timer tool
			Intent intent = new Intent(MenuActivity.this, TimerActivity.class);
			startActivity(intent);
			
		}

	
	public static Intent createIntentForStarting(Context packageContext,
			CharacterSheet[] sheets) {
		Intent intent = new Intent(packageContext, MenuActivity.class);
		String[] filePaths = new String[sheets.length];
		int index = 0;
		for (CharacterSheet oneSheet : sheets) {
			filePaths[index++] = oneSheet.getFileAbsolutePath();
		}
		intent.putExtra(MenuActivity.EXTRA_CHARACTER_ABS_PATH,
				filePaths);
		return intent;
	}

}