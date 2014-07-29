package de.fau.cs.mad.gamekobold.templatebrowser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.AlertDialog.Builder;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.jackson.Template;
import de.fau.cs.mad.gamekobold.slidingmenu.TemplateGeneratorWelcomeActivity;
import de.fau.cs.mad.gamekobold.template_generator.TemplateGeneratorActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.TemplateDetailsActivity.TemplateInfoDialogFragment;

public class CreateNewTemplateActivity extends Activity {
	private Uri imageUri;
	private Template newTemplate;
	private EditText edDescription;
	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;

	private static Activity myActivity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_template);
		myActivity = this;
		newTemplate = new Template();

		ImageButton addImageButton = (ImageButton) findViewById(R.id.imageButtonTemplateIcon);
		edDescription = (EditText)findViewById(R.id.description);
		final TextView tvTemplateName = (TextView) findViewById(R.id.templateName);
		final TextView tvGameName = (TextView) findViewById(R.id.worldName);
		final TextView tvDescription = (TextView) findViewById(R.id.description);
		Button createTemplateButton = (Button) findViewById(R.id.createTemplate);
		Button infoButton = (Button) findViewById(R.id.buttonInfo);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, getResources()
						.getStringArray(R.array.image_picker_items));
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
				Toast.makeText(getApplicationContext(),
						"You want to pick a picture!", Toast.LENGTH_SHORT)
						.show();
				dialog.show();
			}
		});

		infoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// show popup with TemplateInfo
				showPopup();
			}
		});

		createTemplateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tvTemplateName.getEditableText().toString().equals("")) {
					Toast.makeText(
							getApplicationContext(),
							getResources().getString(R.string.warning_set_name),
							Toast.LENGTH_SHORT).show();
					return;
				}
				// add new template to list on template browser-view
				Toast.makeText(getApplicationContext(),
						"Dein Template wird erstellt!", Toast.LENGTH_SHORT)
						.show();

				// String currentDate = new SimpleDateFormat("dd.MM.yyyy")
				// .format(new Date());
				//
				// // TODO Have to store Tempalte-data!
				// // TODO What is the template author's name?
				// // now it is just stub: "Registered Author"
				// // TODO 0 stands for default icon id
				// Template newTemplate = new Template(tvTempalteName.getText()
				// .toString(), tvGameName.getText().toString(),
				// "Registered Author", currentDate, 0, tvDescription
				// .getText().toString());
				//
				// Intent i = new Intent(CreateNewTemplateActivity.this,
				// TemplateBrowserActivity.class);
				//
				// // he currently running instance of activity B in the above
				// // example will either receive the new intent you are
				// starting
				// // here in its onNewIntent() method, or be itself finished
				// and
				// // restarted with the new intent. If it has declared its
				// launch
				// // mode to be "multiple" (the default) and you have not set
				// // FLAG_ACTIVITY_SINGLE_TOP in the same intent, then it will
				// be
				// // finished and re-created; for all other launch modes or if
				// // FLAG_ACTIVITY_SINGLE_TOP is set then this Intent will be
				// // delivered to the current instance's onNewIntent().
				// i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				//
				// // TODO we have to save newTempalte!! or it will be lost!
				// // (detailsView->Browser... got lost)
				// i.putExtra("newtemplate", newTemplate);
				// startActivity(i);

				// it goes template generator
				final Intent intent = new Intent(
						CreateNewTemplateActivity.this,
						TemplateGeneratorActivity.class);

				// it goes to welcome activity
				// Intent intent = new Intent(CreateNewTemplateActivity.this,
				// TemplateGeneratorWelcomeActivity.class);
				// intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

				/*
				 * JACKSON START
				 */
				// flag to distinguish between editing and creating
				intent.putExtra(
						TemplateGeneratorActivity.MODE_CREATE_NEW_TEMPLATE,
						true);
				// create template for data transfer
		//		final de.fau.cs.mad.gamekobold.jackson.Template jTemplate = new Template();
				// set data
