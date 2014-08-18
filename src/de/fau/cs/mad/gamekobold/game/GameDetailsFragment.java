package de.fau.cs.mad.gamekobold.game;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.game.CreateNewGameFragment.GameInfoDialogFragment;
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
	private TextView worldName;
	private ImageView gameIcon;
	private Button infoButton;
	private Button editGameButton;
	private GridView gameCharacterGridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		Bitmap bitmap = null;
		String path = "";

		View view = inflater.inflate(R.layout.fragment_game_details, parent,
				false);
		// for back-button
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

		String gName = (String) getActivity().getIntent().getSerializableExtra(
				EXTRA_GAME_NAME);

		// TODO change it!!!
		game = GameLab.get(getActivity()).getGame(gName);

		if (!(game == null)) {
			getActivity().setTitle(game.getGameName());

		} else {
			getActivity().setTitle("Default");

		}

		gameName = (TextView) view.findViewById(R.id.gameName);
		date = (TextView) view.findViewById(R.id.textViewDate);
		worldName = (TextView) view.findViewById(R.id.textViewWorldName);
		gameIcon = (ImageView) view.findViewById(R.id.iconGame);
		infoButton = (Button) view.findViewById(R.id.buttonGameInfoPopup);
		gameCharacterGridView = (GridView) view
				.findViewById(R.id.gridViewCharacters);

		gameName.setText(game.getGameName());
		worldName.setText(game.getTemplate().getWorldName());
		date.setText(game.getDate());

		final GameDetailsCharacterGridAdapter gameDetailsCharacterGridAdapter = new GameDetailsCharacterGridAdapter(
				getActivity(), R.layout.itemlayout_game_details_charakter, game);
		gameCharacterGridView.setAdapter(gameDetailsCharacterGridAdapter);

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
				i.putExtra(PlayCharacterFragment.EXTRA_PLAYED_GAME, game);
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
										gameDetailsCharacterGridAdapter
												.notifyDataSetChanged();
									}
								});
						builder.create().show();
						return true;
					}
				});

		infoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// show popup with some space for Game Info
				showPopup();
			}
		});

		editGameButton = (Button) view.findViewById(R.id.buttonEditGame);
		editGameButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// go into edit game mode
				Toast.makeText(
						getActivity(),

						getActivity().getResources().getString(
								R.string.msg_edit_game_mode),
						Toast.LENGTH_SHORT).show();
				// TODO
				// Start createNewGameActivity with current game values!
				Intent i = new Intent(getActivity(),
						CreateNewGameActivity.class);

				i.putExtra(CreateNewGameFragment.EXTRA_GAME_TO_EDIT, game);

				startActivity(i);
			}
		});

		// TODO Check it! is it necessary?
		Log.e("getIconPath is null?", "" + (game.getIconPath() == null));

		if (game.getIconPath() == null) {
			// set some default game icon
			bitmap = BitmapFactory.decodeResource(getActivity().getResources(),
					R.drawable.game_default_white);
		} else {
			bitmap = BitmapFactory.decodeFile(path);
		}

		if (bitmap != null) {
			// set game icon
			gameIcon.setImageBitmap(bitmap);
		}

		return view;
	}

	private void showPopup() {
		GameInfoDialogFragment gameInfoDialogFragment = GameInfoDialogFragment
				.newInstance(game);
		gameInfoDialogFragment.show(getFragmentManager(),
				"popupGameInfoFragment");

	}

	// handling back-button
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(getActivity(), GameBrowserActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			// finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
