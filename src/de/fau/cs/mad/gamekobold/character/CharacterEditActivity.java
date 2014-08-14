package de.fau.cs.mad.gamekobold.character;


import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.template_generator.FolderElementAdapter;
import de.fau.cs.mad.gamekobold.template_generator.FolderElementData;

public class CharacterEditActivity extends SlideoutNavigationActivity {

	boolean editMode = false;

	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 setContentView(R.layout.activity_template_generator_welcome2);
		 super.onCreate(savedInstanceState);
		 //enable all checkboxes exept in slideout-menu
//		 for(FolderElementData datum : rootFragment.allData){
//			 datum.childFragment.
//		 }
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

	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		 int id = item.getItemId();
		 if (id == R.id.action_edit_mode) {
			 editMode = !editMode;
			 Log.d("CharacterEditActivity", "edit mode set to: " + editMode);
			 
			 
			 
//			 for(FolderElementData oneDatum: rootFragment.allData){
//				 oneDatum.allowChecking = !oneDatum.allowChecking;
//			 }
//			 rootFragment.dataAdapter.notifyDataSetChanged();
			 rootFragment.dataAdapter.toggleCheckboxVisibility();
		 }
		 return super.onOptionsItemSelected(item);
	 }

}
