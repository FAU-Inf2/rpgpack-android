package de.fau.cs.mad.rpgpack.game;

import android.app.Fragment;

public class GameBrowserActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new GameBrowserFragment();
	}
	
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.play_actions, menu);
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
	*/
}
