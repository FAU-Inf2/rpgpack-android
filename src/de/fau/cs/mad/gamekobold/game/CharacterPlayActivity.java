package de.fau.cs.mad.gamekobold.game;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.Menu;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;

public class CharacterPlayActivity extends SlideoutNavigationActivity {
	public static String EXTRA_CHARACTER_ABS_PATH = "EXTRA_CHARACTER_ABS_PATH";
	private CharacterSheet characterSheet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_template_generator_welcome2);
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		characterSheet = null;
		final String characterAbsPath = intent
				.getStringExtra(EXTRA_CHARACTER_ABS_PATH);
		if (characterAbsPath != null) {
			try {
				characterSheet = JacksonInterface.loadCharacterSheet(new File(
						characterAbsPath), false);
				Log.d("CharacterPlayActivity", "loaded sheet");
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (characterSheet != null) {
			super.inflate(characterSheet.getRootTable());
		}

	}

	@Override
	public void onPause() {
		Log.d("CharacterPlayActivity", "onPause, sheet:" + characterSheet);
		if (characterSheet != null) {

			try {
				// TODO add simple characterAltered Flag to prevent some
				// unneeded saving
				Log.d("Trying to save sheet", "path:"
						+ characterSheet.getFileAbsolutePath());
				// open file
				final File jsonFile = new File(characterSheet.getFileAbsolutePath());
				// save
				JacksonInterface.saveCharacterSheet(characterSheet, jsonFile);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		super.onPause();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if (SlideoutNavigationActivity.getAc().getDrawerLayout()
				.isDrawerOpen(GravityCompat.START)) {
			getMenuInflater().inflate(R.menu.character_editor, menu);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Creates and returns a new intent with which you can start the
	 * CharacterPlayActivity. The intent will already have all necessary flags
	 * and extras set.
	 * 
	 * @param packageContext
	 *            Same as the first parameter when creating a new intent.
	 * @param sheet
	 *            The CharacterSheet to edit.
	 * @return The created ready to use intent.
	 */
	public static Intent createIntentForStarting(Context packageContext,
			CharacterSheet sheet) {
		Intent intent = new Intent(packageContext, CharacterPlayActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		// set flag so we do not use template mode
		intent.putExtra(SlideoutNavigationActivity.MODE_TEMPLATE, true);
		
		intent.putExtra(SlideoutNavigationActivity.MODE_CREATE_NEW_TEMPLATE, false);
		intent.putExtra(SlideoutNavigationActivity.MODE_PLAY_CHARACTER, true);

		intent.putExtra(SlideoutNavigationActivity.WELCOME_TYPE_PLAY_CHARACTER,
				true);
		intent.putExtra(CharacterPlayActivity.EXTRA_CHARACTER_ABS_PATH,
				sheet.getFileAbsolutePath());
		intent.putExtra(SlideoutNavigationActivity.EXTRA_CHARACTER_ABS_PATH,
				sheet.getFileAbsolutePath());
		Log.d("Intent is created!!!!!!", "CREATED!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		return intent;
	}
}
