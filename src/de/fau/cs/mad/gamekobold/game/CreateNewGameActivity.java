package de.fau.cs.mad.gamekobold.game;

import de.fau.cs.mad.gamekobold.R;
import android.app.Fragment;

public class CreateNewGameActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new CreateNewGameFragment();
	}
	
	@Override
	protected int getLayoutResId() {
	return R.layout.activity_create_new_game;
	}
}
