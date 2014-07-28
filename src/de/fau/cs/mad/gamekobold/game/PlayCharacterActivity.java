package de.fau.cs.mad.gamekobold.game;

import de.fau.cs.mad.gamekobold.R;
import android.app.Fragment;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

public class PlayCharacterActivity extends SingleFragmentActivity {
	
	public void openTools(){		
		Intent intent = new Intent(PlayCharacterActivity.this, ToolboxActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected Fragment createFragment() {
		return new PlayCharacterFragment();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_actions, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_tools:
	            openTools();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
}
