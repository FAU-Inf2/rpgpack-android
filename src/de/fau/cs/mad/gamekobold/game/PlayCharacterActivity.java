package de.fau.cs.mad.gamekobold.game;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.MainActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class PlayCharacterActivity extends SingleFragmentActivity {

	
	
	/*
	public static final String EXTRA_PLAYED_CHARACTER = "de.fau.cs.mad.gamekobold.game.character";
	public static final String EXTRA_PLAYED_GAME = "de.fau.cs.mad.gamekobold.game";

	private Game playedGame;
	private GameCharacter playedCharacter;
	private TextView characterName;
	*/
	
	public void openTools() {
		Intent intent = new Intent(PlayCharacterActivity.this,
				ToolboxActivity.class);
		startActivity(intent);
	}

	/*
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_play_character);

		playedGame = (Game) this.getIntent().getSerializableExtra(
				EXTRA_PLAYED_GAME);

		Log.i("playedGame is null?", "" + (playedGame == null));

		playedCharacter = (GameCharacter) this.getIntent()
				.getSerializableExtra(EXTRA_PLAYED_CHARACTER);

		Log.i("playedCharacter is null?", "" + (playedCharacter == null));

		// FIXME check it for character grid view! null pointer exception!
		this.setTitle(
				playedGame.getGameName() + " > "
						+ playedCharacter.getCharacterName());

		characterName = (TextView) findViewById(R.id.textCharacterName);
		characterName
				.setText("PlayCharacterFragment! Du wirst jetzt mit dem Charakter: "
						+ playedCharacter.getCharacterName() + " spielen!");
	} */
	@Override
	protected Fragment createFragment() {
		return new PlayCharacterFragment();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_actions, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_tools:
			openTools();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
