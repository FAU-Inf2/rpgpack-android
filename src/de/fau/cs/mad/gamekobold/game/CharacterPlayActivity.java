package de.fau.cs.mad.gamekobold.game;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.character.CharacterEditActivity;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;

public class CharacterPlayActivity extends SlideoutNavigationActivity {
	public static String EXTRA_CHARACTER_ABS_PATH = "EXTRA_CHARACTER_ABS_PATH";
	private CharacterSheet characterSheet;
	private boolean editMode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_template_generator_welcome2);
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		characterSheet = null;
		final String characterAbsPath = intent
				.getStringExtra(EXTRA_CHARACTER_ABS_PATH);
		Log.d("CharacterEditActivity", "onCreate absPath:" + characterAbsPath);
		if (characterAbsPath != null) {
			try {
				characterSheet = JacksonInterface.loadCharacterSheet(new File(
						characterAbsPath), false);
				Log.d("CharacterEditActivity", "loaded sheet");
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (characterSheet != null) {
			super.inflate(characterSheet.getRootTable());
		}
		// enable all checkboxes except in slideout-menu
		// use an asynctask to allow inflation of folders/tables/matrices
		// by superclass before
		SetCheckboxVisibilityTask task = new SetCheckboxVisibilityTask();
		task.execute(Boolean.valueOf(true));

		// Override Slideout-listener to disable checkboxes when closed
		ActionBarDrawerToggle newToggler = new ActionBarDrawerToggle(this, /*
																			 * host
																			 * Activity
																			 */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			@Override
			public void onDrawerClosed(View drawerView) {
				mDrawerToggle.onDrawerClosed(drawerView);
				setCheckboxVisibilityInSlideoutmenu(false);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				mDrawerToggle.onDrawerOpened(drawerView);
			}
		};
		mDrawerLayout.setDrawerListener(newToggler);

	}

	@Override
	public void onPause() {
		Log.d("CharacterEditActivity", "onPause, sheet:" + characterSheet);
		if (characterSheet != null) {

			try {
				// TODO add simple characterAltered Flag to prevent some
				// unneeded saving
				Log.d("Trying to save sheet", "path:"
						+ characterSheet.fileAbsolutePath);
				// open file
				final File jsonFile = new File(characterSheet.fileAbsolutePath);
				// save
				JacksonInterface.saveCharacterSheet(characterSheet, jsonFile);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		super.onPause();
	}

	/**
	 * set visibility for all checkboxes except in slideout-menu
	 * 
	 * @param visible
	 */
	private void setCheckboxVisibiltyExceptSlideoutmenu(boolean visible) {
		rootFragment.setCheckboxVisibilityBelow(true);
	}

	/**
	 * set visibility for checkboxes inside slideout-menu
	 * 
	 * @param visible
	 */
	private void setCheckboxVisibilityInSlideoutmenu(boolean visible) {
		editMode = visible;
		rootFragment.setCheckboxVisibility(visible);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_edit_mode) {
			setCheckboxVisibilityInSlideoutmenu(!editMode);
		}
		return super.onOptionsItemSelected(item);
	}

	private class SetCheckboxVisibilityTask extends
			AsyncTask<Boolean, Void, Boolean> {
		@Override
		protected void onPostExecute(Boolean visible) {
			setCheckboxVisibiltyExceptSlideoutmenu(visible.booleanValue());
		}

		@Override
		protected Boolean doInBackground(Boolean... params) {
			if (params[0] == null) {
				// if no boolean parameter given: show checkboxes
				return Boolean.valueOf(true);
			}
			return params[0];
		}
	}

	/**
	 * Creates and returns a new intent with which you can start the
	 * CharacterEditActivity. The intent will already have all necessary flags
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
		Intent intent = new Intent(packageContext, CharacterEditActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		// set flag so we do not use template mode
		intent.putExtra(SlideoutNavigationActivity.MODE_TEMPLATE, false);
		intent.putExtra(SlideoutNavigationActivity.WELCOME_TYPE_PLAY_CHARACTER, true);

		intent.putExtra(CharacterEditActivity.EXTRA_CHARACTER_ABS_PATH,
				sheet.fileAbsolutePath);
		return intent;
	}
}
