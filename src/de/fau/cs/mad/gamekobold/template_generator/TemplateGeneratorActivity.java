package de.fau.cs.mad.gamekobold.template_generator;

import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.jackson.Template;
import de.fau.cs.mad.gamekobold.jackson.TemplateSaverTask;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class TemplateGeneratorActivity extends SlideoutNavigationActivity {

	@Override
	 protected void onCreate(Bundle savedInstanceState) {
		 setContentView(R.layout.activity_template_generator_welcome2);
		 super.onCreate(savedInstanceState);
	 }
	 
	@Override
	protected void onStart() {
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
		getMenuInflater().inflate(R.menu.template_generator, menu);
		

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
        int id = item.getItemId();
        if (id == R.id.action_edit_mode) {
        	//TODO: changes to apply in edit mode -> allow switching table elements
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
    
    private void saveTemplateAsync() {
    	TemplateSaverTask saverTask = new TemplateSaverTask(getApplicationContext(), true);
    	saverTask.execute(new Template [] { myTemplate });
    }

	
}
