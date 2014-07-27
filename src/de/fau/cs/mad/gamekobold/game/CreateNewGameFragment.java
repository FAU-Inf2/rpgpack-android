package de.fau.cs.mad.gamekobold.game;

import java.io.File;
import java.util.ArrayList;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.template_generator.TemplateGeneratorActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.CreateNewTemplateActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;
import android.app.AlertDialog;
import android.app.Fragment;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CreateNewGameFragment extends Fragment {
	// private List<String> tagList;

	private Uri imageUri;
	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;

	private ArrayList<Template> templates;
	private Game newGame;
	private TextView gameName;
	private GridView pickedCharacterGridView;
	private Button createGameButton;
	private ImageButton addImageButton;
	private ExpandableListView expandableTemplateList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		templates = TemplateLab.get(getActivity()).getTemplates();
		newGame = new Game();
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_create_new_game, parent,
				false);

		expandableTemplateList = (ExpandableListView) view
				.findViewById(R.id.expandableTemplateList);

		ExpandableListArrayAdapter expandableListAdapter = new ExpandableListArrayAdapter(
				getActivity(), templates, newGame);
		expandableTemplateList.setAdapter(expandableListAdapter);

		pickedCharacterGridView = (GridView) view
				.findViewById(R.id.pickedCharacterGridView);

		final CharacterGridAdapter characterGridAdapter = new CharacterGridAdapter(
				getActivity(), R.layout.itemlayout_grid_picked_character,
				newGame);
		pickedCharacterGridView.setAdapter(characterGridAdapter);

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
										newGame.removeCharacter(curGameCharacter);
										characterGridAdapter
												.notifyDataSetChanged();
									}
								});
						builder.create().show();
						return true;
					}
				});

		gameName = (EditText) view.findViewById(R.id.gameName);
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

						getActivity().startActivityForResult(intent,
								PICK_FROM_CAMERA);
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

					getActivity().startActivityForResult(
							Intent.createChooser(intent,
									"Complete action using"), PICK_FROM_FILE);
				}
			}
		});

		addImageButton = (ImageButton) view.findViewById(R.id.buttonAddIcon);

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

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_templates_list, menu);
	}

}
