package de.fau.cs.mad.gamekobold.game;

import java.io.File;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.character.FavoriteTable;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.template_generator.FolderFragment;
import de.fau.cs.mad.gamekobold.template_generator.WelcomePlayCharacterFragment;

public class CharacterPlayActivity extends SlideoutNavigationActivity implements
		OnItemSelectedListener {
	public static String EXTRA_CHARACTER_ABS_PATH = "EXTRA_CHARACTER_ABS_PATH";
	public static String INFLATE_CHARACTER_NUMBER = "INFLATE_CHARACTER_NUMBER";
	private CharacterSheet[] characterSheets;
	static int lastCharSelected = 0;
	private boolean favoritesInSlideoutShown = false;

	// modes mode;
	//
	// enum modes{
	// selection, edit
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_template_generator_welcome2);
		super.onCreate(savedInstanceState);
		onlySelected = true;
		Intent intent = getIntent();
		final String[] characterAbsPaths = intent
				.getStringArrayExtra(EXTRA_CHARACTER_ABS_PATH);
		if (savedInstanceState != null) {
			characterSheets = (CharacterSheet[]) savedInstanceState
					.getParcelableArray("characterSheets");
		} else {
			characterSheets = new CharacterSheet[characterAbsPaths.length];
			if (characterAbsPaths != null) {
				Log.d("CharacterPlayActivity", "characterAbsPath != null");
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
			if (characterSheets != null) {
				ContainerTable table = null;
				final int charNumberToInflate = intent.getIntExtra(
						INFLATE_CHARACTER_NUMBER, -1);
				if (charNumberToInflate != -1) {
					table = characterSheets[charNumberToInflate].getRootTable();
				} else {
					table = characterSheets[0].getRootTable();
					table.addTable(new FavoriteTable("Favorites"));
				}
				// TODO add one table more - Favorite, add it for
				// inflation!
				super.inflate(table);
			}
		}

		// //use custom adapter
		// // Log.d("CharacterPlayActivity",
		// "constructor of CharacterSelectAdapter will be called");
		// ArrayAdapter<CharacterSheet> characterAdapter =
		// new CharacterSelectAdapter(getAc(),
		// android.R.layout.simple_spinner_item, characterSheets); //selected
		// item will look like a spinner set from XML
		// Spinner spinner = new Spinner(this);
		//
		// characterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// spinner.setAdapter(characterAdapter);
		//
		//
		// //add dropdown menu to select characters
		// // Spinner spinner = new Spinner(this);
		// // final ArrayList<String> spinnerArray = new ArrayList<String>();
		// // final String[] characterNames = new
		// String[characterSheets.length];
		// // int index=0;
		// // for(CharacterSheet oneCharSheet: characterSheets){
		// // characterNames[index] = oneCharSheet.getName();
		// // spinnerArray.add(characterNames[index]);
		// // index++;
		// // }
		// // ArrayAdapter<String> spinnerArrayAdapter = new
		// ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
		// spinnerArray); //selected item will look like a spinner set from XML
		// //
		// spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// // spinner.setAdapter(spinnerArrayAdapter);
		// // //
		// // // Adapter
		// //// SpinnerAdapter adapter =
		// //// ArrayAdapter.createFromResource(this, R.array.actions,
		// //// android.R.layout.simple_spinner_dropdown_item);
		// //
		// // Callback
		// OnNavigationListener callback = new OnNavigationListener() {
		//
		//
		// // String[] items = (String[]) spinnerArray.toArray(); // List items
		// from res
		//
		// @Override
		// public boolean onNavigationItemSelected(int position, long id) {
		//
		// // Do stuff when navigation item is selected
		//
		// Log.d("CharacterPlayActivity", "selected char: "); // Debug
		//
		// return true;
		//
		// }
		//
		// };
		//
		// // IcsLinearLayout listNavLayout = (IcsLinearLayout)
		// getLayoutInflater()
		// // .inflate(R.layout.abs__action_bar_tab_bar_view, null);
		// // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		// // LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		// // params.gravity = Gravity.CENTER;
		// // listNavLayout.addView(spinner, params);
		// // listNavLayout.setGravity(Gravity.RIGHT);
		//
		//
		// // Action Bar
		// ActionBar actions = getActionBar();
		// actions.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		// actions.setDisplayShowTitleEnabled(false);
		// // actions.setListNavigationCallbacks(characterAdapter, callback);
		// // actions.setCustomView(spinner);
		// RelativeLayout myView = (RelativeLayout) actions.getCustomView();
		// myView.addView(spinner, Gravity.RIGHT);
	}

	@Override
	public void onPause() {
		Log.d("CharacterPlayActivity", "onPause, sheets:" + characterSheets);
		if (characterSheets != null) {

			try {
				// TODO add simple characterAltered Flag to prevent some
				// unneeded saving
				// Log.d("Trying to save sheets",
				// "path:" + characterSheet.getFileAbsolutePath());
				Log.d("Trying to save sheets", "");

				// open files
				for (CharacterSheet charSheet : characterSheets) {
					final File jsonFile = new File(
							charSheet.getFileAbsolutePath());
					// save
					JacksonInterface.saveCharacterSheet(charSheet, jsonFile);
				}
				// final File jsonFile = new File(
				// characterSheet.getFileAbsolutePath());
				// // save
				// JacksonInterface.saveCharacterSheet(characterSheet,
				// jsonFile);
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
			// XXX: later use line thats commented out -> has edit button to
			// show favorite stars
			// getMenuInflater().inflate(R.menu.combined_play, menu);
			getMenuInflater().inflate(R.menu.play_actions, menu);
		} else {
			getMenuInflater().inflate(R.menu.play_actions, menu);
		}
		MenuItem invisibleItem = menu.findItem(R.id.action_show_invisible);
		invisibleItem.setCheckable(true);
		invisibleItem.setChecked(showInvisible);
		// if(inEditMode()){
		// Log.d("CharacterPlayActivity", "adapt menu for edit mode");
		// MenuItem menuItem = menu.findItem(R.id.action_editable_mode);
		// CharSequence menuTitle = menuItem.getTitle();
		// SpannableString styledMenuTitle = new SpannableString(menuTitle);
		// styledMenuTitle.setSpan(new UnderlineSpan(), 0, menuTitle.length(),
		// 0);
		// menuItem.setTitle(styledMenuTitle);
		// menuItem = menu.findItem(R.id.action_selection_mode);
		// menuTitle = menuItem.getTitle();
		// styledMenuTitle = new SpannableString(menuTitle);
		// Object[] spans = styledMenuTitle.getSpans(0, menuTitle.length(),
		// UnderlineSpan.class);
		// styledMenuTitle.removeSpan(spans);
		// }
		// else if(inSelectionMode()){
		// Log.d("CharacterPlayActivity", "adapt menu for selection mode");
		// MenuItem menuItem = menu.findItem(R.id.action_selection_mode);
		// CharSequence menuTitle = menuItem.getTitle();
		// SpannableString styledMenuTitle = new SpannableString(menuTitle);
		// styledMenuTitle.setSpan(new UnderlineSpan(), 0, menuTitle.length(),
		// 0);
		// menuItem.setTitle(styledMenuTitle);
		// menuItem = menu.findItem(R.id.action_editable_mode);
		// menuTitle = menuItem.getTitle();
		// styledMenuTitle = new SpannableString(menuTitle);
		// Object[] spans = styledMenuTitle.getSpans(0, menuTitle.length(),
		// UnderlineSpan.class);
		// styledMenuTitle.removeSpan(spans);
		// }
		MenuItem editModeItem = menu.findItem(R.id.action_editable_mode);
		editModeItem.setCheckable(true);
		editModeItem.setChecked(inEditMode());
		return super.onPrepareOptionsMenu(menu);
	}

	public void openTools() {
		Intent intent = new Intent(CharacterPlayActivity.this,
				ToolboxActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_tools:
			openTools();
			return true;
			// case R.id.action_selection_mode:
			// // Log.d("CharacterPlayActivity", "set selection mode");
			// if(mode==modes.selection){
			// return true;
			// }
			// else{
			// mode = modes.selection;
			// reinflate();
			// invalidateOptionsMenu();
			// return true;
			// }
		case R.id.action_editable_mode:
			if (item.isChecked()) {
				item.setChecked(false);
				mode = modes.selection;
				rootFragment.dataAdapter.setEditable(false);
			} else {
				item.setChecked(true);
				mode = modes.edit;
				rootFragment.dataAdapter.setEditable(true);
			}
			reinflate();
			invalidateOptionsMenu();
			return true;
		case R.id.action_show_invisible:
			if (item.isChecked()) {
				Log.d("CharacterPlayActivity", "showInvisible == false");
				item.setChecked(false);
				SlideoutNavigationActivity.getAc().showInvisible = false;
				reinflate();
			} else {
				Log.d("CharacterPlayActivity", "showInvisible == true");
				item.setChecked(true);
				SlideoutNavigationActivity.getAc().showInvisible = true;
				reinflate();
			}
			return true;
		case R.id.action_edit:
			favoritesInSlideoutShown = !favoritesInSlideoutShown;
			rootFragment.setCheckboxVisibility(favoritesInSlideoutShown);
			Log.d("CharacterPlayActivity", "favorites shown == "
					+ favoritesInSlideoutShown);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArray("characterSheets", characterSheets);
	};

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
			CharacterSheet[] sheets) {
		Log.d("CharacterPlayActivity",
				"createIntentForStarting: sheets.length == " + sheets.length);
		Intent intent = new Intent(packageContext, CharacterPlayActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		// set flag so we do not use template mode
		intent.putExtra(SlideoutNavigationActivity.EXTRA_MODE,
				SlideoutNavigationActivity.MODE_PLAY_CHARACTER);

		String[] filePaths = new String[sheets.length];
		int index = 0;
		for (CharacterSheet oneSheet : sheets) {
			filePaths[index++] = oneSheet.getFileAbsolutePath();
		}
		intent.putExtra(CharacterPlayActivity.EXTRA_CHARACTER_ABS_PATH,
				filePaths);
		intent.putExtra(SlideoutNavigationActivity.EXTRA_CHARACTER_ABS_PATH,
				filePaths);
		return intent;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// inflate new fragments here
		if (position != lastCharSelected) {
			lastCharSelected = position;
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			// transaction.remove(rootFragment);

			String charName = characterSheets[position].getName();
			Log.d("CharacterPlayActivity",
					"inflating new rootFragment of char: " + charName
							+ "; position: " + position);
			rootFragment = new FolderFragment();
			rootFragment.isATopFragment = true;
			// following is needed!
			super.inflate(characterSheets[position].getRootTable());
			// ((FolderFragment)
			// rootFragment).setJacksonTable(characterSheets[position].getRootTable());
			// super.inflate(characterSheets[position].getRootTable());

			// transaction.add(R.id.navigation_drawer, rootFragment,
			// "rootFragment");
			Log.d("CharacterPlayActivity", "replacing rootFragment;"
					+ "new one has " + rootFragment.getElementCount());
			Log.d("CharacterPlayActivity", "new root is editable "
					+ SlideoutNavigationActivity.theActiveActivity.inEditMode());

			transaction.replace(R.id.navigation_drawer, rootFragment,
					"rootFragment");

			// transaction.commit();
			topFragment = new WelcomePlayCharacterFragment();
			topFragment.elementName = getResources().getString(
					R.string.titel_play_character_welcome);
			topFragment.isATopFragment = true;
			currentFragment = topFragment;
			transaction.replace(R.id.frame_layout_container, currentFragment,
					"currentFragment");
			transaction.commit();
			getFragmentManager().executePendingTransactions();
			// ContainerTable table = characterSheets[position].getRootTable();
			// super.inflate(table);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public void reinflate() {
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		Log.d("CharacterPlayActivity", "reinflate; editmode == "
				+ getAc().inEditMode() + "; selectionMode == "
				+ getAc().inSelectionMode());
		// super.inflate(characterSheets[lastCharSelected].getRootTable());
		// transaction.replace(R.id.navigation_drawer, rootFragment,
		// "rootFragment");
		// super.inflate(characterSheets[lastCharSelected].getRootTable());
		// rootFragment = new FolderFragment();
		// rootFragment.isATopFragment = true;
		// //following is needed!
		// rootFragment = new FolderFragment();
		// rootFragment.isATopFragment = true;
		// following is needed!
		// super.inflate(characterSheets[lastCharSelected].getRootTable());
		// mDrawerLayout.closeDrawers();
		// TODO: find out why replacing the fragment in the drawer doesnt work
		// as expected
		// (works for character switching; see onItemSelected above)
		transaction.replace(R.id.navigation_drawer, rootFragment,
				"rootFragment");
		// mDrawerLayout.invalidate();

		// topFragment = new WelcomePlayCharacterFragment();
		// topFragment.elementName = getResources().getString(
		// R.string.titel_play_character_welcome);
		// topFragment.isATopFragment = true;
		// currentFragment = topFragment;
		// transaction.replace(R.id.frame_layout_container,
		// currentFragment, "currentFragment");
		// transaction.replace(R.id.frame_layout_container, currentFragment,
		// "currentFragment");
		transaction.replace(R.id.frame_layout_container, currentFragment);
		transaction.commit();
		getFragmentManager().executePendingTransactions();
		mDrawerLayout.invalidate();

		Log.d("CharacterPlayActivity", "reinflate; editmode == "
				+ getAc().inEditMode() + "; matrix view for editing!");

	}

}
