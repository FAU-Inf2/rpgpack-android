package de.fau.cs.mad.gamekobold.characterbrowser;

import android.app.Fragment;
import android.util.Log;
import de.fau.cs.mad.gamekobold.characterbrowser.CharacterBrowserFragment.CallbacksCharBrowser;
import de.fau.cs.mad.gamekobold.game.Game;
import de.fau.cs.mad.gamekobold.game.SingleFragmentActivity;
import de.fau.cs.mad.gamekobold.game.CreateNewGameFragment.CallbacksCreateNewGame;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;

public class CharacterBrowserActivity extends SingleFragmentActivity implements
CallbacksCharBrowser, CallbacksCreateNewGame{

	@Override
	protected Fragment createFragment() {
		return new CharacterBrowserFragment();
	}

	@Override
	public void onGameNamePass(String gameName) {
		Log.d("LOG", "gameName " + gameName);
	
	}

	@Override
	public void onCharacterSelected(CharacterSheet clickedChar) {
		Log.d("LOG", "clickedChar " + clickedChar.getName());

	}

	@Override
	public void onGamePass(Game curGame) {
		Log.d("LOG", "curGame " + curGame.getGameName());
	
	}
	
	
}
