package de.fau.cs.mad.gamekobold.game;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
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
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.ThumbnailLoader;
import de.fau.cs.mad.gamekobold.character.CharacterEditActivity;
import de.fau.cs.mad.gamekobold.game.CreateNewGameFragment.GameInfoDialogFragment;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.template_generator.TemplateGeneratorActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.CharacterDetailsActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;
import de.fau.cs.mad.gamekobold.templatebrowser.TemplateDetailsActivity;

public class GameDetailsFragment extends Fragment {
	public static final String EXTRA_GAME_NAME = "de.fau.cs.mad.gamekobold.game.gamename";
	public static final String EXTRA_GAME = "de.fau.cs.mad.gamekobold.game";
	public static final String MODE_TEMPLATE = "MODE_TEMPLATE";
	public static final String WELCOME_TYPE_PLAY_CHARACTER = "WELCOME_PLAY_CHARACTER";
	public static final String WELCOME_TYPE_TEMPLATE = "WELCOME_TEMPLATE";
	public static final String EXTRA_MODE_GAME_CREATION = "de.fau.cs.mad.gamekobold.gamecreation";
	// private List<GameCharakter> charakterList;
	// private List<String> tagList;

	private Game game;
	private Template template;
	private CharacterSheet curCharacter;

	private TextView gameName;
	private TextView date;
	private TextView description;
	private TextView worldName;
	private TextView gameMaster;
	private ImageView gameIcon;
	private Button infoButton;
	private Button editGameButton;
	private Button playgameButton;
	private GridView gameCharacterGridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		String path = "";

		View view = inflater.inflate(R.layout.fragment_game_details, parent,
				false);
		// for back-button
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

		// String gName = (String)
		// getActivity().getIntent().getSerializableExtra(
		// EXTRA_GAME_NAME);

		// TODO change it!!!
		// Log.d("GameDetailsFragment", "getGame >>" + gName);
		// game = GameLab.get(getActivity()).getGame(gName);

		// check if it is game edit mode or game creation mode
		if ((getActivity().getIntent().hasExtra(EXTRA_GAME))) {
			game = (Game) getActivity().getIntent().getParcelableExtra(
					EXTRA_GAME);
			// curGame = (Game) getActivity().getIntent().getSerializableExtra(
			// EXTRA_GAME_TO_EDIT);
		} else {
			game = new Game();
		}

		getActivity().setTitle(game.getGameName());

		gameName = (TextView) view.findViewById(R.id.gameName);
		date = (TextView) view.findViewById(R.id.textViewDate);
		worldName = (TextView) view.findViewById(R.id.textViewWorldName);
		gameMaster = (TextView) view.findViewById(R.id.textViewGM);
		gameIcon = (ImageView) view.findViewById(R.id.iconGame);
		infoButton = (Button) view.findViewById(R.id.buttonGameInfoPopup);
		gameCharacterGridView = (GridView) view
				.findViewById(R.id.gridViewCharacters);

		gameName.setText(game.getGameName());
		worldName.setText(game.getWorldName());
		date.setText(game.getDate());
		gameMaster.setText(getResources().getString(R.string.gamemaster) + " "
				+ game.getGameMaster());

		final GameDetailsCharacterGridAdapter gameDetailsCharacterGridAdapter = new GameDetailsCharacterGridAdapter(
				getActivity(), R.layout.itemlayout_game_details_charakter, game);
		gameCharacterGridView.setAdapter(gameDetailsCharacterGridAdapter);

		gameCharacterGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				curCharacter = (CharacterSheet) adapterView
						.getItemAtPosition(position);

				Toast.makeText(
						getActivity(),
						((TextView) view.findViewById(R.id.textItemTitle))
								.getText(), Toast.LENGTH_SHORT).show();

				// Start CharacterPlayActivity
				File jsonFile = new File(curCharacter.getFileAbsolutePath());
				curCharacter.getFileAbsolutePath();

				try {
					CharacterSheet sheet = JacksonInterface.loadCharacterSheet(
							jsonFile, false);
					CharacterSheet[] oneSheetAsArray = new CharacterSheet[1];
					oneSheetAsArray[0] = sheet;
					Intent intent = CharacterPlayActivity
							.createIntentForStarting(getActivity(),
									oneSheetAsArray);
					startActivity(intent);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		gameCharacterGridView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> adapterView,
							View view, final int position, long id) {
						
						final CharacterSheet curGameCharacter = (CharacterSheet) adapterView
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
										game.removeCharacterSheet(curGameCharacter);
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
				showPopup(game);
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
				// Start createNewGameActivity with current game values!
				Intent i = new Intent(getActivity(),
						CreateNewGameActivity.class);
				i.putExtra(CreateNewGameFragment.EXTRA_GAME_TO_EDIT,
						(Parcelable) game);
				i.putExtra(EXTRA_MODE_GAME_CREATION, true);
				startActivity(i);
			}
		});

		// XXX
		playgameButton = (Button) view.findViewById(R.id.buttonPlayGame);
		playgameButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// curCharacter = (GameCharacter)
				// gameCharacterGridView.getAdapter()
				// .getItemAtPosition(0);
				// just use the first character
				// curCharacter = (GameCharacter)
				// gameDetailsCharacterGridAdapter.getItem(0);
				// Start CharacterPlayActivity
				// get all character-sheets
				CharacterSheet sheets[] = new CharacterSheet[gameDetailsCharacterGridAdapter
						.getCount()];
				int index = 0;
				for (CharacterSheet oneChar : gameDetailsCharacterGridAdapter
						.getItems()) {
					File jsonFile = new File(oneChar.getFileAbsolutePath());
					try {
						CharacterSheet sheet = JacksonInterface
								.loadCharacterSheet(jsonFile, false);
						sheets[index++] = sheet;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// File jsonFile = new File(curCharacter.getFileAbsPath());
				// curCharacter.getFileAbsPath();
				Intent intent = CharacterPlayActivity.createIntentForStarting(
						getActivity(), sheets);
				startActivity(intent);
				// try {
				// CharacterSheet sheet = JacksonInterface.loadCharacterSheet(
				// jsonFile, false);
				// Intent intent = CharacterPlayActivity
				// .createIntentForStarting(getActivity(), sheet);
				// startActivity(intent);
				//
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
		});

		// XXX

		// TODO Check it! is it necessary?
		Log.e("getIconPath is null?", "" + (game.getIconPath() == null));

		final Bitmap bitmap = ThumbnailLoader.loadThumbnail(game.getIconPath(),
				getActivity());
		if (bitmap == null) {
			// set some default group icon
			gameIcon.setImageResource(R.drawable.group_white);
		} else {
			// set game icon
			gameIcon.setImageBitmap(bitmap);
		}
		return view;
	}
//
	private void showPopup(Game game) {
		GameInfoDialogFragment gameInfoDialogFragment = GameInfoDialogFragment
				.newInstance(game, false);
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

	protected String getFileName() {
		int lastSlashPos = curCharacter.getTemplate().fileAbsolutePath
				.lastIndexOf("/");
		if (lastSlashPos == -1) {
			return curCharacter.getTemplate().fileAbsolutePath;
		} else {
			return curCharacter.getTemplate().fileAbsolutePath
					.substring(lastSlashPos + 1);
		}
	}

}
