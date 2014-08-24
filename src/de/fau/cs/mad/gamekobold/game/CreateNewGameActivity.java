package de.fau.cs.mad.gamekobold.game;

import android.app.Fragment;
import android.content.Intent;

public class CreateNewGameActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new CreateNewGameFragment();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(CreateNewGameActivity.this, GameBrowserActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}
