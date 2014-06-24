package de.fau.cs.mad.gamekobold.template_generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.jackson.Template;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
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
	 public static Activity myActivity = null;
	 /*
	  * JACKSON END
	  */
	
//	 private Menu globalMenu;
	 protected FolderElementAdapter dataAdapter;
	 protected ArrayList<FolderElementData> allData;
	 //the only fragment used till now
	 GeneralFragment currentFragment;
	 FolderFragment topFragment;
	 protected static Activity theActiveActivity;
//	 AlertDialog dialogTableView;
//	 View dialogViewTableView;


	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 theActiveActivity = this;
		 setContentView(R.layout.activity_empty);
		 
		 
		 //old approach with number-list
//		 String[] numbers = new String[500];
//		 for(Integer i=0; i<500; i++){
//			 Integer offset = (Integer) i+1;
//			 numbers[i] = offset.toString();
//		 }
//		 alertDialogBuilder.setItems(numbers, new  DialogInterface.OnClickListener() {
//             public void onClick(DialogInterface dialog, int pos) {
//                 //selection processing code
//
//         }});
		 
		 // Inflate and set the layout for the dialog
		 // Pass null as the parent view because its going in the dialog layout
		 
		 
		 
		 /*
		  * JACKSON START
		  */
		 myActivity = this;
		 boolean creationMode = true;
		 CountDownLatch countDownLatch = null;
		 Log.d("Saved instance state", ""+savedInstanceState);
		 if(savedInstanceState == null) {
			 Intent intent = getIntent();
			 creationMode = intent.getBooleanExtra(MODE_CREATE_NEW_TEMPLATE, true);
			 // is a new template created?
			 if(creationMode) {
				 Template template = (Template)intent.getParcelableExtra(Template.PARCELABLE_STRING);
				 if(template != null) {
					 Log.d("MainTemplateGenerator", "Got template meta data in intent!");
					 template.print();
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
				 // set field
				 task.generatorActivity = this;
				 // create the CountDownLatch and set its counter to 1
				 countDownLatch = new CountDownLatch(1);
				 // set countDownLatch
				 task.countDownLatch = countDownLatch;
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
		 
//		 FolderFragment mainFragment = (FolderFragment) getFragmentManager().findFragmentByTag("mainFragment");
		 topFragment = (FolderFragment) getFragmentManager().findFragmentByTag("topFragment");
		 //method: use fragment to store everything
		 if(topFragment == null){
			 FragmentManager fragmentManager = getFragmentManager();
			 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			 currentFragment = new FolderFragment();
			 topFragment = (FolderFragment) currentFragment;
			 fragmentTransaction.add(R.id.main_view_empty, currentFragment, "topFragment");
			 fragmentTransaction.commit();
			 getFragmentManager().executePendingTransactions();
			 /*
			  * JACKSON START
			  */
			 // if we are creating a new one, a template has been already created.
			 // So we set the jackson table
			 // if we are editing a template, the async task will set the table and start inflation
			 if(creationMode) {
				 ((FolderFragment)currentFragment).setJacksonTable(myTemplate.characterSheet.rootTable);
			 }
			 /*
			  * JACKSON END
			  */
		 }
		 if(currentFragment == null){
				Log.d("onCreate", "currentFragment == null");
				if(savedInstanceState != null){
					currentFragment = (GeneralFragment) getFragmentManager().getFragment(savedInstanceState, "currentFragment");
				}
				else{
					currentFragment = topFragment;
				}
		 }
		 else{
			 Log.d("NICE", "mainFragment FOUND!!!");
//             FragmentManager fragmentManager = getFragmentManager();
//			 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//			 fragmentTransaction.attach(mainFragment);
//			 fragmentTransaction.commit();
		 }
		 
		//think it should be done in fragment: save allData (+restore)
//		 if(savedInstanceState != null) {
//        	allData = savedInstanceState.getParcelableArrayList("key2");
//        }
		 /*
		  * JACKSON START
		  */
		 // CountDownLatch. If we are editing a template the async task will wait with inflation till
		 // onCreate finishes
		 if(countDownLatch != null) {
			countDownLatch.countDown();
		 }
		 /*
		  * JACKSON END
		  */
	 }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	//think it should be done in fragment: save allData (+restore)
//    	outState.putParcelableArrayList("key123", allData);
    	getFragmentManager().putFragment(outState, "currentFragment", currentFragment);
//    	getSupportFragmentManager().putFragment(outState, "mContent", currentFragment);
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
    	ActionBar actionBar = getActionBar(); 
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getMenuInflater().inflate(R.menu.template_generator_table_layout, menu);
		if(currentFragment != topFragment){
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
		}
    	if(currentFragment instanceof TableFragment){
//    		setTitle(((TableFragment) currentFragment).tableName);
    		View v = getActionBar().getCustomView();
    	    TextView titleTxtView = (TextView) v.findViewById(R.id.actionbar_title);
    	    ((View) titleTxtView).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					currentFragment.showDialog();
				}
    	    });
    	    titleTxtView.setText(currentFragment.elementName);
