package de.fau.cs.mad.gamekobold.game;

import java.io.File;

import de.fau.cs.mad.gamekobold.AbstractThreeButtonMenu;
import de.fau.cs.mad.gamekobold.CharacterMenu;
import de.fau.cs.mad.gamekobold.GameMenu;
import de.fau.cs.mad.gamekobold.MainMenu;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.TemplateMenu;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class ToolboxActivity2 extends AbstractThreeButtonMenu {
		
		private CharacterSheet[] characterSheets;
		public static String EXTRA_CHARACTER_ABS_PATH = "EXTRA_CHARACTER_ABS_PATH";
		public static String INFLATE_CHARACTER_NUMBER = "INFLATE_CHARACTER_NUMBER";
		
		private Toast leaveToast = null;

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
					Log.d("Toolbox", "characterAbsPath != null");
					try {
						int index = 0;
						for (String onePath : characterAbsPaths) {
							characterSheets[index++] = JacksonInterface
									.loadCharacterSheet(new File(onePath), false);
						}

						// characterSheet = JacksonInterface.loadCharacterSheet(new
						// File(
						// characterAbsPath), false);
						Log.d("CharacterPlayActivity", "loaded sheets");
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
			Intent intent = new Intent(ToolboxActivity2.this,
					ToolboxDiceActivity.class);
			startActivity(intent);
		}

		/**
		 * This gets called when button 2 is clicked.
		 */
		@Override
		protected void button2Action() {
			// Go to Character sub menu
			Intent intent = ToolboxMapActivity.createIntentForStarting(this, characterSheets);	
			startActivity(intent);
		}

		/**
		 * This gets called when button 3 is clicked.
		 */
		@Override
		protected void button3Action() {
			// Go to template sub menu
			Intent intent = new Intent(ToolboxActivity2.this, ToolboxTimerActivity.class);
			startActivity(intent);
			
		}

	
	public static Intent createIntentForStarting(Context packageContext,
			CharacterSheet[] sheets) {
		Log.i("ToolboxActivity",
				"createIntentForStarting: sheets.length == " + sheets.length);
		Intent intent = new Intent(packageContext, ToolboxActivity2.class);
		String[] filePaths = new String[sheets.length];
		int index = 0;
		for (CharacterSheet oneSheet : sheets) {
			filePaths[index++] = oneSheet.getFileAbsolutePath();
		}
		intent.putExtra(ToolboxActivity2.EXTRA_CHARACTER_ABS_PATH,
				filePaths);
		return intent;
	}

}