package de.fau.cs.mad.gamekobold.templatebrowser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.character.CharacterEditActivity;
import de.fau.cs.mad.gamekobold.colorpicker.ColorPickerDialog;
import de.fau.cs.mad.gamekobold.colorpicker.ColorPickerDialogInterface;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.jackson.Template;

public class CreateNewCharacterActivity extends Activity implements
		ColorPickerDialogInterface {
	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;

	private RelativeLayout relLayout;
	private CharacterSheet sheet;
	private Uri iconUri;
	private ImageButton characterIconButton;
	private String templateFileName;
	private boolean characterAltered;
	private File characterDirectoryFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_create_new_character);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		sheet = null;
		characterAltered = false;
		characterDirectoryFile = null;

		relLayout = (RelativeLayout) findViewById(R.id.relativeLayout1);
		final EditText description = (EditText) findViewById(R.id.editText1);
		final Button colorChangeButton = (Button) findViewById(R.id.button2);
		final EditText characterName = (EditText) findViewById(R.id.character_name_edittext);
		final EditText characterLevel = (EditText) findViewById(R.id.character_level_edittext);
		characterIconButton = (ImageButton) findViewById(R.id.imageButton1);

		final Intent intent = getIntent();
		final Bundle extras = intent.getExtras();
		if (extras != null) {
			templateFileName = (String) extras.getString("templateFileName");
			// create new sheet
			try {
				final Template template = JacksonInterface.loadTemplate(this,
						templateFileName, false);
				characterDirectoryFile = JacksonInterface
						.getDirectoryForCharacters(template, this, true);
				sheet = template.characterSheet;
			} catch (Throwable e) {
				e.printStackTrace();
				// TODO correct error handling
			}
		}

		characterIconButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						CreateNewCharacterActivity.this,
						android.R.layout.select_dialog_item, getResources()
								.getStringArray(R.array.image_picker_items));
				AlertDialog.Builder builder = new AlertDialog.Builder(
						CreateNewCharacterActivity.this);

				builder.setTitle(getResources().getString(R.string.add_icon));
				builder.setAdapter(adapter,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								if (item == 0) {
									// create an intent to open Camera app
									Intent intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									// TODO use tmp file functions?
									// TODO save file in app folder
									// create temporary file to hold the image
									// from Camera.
									File file = new File(
											Environment
													.getExternalStorageDirectory(),
											"tmp_avatar_"
													+ String.valueOf(System
															.currentTimeMillis())
													+ ".jpg");
									iconUri = Uri.fromFile(file);

									try {
										intent.putExtra(
												android.provider.MediaStore.EXTRA_OUTPUT,
												iconUri);
										intent.putExtra("return-data", true);

										startActivityForResult(intent,
												PICK_FROM_CAMERA);
									} catch (Exception e) {
										e.printStackTrace();
									}

									dialog.cancel();
								} else {
									// if user choose to select image from
									// sdcard, start the
									// intent to open image chooser dialog.
									// the image chooser dialog will display
									// list File Manager
									// (if exist) apps and default Gallery app.
									Intent intent = new Intent();

									intent.setType("image/*");
									intent.setAction(Intent.ACTION_GET_CONTENT);

									startActivityForResult(Intent
											.createChooser(intent,
													"Complete action using"),
											PICK_FROM_FILE);
								}
							}
						});

				final AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		colorChangeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Create and show the dialog.
				final ColorPickerDialog dialog = ColorPickerDialog
						.newInstance(colorChangeButton);
				dialog.show(getFragmentManager(), "ColorPickerDialog");
			}
		});

		description.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (sheet != null) {
					if (!sheet.description.equals(s.toString())) {
						sheet.description = s.toString();
						characterAltered = true;
					}
				}
			}
		});

		characterName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (sheet != null) {
					sheet.name = s.toString();
					setTitle(s.toString());
					characterAltered = true;
				}
			}
		});

		characterLevel.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (sheet != null) {
					try {
						sheet.level = Integer.parseInt(s.toString());
						characterAltered = true;
					} catch (NumberFormatException e) {
						e.printStackTrace();
						sheet.level = 0;
					}
				}
			}
		});

		final Button characterEditButton = (Button) findViewById(R.id.button1);
		characterEditButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (characterName.getEditableText().toString().isEmpty()) {
					Toast.makeText(CreateNewCharacterActivity.this,
							getString(R.string.warning_set_character_name),
							Toast.LENGTH_LONG).show();
					return;
				}
				// save sheet because we need the file path
				// we will not double save becaus of alteration flag
				saveCharacterSheet();
				Intent intent = new Intent(CreateNewCharacterActivity.this,
						CharacterEditActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				// set flag so we do not use template mode
				intent.putExtra(SlideoutNavigationActivity.MODE_TEMPLATE, false);

				intent.putExtra(CharacterEditActivity.EXTRA_CHARACTER_ABS_PATH,
						sheet.fileAbsolutePath);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onPause() {
		saveCharacterSheet();
		super.onPause();
	}

	private void saveCharacterSheet() {
		// check if character has been altered
		Log.d("Trying to save sheet", "sheet:" + sheet);
		Log.d("Trying to save sheet", "altered:" + characterAltered);
		if (sheet != null && characterAltered) {
			if (!sheet.name.isEmpty()) {
				Log.d("Trying to save sheet", "name not empty!");
				if (sheet.fileAbsolutePath.isEmpty()) {
					Log.d("Trying to save sheet", "path is empty");
					// create file
					if (characterDirectoryFile != null) {
						Log.d("Trying to save sheet", "dir not null");
						String fileName = JacksonInterface
								.getSanitizedFileName(sheet.name);
						if (fileName.isEmpty()) {
							SimpleDateFormat format = new SimpleDateFormat(
									"yyyy-MM-dd--HH-mm-ss");
							Date date = new Date();
							fileName = format.format(date);
						}
						sheet.fileAbsolutePath = characterDirectoryFile
								.getAbsolutePath()
								+ File.separatorChar
								+ fileName;
					}
				}
				// save
				try {
					Log.d("Trying to save sheet", "path:"
							+ sheet.fileAbsolutePath);
					// open file
					final File jsonFile = new File(sheet.fileAbsolutePath);
					JacksonInterface.saveCharacterSheet(sheet, jsonFile);
					// clear flag
					characterAltered = false;
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		/*
		 * if (id == R.id.action_settings) { return true; }
		 */
		if (id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// callback for ColorPickerDialog
	@Override
	public void onColorPicked(int color) {
		Log.d("CharacterDetails", "picked color:" + color);
		setCharacterColor(color);
	}

	private void setCharacterColor(int color) {
		relLayout.setBackgroundColor(color);
		if (sheet != null) {
			if (sheet.color != color) {
				sheet.color = color;
				characterAltered = true;
			}
		}
	}

	@Override
	// to handle the selected image
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;

		Bitmap bitmap = null;
		String path = "";

		if (requestCode == PICK_FROM_FILE) {
			// get the uri of selected image
			iconUri = data.getData();

			// Assume user selects the image from sdcard using Gallery app. The
			// uri from Gallery app does not give the real path to selected
			// image, so it has to be resolved on content provider. Method
			// getRealPathFromURI used to resolve the real path from the uri.
			path = getRealPathFromURI(iconUri); // from Gallery

			// If the path is null, assume user selects the image using File
			// Manager app. File Manager app returns different information than
			// Gallery app. To get the real path to selected image, use
			// getImagePath method from the uri
			if (path == null)
				path = iconUri.getPath(); // from File Manager

			if (path != null)
				bitmap = BitmapFactory.decodeFile(path);

		} else if (requestCode == PICK_FROM_CAMERA) {
			// If user choose to take picture from camera, get the real path of
			// temporary file
			// path = iconUri.getPath();
			// bitmap = BitmapFactory.decodeFile(path);
			// gets the thumbnail
			final Bundle extras = data.getExtras();
			bitmap = (Bitmap) extras.get("data");
		}
		if (bitmap != null) {
			characterIconButton.setImageBitmap(bitmap);
		}
		// TODO store image path for later use
	}

	// TODO refactoring?
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null,
				null, null);
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
