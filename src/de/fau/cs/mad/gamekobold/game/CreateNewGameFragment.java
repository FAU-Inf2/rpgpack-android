package de.fau.cs.mad.gamekobold.game;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;
import de.fau.cs.mad.gamekobold.templatestore.TemplateStoreMainActivity;

public class CreateNewGameFragment extends Fragment {
	// private List<String> tagList;

	public static final String EXTRA_GAME_TO_EDIT = "de.fau.cs.mad.gamekobold.gametoedit";

	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;

	private Uri imageUri;
	private ArrayList<Template> templates;
	private Game newGame;
	private Game gameToEdit;
	private EditText gameName;
	private EditText worldName;
	private EditText gameDate;
	private GridView pickedCharacterGridView;
	private Button createGameButton;
	private ImageButton addImageButton;
	private ExpandableListView expandableTemplateList;
	private GameCharacter curCharacter;
	private Button infoButton;
	private PickedCharacterGridAdapter pickedCharacterGridAdapter;
	private ExpandableListArrayAdapter expandableListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// templates we want to display
		templates = TemplateLab.get(getActivity()).getTemplates();
		newGame = new Game();
		setHasOptionsMenu(true);
		getActivity().setTitle(
				getResources().getString(R.string.titel_create_game));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_create_new_game, parent,
				false);

		// for back-button
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

		gameName = (EditText) view.findViewById(R.id.gameNameText);
		worldName = (EditText) view.findViewById(R.id.worldNameText);
		gameDate = (EditText) view.findViewById(R.id.gameDateText);
		addImageButton = (ImageButton) view.findViewById(R.id.buttonAddIcon);
		infoButton = (Button) view.findViewById(R.id.buttonInfoPopup);
		pickedCharacterGridView = (GridView) view
				.findViewById(R.id.pickedCharacterGridView);
		expandableTemplateList = (ExpandableListView) view
				.findViewById(R.id.expandableTemplateList);

		// we've got a game for edit
		if ((getActivity().getIntent().hasExtra(EXTRA_GAME_TO_EDIT))) {
			gameToEdit = (Game) getActivity().getIntent().getSerializableExtra(
					EXTRA_GAME_TO_EDIT);

			// remove last fake item
			gameToEdit.removeCharacter(gameToEdit.getCharakterList().get(
					gameToEdit.getCharakterList().size() - 1));
			gameName.setText(gameToEdit.getGameName());
			worldName.setText(gameToEdit.getTemplate().getWorldName());
			gameDate.setText(gameToEdit.getDate());
			// addImageButton.setImageBitmap(gameToEdit.getBitmap);

		}

		if ((getActivity().getIntent().hasExtra(EXTRA_GAME_TO_EDIT))) {
			expandableListAdapter = new ExpandableListArrayAdapter(
					getActivity(), templates, gameToEdit);
			pickedCharacterGridAdapter = new PickedCharacterGridAdapter(
					getActivity(), R.layout.itemlayout_grid_picked_character,
					gameToEdit);
		} else {
			expandableListAdapter = new ExpandableListArrayAdapter(
					getActivity(), templates, newGame);
			pickedCharacterGridAdapter = new PickedCharacterGridAdapter(
					getActivity(), R.layout.itemlayout_grid_picked_character,
					newGame);
		}

		expandableTemplateList.setAdapter(expandableListAdapter);
		pickedCharacterGridView.setAdapter(pickedCharacterGridAdapter);

		pickedCharacterGridView
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long id) {

						Toast.makeText(
								getActivity(),
								((TextView) view
										.findViewById(R.id.textItemTitle))
										.getText()
										+ " Item " + position + " was clicked",
								Toast.LENGTH_SHORT).show();

						// TODO hier gehts zur Characteransicht zum Spielen
						curCharacter = (GameCharacter) adapterView
								.getItemAtPosition(position);

						Toast.makeText(
								getActivity(),
								((TextView) view
										.findViewById(R.id.textItemTitle))
										.getText(), Toast.LENGTH_SHORT).show();

						// Start playCharactedActivity
						Intent i = new Intent(getActivity(),
								PlayCharacterActivity.class);
						i.putExtra(
								PlayCharacterFragment.EXTRA_PLAYED_CHARACTER,
								curCharacter);
						startActivity(i);

					}
				});

		pickedCharacterGridView
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
										// remove picked character from the new
										// game

										if ((getActivity().getIntent()
												.hasExtra(EXTRA_GAME_TO_EDIT))) {
											gameToEdit
													.removeCharacter(curGameCharacter);
											pickedCharacterGridAdapter
													.notifyDataSetChanged();
										} else {
											newGame.removeCharacter(curGameCharacter);
											pickedCharacterGridAdapter
													.notifyDataSetChanged();
										}
									}
								});
						builder.create().show();
						return true;
					}
				});

		gameName.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				// TODO speichern in objekt
				newGame.setGameName(c.toString());

			}

			public void beforeTextChanged(CharSequence c, int start, int count,
					int after) {
				// This space intentionally left blank
			}

			public void afterTextChanged(Editable c) {
				// This one too
			}

		});

		createGameButton = (Button) view.findViewById(R.id.buttonCreateGame);
		createGameButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Dein Game wird erstellt!",
						Toast.LENGTH_SHORT).show();
				// TODO create newGame object speichern!!!

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

		infoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// show popup with some space for Game Info

				if ((getActivity().getIntent().hasExtra(EXTRA_GAME_TO_EDIT))) {
					showPopup(gameToEdit);
				} else
					showPopup(newGame);
			}
		});

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_templates_list, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_item_load_template_from_store:
			openStore();
			return true;
			// TODO check it!!!
			// handling back-button
			// case android.R.id.home:
			// Intent intent = new Intent(getActivity(),
			// GameBrowserActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(intent);
			// // finish();
			// return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openStore() {
		// Spiel erstellen
		Intent intent = new Intent(getActivity(),
				TemplateStoreMainActivity.class);
		startActivity(intent);
	}

	// TODO check this!

	private void showPopup(Game game) {
		GameInfoDialogFragment gameInfoDialogFragment = GameInfoDialogFragment
				.newInstance(game);
		gameInfoDialogFragment.show(getFragmentManager(),
				"popupGameInfoFragment");

	}

	public static class GameInfoDialogFragment extends DialogFragment {
		private EditText editTextInfo;
		private Game curGame;

		public static GameInfoDialogFragment newInstance(Game game) {
			GameInfoDialogFragment fragment = new GameInfoDialogFragment();
			fragment.curGame = game;
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
			Log.d("curGame is null?", "" + (curGame == null));
			// TODO Check it!!!!
			// if (!curGame.getDescription().isEmpty()) {
			// editTextInfo.setText(curGame.getDescription());
			// } else {
			// editTextInfo.setText(getActivity().getString(
			// R.string.no_description_found));
			// }

			builder.setView(view);

			builder.setMessage(getString(R.string.popup_create_game_info_titel));
			builder.setPositiveButton(getString(R.string.save_changes),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// save new game notices
							curGame.setDescription(editTextInfo
									.getEditableText().toString());
							// game changed!!!
							// TODO check it!! newGame vs myGame
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
		if (resultCode != Activity.RESULT_OK)
			return;

		Bitmap bitmap = null;
		String path = "";

		Log.i("Here is Bitmap null!", "" + (bitmap == null));

		if (requestCode == PICK_FROM_FILE) {
			// get the uri of selected image
			imageUri = intent.getData();

			// Assume user selects the image from sdcard using Gallery app. The
			// uri from Gallery app does not give the real path to selected
			// image, so it has to be resolved on content provider. Method
			// getRealPathFromURI used to resolve the real path from the uri.
			path = getRealPathFromURI(getActivity(), imageUri); // from Gallery

			// If the path is null, assume user selects the image using File
			// Manager app. File Manager app returns different information than
			// Gallery app. To get the real path to selected image, use
			// getImagePath method from the uri
			if (path == null)
				path = imageUri.getPath(); // from File Manager

			if (path != null)
				bitmap = BitmapFactory.decodeFile(path);

			Log.i("Bitmap is null?", "" + (bitmap == null));

		} else if (requestCode == PICK_FROM_CAMERA) {
			// If user choose to take picture from camera, get the real path of
			// temporary file
			path = imageUri.getPath();
			bitmap = BitmapFactory.decodeFile(path);

			Log.i("Bitmap is null?", "" + (bitmap == null));
		}
		if (bitmap != null) {
			addImageButton.setImageBitmap(bitmap);
		}
		// TODO store image path for later use
	}

	// TODO refactoring?
	public String getRealPathFromURI(Context context, Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, proj,
				null, null, null);
		if (cursor == null)
			return null;

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		if (cursor.moveToFirst()) {
			return cursor.getString(column_index);
		} else
			return null;
	}

}
