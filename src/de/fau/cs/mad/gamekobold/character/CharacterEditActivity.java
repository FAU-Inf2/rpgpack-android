package de.fau.cs.mad.gamekobold.character;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;

public class CharacterEditActivity extends SlideoutNavigationActivity {

	/**
	 * in editMode -> checkboxes in slideout-menu are shown -> set with setCheckboxVisibilityInSlideoutmenu(boolean)
	 */
	private boolean editMode = false;

	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 setContentView(R.layout.activity_template_generator_welcome2);
		 super.onCreate(savedInstanceState);
		 //enable all checkboxes exept in slideout-menu
		 //use an asynctask to allow inflation of folders/tables/matrices
		 //by superclass before
		 SetCheckboxVisibilityTask task = new SetCheckboxVisibilityTask();
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
				 setCheckboxVisibilityInSlideoutmenu(false);
			 }
			 @Override
			 public void onDrawerOpened(View drawerView) {
				 mDrawerToggle.onDrawerOpened(drawerView);
			 }
		 };
		 mDrawerLayout.setDrawerListener(newToggler);

	 }
	 
	 /**
	  * set visibility for all checkboxes except in slideout-menu
	  * @param visible
	  */
	 private void setCheckboxVisibiltyExceptSlideoutmenu(boolean visible){
		 rootFragment.setCheckboxVisibilityBelow(true);
	 }
	 
	 /**
	  * set visibility for checkboxes inside slideout-menu
	  * @param visible
	  */
	 private void setCheckboxVisibilityInSlideoutmenu(boolean visible){
		 editMode = visible;
		 rootFragment.setCheckboxVisibility(visible);
	 }


	 @Override
	 public boolean onPrepareOptionsMenu(Menu menu) {
		 menu.clear();
		 if(SlideoutNavigationActivity.getAc().getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
			 getMenuInflater().inflate(R.menu.character_editor, menu);
		 }
		 return super.onPrepareOptionsMenu(menu);
	 }
	 
	 
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		 int id = item.getItemId();
		 if (id == R.id.action_edit_mode) {
			 setCheckboxVisibilityInSlideoutmenu(!editMode);
		 }
		 return super.onOptionsItemSelected(item);
	 }
	 
	 private class SetCheckboxVisibilityTask extends AsyncTask<Boolean, Void, Boolean> {
			@Override
			protected void onPostExecute(Boolean visible) {
				setCheckboxVisibiltyExceptSlideoutmenu(visible.booleanValue());
			}
			@Override
			protected Boolean doInBackground(Boolean... params) {
				if(params[0] == null){
					//if no boolean parameter given: show checkboxes
					return Boolean.valueOf(true);
				}
				return params[0];
			}
			
	    }

}
