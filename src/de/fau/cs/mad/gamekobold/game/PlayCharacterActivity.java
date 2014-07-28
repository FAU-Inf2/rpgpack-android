package de.fau.cs.mad.gamekobold.game;

import de.fau.cs.mad.gamekobold.R;
import android.app.Fragment;
import android.view.Menu;

public class PlayCharacterActivity extends SingleFragmentActivity {
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
}
