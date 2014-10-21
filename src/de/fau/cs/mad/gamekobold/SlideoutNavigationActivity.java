package de.fau.cs.mad.gamekobold;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.character.CharacterEditActivity;
import de.fau.cs.mad.gamekobold.game.CharacterPlayActivity;
import de.fau.cs.mad.gamekobold.game.CharacterSelectAdapter;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.jackson.Template;
import de.fau.cs.mad.gamekobold.template_generator.FolderFragment;
import de.fau.cs.mad.gamekobold.template_generator.GeneralFragment;
import de.fau.cs.mad.gamekobold.template_generator.TemplateGeneratorActivity;
import de.fau.cs.mad.gamekobold.template_generator.WelcomeFragment;
import de.fau.cs.mad.gamekobold.template_generator.WelcomeNewCharacterFragment;
import de.fau.cs.mad.gamekobold.template_generator.WelcomePlayCharacterFragment;

public class SlideoutNavigationActivity extends FragmentActivity{

	public static Template myTemplate = null;
	public static CharacterSheet myCharacter = null;
	
	// for distinguishing between template creation/editing and character
	public static final String EXTRA_MODE = "EXTRA_MODE";
	public static final int MODE_CREATE_TEMPLATE = 0;
	public static final int MODE_EDIT_TEMPLATE = 1;
	public static final int MODE_PLAY_CHARACTER = 2;
	public static final int MODE_EDIT_CARACTER = 3;

	// strings for passing data via intent
	public static final String EXTRA_TEMPLATE_FILE_NAME = "TEMPLATE_FILE_NAME";
	public static final String EXTRA_CHARACTER_ABS_PATH = "CHARACTER_ABS_PATH";
	
	// shared preferences strings
	public static final String SHARED_PREFERENCES_FILE_NAME = "TemplateGeneratorPrefs";
	public static final String PREFERENCE_AUTO_SAVE = "AUTO_SAVE";
	public static final String PREFERENCE_LAST_EDITED_TEMPLATE_NAME = "LAST_EDITED_TEMPLATE_NAME";



	public static boolean saveOnNextOnPause = false;
	public static SlideoutNavigationActivity myActivity = null;
	public CountDownLatch countDownLatch;
	protected GeneralFragment currentFragment;
	protected GeneralFragment topFragment;
	protected FolderFragment rootFragment;
	public static SlideoutNavigationActivity theActiveActivity;
	protected DrawerLayout mDrawerLayout;
	protected ActionBarDrawerToggle mDrawerToggle;
	protected CharacterSheet[] characterSheets;
	protected Spinner characterSelectSpinner;
//	protected boolean editable = false;
	protected modes mode = modes.selection;
	public boolean onlySelected = false;
	public boolean showInvisible = false;
	
	public enum modes{
		selection, edit
	}
	
	public boolean inEditMode(){
		return mode==modes.edit?true:false;
	}
	
