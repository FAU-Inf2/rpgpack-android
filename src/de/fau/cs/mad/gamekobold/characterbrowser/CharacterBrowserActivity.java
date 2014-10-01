package de.fau.cs.mad.gamekobold.characterbrowser;

import android.app.Fragment;
import de.fau.cs.mad.gamekobold.game.SingleFragmentActivity;

public class CharacterBrowserActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new CharacterBrowserFragment();
	}
}
