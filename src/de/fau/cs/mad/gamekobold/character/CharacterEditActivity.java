package de.fau.cs.mad.gamekobold.character;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.template_generator.FolderElementData;
import de.fau.cs.mad.gamekobold.template_generator.FolderFragment;

public class CharacterEditActivity extends SlideoutNavigationActivity {

	boolean editMode = false;

	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 setContentView(R.layout.activity_template_generator_welcome2);
		 super.onCreate(savedInstanceState);
//		 toggleAllCheckboxes();
		 //enable all checkboxes exept in slideout-menu (asynctask to allow slideout-menu inflation
		 //via asynctask before
		 toggleCheckboxTask task = new toggleCheckboxTask();
		 task.execute(Boolean.valueOf(true));
		 
		 //Override Slideout-listener to disable checkboxes when closed
		 ActionBarDrawerToggle newToggler = new ActionBarDrawerToggle(this, /* host Activity */
				 mDrawerLayout, /* DrawerLayout object */
				 R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
				 R.string.drawer_open, /* "open drawer" description for accessibility */
				 R.string.drawer_close /* "close drawer" description for accessibility */
				 ) {
			 @Override
			 public void onDrawerClosed(View drawerView) {
				 mDrawerToggle.onDrawerClosed(drawerView);
				 setEditMode(false);
			 }
			 @Override
			 public void onDrawerOpened(View drawerView) {
				 mDrawerToggle.onDrawerOpened(drawerView);
			 }
		 };
		 mDrawerLayout.setDrawerListener(newToggler);

	 }
	 
//	 /**
//	  * shows all checkboxes except for the slideout-menu
//	  * doesn't work atm because we need to refresh the adapter also
//	  * -> use toggleAllCheckBoxes!
//	  */
//	 private void enableAllCheckboxes(){
//		 for(FolderElementData datum : rootFragment.allData){
//			 if(datum.childFragment != null){
//				 if(datum.childFragment instanceof FolderFragment){
//					 for(FolderElementData subDatum : ((FolderFragment) datum.childFragment).allData){
//						 enableCheckboxAndBelow(subDatum);
//					 }
//				 }
//			 }
//		 }
//	 }
//	 
//	 private void enableCheckboxAndBelow(FolderElementData datum){
//		 datum.checkBoxVisible = true;
//		 if(datum.childFragment != null){
//			 if(datum.childFragment instanceof FolderFragment){
//				 for(FolderElementData subDatum : ((FolderFragment) datum.childFragment).allData){
//					 enableCheckboxAndBelow(subDatum);
//				 }
//			 }
//		 }
//	 }
	 
	 private void toggleAllCheckboxes(boolean visible){
		 Log.d("CharacterEditActivity", "toggleAllCheckboxes");
		 if(rootFragment.allData != null){
			 for(FolderElementData datum: rootFragment.allData){
				 Log.d("CharacterEditActivity", "toggleAllCheckboxes; subfragment found!");
				 if(datum.childFragment != null){
					 if(datum.childFragment instanceof FolderFragment){
						 toggleCheckboxAndBelow(((FolderFragment) datum.childFragment), visible);
					 }
				 }
			 }
		 }
	 }

	 private void toggleCheckboxAndBelow(FolderFragment fragment, boolean visible){
		 Log.d("CharacterEditActivity", "fragment to enable checkboxes: " + fragment.elementName);
		 if(fragment.dataAdapter != null){
			 fragment.dataAdapter.toggleCheckboxVisibility(visible);
		 }
		 if(fragment.allData != null){
			 for(FolderElementData datum: fragment.allData){
				 if(datum.childFragment != null){
					 if(datum.childFragment instanceof FolderFragment){
						 toggleCheckboxAndBelow(((FolderFragment) datum.childFragment), visible);
					 }
				 }
			 }
		 }
	 }

	 @Override
	 public boolean onPrepareOptionsMenu(Menu menu) {
		 menu.clear();
		 //		 boolean returnValue = super.onPrepareOptionsMenu(menu);
		 if(SlideoutNavigationActivity.getAc().getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
			 getMenuInflater().inflate(R.menu.character_editor, menu);
			 
		 }
		 //		 return returnValue;
		 return super.onPrepareOptionsMenu(menu);
	 }
	 
	 private void setEditMode(boolean editEnabled){
		 editMode = editEnabled;
		 Log.d("CharacterEditActivity", "edit mode set to: " + editMode);
		 rootFragment.dataAdapter.toggleCheckboxVisibility(editMode);
	 }

	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		 int id = item.getItemId();
		 if (id == R.id.action_edit_mode) {
			 setEditMode(!editMode);
		 }
		 return super.onOptionsItemSelected(item);
	 }
	 
	 private class toggleCheckboxTask extends AsyncTask<Boolean, Void, Boolean> {
			@Override
			protected void onPostExecute(Boolean visible) {
				toggleAllCheckboxes(visible.booleanValue());
			}

			@Override
			protected Boolean doInBackground(Boolean... params) {
				if(params[0] == null){
					//if nothing given show checkboxes
					return Boolean.valueOf(true);
				}
				return params[0];
			}
			
	    }

}
