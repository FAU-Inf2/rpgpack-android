package de.fau.cs.mad.gamekobold.templatebrowser;

import java.io.File;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.colorpicker.ColorPickerDialog;
import de.fau.cs.mad.gamekobold.colorpicker.ColorPickerDialogInterface;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CharacterDetailsActivity extends Activity implements ColorPickerDialogInterface{
	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;
	
	private RelativeLayout relLayout;
	private CharacterSheet sheet;
	private Uri iconUri;
	private ImageButton characterIconButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_character_details);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		relLayout = (RelativeLayout)findViewById(R.id.relativeLayout1);
		final EditText description = (EditText)findViewById(R.id.editText1);
		final Button colorChangeButton = (Button)findViewById(R.id.button2);
		final TextView characterName = (TextView)findViewById(R.id.textView1);
		final TextView levelLabel = (TextView)findViewById(R.id.textView3);
		characterIconButton = (ImageButton)findViewById(R.id.imageButton1);
		
		characterIconButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(CharacterDetailsActivity.this,
						android.R.layout.select_dialog_item, getResources()
								.getStringArray(R.array.image_picker_items));
				AlertDialog.Builder builder = new AlertDialog.Builder(CharacterDetailsActivity.this);

				builder.setTitle(getResources().getString(R.string.add_icon));
				builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						if (item == 0) {
							// create an intent to open Camera app
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							// TODO use tmp file functions?
							// TODO save file in app folder
							// create temporary file to hold the image from Camera.
							File file = new File(Environment
									.getExternalStorageDirectory(), "tmp_avatar_"
									+ String.valueOf(System.currentTimeMillis())
									+ ".jpg");
							iconUri = Uri.fromFile(file);

							try {
								intent.putExtra(
										android.provider.MediaStore.EXTRA_OUTPUT,
										iconUri);
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
				dialog.show();
			}
		});

		colorChangeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Create and show the dialog.
				final ColorPickerDialog dialog = ColorPickerDialog.newInstance(colorChangeButton);
				dialog.show(getFragmentManager(), "ColorPickerDialog");
			}
		});

		final Intent intent = getIntent();
		final Bundle extras = intent.getExtras();		
		if (extras != null) {
			sheet = (CharacterSheet)extras.getParcelable("CharacterSheet");
			// remove next line later when we got a character
			if(sheet != null) {
				setTitle(sheet.name);
				characterName.setText(sheet.name);
				levelLabel.setText(String.valueOf(sheet.level));
				description.setTag(sheet.description);
				// set to character color
				relLayout.setBackgroundColor(sheet.color);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		/*if (id == R.id.action_settings) {
			return true;
		}*/
		if(id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// callback for ColorPickerDialog
	@Override
	public void onColorPicked(int color) {
		Log.d("CharacterDetails", "picked color:"+color);
		setCharacterColor(color);
	}

	private void setCharacterColor(int color) {
		relLayout.setBackgroundColor(color);
		if(sheet != null) {
			sheet.color = color;
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

		}
		else if(requestCode == PICK_FROM_CAMERA){
			// If user choose to take picture from camera, get the real path of
			// temporary file
			//path = iconUri.getPath();
			//bitmap = BitmapFactory.decodeFile(path);
			// gets the thumbnail
			final Bundle extras = data.getExtras();
			bitmap = (Bitmap)extras.get("data");
		}
		if(bitmap != null) {
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
