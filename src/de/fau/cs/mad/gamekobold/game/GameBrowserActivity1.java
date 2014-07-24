package de.fau.cs.mad.gamekobold.game;

import android.app.Fragment;

public class GameBrowserActivity1 extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new GameBrowserFragment();
	}
}
