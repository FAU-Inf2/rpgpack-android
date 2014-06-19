package de.fau.cs.mad.gamekobold.template_generator;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.Template;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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
		 if(savedInstanceState == null) {
			 Intent intent = getIntent();
			 creationMode = intent.getBooleanExtra(MODE_CREATE_NEW_TEMPLATE, true);
			 // is a new template created?
			 if(creationMode) {
				 Template template = (Template)intent.getParcelableExtra(Template.PARCELABLE_STRING);
				 if(template != null) {
					 Log.d("MainTemplateGenerator", "Got template in intent!");
					 template.print();
					 myTemplate = template;
				 }
			 } else {
				 Log.d("MainTemplateGenerator", "Edit mode!");
				 // we are editing an old one, so load it
				 //myTemplate = Template.loadFromJSONFile(getApplication(), fileName);
				 String templateFileName = intent.getStringExtra(EDIT_TEMPLATE_FILE_NAME);
				 try {
					 myTemplate = Template.loadFromJSONFile(getApplication(), templateFileName);
					 Log.d("MainTemplateGenerator", "Loaded Template");
				 } catch (JsonParseException | JsonMappingException e) {
					 e.printStackTrace();
				 }
				 catch(IOException e) {
					 e.printStackTrace();
				 }
				 // check if template loaded
				 if(myTemplate == null) {
					 // error while loading, so create empty template
					 // TODO show warning dialog
					 myTemplate = new Template(new CharacterSheet(new ContainerTable()));
					 Toast.makeText(getApplication(), "Failed to load Template:"+templateFileName, Toast.LENGTH_SHORT).show();
				 }
			 }
		 }
		 // TODO save in onPause
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
			 ((FolderFragment)currentFragment).setJacksonTable(myTemplate.characterSheet.rootTable);
			 // are we editing a template?
			 if(!creationMode) {
				// for editing : recreate fragment structure and all adapter data
				// inflate fragments recursive
				Log.d("MainTemplateGenerator", "EDIT MODE!");
				((FolderFragment)currentFragment).inflateWithJacksonData(myTemplate.characterSheet.rootTable, this);
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
//    		Log.d("menu-Creation", "MENU DEFAULT");
    	}
//        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
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
//    		fa.add(R.id.main_view_empty, fragment.fragment_parent);
    		fa.attach(currentFragment.fragment_parent);
    		currentFragment = currentFragment.fragment_parent;
//    		fa.addToBackStack(name);
    		fa.commit();
    	}
    	invalidateOptionsMenu();
    }
    
    @Override
    public void onBackPressed(){
    	FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();  
        }
    }
    
    /*
     * JACKSON START
     */
    // remove this later if we know our filename
    /*public static void saveTemplate() {
    	saveTemplate("");
    }
    public static void saveTemplate(String filename) {
    	//filename currently ignored
		try {
			if( (myActivity != null) && (myTemplate != null) ) {
				myTemplate.saveToJSON(myActivity, "testTemplate.json");
				Log.d("MAIN_TEMPALTE_GENERATOR", "saved Template");
			}
		} catch (JsonGenerationException | JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }*/
    
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
					//Log.d("MAIN_TEMPALTE_GENERATOR", "saved Template");
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
    /*
     * JACKSON END
     */
}
