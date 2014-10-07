package de.fau.cs.mad.gamekobold.game;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
	public static final String EXTRA_MODE_GAME_EDITION = "de.fau.cs.mad.gamekobold.gameedition";

	private Button createGameButton;
	private Game curGame;
	private PickedCharacterGridAdapter pickedCharacterGridAdapter;
	private CharacterSheet clickedChar;

	// private boolean editMode;

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
		// editMode = false;

		if (getIntent().hasExtra(EXTRA_GAME_TO_EDIT)) {
			curGame = (Game) getIntent().getParcelableExtra(EXTRA_GAME_TO_EDIT);
			// editMode = true;
		}

		createGameButton = (Button) findViewById(R.id.buttonCreateGame);
		createGameButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// check if game name is set or not
				if (curGame.getGameName().equals("")) {
					Toast.makeText(
							CreateNewGameActivity.this,
							getResources().getString(
									R.string.warning_set_gamename),
							Toast.LENGTH_SHORT).show();
					return;
				}
				// if (editMode) {
				// Log.d("editmode!",
				// "try to save new values!");
				// }
				// Save and start GameDetailsActivity
				try {
					// if (curGame.getDate().equals("")) {
					// set creation date
					final SimpleDateFormat format = new SimpleDateFormat(
							"yyyy/MM/dd HH:mm:ss");
					final Date date = new Date();
					curGame.setDate(format.format(date));
					// }
					// save game
					JacksonInterface.saveGame(curGame,
							CreateNewGameActivity.this);

					// if not we want to got to the details fragment ->
					// start it
					Intent i = new Intent(CreateNewGameActivity.this,
							GameDetailsActivity.class);
					i.putExtra(GameDetailsFragment.EXTRA_GAME,
							(Parcelable) curGame);
					// i.putExtra(GameDetailsFragment.EXTRA_GAME_NAME,
					// curGame.getGameName());
					startActivity(i);

					// check if we are editing a game
					// if (getIntent().hasExtra(EXTRA_GAME_TO_EDIT)) {
					// // if so we were already in the details fragment-> just
					// // go back
					// onBackPressed();
					// } else {
					// // if not we want to got to the details fragment ->
					// // start it
					// Intent i = new Intent(CreateNewGameActivity.this,
					// GameDetailsActivity.class);
					// i.putExtra(GameDetailsFragment.EXTRA_GAME,
					// (Parcelable) curGame);
					// // i.putExtra(GameDetailsFragment.EXTRA_GAME_NAME,
					// // curGame.getGameName());
					// startActivity(i);
					// }

				} catch (Throwable e) {
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	public void onCharacterSelected(CharacterSheet clickedChar) {
		Log.d("CreateNewGameActivity", "clickedChar " + clickedChar.getName());
		this.clickedChar = clickedChar;
		if (curGame.isInCharacterList(clickedChar)) {
			curGame.removeCharacterSheet(clickedChar);
		} else {
			curGame.addCharacterSheet(clickedChar);
		}
		pickedCharacterGridAdapter.notifyDataSetChanged();
	}

	@Override
	public void onGamePass(Game curGame) {
		Log.d("CreateNewGameActivity", "curGame " + curGame.getGameName());
		this.curGame = curGame;
	}

	@Override
	public void onSelCharAdapterPass(
			PickedCharacterGridAdapter pickedCharacterGridAdapter) {
		Log.d("CreateNewGameActivity", "pickedCharacterGridAdapter "
				+ pickedCharacterGridAdapter.toString());
		this.pickedCharacterGridAdapter = pickedCharacterGridAdapter;

	}
}