//    	    titleTxtView.setTextColor(getResources().getColor(R.color.blue));
    		Log.d("table name:", "name == " + currentFragment.elementName);
    	}
    	else if(currentFragment instanceof FolderFragment){
    		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    		if(currentFragment == topFragment){
    			//now we are on top level and should allow to show the slideout menu
    			//TODO (Anna): add the SlideoutMenu as an item to the ActionBar
    			//(maybe use an own "R.menu.resource_file"
    			//which contains the button that shall open the slideout-menu)
    			//also todo: tell the button what to do -> show slideout-menu
    			//adapt the following lines (commented out)
//    			actionBar.setCustomView(R.layout.actionbar_template_generator_back_button);
//    			ImageButton slideOutButton = (ImageButton) findViewById(R.id.button_back);
//    			backButton.setOnClickListener(new View.OnClickListener() {
//    				@Override
//    				public void onClick(View v) {
//    					showSlideoutMenu();
//    				}
//    			});
    			
    		}
    		View v = getActionBar().getCustomView();
    	    TextView titleTxtView = (TextView) v.findViewById(R.id.actionbar_title);
    	    ((View) titleTxtView).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					currentFragment.showDialog();
				}
    	    });
    	    titleTxtView.setText(currentFragment.elementName);
//    		Log.d("menu-Creation", "MENU 2");
    	}
    	else{
    		Log.d("menu-Creation", "App doesn't know what actionbar should be used for this Fragment!");
    		getMenuInflater().inflate(R.menu.template_generator_standard, menu);
//    		Log.d("menu-Creation", "MENU DEFAULT");.s
    	}
