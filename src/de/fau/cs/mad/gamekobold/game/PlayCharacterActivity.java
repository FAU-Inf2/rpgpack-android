package de.fau.cs.mad.gamekobold.game;

import android.app.Fragment;

public class PlayCharacterActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return new PlayCharacterFragment();
	}
}
