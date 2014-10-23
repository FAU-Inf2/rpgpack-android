package de.fau.cs.mad.rpgpack.characterbrowser;

import android.app.Fragment;
import android.util.Log;
import de.fau.cs.mad.rpgpack.characterbrowser.CharacterBrowserFragment.CallbacksCharBrowser;
import de.fau.cs.mad.rpgpack.game.Game;
import de.fau.cs.mad.rpgpack.game.PickedCharacterGridAdapter;
import de.fau.cs.mad.rpgpack.game.SingleFragmentActivity;
import de.fau.cs.mad.rpgpack.game.CreateNewGameFragment.CallbacksCreateNewGame;
import de.fau.cs.mad.rpgpack.jackson.CharacterSheet;

public class CharacterBrowserActivity extends SingleFragmentActivity implements
		CallbacksCharBrowser, CallbacksCreateNewGame {

	@Override
	protected Fragment createFragment() {
		return new CharacterBrowserFragment();
	}

//	@Override
//	public void onGameNamePass(String gameName) {
//		Log.d("CharacterBrowserActivity", "gameName " + gameName);
//
//	}

	@Override
	public void onCharacterSelected(CharacterSheet clickedChar) {
		Log.d("CharacterBrowserActivity",
				"clickedChar " + clickedChar.getName());

	}

	@Override
	public void onGamePass(Game curGame) {
		Log.d("CharacterBrowserActivity", "curGame " + curGame.getGameName());

	}

	@Override
	public void onSelCharAdapterPass(
			PickedCharacterGridAdapter pickedCharacterGridAdapter) {
		Log.d("CharacterBrowserActivity", "pickedCharacterGridAdapter "
				+ pickedCharacterGridAdapter.toString());

	}

}