//        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
    	
    	MenuItem myItem = menu.findItem(R.id.action_auto_save_on_exit);
    	if(myItem != null) {
    		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
    		myItem.setChecked(prefs.getBoolean(AUTO_SAVE_TEMPLATE_ON_EXIT, false));
    	}
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//    	globalMenu = menu;
//    	adaptActionBar();
    	//TODO benni: hat bei mir den stackoverflow gefixt. keine ahnung warum
    	//invalidateOptionsMenu();
    	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_main) {
        	Intent startNewActivityOpen = new Intent(TemplateGeneratorActivity.this, TemplateGeneratorActivity.class);
//        	startNewActivityOpen.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        	startActivityForResult(startNewActivityOpen, 0);
        }
        else if (id == R.id.action_add_folder) {
        	currentFragment.addItemList();
        }
        else if (id == R.id.action_edit_mode) {
        	
        }
        else if (id == R.id.action_go_above) {
        	goAbove();
//        	Toast.makeText(this, "selected: " + getResources().getIdentifier("choices", "values", getPackageName()) ,Toast.LENGTH_LONG).show();
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
            Log.d("aaa", "es existiert kein Ordner darueber");
        	Toast.makeText(this, "es existiert kein Ordner darueber", Toast.LENGTH_LONG).show();
    	}
    	else{
            Log.d("aaa", "hiding and showing above");
    		Toast.makeText(this, "hiding fragment!", Toast.LENGTH_LONG).show();
    		FragmentTransaction fa = getFragmentManager().beginTransaction();
    		fa.detach(currentFragment);
    		currentFragment.fragment_parent.backStackElement = currentFragment;
//    		fa.add(R.id.main_view_empty, fragment.fragment_parent);
    		fa.attach(currentFragment.fragment_parent);
    		currentFragment = currentFragment.fragment_parent;
//    		fa.addToBackStack(name);
    		fa.addToBackStack(null);
    		fa.commit();
    	}
    	invalidateOptionsMenu();
    }
    
    @Override
    public void onBackPressed(){
    	FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            currentFragment = currentFragment.backStackElement;
        } else {
        	DialogFragment dialog = WarningLeaveDialog.newInstance();
        	dialog.show(fm, "");
//            super.onBackPressed();  
        }
        invalidateOptionsMenu();
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
    	((FolderFragment)currentFragment).setJacksonTable(myTemplate.characterSheet.rootTable);
   	 	((FolderFragment)currentFragment).inflateWithJacksonData(myTemplate.characterSheet.rootTable, this);
    }
    
    public static void saveTemplateAsync() {
    	saveTemplateAsync("testTemplate.json");
    }
    
    public static void saveTemplateAsync(String filename) {
    	JacksonSaveTemplateTask task = new JacksonSaveTemplateTask();
    	task.execute(new String[] {filename});
    }
    
    private static class JacksonSaveTemplateTask extends AsyncTask<String, Void, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			if(params.length != 1) {
				return Boolean.FALSE;
			}
			String filename = params[0];
			try {
				if( (myActivity != null) && (myTemplate != null) ) {
					myTemplate.saveToJSON(myActivity, filename);
				}
			} catch (JsonGenerationException | JsonMappingException e) {
				e.printStackTrace();
				return Boolean.FALSE;
			} catch (IOException e) {
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
    
    /**
     * Class for loading a template asynchronously. Shows a ProgressDialog, waits for the activity to finish creation
     * and then inflates it with the loaded data. The ProgessDialog is automatically closed when the whole loading
     * process has finished.
     * ! You have to manually set the fields "generator" and "countDownLatch"!
     */
    private class jacksonLoadTemplateAsync extends AsyncTask<String, Void, Template> {
    	private ProgressDialog pd;
    	
    	public TemplateGeneratorActivity generatorActivity;
    	public CountDownLatch countDownLatch;
    	
    	@Override
    	protected void onPreExecute() {
    		if(generatorActivity == null || countDownLatch == null) {
    			return;
    		}
    		pd = new ProgressDialog(myActivity);
    		pd.setTitle("Loading Template...");
    		pd.setMessage("Please wait...");
    		pd.setCancelable(false);
    		pd.setIndeterminate(true);
    		pd.show();
    	}
    	
		@Override
		protected Template doInBackground(String... params) {
    		if(generatorActivity == null || countDownLatch == null) {
    			return null;
    		}
			final String FILE_NAME = params[0];
			Template template = null;
			try {
				template = Template.loadFromJSONFile(generatorActivity, FILE_NAME);
				// TODO this is just for test purpose
				// remove this later!
				for(int i = 0; i < 12; i++) {
					Thread.sleep(200);
				}
				//
			} catch (JsonParseException | JsonMappingException e) {
				return null;
			}
			catch (IOException e) {
				return null;
			} catch (InterruptedException e) {
				return null;
			}
			return template;
		}
		
		@Override
		protected void onPostExecute(Template result) {
			// wrong set up
			if(generatorActivity == null || countDownLatch == null) {
				if(pd != null){
					pd.dismiss();
				}
				return;
			}
			// no result
			if(result == null) {
				Toast.makeText(generatorActivity, "Failed to load template", Toast.LENGTH_LONG).show();
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
			generatorActivity.inflate();
			if(pd != null){
				pd.dismiss();
			}
		}
    }
    /*
     * JACKSON END
     */
}
