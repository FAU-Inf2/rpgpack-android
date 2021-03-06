package de.fau.cs.mad.rpgpack.game;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;
import de.fau.cs.mad.rpgpack.R;
import de.fau.cs.mad.rpgpack.ThumbnailLoader;
import de.fau.cs.mad.rpgpack.URIUtils;
import de.fau.cs.mad.rpgpack.characterbrowser.CharacterBrowserArrayAdapter;
import de.fau.cs.mad.rpgpack.jackson.CharacterSheet;

public class CreateNewGameFragment extends Fragment {
	public static final String EXTRA_GAME_TO_EDIT = "de.fau.cs.mad.gamekobold.gametoedit";
	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;

	private CallbacksCreateNewGame mCallbacks;

	private CharacterBrowserArrayAdapter adapter = null;

	private Uri imageUri;

	private Game curGame;
	private EditText gameName;
	private EditText worldName;
	private EditText gameDate;
	private GridView pickedCharacterGridView;
	private ImageButton addImageButton;
	private Button infoButton;
	private PickedCharacterGridAdapter pickedCharacterGridAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().setTitle(
				getResources().getString(R.string.titel_create_game));

		// check if we have to deal with newGame or gameToEdit
		if ((getActivity().getIntent().hasExtra(EXTRA_GAME_TO_EDIT))) {
			curGame = (Game) getActivity().getIntent().getParcelableExtra(
					EXTRA_GAME_TO_EDIT);
		} else {
			curGame = new Game();
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_create_new_game1,
				parent, false);

		// for back-button
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

		gameName = (EditText) view.findViewById(R.id.gameNameText);
		worldName = (EditText) view.findViewById(R.id.worldNameText);
		gameDate = (EditText) view.findViewById(R.id.gameDateText);
		addImageButton = (ImageButton) view.findViewById(R.id.buttonAddIcon);
		infoButton = (Button) view.findViewById(R.id.buttonInfoPopup);

		pickedCharacterGridView = (GridView) view
				.findViewById(R.id.pickedCharacterGridView);

		// we've got a game for edit
		if ((getActivity().getIntent().hasExtra(EXTRA_GAME_TO_EDIT))) {
			curGame = (Game) getActivity().getIntent().getParcelableExtra(
					EXTRA_GAME_TO_EDIT);
			// curGame = (Game) getActivity().getIntent().getSerializableExtra(
			// EXTRA_GAME_TO_EDIT);

			gameName.setText(curGame.getGameName());
			// we can not edit game name because of it is used in the game file
			// name
			gameName.setEnabled(false);
			// gameDate.setText(curGame.getDate());
			worldName.setText(curGame.getWorldName());
			worldName.requestFocus();
			// TODO change gameDate to gameMaster
			gameDate.setText(curGame.getGameMaster());

			final Bitmap icon = ThumbnailLoader.loadThumbnail(
					curGame.getIconPath(), getActivity());
			if (icon != null) {
				addImageButton.setImageBitmap(icon);
			}
			getActivity().setTitle(curGame.getGameName());
		}

		pickedCharacterGridAdapter = new PickedCharacterGridAdapter(
				getActivity(), R.layout.itemlayout_grid_picked_character,
				curGame);
		// pass adapter to activity
		mCallbacks.onSelCharAdapterPass(pickedCharacterGridAdapter);

		pickedCharacterGridView.setAdapter(pickedCharacterGridAdapter);