	public boolean inSelectionMode(){
		return mode==modes.selection?true:false;
	}
	
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		theActiveActivity = this;
		if(savedInstanceState != null){
			mode = (modes) savedInstanceState.getSerializable("mode");
			showInvisible = savedInstanceState.getBoolean("showInvisible");
		}
		else{
			//if no mode is saved -> take default one
			if(theActiveActivity instanceof CharacterPlayActivity
					&& ((CharacterPlayActivity) theActiveActivity).inEditMode()){
				mode = modes.edit;
			}
			else if(theActiveActivity instanceof CharacterPlayActivity
					&& ((CharacterPlayActivity) theActiveActivity).inSelectionMode()){
				mode = modes.selection;
			}
			else if(theActiveActivity instanceof CharacterEditActivity){
				mode = modes.selection;
			}
			else{
				Log.d("FolderFragment","case not known! set to editable == true");
				mode = modes.edit;
			}
		}
		/*
		 * JACKSON START
		 */
		myActivity = this;
		boolean createTemplateMode = false;
		boolean editTemplateMode = false;
//		boolean editCharacterMode = false;
		boolean playingMode = false;
		Log.d("Saved instance state", "" + savedInstanceState);
		if (savedInstanceState == null) {
			final Intent intent = getIntent();
			// check if we are in template mode
			if (getAc() instanceof TemplateGeneratorActivity) {
				// check mode
				if(intent.getIntExtra(EXTRA_MODE, MODE_CREATE_TEMPLATE) == MODE_CREATE_TEMPLATE) {
					createTemplateMode = true;
					final Template template = (Template) intent
							.getParcelableExtra(Template.PARCELABLE_STRING);
					if (template != null) {
						Log.d("MainTemplateGenerator",
								"Got template meta data in intent!");
						myTemplate = template;
					} else {
						// TODO show error?!
					}
				}
				else if(intent.getIntExtra(EXTRA_MODE, MODE_CREATE_TEMPLATE) == MODE_EDIT_TEMPLATE) {
					Log.d("MainTemplateGenerator", "Edit mode!");
					editTemplateMode = true;
					// get file name
					final String templateFileName = intent
							.getStringExtra(EXTRA_TEMPLATE_FILE_NAME);
					// we are editing an old one, so load it
					// create new async task
					jacksonLoadTemplateAsync task = new jacksonLoadTemplateAsync();
					// do the setup
					countDownLatch = task.doSetup(this);
					// start
					task.execute(templateFileName);
				}
			}
			else if(getAc() instanceof CharacterEditActivity) {
				Log.d("MainTemplateGenerator", "Character edit mode!");
//				editCharacterMode = true;
				// we are in edit character mode
			}
//				else{
//					Log.d("SlideoutNavigationActivity", "mode to inflate not recognized!");
//					System.exit(1);
//				}
				
//			}
			 else if (intent.getIntExtra(EXTRA_MODE, MODE_PLAY_CHARACTER) == MODE_PLAY_CHARACTER) {
					playingMode = true;
					Log.d("MODE_PLAY_CHARACTER", "MODE_PLAY_CHARACTER!");
					// we are in a play mode

				}
			 else{
				Log.d("SlideoutNavigationActivity", "mode to inflate not recognized!");
				System.exit(1);
			 }
		} else {
			Log.d("SlideoutNavigationActivity", "got savedInstance");
		}
		/*
		 * JACKSON END
		 */
		rootFragment = (FolderFragment) getFragmentManager().findFragmentByTag(
				"rootFragment");
		// create it because we need its data for slideoutmenu
		if (rootFragment == null) {
			Log.d("SlideoutNavigationActivity", "rootFragment == null");
			rootFragment = new FolderFragment();
			rootFragment.isATopFragment = true;
			/*
			 * JACKSON START
			 */
			if (createTemplateMode) {
				// if we are creating a new one, a template has been already created.
				// So we set the jackson table
				// if we are editing a template, the async task will set the
				// table and start inflation
					((FolderFragment) rootFragment).setJacksonTable(myTemplate
							.getCharacterSheet().getRootTable());
			}
			/*
			 * JACKSON END
			 */
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.add(R.id.navigation_drawer, rootFragment,
					"rootFragment");
			transaction.commit();
		}
		// method: use fragment to store everything
		// if(topFragment == null){
		// FragmentTransaction fragmentTransaction =
		// getFragmentManager().beginTransaction();
		// topFragment = new WelcomeFragment();
		// topFragment.isATopFragment = true;
		// topFragment.elementName = "Welcome";
		// currentFragment = topFragment;
		// fragmentTransaction.add(R.id.frame_layout_container,
		// currentFragment);
		// fragmentTransaction.commit();
		// getFragmentManager().executePendingTransactions();
		// }
		if (savedInstanceState == null) {
			FragmentTransaction fragmentTransaction = getFragmentManager()
					.beginTransaction();

			// pass the current WelcomeFragment!
			if (createTemplateMode || editTemplateMode) {
				topFragment = new WelcomeFragment();
				topFragment.elementName = getResources().getString(
						R.string.titel_template_generator_welcome);

			} else if (playingMode)
			{
				topFragment = new WelcomePlayCharacterFragment();
				topFragment.elementName = getResources().getString(
						R.string.titel_play_character_welcome);

			}
			else{
				//now assume character edit mode
				topFragment = new WelcomeNewCharacterFragment();
				topFragment.elementName = getResources().getString(
						R.string.titel_character_generator_welcome);
			}
			topFragment.isATopFragment = true;
			currentFragment = topFragment;
			fragmentTransaction.add(R.id.frame_layout_container,
					currentFragment, "currentFragment");
			fragmentTransaction.commit();
			getFragmentManager().executePendingTransactions();
		}