//				jTemplate.templateName = tvTemplateName.getText().toString();
//				jTemplate.gameName = tvGameName.getText().toString();
				newTemplate.templateName = tvTemplateName.getText().toString();
				newTemplate.gameName = tvGameName.getText().toString();
				// TODO author
//				jTemplate.author = "Registered Author";
//				jTemplate.date = new SimpleDateFormat("dd.MM.yyyy")
				newTemplate.author = "Registered Author";
				newTemplate.date = new SimpleDateFormat("dd.MM.yyyy")
						.format(new Date());
				// TODO icon id
//				jTemplate.iconID = 0;
//				jTemplate.description = tvDescription.getText().toString();
				newTemplate.iconID = 0;
				newTemplate.description = tvDescription.getText().toString();
				// check to see if a file for this template already exists
				if (newTemplate.doesTemplateFileExist(myActivity)) {
					// if yes we show a dialog and ask whether to overwrite the
					// file or not.
					// Log.d("CreateNewTemplateActivity",
					// "File already exists!");
					final AlertDialog.Builder builder = new AlertDialog.Builder(
							myActivity);
					builder.setTitle(getResources().getString(
							R.string.msg_file_already_exists));
					builder.setMessage(getResources().getString(
							R.string.msg_yes_to_overwrite));
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
									intent.putExtra(
											de.fau.cs.mad.gamekobold.jackson.Template.PARCELABLE_STRING,
											newTemplate);
									startActivity(intent);
								}
							});
					AlertDialog dialog = builder.create();
					dialog.show();
				} else {
					// if the file does not exists continue
					// Log.d("CreateNewTemplateActivity",
					// "File does not exist.");
					intent.putExtra(
							de.fau.cs.mad.gamekobold.jackson.Template.PARCELABLE_STRING,
							newTemplate);
					startActivity(intent);
				}
				/*
				 * JACKSON END
				 */
			}
		});

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
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
			imageUri = data.getData();

			path = getRealPathFromURI(imageUri); // from Gallery

			if (path == null)
				path = imageUri.getPath(); // from File Manager

			if (path != null)
				bitmap = BitmapFactory.decodeFile(path);

		} else {
			// If user choose to take picture from camera, get the real path of
			// temporary file
			path = imageUri.getPath();
			bitmap = BitmapFactory.decodeFile(path);

		}

		final ImageButton addImageButton = (ImageButton) findViewById(R.id.imageButtonTemplateIcon);
		addImageButton.setImageBitmap(bitmap);
		// TODO store image path for later use
		// TemplateIcons.getInstance().addTemplateIcon(path);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_new_template, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showPopup() {
		TemplateInfoDialogFragment popupTemplateInfoFragment = TemplateInfoDialogFragment
				.newInstance(this, newTemplate);
		popupTemplateInfoFragment.show(getFragmentManager(),
				"popupTemplateInfoFragment");
	}

	public static class TemplateInfoDialogFragment extends DialogFragment {
		public CreateNewTemplateActivity createNewTemplateActivity;
		private Template myTemplate;

		public static TemplateInfoDialogFragment newInstance(
				CreateNewTemplateActivity createNewTemplateActivity,
				Template template) {
			TemplateInfoDialogFragment fragment = new TemplateInfoDialogFragment();
			fragment.createNewTemplateActivity = createNewTemplateActivity;
			fragment.myTemplate = template;
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
			View view = inflater.inflate(R.layout.popup_template_details_info,
					null);

			builder.setView(view);

			// set Dialog characteristics
			// get right button text
			String positiveButtonText;

			positiveButtonText = getString(R.string.save_changes);
			final EditText editText = (EditText)view.findViewById(R.id.editTextAdditionalInformation);
			editText.setText(myTemplate.description);

			builder.setMessage(getString(R.string.popup_template_details_info_titel));
			builder.setPositiveButton(positiveButtonText,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							myTemplate.description = editText.getEditableText().toString();
							createNewTemplateActivity.edDescription.setText(editText.getEditableText().toString());
						}
					});

			builder.setNegativeButton(getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog
							TemplateInfoDialogFragment.this.getDialog()
									.cancel();
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_create_new_template, container, false);
			return rootView;
		}
	}

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