		// delete character on long click
		pickedCharacterGridView
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
						// user says no - do nothing
						builder.setNegativeButton(
								getResources().getString(R.string.no),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}
								});
						// users says yes - delete character from game
						builder.setPositiveButton(
								getResources().getString(R.string.yes),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// remove picked character from the new
										// game
										curGame.removeCharacterSheet(curGameCharacter);
										pickedCharacterGridAdapter
												.notifyDataSetChanged();
									}
								});
						builder.create().show();
						return true;
					}
				});

		gameName.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				// save
				curGame.setGameName(c.toString());
				Log.d("addTextChangedListener", c.toString());
				// mCallbacks.onGameNamePass(c.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});

		worldName.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				// save
				curGame.setWorldName(c.toString());
				// mCallbacks.onWorldNamePass(c.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});

		gameDate.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				// save
				curGame.setGameMaster(c.toString());
				// mCallbacks.onGameMasterPass(c.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.select_dialog_item, getResources()
						.getStringArray(R.array.image_picker_items));
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(getResources().getString(R.string.add_icon));
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) {
					// create an intent to open Camera app
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// create temporary file to hold the image from Camera.
					File file = new File(Environment
							.getExternalStorageDirectory(), "tmp_avatar_"
							+ String.valueOf(System.currentTimeMillis())
							+ ".jpg");
					imageUri = Uri.fromFile(file);

					try {
						intent.putExtra(
								android.provider.MediaStore.EXTRA_OUTPUT,
								imageUri);
						intent.putExtra("return-data", true);
						startActivityForResult(intent, PICK_FROM_CAMERA);

					} catch (Exception e) {
						e.printStackTrace();
					}

					dialog.cancel();
				} else {
					// if user choose to select image from sdcard, start the
					// intent to open image chooser dialog.
					// the image chooser dialog will display list File Manager
					// (if exist) apps and default Gallery app.
					Intent intent = new Intent();

					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);

					startActivityForResult(Intent.createChooser(intent,
							"Complete action using"), PICK_FROM_FILE);
				}
			}
		});

		final AlertDialog dialog = builder.create();

		addImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// open OpenPictureDialog
				Toast.makeText(getActivity(), "You want to pick a picture!",
						Toast.LENGTH_SHORT).show();
				dialog.show();
			}
		});
		// popup with game notices
		infoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// show popup with some space for Game Infos
				showPopup(curGame);
			}
		});

		// pass game to the activity
		mCallbacks.onGamePass(curGame);
		Log.d("mCallbacks.onGamePass(curGame);", curGame.getGameName());
		return view;
	}

	private void showPopup(Game game) {
		GameInfoDialogFragment gameInfoDialogFragment = GameInfoDialogFragment
				.newInstance(game, false);
		gameInfoDialogFragment.show(getFragmentManager(),
				"popupGameInfoFragment");

	}

	// popup with game notices
	public static class GameInfoDialogFragment extends DialogFragment {
		private EditText editTextInfo;
		private Game cGame;
		private boolean infoMode;

		// due to avoiding of using non-default constructor in fragment
		public static GameInfoDialogFragment newInstance(Game game,
				boolean infoMode) {
			GameInfoDialogFragment fragment = new GameInfoDialogFragment();
			fragment.cGame = game;
			fragment.infoMode = infoMode;
			return fragment;
		}

		@Override
		public Dialog onCreateDialog(Bundle SaveInstanceState) {
			// Use the Builder class for convenient Dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			// Get the layout inflater
			LayoutInflater inflater = getActivity().getLayoutInflater();

			// Inflate and set the layout for the dialog
			// Pass null as the parent view because its going in the dialog
			// layout
			View view = inflater.inflate(R.layout.popup_create_game_info, null);

			// get all EditTexts
			editTextInfo = (EditText) view
					.findViewById(R.id.editTextAdditionalInformation);
			// disable editing if in info mode
			if (infoMode) {
				editTextInfo.setEnabled(false);
				if (cGame != null) {
					if (cGame.getDescription().isEmpty()) {
						// change hint if the description is empty
						editTextInfo.setHint(R.string.no_description_found);
					}
				}
			}

			if (cGame != null) {
				if (!cGame.getDescription().isEmpty()) {
					editTextInfo.setText(cGame.getDescription());
				}
			}

			builder.setView(view);

			builder.setMessage(getString(R.string.popup_create_game_info_titel));
			String posButtonString = null;
			if (infoMode) {
				posButtonString = getString(R.string.ok);
			} else {
				posButtonString = getString(R.string.save_changes);
			}

			builder.setPositiveButton(posButtonString,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// do not save in info mode
							if (!infoMode) {
								// save new game notices
								cGame.setDescription(editTextInfo
										.getEditableText().toString());
								// game changed!!!
							}
						}
					});

			builder.setNegativeButton(getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog
							GameInfoDialogFragment.this.getDialog().cancel();
						}
					});

			// Create the AlertDialog object and return it
			final AlertDialog dialog = builder.create();

			dialog.setOnShowListener(new DialogInterface.OnShowListener() {
				@Override
				public void onShow(final DialogInterface dialog) {

					Button positiveButton = ((AlertDialog) dialog)
							.getButton(DialogInterface.BUTTON_POSITIVE);

					// set OK button color here
					positiveButton.setBackgroundColor(getActivity()
							.getResources().getColor(R.color.bright_green));
					positiveButton.invalidate();
				}
			});
			return dialog;
		}
	}

	// to handle the selected image with image picker
	// public void returnResult() {
	// getActivity().setResult(Activity.RESULT_OK, null);
	// }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.d("mCallbacks.onGamePass(curGame) - onActivityResult;",
				curGame.getGameName());
		if (resultCode != Activity.RESULT_OK)
			return;

		Bitmap bitmap = null;
		String path = "";

		Log.i("Here is Bitmap null!", "" + (bitmap == null));

		if (requestCode == PICK_FROM_FILE) {
			// get the uri of selected image
			imageUri = intent.getData();

			path = URIUtils.getPath(getActivity(), imageUri);

			Log.i("Here is imageUri = ", "" + imageUri);
			Log.i("Here is path = ", "" + path);

			if (path != null)
				bitmap = ThumbnailLoader.loadThumbnail(path, getActivity());
			Log.i("Bitmap is null?", "" + (bitmap == null));

		} else if (requestCode == PICK_FROM_CAMERA) {
			// If user choose to take picture from camera, get the real path of
			// temporary file
			path = imageUri.getPath();
			bitmap = ThumbnailLoader.loadThumbnail(path, getActivity());

			Log.i("Here is imageUri = ", "" + imageUri);
			Log.i("Here is path = ", "" + path);

			Log.i("Bitmap is null?", "" + (bitmap == null));
		}

		if (bitmap != null) {
			addImageButton.setImageBitmap(bitmap);
		}

		// store image path for later use
		curGame.setIconPath(path);
		mCallbacks.onGamePass(curGame);
	}

	/**
	 * Required interface for hosting activities.
	 */
	// have to pass objects to activity
	public interface CallbacksCreateNewGame {
		public void onGamePass(Game curGame);

		public void onSelCharAdapterPass(
				PickedCharacterGridAdapter pickedCharacterGridAdapter);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (CallbacksCreateNewGame) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mCallbacks.onGamePass(curGame);
	}
}
