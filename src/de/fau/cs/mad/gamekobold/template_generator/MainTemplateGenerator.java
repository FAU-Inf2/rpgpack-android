package de.fau.cs.mad.gamekobold.template_generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.Template;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class MainTemplateGenerator extends FragmentActivity{
	 /*
	  * JACKSON START
	  */
	 public static Template myTemplate = null;
	 public static final String MODE_CREATE_NEW_TEMPLATE = "MODE_CREATE_NEW_TEMPLATE";
	 public static final String EDIT_TEMPLATE_FILE_NAME = "FILE_NAME";
	 public static volatile AtomicBoolean jacksonInflatingInProcess = null;
	 public static volatile boolean inflatingInProcess = false;
	 /*
	  * JACKSON END
	  */
	
//	 private Menu globalMenu;
	 protected DataAdapter dataAdapter;
	 protected ArrayList<DataHolder> allData;
	 //the only fragment used till now
	 GeneralFragment currentFragment;
	 FolderFragment topFragment;
	 protected static Activity theActiveActivity;
	 AlertDialog dialogTableView;
	 View dialogViewTableView;


	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_empty);
		 //TODO: implement AlertDialog tableViewDialog
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		 LayoutInflater inflater = getLayoutInflater();
		 
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
		 dialogViewTableView = inflater.inflate(R.layout.alertdialog_template_generator_tableview, null);
		 alertDialogBuilder.setView(dialogViewTableView);
		 NumberPicker np = ((NumberPicker) dialogViewTableView.findViewById(R.id.numberPicker1));
		 np.setMaxValue(99);
		 np.setMinValue(0);
//		 np.setValue(((TableFragment) currentFragment).amountColumns);
		 // set dialog message
		 alertDialogBuilder
		 .setCancelable(false)
		 .setPositiveButton("Tabelle speichern",new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog,int id) {
				 //TODO: adapt table
			 }
		 })
		 .setNegativeButton("Zurück",new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog,int id) {
				 dialog.cancel();
			 }
		 });
		 // create alert dialog
		 dialogTableView = alertDialogBuilder.create();
		 
		 
		 /*
		  * JACKSON START
		  */
		 if(jacksonInflatingInProcess == null) {
			 jacksonInflatingInProcess =  new AtomicBoolean(false);
		 }
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
			// check if template loaded
			if(myTemplate == null) {
				// error while loading, so create empty template
				// TODO show warning dialog
				myTemplate = new Template(new CharacterSheet(new ContainerTable()));
				Toast.makeText(getApplication(), "Failed to load Template:"+templateFileName, Toast.LENGTH_SHORT).show();
			}
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
			 ((FolderFragment)currentFragment).setJacksonTable(myTemplate.characterSheet.rootTable);
			 // are we editing a template?
			 if(!creationMode) {
				// for editing : recreate fragment structure and all adapter data
				// inflate fragments recursive
				Log.d("MainTemplateGenerator", "EDIT MODE!");
				// set flag for dirty fix
				jacksonInflatingInProcess.set(true);
				inflatingInProcess = true;
				Log.d("maintemplategenerator", ""+jacksonInflatingInProcess);
				((FolderFragment)currentFragment).inflateWithJacksonData(myTemplate.characterSheet.rootTable, this);
				// unset flag for dirty fix
				jacksonInflatingInProcess.set(false);
				inflatingInProcess = false;
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
		 theActiveActivity = this;
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
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	menu.clear();
    	if(currentFragment instanceof TableFragment){
    		getMenuInflater().inflate(R.menu.main, menu);

    		ActionBar actionBar = getActionBar(); 
    		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
    		actionBar.setCustomView(R.layout.actionbar_template_generator_tableview);
//    		setTitle(((TableFragment) currentFragment).tableName);
    		View v = getActionBar().getCustomView();
    	    TextView titleTxtView = (TextView) v.findViewById(R.id.actionbar_title);
    	    ((View) titleTxtView).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dialogTableView.setTitle(((TableFragment) currentFragment).tableName);
					NumberPicker np = ((NumberPicker) dialogViewTableView.findViewById(R.id.numberPicker1));
					np.setValue(((TableFragment) currentFragment).amountColumns);
					dialogTableView.show();
				}
    	    });
    	    titleTxtView.setText(((TableFragment) currentFragment).tableName);
    		Log.d("table name:", "name == " + ((TableFragment) currentFragment).tableName);
    	}
    	else if(currentFragment instanceof FolderFragment){
    		getMenuInflater().inflate(R.menu.template_generator_table_layout, menu);
//    		Log.d("menu-Creation", "MENU 2");
    	}
    	else{
    		Log.d("menu-Creation", "App doesn't know what actionbar should be used for this Fragment!");
    		getMenuInflater().inflate(R.menu.main, menu);
//    		Log.d("menu-Creation", "MENU DEFAULT");
    	}
//        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//    	globalMenu = menu;
//    	adaptActionBar();
    	invalidateOptionsMenu();
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
        	invalidateOptionsMenu();
//        	Toast.makeText(this, "selected: " + getResources().getIdentifier("choices", "values", getPackageName()) ,Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
