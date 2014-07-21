package de.fau.cs.mad.gamekobold.template_generator;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.jackson.Template;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TemplateGeneratorActivity extends FragmentActivity {
	 /*
	  * JACKSON START
	  */
	 public static Template myTemplate = null;
	 public static final String MODE_CREATE_NEW_TEMPLATE = "MODE_CREATE_NEW_TEMPLATE";
	 public static final String EDIT_TEMPLATE_FILE_NAME = "FILE_NAME";
	 public static final String AUTO_SAVE_TEMPLATE_ON_EXIT = "AUTO_SAVE_TEMPLATE_ON_EXIT";
	 public static final String LAST_EDITED_TEMPLATE_NAME = "LAST_EDITED_TEMPLATE_NAME";
	 public static final String SHARED_PREFERENCES_FILE_NAME = "TemplateGeneratorPrefs";
	 public static boolean skipNextOnPauseSave = false;
	 public static boolean forceSaveOnNextOnPause = false;
	 //needed for saving
	 public static TemplateGeneratorActivity myActivity = null;
	 private CountDownLatch countDownLatch;
	 /*
	  * JACKSON END
	  */
	
	 protected FolderElementAdapter dataAdapter;
	 protected ArrayList<FolderElementData> allData;
	 protected GeneralFragment currentFragment;
	 //the one where you cant go up further
	 GeneralFragment topFragment;
	 //the one above which is only visible in the slideout-menu
	 FolderFragment rootFragment;
	 protected static Activity theActiveActivity;
	 
	 //variables for slideout-menu
	 protected DrawerLayout mDrawerLayout;
	 private ActionBarDrawerToggle mDrawerToggle;


	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 theActiveActivity = this;
		 setContentView(R.layout.activity_template_generator_welcome2);
		 /*
		  * JACKSON START
		  */
		 myActivity = this;
		 boolean creationMode = true;
		 Log.d("Saved instance state", ""+savedInstanceState);
		 if(savedInstanceState == null) {
			 Intent intent = getIntent();
			 creationMode = intent.getBooleanExtra(MODE_CREATE_NEW_TEMPLATE, true);
			 // is a new template created?
			 if(creationMode) {
				 Template template = (Template)intent.getParcelableExtra(Template.PARCELABLE_STRING);
				 if(template != null) {
					 Log.d("MainTemplateGenerator", "Got template meta data in intent!");
					// template.print();
					 myTemplate = template;
				 }
				 else {
					 //TODO show error?!
				 }
			 } else {
				 Log.d("MainTemplateGenerator", "Edit mode!");
				 // we are editing an old one, so load it
				 // get file name
				 String templateFileName = intent.getStringExtra(EDIT_TEMPLATE_FILE_NAME);
				 // create new async task
				 jacksonLoadTemplateAsync task = new jacksonLoadTemplateAsync();
				 // do the setup
				 countDownLatch = task.doSetup(this);
				 // start
				 task.execute(templateFileName);
			 }
		 }
		 else {
			 Log.d("TemplateGeneratorActivity", "got savedInstance");
		 }
		 /*
		  * JACKSON END
		  */
		 rootFragment = (FolderFragment) getFragmentManager().findFragmentByTag("rootFragment");
		 //create it because we need its data for slideoutmenu
		 if(rootFragment == null){
			 rootFragment = new FolderFragment();
			 rootFragment.isATopFragment = true;
			 /*
			  * JACKSON START
			  */
			 // if we are creating a new one, a template has been already created.
			 // So we set the jackson table
			 // if we are editing a template, the async task will set the table and start inflation
			 if(creationMode) {
				 ((FolderFragment)rootFragment).setJacksonTable(myTemplate.characterSheet.getRootTable());
			 }
			 /*
			  * JACKSON END
			  */
			 FragmentTransaction transaction = getFragmentManager().beginTransaction();
			 transaction.add(R.id.navigation_drawer, rootFragment, "rootFragment");
			 transaction.commit(); 
		 }
		 //method: use fragment to store everything
//		 if(topFragment == null){
//			 FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//			 topFragment = new WelcomeFragment();
//			 topFragment.isATopFragment = true;
//			 topFragment.elementName = "Welcome";
//			 currentFragment = topFragment;
//			 fragmentTransaction.add(R.id.frame_layout_container, currentFragment);
//			 fragmentTransaction.commit();
//			 getFragmentManager().executePendingTransactions();
//		 }
		 if(savedInstanceState == null){
			 FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			 topFragment = new WelcomeFragment();
			 topFragment.isATopFragment = true;
			 topFragment.elementName = "Welcome";
			 currentFragment = topFragment;
			 fragmentTransaction.add(R.id.frame_layout_container, currentFragment);
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
				 super.onDrawerOpened(drawerView);
				 invalidateOptionsMenu();
			 }
		 };
		 mDrawerLayout.setDrawerListener(mDrawerToggle);
		 // mistakenly called?
		 //super.onResume();
	 }
	 
	@Override
	protected void onStart() {
		 //
		 // JACKSON START
		 //
		 // CountDownLatch. If we are editing a template the async task will wait with inflation till
		 // onCreate finishes
		 if(countDownLatch != null) {
			countDownLatch.countDown();
		 }
		 //
		 // JACKSON END
		 //
		 super.onStart();
	}
	 
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//    	getFragmentManager().putFragment(outState, "currentFragment", currentFragment);
        outState.putBoolean("activityExistedBefore", true);
    	super.onSaveInstanceState(outState);
    }
    
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) 
    {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("onRestoreInstanceState", "onRestoreInstanceState!!!");
    }
    
    /**
     * Menu gets its layout here -> action bar on top gets adapted
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	menu.clear();
    	//set right menu and navigation items
    	ActionBar actionBar = getActionBar(); 
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getMenuInflater().inflate(R.menu.template_generator, menu);
		if(!currentFragment.isATopFragment){
			actionBar.setCustomView(R.layout.actionbar_template_generator_back_button);
			ImageButton backButton = (ImageButton) findViewById(R.id.button_back);
			backButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					goAbove();
				}
			});
		}
		else{
			actionBar.setCustomView(R.layout.actionbar_template_generator);
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);
			getActionBar().setDisplayShowHomeEnabled(true);
		}
		//set up name and dialog for fragment
		View v = getActionBar().getCustomView();
	    TextView titleTxtView = (TextView) v.findViewById(R.id.actionbar_title);
	    titleTxtView.setText(currentFragment.elementName);
	    ((View) titleTxtView).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				currentFragment.showDialog();
			}
	    });

	    MenuItem myItem = menu.findItem(R.id.action_auto_save_on_exit);
    	if(myItem != null) {
    		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    		myItem.setChecked(prefs.getBoolean(AUTO_SAVE_TEMPLATE_ON_EXIT, false));
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
    	if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
        int id = item.getItemId();
        if (id == R.id.action_edit_mode) {
        	//TODO: changes to apply in edit mode
        }
        else if (id == R.id.action_go_above) {
        	goAbove();
        }
        /*
         * JACKSON START
         */
        else if(id == R.id.action_save_template) {
        	saveTemplateAsync();
        }
        else if(id == R.id.action_auto_save_on_exit) {
    		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    		SharedPreferences.Editor editor = prefs.edit();
        	if(item.isChecked()) {
        		item.setChecked(false);
        		editor.putBoolean(AUTO_SAVE_TEMPLATE_ON_EXIT, false);
        	}
        	else {
        		item.setChecked(true);
        		editor.putBoolean(AUTO_SAVE_TEMPLATE_ON_EXIT, true);
        	}
    		editor.commit();
        	Log.d("AUTO SAVE","state:"+item.isChecked());
        }
        /*
         * JACKSON END
         */
        return super.onOptionsItemSelected(item);
    }
    
    protected void goAbove(){
    	if(currentFragment.fragment_parent == null){
        	Toast.makeText(this, "es existiert kein Ordner darueber", Toast.LENGTH_LONG).show();
    	}
    	else{
    		if(currentFragment != topFragment){
    			Toast.makeText(this, "hiding fragment!", Toast.LENGTH_LONG).show();
    			FragmentTransaction fa = getFragmentManager().beginTransaction();
    			fa.detach(currentFragment);
    			currentFragment.fragment_parent.backStackElement = currentFragment;
    			fa.attach(currentFragment.fragment_parent);
    			currentFragment = currentFragment.fragment_parent;
    			fa.addToBackStack(null);
    			fa.commit();
    		}
    		else{
    			Toast.makeText(this, "we are already at top level!", Toast.LENGTH_LONG).show();
    		}
    	}
    	invalidateOptionsMenu();
    }
    
    @Override
    public void onBackPressed(){
    	FragmentManager fm = getFragmentManager();
    	//handling before -> stepping back to last viewed page
//        if (fm.getBackStackEntryCount() > 0) {
//            fm.popBackStack();
//            currentFragment = currentFragment.backStackElement;
//            if(currentFragment.isATopFragment){
//            	topFragment = currentFragment;
//            }
//        } else {
//        	DialogFragment dialog = WarningLeaveDialog.newInstance();
//        	if(currentFragment instanceof FolderFragment){
//        		Log.d("MainTemplateGenerator", "elemente: " + ((FolderFragment) currentFragment).allData.size());
//        	}
//        	dialog.show(fm, "");
//        }
//        invalidateOptionsMenu();
    	//handling now: just leave activity
    	DialogFragment dialog = WarningLeaveDialog.newInstance();
    	if(currentFragment instanceof FolderFragment){
    		Log.d("MainTemplateGenerator", "elemente: " + ((FolderFragment) currentFragment).allData.size());
    	}
    	dialog.show(fm, "");
    }
    
    protected void superBackPressed(){
    	super.onBackPressed();
    }
    
    @Override
    protected void onPause() {
    	/*
    	 * JACKSON START
    	 */
    	if(!skipNextOnPauseSave || forceSaveOnNextOnPause) {
    		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    		if(prefs.getBoolean(AUTO_SAVE_TEMPLATE_ON_EXIT, false) || forceSaveOnNextOnPause) {
    			forceSaveOnNextOnPause = false;
    			saveTemplateAsync();
    		}
    	}
    	else {
    		skipNextOnPauseSave = false;	
    	}
    	/*
    	 * JACKSON END
    	 */
    	super.onPause();
    }
    
    /*
     * JACKSON START
     */
    public void inflate() {
    	((FolderFragment)rootFragment).setJacksonTable(myTemplate.characterSheet.getRootTable());
   	 	((FolderFragment)rootFragment).inflateWithJacksonData(myTemplate.characterSheet.getRootTable(), this);
    }
    
    public static void saveTemplateAsync() {
    	JacksonSaveTemplateTask task = new JacksonSaveTemplateTask();
    	task.execute();
    }
    
    /*public static void saveTemplateAsync(String filename) {
    	JacksonSaveTemplateTask task = new JacksonSaveTemplateTask();
//    	task.execute(new String[] {filename});
    	task.execute();
    }*/
    
    private static class JacksonSaveTemplateTask extends AsyncTask</*String*/ Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(/*String... params*/Void... params) {
			/*if(params.length != 1) {
				return Boolean.FALSE;
			}
			String filename = params[0];*/
			try {
				if( (myActivity != null) && (myTemplate != null) ) {
					myTemplate.saveToJSON(myActivity);
				}
			} catch (Throwable e) {
				e.printStackTrace();
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}
    	
		@Override
		protected void onPostExecute(Boolean result) {
			if(!result) {
				Toast.makeText(myActivity, "Failed to save template!", Toast.LENGTH_LONG).show();
			}
			else {
				Log.d("MainTemplateGenerator", "saved template async");
			}
		}
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
     * Class for loading a template asynchronously. Shows a ProgressDialog, waits for the activity to finish creation
     * and then inflates it with the loaded data. The ProgessDialog is automatically closed when the whole loading
     * process has finished.
     * !You have to call "onSetup" and save the returned CountDownLatch!
     */
    private class jacksonLoadTemplateAsync extends AsyncTask<String, Void, Template> {
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
    		if(appContext == null || countDownLatch == null) {
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
    		if(appContext == null || countDownLatch == null) {
    			return null;
    		}
			final String FILE_NAME = params[0];
			Template template = null;
			try {
				template = Template.loadFromJSONFile(appContext, FILE_NAME);
			} catch (Throwable e) {
				//e.printStackTrace();
				return null;
			}
			return template;
		}
		
		@Override
		protected void onPostExecute(Template result) {
			// wrong set up
			if(appContext == null || countDownLatch == null) {
				if(pd != null){
					pd.dismiss();
				}
				return;
			}
			// no result
			if(result == null) {
				Toast.makeText(myActivity, getString(R.string.loading_template_failure), Toast.LENGTH_LONG).show();
				if(pd != null){
					pd.dismiss();
				}
				return;
			}
			myTemplate = result;
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				if(pd != null){
					pd.dismiss();
				}
				return;
			}
			myActivity.inflate();
			if(pd != null){
				pd.dismiss();
			}
			myActivity.countDownLatch = null;
		}
    }
    /*
     * JACKSON END
     */
}
