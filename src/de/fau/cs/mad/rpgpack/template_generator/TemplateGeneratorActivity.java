package de.fau.cs.mad.rpgpack.template_generator;

import de.fau.cs.mad.rpgpack.R;
import de.fau.cs.mad.rpgpack.*;
import de.fau.cs.mad.rpgpack.jackson.AutosaveHandler;
import de.fau.cs.mad.rpgpack.jackson.Template;
import de.fau.cs.mad.rpgpack.jackson.TemplateSaverTask;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class TemplateGeneratorActivity extends SlideoutNavigationActivity {
	private AutosaveHandler autosaveHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_template_generator_welcome2);
		super.onCreate(savedInstanceState);
		// has to be created AFTER SlideoutNavigationActivity.onCreate
		autosaveHandler = new AutosaveHandler(this, myTemplate);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		// check for user setting
		final SharedPreferences prefs = getPreferences(MODE_PRIVATE);
		if (prefs.getBoolean(PREFERENCE_AUTO_SAVE, true)) {
			autosaveHandler.start();
		}
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// getFragmentManager().putFragment(outState, "currentFragment",
		// currentFragment);
		outState.putBoolean("activityExistedBefore", true);
		Log.d("onSaveInstanceState",
				"putBoolean(activityExistedBefore, true)!!!");

		super.onSaveInstanceState(outState);
	}

	/**
	 * Menu gets its layout here -> action bar on top gets adapted
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		// set right menu and navigation items
		getMenuInflater().inflate(R.menu.template_generator, menu);

		MenuItem myItem = menu.findItem(R.id.action_auto_save_menu_item);
		if (myItem != null) {
			SharedPreferences prefs = getPreferences(MODE_PRIVATE);
			myItem.setChecked(prefs.getBoolean(PREFERENCE_AUTO_SAVE, true));
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_edit_mode) {
			// TODO: changes to apply in edit mode -> allow switching table
			// elements
		} else if (id == R.id.action_go_above) {
			goAbove();
		}
		/*
		 * JACKSON START
		 */
		else if (id == R.id.action_save_template) {
			saveTemplateAsync();
		} else if (id == R.id.action_auto_save_menu_item) {
			SharedPreferences prefs = getPreferences(MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			if (item.isChecked()) {
				item.setChecked(false);
				editor.putBoolean(PREFERENCE_AUTO_SAVE, false);
				autosaveHandler.stop();
			} else {
				item.setChecked(true);
				editor.putBoolean(PREFERENCE_AUTO_SAVE, true);
				autosaveHandler.start();
			}
			editor.commit();
			Log.d("AUTO SAVE", "state:" + item.isChecked());
		}
		/*
		 * JACKSON END
		 */
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		FragmentManager fm = getFragmentManager();
		// handling before -> stepping back to last viewed page
		// if (fm.getBackStackEntryCount() > 0) {
		// fm.popBackStack();
		// currentFragment = currentFragment.backStackElement;
		// if(currentFragment.isATopFragment){
		// topFragment = currentFragment;
		// }
		// } else {
		// DialogFragment dialog = WarningLeaveDialog.newInstance();
		// if(currentFragment instanceof FolderFragment){
		// Log.d("MainTemplateGenerator", "elemente: " + ((FolderFragment)
		// currentFragment).allData.size());
		// }
		// dialog.show(fm, "");
		// }
		// invalidateOptionsMenu();
		// handling now: just leave activity
		DialogFragment dialog = WarningLeaveDialog.newInstance();
		if (currentFragment instanceof FolderFragment) {
			Log.d("MainTemplateGenerator", "elemente: "
					+ ((FolderFragment) currentFragment).allData.size());
		}
		dialog.show(fm, "");
	}

	protected void superBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		autosaveHandler.stop();
		/*
		 * JACKSON START
		 */
		if (saveOnNextOnPause) {
			saveOnNextOnPause = false;
			saveTemplateAsync();
		}
		/*
		 * JACKSON END
		 */
		super.onPause();
	}

	private void saveTemplateAsync() {
		TemplateSaverTask saverTask = new TemplateSaverTask(
				getApplicationContext(), true);
		saverTask.execute(new Template[] { myTemplate });
	}

}
