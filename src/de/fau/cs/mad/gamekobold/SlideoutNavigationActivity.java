package de.fau.cs.mad.gamekobold;

import java.util.concurrent.CountDownLatch;

import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.jackson.Template;
import de.fau.cs.mad.gamekobold.template_generator.FolderFragment;
import de.fau.cs.mad.gamekobold.template_generator.GeneralFragment;
import de.fau.cs.mad.gamekobold.template_generator.TemplateGeneratorActivity;
import de.fau.cs.mad.gamekobold.template_generator.WelcomeFragment;
import de.fau.cs.mad.gamekobold.template_generator.WelcomeNewCharacterFragment;
import de.fau.cs.mad.gamekobold.template_generator.WelcomePlayCharacterFragment;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SlideoutNavigationActivity extends FragmentActivity {

	public static Template myTemplate = null;
	// for distinguishing between template creation/editing and character
	public static final String MODE_TEMPLATE = "MODE_TEMPLATE";
	public static final String MODE_CREATE_NEW_TEMPLATE = "MODE_CREATE_NEW_TEMPLATE";
	public static final String EDIT_TEMPLATE_FILE_NAME = "FILE_NAME";
	public static final String AUTO_SAVE_TEMPLATE_ON_EXIT = "AUTO_SAVE_TEMPLATE_ON_EXIT";
	public static final String LAST_EDITED_TEMPLATE_NAME = "LAST_EDITED_TEMPLATE_NAME";
	public static final String SHARED_PREFERENCES_FILE_NAME = "TemplateGeneratorPrefs";

	public static final String WELCOME_TYPE_TEMPLATE = "WELCOME_TEMPLATE";
	public static final String WELCOME_TYPE_NEW_CHARACTER = "WELCOME_NEW_CHARACTER";
	public static final String WELCOME_TYPE_PLAY_CHARACTER = "WELCOME_PLAY_CHARACTER";

	public static boolean skipNextOnPauseSave = false;
	public static boolean forceSaveOnNextOnPause = false;
	public static SlideoutNavigationActivity myActivity = null;
	public CountDownLatch countDownLatch;
	protected GeneralFragment currentFragment;
	protected GeneralFragment topFragment;
	protected FolderFragment rootFragment;
	public static SlideoutNavigationActivity theActiveActivity;
	protected DrawerLayout mDrawerLayout;
	protected ActionBarDrawerToggle mDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		theActiveActivity = this;
		/*
		 * JACKSON START
		 */
		myActivity = this;
		boolean creationMode = true;
		Log.d("Saved instance state", "" + savedInstanceState);
		if (savedInstanceState == null) {
			Intent intent = getIntent();
			// check if we are in template mode
			if (intent.getBooleanExtra(MODE_TEMPLATE, true)) {
				creationMode = intent.getBooleanExtra(MODE_CREATE_NEW_TEMPLATE,
						true);
				// is a new template created?
				if (creationMode) {
					Template template = (Template) intent
							.getParcelableExtra(Template.PARCELABLE_STRING);
					if (template != null) {
						Log.d("MainTemplateGenerator",
								"Got template meta data in intent!");
						// template.print();
						myTemplate = template;
					} else {
						// TODO show error?!
					}
				} else {
					Log.d("MainTemplateGenerator", "Edit mode!");
					// we are editing an old one, so load it
					// get file name
					String templateFileName = intent
							.getStringExtra(EDIT_TEMPLATE_FILE_NAME);
					// create new async task
					jacksonLoadTemplateAsync task = new jacksonLoadTemplateAsync();
					// do the setup
					countDownLatch = task.doSetup(this);
					// start
					task.execute(templateFileName);
				}
			}
		} else {
			Log.d("TemplateGeneratorActivity", "got savedInstance");
		}
		/*
		 * JACKSON END
		 */
		rootFragment = (FolderFragment) getFragmentManager().findFragmentByTag(
				"rootFragment");
		// create it because we need its data for slideoutmenu
		if (rootFragment == null) {
			rootFragment = new FolderFragment();
			rootFragment.isATopFragment = true;
			/*
			 * JACKSON START
			 */
			if (getIntent().getBooleanExtra(MODE_TEMPLATE, true)) {
				// if we are creating a new one, a template has been already
				// created.
				// So we set the jackson table
				// if we are editing a template, the async task will set the
				// table and start inflation
				if (creationMode) {
					((FolderFragment) rootFragment)
							.setJacksonTable(myTemplate.getCharacterSheet()
									.getRootTable());
				}
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
			if (getIntent().hasExtra(WELCOME_TYPE_TEMPLATE)) {
				topFragment = new WelcomeFragment();
			} else if (getIntent().hasExtra(WELCOME_TYPE_NEW_CHARACTER)) {
				topFragment = new WelcomeNewCharacterFragment();
			} else if (getIntent().hasExtra(WELCOME_TYPE_PLAY_CHARACTER)) {
				topFragment = new WelcomePlayCharacterFragment();
			}

			topFragment.isATopFragment = true;
			topFragment.elementName = "Welcome";
			currentFragment = topFragment;
			fragmentTransaction.add(R.id.frame_layout_container,
					currentFragment);
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
	}

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
		((FolderFragment) rootFragment).inflateWithJacksonData(table, this);
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

	/**
	 * Class for loading a template asynchronously. Shows a ProgressDialog,
	 * waits for the activity to finish creation and then inflates it with the
	 * loaded data. The ProgessDialog is automatically closed when the whole
	 * loading process has finished. !You have to call "onSetup" and save the
	 * returned CountDownLatch!
	 */
	private class jacksonLoadTemplateAsync extends
			AsyncTask<String, Void, Template> {
		private ProgressDialog pd;
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
			if (appContext == null || countDownLatch == null) {
				return;
			}
			pd = new ProgressDialog(myActivity);
			pd.setTitle(getString(R.string.loading_template));
			pd.setMessage(getString(R.string.wait));
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
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

		// TODO redo ?
		@Override
		protected void onPostExecute(Template result) {
			// wrong set up
			if (appContext == null || countDownLatch == null) {
				if (pd != null) {
					pd.dismiss();
				}
				return;
			}
			// no result
			if (result == null) {
				Toast.makeText(myActivity,
						getString(R.string.loading_template_failure),
						Toast.LENGTH_LONG).show();
				if (pd != null) {
					pd.dismiss();
				}
				return;
			}
			myTemplate = result;
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				if (pd != null) {
					pd.dismiss();
				}
				return;
			}
			// myActivity.inflate();
			myActivity.inflate(myTemplate.getCharacterSheet().getRootTable());
			if (pd != null) {
				pd.dismiss();
			}
			myActivity.countDownLatch = null;
		}
	}

}