		mDrawerLayout = (DrawerLayout) findViewById(R.id.main_view_empty);
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				Log.d("SlideoutNavigationActivity", "invalidateOptionsMenu!");
				invalidateOptionsMenu();
				super.onDrawerOpened(drawerView);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		if(getAc() instanceof CharacterPlayActivity){
			final Intent intent = getIntent();
			final String[] characterAbsPaths = intent
					.getStringArrayExtra(EXTRA_CHARACTER_ABS_PATH);
			characterSheets = new CharacterSheet[characterAbsPaths.length];
			if (characterAbsPaths != null) {
				Log.d("CharacterPlayActivity", "characterAbsPath != null");
				try {
					int index=0;
					for(String onePath: characterAbsPaths){
						characterSheets[index++] = JacksonInterface.loadCharacterSheet(new File(
								onePath), false);
					}
						
						
//					characterSheet = JacksonInterface.loadCharacterSheet(new File(
//							characterAbsPath), false);
					Log.d("SlideoutNavigationActivity", "loaded sheets");
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
		if(getAc() instanceof CharacterPlayActivity){
			//TODO spinner might not be needed to be inflated every
			//time you open it?!
			ArrayAdapter<CharacterSheet> characterAdapter =
					new CharacterSelectAdapter(getAc(), android.R.layout.simple_spinner_item, characterSheets); //selected item will look like a spinner set from XML
//			Spinner spinner = new Spinner(this);
			final Spinner spinner = (Spinner) getLayoutInflater().inflate(R.layout.character_spinner, null, false);
//			spinner.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//					 LayoutParams.WRAP_CONTENT));
			
//			characterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(characterAdapter);

			
			//add dropdown menu to select characters
//			Spinner spinner = new Spinner(this);
//			final ArrayList<String> spinnerArray = new ArrayList<String>();
//			final String[] characterNames = new String[characterSheets.length];
//			int index=0;
//			for(CharacterSheet oneCharSheet: characterSheets){
//				characterNames[index] = oneCharSheet.getName();
//				spinnerArray.add(characterNames[index]);
//				index++;
//			}
//			ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
//			spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//			spinner.setAdapter(spinnerArrayAdapter);
//			//
//			// Adapter
////			SpinnerAdapter adapter =
////			        ArrayAdapter.createFromResource(this, R.array.actions,
////			        android.R.layout.simple_spinner_dropdown_item);
	//
			// Callback
//			OnNavigationListener callback = new OnNavigationListener() {
//
//				
////			    String[] items = (String[]) spinnerArray.toArray(); // List items from res
//
//			    @Override
//			    public boolean onNavigationItemSelected(int position, long id) {
//
//			        // Do stuff when navigation item is selected
//
//			        Log.d("CharacterPlayActivity", "selected char: "); // Debug
//
//			        return true;
//
//			    }
//
//			};

//			IcsLinearLayout listNavLayout = (IcsLinearLayout) getLayoutInflater()
//		            .inflate(R.layout.abs__action_bar_tab_bar_view, null);
//		    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//		            LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
//		    params.gravity = Gravity.CENTER;
//		    listNavLayout.addView(spinner, params);
//		    listNavLayout.setGravity(Gravity.RIGHT);		
			
			
			// Action Bar
			ActionBar actions = getActionBar();
//			actions.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			actions.setDisplayShowTitleEnabled(false);
//			actions.setListNavigationCallbacks(characterAdapter, callback);
//			actions.setCustomView(spinner);
//			RelativeLayout myView = (RelativeLayout) actions.getCustomView();
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
//			params.addRule(RelativeLayout.RIGHT_OF, titleTxtView.getId());
			spinner.setLayoutParams(params);
			//not: min API for this call is 17, atm we are developing for 14!!!
//			spinner.setId(View.generateViewId());
//			spinner.setGravity(Gravity.RIGHT);
//			myView.addView(spinner);
			
//			TableLayout.LayoutParams titleParams = (TableLayout.LayoutParams) titleTxtView.getLayoutParams();
//			titleParams.addRule(RelativeLayout.RIGHT_OF, titleTxtView.getId());
//			spinner.setLayoutParams(params);
			spinner.post(new Runnable() {
			    public void run() {
			        spinner.setOnItemSelectedListener((CharacterPlayActivity) getAc());
			    }
			});
			characterSelectSpinner = spinner;
//			spinner.setOnItemSelectedListener((CharacterPlayActivity) getAc());
		}
	}
	
//	public void checkEditable(){
//		if(theActiveActivity instanceof CharacterPlayActivity
//				&& ((CharacterPlayActivity) theActiveActivity).inEditMode()){
//        	editable = true;
//        }
//        else if(theActiveActivity instanceof CharacterPlayActivity
//        	&& ((CharacterPlayActivity) theActiveActivity).inSelectionMode()){
//        	editable = false;
//        }
//        else if(theActiveActivity instanceof CharacterEditActivity){
//        	editable = false;
//        }
//        else{
//    		Log.d("FolderFragment","case not known! set to editable == true");
//    		editable = true;
//        }
//	}

	@Override
	protected void onStart() {
		//
		// JACKSON START
		//
		// CountDownLatch. If we are editing a template the async task will wait
		// with inflation till
		// onCreate finishes
		if (countDownLatch != null) {
			countDownLatch.countDown();
		}
		//
		// JACKSON END
		//
		super.onStart();
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		if (!currentFragment.isATopFragment) {
			actionBar
					.setCustomView(R.layout.actionbar_template_generator_back_button);
			ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
			backButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					goAbove();
				}
			});
		} else {
			actionBar.setCustomView(R.layout.actionbar_template_generator);
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);
			getActionBar().setDisplayShowHomeEnabled(true);
		}
		// set up name and dialog for fragment
		View v = getActionBar().getCustomView();
		TextView titleTxtView = (TextView) v.findViewById(R.id.actionbar_title);
		titleTxtView.setText(currentFragment.elementName);
		// check if we are in character or template mode
		// only add click listener if we are editing a template
		if (theActiveActivity instanceof TemplateGeneratorActivity) {
			((View) titleTxtView).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					currentFragment.showDialog();
				}
			});
		}
		if(theActiveActivity instanceof CharacterPlayActivity
				&& characterSelectSpinner != null){
			RelativeLayout customActionBar = (RelativeLayout) getActionBar().getCustomView();
			if(customActionBar.findViewById(R.layout.character_spinner) == null){
				//might be attached to "old" actionbar -> release and place here
				Log.d("SlideoutNavigationActivity", "Character-Spinner added again");
				if(characterSelectSpinner.getParent() != null){
					((ViewGroup)characterSelectSpinner.getParent()).removeView(characterSelectSpinner);
				}
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
				customActionBar.addView(characterSelectSpinner, params);
//				customActionBar.addView(characterSelectSpinner);
			}
			//modify action_bar_title params so that its left of spinner
			RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(
			        RelativeLayout.LayoutParams.MATCH_PARENT,
			        RelativeLayout.LayoutParams.WRAP_CONTENT);
			Log.d("SlideoutNavigationActivity", "Layout set! id spinner: " + characterSelectSpinner.getId());
			relativeLayoutParams.addRule(RelativeLayout.LEFT_OF,
					characterSelectSpinner.getId());
			relativeLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
			titleTxtView.setLayoutParams(relativeLayoutParams);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void goAbove() {
		if (currentFragment.getParent() == null) {
			Toast.makeText(this, "es existiert kein Ordner darueber",
					Toast.LENGTH_LONG).show();
		} else {
			if (currentFragment != topFragment) {
				Toast.makeText(this, "hiding fragment!", Toast.LENGTH_LONG)
						.show();
				FragmentTransaction fa = getFragmentManager()
						.beginTransaction();
				fa.detach(currentFragment);
				currentFragment.getParent()
						.setBackStackElement(currentFragment);
				fa.attach(currentFragment.getParent());
				currentFragment = currentFragment.getParent();
				fa.addToBackStack(null);
				fa.commit();
			} else {
				Toast.makeText(this, "we are already at top level!",
						Toast.LENGTH_LONG).show();
			}
		}
		invalidateOptionsMenu();
	}

	public DrawerLayout getDrawerLayout() {
		return mDrawerLayout;
	}

	public ActionBarDrawerToggle getActionBarDrawerToggle() {
		return mDrawerToggle;
	}

	public FolderFragment getRootFragment() {
		return rootFragment;
	}

	public GeneralFragment getCurrentFragment() {
		return currentFragment;
	}

	public void setCurrentFragment(GeneralFragment frag) {
		currentFragment = frag;
	}

	public GeneralFragment getTopFragment() {
		return topFragment;
	}

	public void setTopFragment(GeneralFragment frag) {
		topFragment = frag;
	}

	public static SlideoutNavigationActivity getAc() {
		return theActiveActivity;
	}

	public SlideoutNavigationActivity() {
		super();
	}

	public void inflate(ContainerTable table) {
		// ((FolderFragment)rootFragment).setJacksonTable(myTemplate.characterSheet.getRootTable());
		// ((FolderFragment)rootFragment).inflateWithJacksonData(myTemplate.characterSheet.getRootTable(),
		// this);
		((FolderFragment) rootFragment).setJacksonTable(table);
		((FolderFragment) rootFragment).inflateWithJacksonData(table, theActiveActivity);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	 public void onSaveInstanceState(Bundle savedInstanceState) {
	   super.onSaveInstanceState(savedInstanceState);
	   savedInstanceState.putSerializable("mode", mode);
	   savedInstanceState.putBoolean("showInvisible", showInvisible);
	 }

	/**
	 * Class for loading a template asynchronously. Shows a ProgressDialog,
	 * waits for the activity to finish creation and then inflates it with the
	 * loaded data. The ProgessDialog is automatically closed when the whole
	 * loading process has finished. !You have to call "onSetup" and save the
	 * returned CountDownLatch!
	 */
	private class jacksonLoadTemplateAsync extends
			AsyncTaskWithProgressDialog<String, Void, Template> {
		// context used for async loading
		private Context appContext;
		private CountDownLatch countDownLatch;

		public CountDownLatch doSetup(Activity activity) {
			appContext = activity.getApplicationContext();
			countDownLatch = new CountDownLatch(1);
			return countDownLatch;
		}

		@Override
		protected void onPreExecute() {
			Log.d("jacksonLoadTemplateAsync", "loading template");
			super.onPreExecute(SlideoutNavigationActivity.this,
								getString(R.string.loading_template),
								getString(R.string.wait));
		}

		@Override
		protected Template doInBackground(String... params) {
			if (appContext == null || countDownLatch == null) {
				return null;
			}
			final String FILE_NAME = params[0];
			Template template = null;
			try {
				template = JacksonInterface.loadTemplate(appContext, FILE_NAME,
						false);
			} catch (Throwable e) {
				e.printStackTrace();
				return null;
			}
			return template;
		}

		@Override
		protected void onPostExecute(Template result) {
			// call super later to wait for inflation
			// no result
			if (result == null) {
				Toast.makeText(myActivity,
						getString(R.string.loading_template_failure),
						Toast.LENGTH_LONG).show();
			}
			else {
				myTemplate = result;
				try {
					countDownLatch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				myActivity.inflate(myTemplate.getCharacterSheet().getRootTable());
				myActivity.countDownLatch = null;	
			}
			//discard progress dialog
			super.onPostExecute(result);
		}
	}
}