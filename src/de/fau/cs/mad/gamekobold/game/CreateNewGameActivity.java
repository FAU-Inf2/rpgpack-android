package de.fau.cs.mad.gamekobold.game;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.characterbrowser.CharacterBrowserFragment.CallbacksCharBrowser;
import de.fau.cs.mad.gamekobold.game.CreateNewGameFragment.CallbacksCreateNewGame;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;

public class CreateNewGameActivity extends SingleFragmentActivity implements
		CallbacksCharBrowser, CallbacksCreateNewGame {
	public static final String EXTRA_GAME_TO_EDIT = "de.fau.cs.mad.gamekobold.gametoedit";

	Button createGameButton;
	private String gameName;
	private CharacterSheet clickedChar;
	private Game curGame;

	@Override
	protected Fragment createFragment() {
		return new CreateNewGameFragment();
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_create_new_game;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createGameButton = (Button) findViewById(R.id.buttonCreateGame);
		createGameButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (gameName.equals("")) {
					Toast.makeText(
							CreateNewGameActivity.this,
							getResources().getString(
									R.string.warning_set_gamename),
							Toast.LENGTH_SHORT).show();
					return;
				}

				// now it goes to GameDetailsFragment
				// Save and start GameDetailsActivity
				try {
					if (curGame.getDate() == null) {
						// set creation date
						final SimpleDateFormat format = new SimpleDateFormat(
								"dd.MM.yyyy");
						final Date date = new Date();
						curGame.setDate(format.format(date));
					}
					// save game
					JacksonInterface.saveGame(curGame,
							CreateNewGameActivity.this);
					// check if we are editing a game
					if (getIntent().hasExtra(EXTRA_GAME_TO_EDIT)) {
						// if so we were already in the details fragment-> just
						// go back
						onBackPressed();
					} else {
						// if not we want to got to the details fragment ->
						// start it
						Intent i = new Intent(CreateNewGameActivity.this,
								GameDetailsActivity.class);
						i.putExtra(GameDetailsFragment.EXTRA_GAME_NAME,
								curGame.getGameName());
						startActivity(i);
					}

				} catch (Throwable e) {
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	public void onGameNamePass(String gameName) {
		Log.d("LOG", "gameName " + gameName);
		this.gameName = gameName;
	}

	@Override
	public void onCharacterSelected(CharacterSheet clickedChar) {
		Log.d("LOG", "clickedChar " + clickedChar.getName());
		this.clickedChar = clickedChar;
		curGame.addCharacterSheet(clickedChar);
	}

	@Override
	public void onGamePass(Game curGame) {
		Log.d("LOG", "curGame " + curGame.getGameName());
		this.curGame = curGame;
	}
}
