package de.fau.cs.mad.gamekobold.game;

import android.app.Fragment;

public class CreateNewGameActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new CreateNewGameFragment();
	}
}
