package de.fau.cs.mad.gamekobold.game;

import android.app.Fragment;

public class GameBrowserActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new GameBrowserFragment();
	}
}
