package de.fau.cs.mad.gamekobold.game;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class GameDetailsFragment extends Fragment {
	public static final String EXTRA_GAME_NAME = "de.fau.cs.mad.gamekobold.game.gamename";

	// private List<GameCharakter> charakterList;
	// private List<String> tagList;

	private Game game;
	private Template template;
	private GameCharacter curCharacter;

	private TextView gameName;
	private TextView date;
	private TextView description;
	private TextView templateName;
	private ImageView gameIcon;
	private Button buttonInfo;
	private GridView gameCharacterGridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_game_details, parent,
				false);

		String gName = (String) getActivity().getIntent().getSerializableExtra(
				EXTRA_GAME_NAME);
		game = GameLab.get(getActivity()).getGame(gName);

		gameName = (TextView) view.findViewById(R.id.gameName);
		date = (TextView) view.findViewById(R.id.textViewDate);
		templateName = (TextView) view.findViewById(R.id.textViewWorldName);
		gameIcon = (ImageView) view.findViewById(R.id.iconGame);
		buttonInfo = (Button) view.findViewById(R.id.buttonInfoPopup);
		gameCharacterGridView = (GridView) view
				.findViewById(R.id.gridViewCharacters);

		gameName.setText(game.getGameName());
		templateName.setText(game.getTemplate().getTemplateName());
		date.setText(game.getDate());

		final CharacterGridAdapter characterGridAdapter = new CharacterGridAdapter(
				getActivity(), R.layout.itemlayout_expandablelist_charakter,
				game);
		gameCharacterGridView.setAdapter(characterGridAdapter);

		gameCharacterGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				curCharacter = (GameCharacter) adapterView
						.getItemAtPosition(position);

				Toast.makeText(
						getActivity(),
						((TextView) view.findViewById(R.id.textItemTitle))
								.getText(), Toast.LENGTH_SHORT).show();

				// Start playCharactedActivity
				Intent i = new Intent(getActivity(),
						PlayCharacterActivity.class);
				i.putExtra(PlayCharacterFragment.EXTRA_PLAYED_CHARACTER,
						curCharacter);
				startActivity(i);

			}
		});

		gameCharacterGridView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> adapterView,
							View view, final int position, long id) {
						Log.d("LONG CLICK", "pos:" + position);

						final GameCharacter curGameCharacter = (GameCharacter) adapterView
								.getItemAtPosition(position);

						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setTitle(getResources().getString(
								R.string.text_remove_character_from_game));
						builder.setMessage(getResources()
								.getString(
										R.string.text_click_to_remove_character_from_game));
						builder.setNegativeButton(
								getResources().getString(R.string.no),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								});
						builder.setPositiveButton(
								getResources().getString(R.string.yes),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// remove picked character from the
										// current game
										game.removeCharacter(curGameCharacter);
										characterGridAdapter
												.notifyDataSetChanged();
									}
								});
						builder.create().show();
						return true;
					}
				});

		return view;
	}
}