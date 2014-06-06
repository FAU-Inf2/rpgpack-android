package de.fau.cs.mad.gamekobold.template_generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.jackson.Template;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainTemplateGenerator extends Activity{
	 /*
	  * JACKSON START
	  */
	 public static Template myTemplate = null;
	 public static final String MODE_CREATE_NEW_TEMPLATE = "MODE_CREATE_NEW_TEMPLATE";
	 public static final String EDIT_TEMPLATE_FILE_NAME = "FILE_NAME";
	 /*
	  * JACKSON END
	  */
	
	 protected DataAdapter dataAdapter;
	 protected ArrayList<DataHolder> allData;
	 //the only fragment used till now
	 GeneralFragment currentFragment;
	 FolderFragment topFragment;
	 OnClickListener onClickAction;


	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_empty);
		 
		 /*
		  * JACKSON START
		  */
		 Intent intent = getIntent();
		 boolean creationMode = intent.getBooleanExtra(MODE_CREATE_NEW_TEMPLATE, true);
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
		 }
		 /*
		  * JACKSON END
		  */
		 
		 FolderFragment mainFragment = (FolderFragment) getFragmentManager().findFragmentByTag("topFragment");
		 //method: use fragment to store everything
		 if(mainFragment == null){
			 FragmentManager fragmentManager = getFragmentManager();
			 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			 currentFragment = new FolderFragment();
			 topFragment = (FolderFragment) currentFragment;
			 fragmentTransaction.add(R.id.main_view_empty, currentFragment, "topFragment");
			 fragmentTransaction.commit();
			 /*
			  * JACKSON START
			  */
			 ((FolderFragment)currentFragment).setJacksonTable(myTemplate.characterSheet.rootTable);
			 // TODO implement editing here?
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
		 else{
             Log.d("aaa", "ELSE!");
             FragmentManager fragmentManager = getFragmentManager();
			 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			 fragmentTransaction.attach(mainFragment);
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
        super.onSaveInstanceState(outState);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
        	Intent startNewActivityOpen = new Intent(MainTemplateGenerator.this, MainTemplateGenerator.class);
//        	startNewActivityOpen.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        	startActivityForResult(startNewActivityOpen, 0);
        }
        else if (id == R.id.action_showlists) {
        	Intent startNewActivityOpen = new Intent(MainTemplateGenerator.this, ShowLists.class);
        	startActivityForResult(startNewActivityOpen, 0);
        }
        else if (id == R.id.action_add_folder) {
//        	Toast.makeText(this, "selected: " + getResources().getIdentifier("choices", "values", getPackageName()) ,Toast.LENGTH_LONG).show();
        	String[] items = getResources().getStringArray(R.array.choices);
        	int index = Arrays.asList(items).indexOf("Ordner");
        	currentFragment.addItemList(index);
        }
        else if (id == R.id.action_go_above) {
        	if(currentFragment.fragment_parent == null){
                Log.d("aaa", "es existiert kein Ordner darueber");
            	Toast.makeText(this, "es existiert kein Ordner darueber", Toast.LENGTH_LONG).show();
        	}
        	else{
                Log.d("aaa", "hiding and showing above");
        		Toast.makeText(this, "hiding fragment!", Toast.LENGTH_LONG).show();
        		FragmentTransaction fa = getFragmentManager().beginTransaction();
        		fa.hide(currentFragment);
//        		fa.add(R.id.main_view_empty, fragment.fragment_parent);
        		fa.show(currentFragment.fragment_parent);
        		currentFragment = currentFragment.fragment_parent;
        		fa.commit();
        	}
//        	Toast.makeText(this, "selected: " + getResources().getIdentifier("choices", "values", getPackageName()) ,Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
