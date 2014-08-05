package de.fau.cs.mad.gamekobold.game;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.MainActivity;
import de.fau.cs.mad.gamekobold.R;

public class PlayCharacterFragment extends Fragment {
	public static final String EXTRA_PLAYED_CHARACTER = "de.fau.cs.mad.gamekobold.game.character";
	public static final String EXTRA_PLAYED_GAME = "de.fau.cs.mad.gamekobold.game";

	private Game playedGame;
	private GameCharacter playedCharacter;
	private TextView characterName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getActivity().setTitle(R.string.played_character_title);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_play_character, parent,
				false);

		playedGame = (Game) getActivity().getIntent().getSerializableExtra(
				EXTRA_PLAYED_GAME);
		playedCharacter = (GameCharacter) getActivity().getIntent()
				.getSerializableExtra(EXTRA_PLAYED_CHARACTER);

		getActivity().setTitle(
				playedGame.getGameName() + " > "
						+ playedCharacter.getCharacterName());

		characterName = (TextView) view.findViewById(R.id.textCharacterName);
		characterName
				.setText("PlayCharacterFragment! Du wirst jetzt mit dem Charakter: "
						+ playedCharacter.getCharacterName() + " spielen!");
		return view;
	}

}
