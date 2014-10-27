package de.fau.cs.mad.rpgpack.game;

import android.app.Fragment;

public class GameDetailsActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		return new GameDetailsFragment();
	}
}
