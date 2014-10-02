package de.fau.cs.mad.gamekobold.templatebrowser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import de.fau.cs.mad.gamekobold.AsyncTaskWithProgressDialog;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.ThumbnailLoader;
import de.fau.cs.mad.gamekobold.filebrowser.FileBrowser;
import de.fau.cs.mad.gamekobold.filebrowser.FileTargetIsSourceException;
import de.fau.cs.mad.gamekobold.filebrowser.FileWouldOverwriteException;
import de.fau.cs.mad.gamekobold.filebrowser.IFileBrowserReceiver;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.jackson.Template;
import de.fau.cs.mad.gamekobold.template_generator.TemplateGeneratorActivity;

public class CreateNewTemplateActivity extends Activity implements IFileBrowserReceiver {
	private Uri imageUri;
	private Template currentTemplate;
	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;

	private static Activity myActivity = null;
	
	private boolean editTemplate;
	private boolean templateEdited;
	
	private TextView tvTemplateName,tvGameName,tvDescription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_template);
		myActivity = this;
		// standard mode is to create a new one
		editTemplate = false;
		templateEdited = false;
		// check if we got a template for editing
		final Intent intent = getIntent();
		final Bundle extras = intent.getExtras();
		if(extras != null) {
			currentTemplate = extras.getParcelable(Template.PARCELABLE_STRING);
			if(currentTemplate != null) {
				// set mode
				editTemplate = true;
			}
		}
		if(editTemplate == false) {
			// creation mode so create a new one
			currentTemplate = new Template();
		}

		final ImageButton addImageButton = (ImageButton) findViewById(R.id.imageButtonTemplateIcon);
		tvTemplateName = (TextView) findViewById(R.id.templateName);
		tvGameName = (TextView) findViewById(R.id.worldName);
		tvDescription = (TextView) findViewById(R.id.description);
		final Button createTemplateButton = (Button) findViewById(R.id.createTemplate);
		final Button infoButton = (Button) findViewById(R.id.buttonInfo);

		// set values from template and change button text
		if(editTemplate) {
			tvTemplateName.setText(currentTemplate.getTemplateName());
			tvGameName.setText(currentTemplate.getGameName());
			tvDescription.setText(currentTemplate.getDescription());
			createTemplateButton.setText(R.string.edit_template);
			final Bitmap icon = ThumbnailLoader.loadThumbnail(currentTemplate.getIconPath(), this);
			if(icon != null) {
				addImageButton.setImageBitmap(icon);
			}
		}

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
				// show popup with TemplateTags
				showPopup();
			}
		});

		createTemplateButton.setOnClickListener(new OnClickListener() {
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View v) {
				// check if the template name is empty
				// prevent templates with no name
				if (tvTemplateName.getEditableText().toString().equals("")) {
					Toast.makeText(
							getApplicationContext(),
							getResources().getString(R.string.warning_set_name),
							Toast.LENGTH_SHORT).show();
					return;
				}
				// it goes template generator
				final Intent intent = new Intent(
						CreateNewTemplateActivity.this,
						TemplateGeneratorActivity.class);

				/*
				 * JACKSON START
				 */
				// set the right extras for the intent according to current mode 
				if(editTemplate) {
					intent.putExtra(SlideoutNavigationActivity.EXTRA_MODE, SlideoutNavigationActivity.MODE_EDIT_TEMPLATE);
					intent.putExtra(SlideoutNavigationActivity.EXTRA_TEMPLATE_FILE_NAME,
							currentTemplate.getFileName());
				}
				else {
					intent.putExtra(SlideoutNavigationActivity.EXTRA_MODE, SlideoutNavigationActivity.MODE_CREATE_TEMPLATE);
				}

				if(!editTemplate) {
					// set data
					currentTemplate.setTemplateName(tvTemplateName.getText().toString());
					currentTemplate.setGameName(tvGameName.getText().toString());
					// TODO author
					currentTemplate.setAuthor("Registered Author");
					currentTemplate.setDate(new SimpleDateFormat("dd.MM.yyyy")
							.format(new Date()));
					currentTemplate.setDescription(tvDescription.getText().toString());
					// check to see if a file for this template already exists
					if (JacksonInterface.doesTemplateFileExist(currentTemplate, myActivity)) {
						// if yes we show a dialog and ask whether to overwrite the
						// file or not.
						final AlertDialog.Builder builder = new AlertDialog.Builder(
							myActivity);
						builder.setTitle(getResources().getString(
							R.string.msg_template_file_already_exists));
						builder.setMessage(getResources().getString(
							R.string.msg_yes_to_overwrite_no_to_edit));
						builder.setNegativeButton(
							getResources().getString(R.string.no),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								}
							});
						builder.setPositiveButton(
							getResources().getString(R.string.yes),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									intent.putExtra(
											Template.PARCELABLE_STRING,
											currentTemplate);
									// show some info toast
									Toast.makeText(getApplicationContext(),
											getString(R.string.toast_create_template_now), Toast.LENGTH_SHORT)
											.show();	
									leaveActivityAndSaveTemplateIfNeeded(intent);
								}
							});
						AlertDialog dialog = builder.create();
						dialog.show();
						return;
					}
				}
				intent.putExtra(
						Template.PARCELABLE_STRING,
						currentTemplate);
				// show some info toast
				if(editTemplate) {
					Toast.makeText(getApplicationContext(),
					getString(R.string.toast_edit_template_now), Toast.LENGTH_SHORT)
					.show();
				}
				else {
					Toast.makeText(getApplicationContext(),
					getString(R.string.toast_create_template_now), Toast.LENGTH_SHORT)
					.show();					
				}
				
				leaveActivityAndSaveTemplateIfNeeded(intent);
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
				bitmap = ThumbnailLoader.loadThumbnail(path, this);

		} else {
			// If user choose to take picture from camera, get the real path of
			// temporary file
			path = imageUri.getPath();
			bitmap = ThumbnailLoader.loadThumbnail(path, this);
		}

		final ImageButton addImageButton = (ImageButton) findViewById(R.id.imageButtonTemplateIcon);
		addImageButton.setImageBitmap(bitmap);
		// set icon path
		currentTemplate.setIconPath(path);
		templateEdited = true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if(editTemplate) {
			// menu while editing
			getMenuInflater().inflate(R.menu.menu_create_template_edit_mode, menu);	
		}
//		else {
//			// menu while creating
//			getMenuInflater().inflate(R.menu.create_new_template, menu);	
//		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(editTemplate) {
			// editing mode
			if(id == R.id.menu_item_export_template_to_file) {
				showFileExplorerPopup();
				return true;
			}
		}
//		else {
//			// creation mode
//			if (id == R.id.action_settings) {
//				return true;
//			}			
//		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		leaveActivityAndSaveTemplateIfNeeded(null);
	}
	
	private void showFileExplorerPopup() {
		FileBrowser.showAsPopup(getFragmentManager(), FileBrowser.newInstance(this, FileBrowser.Mode.PICK_DIRECTORY));
		Toast.makeText(this, getString(R.string.toast_fileexplorer_msg_pick_folder), Toast.LENGTH_LONG).show();
	}
	
	private void showPopup() {
		TemplateTagsDialogFragment popupTemplateTagsFragment = TemplateTagsDialogFragment
				.newInstance(this, currentTemplate);
		popupTemplateTagsFragment.show(getFragmentManager(),
				"popupTemplateTagsFragment");
	}

	private static class TemplateTagsDialogFragment extends DialogFragment {
		public CreateNewTemplateActivity createNewTemplateActivity;
		private Template myTemplate;

		public static TemplateTagsDialogFragment newInstance(
				CreateNewTemplateActivity createNewTemplateActivity,
				Template template) {
			TemplateTagsDialogFragment fragment = new TemplateTagsDialogFragment();
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
			View view = inflater.inflate(R.layout.popup_template_tag_info, null);

			builder.setView(view);

			// set Dialog characteristics
			// get right button text
			final String positiveButtonText = getString(R.string.save_changes);
			final EditText editText = (EditText)view.findViewById(R.id.editTextAdditionalInformation);
			editText.setText(myTemplate.getTagString());

			builder.setMessage(getString(R.string.popup_template_details_tags_titel));
			builder.setPositiveButton(positiveButtonText,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							String newTagString = editText.getEditableText().toString();
							if(!myTemplate.getTagString().equals(newTagString)) {
								myTemplate.setTagString(newTagString);
								createNewTemplateActivity.templateEdited = true;
							}	
						}
					});

			builder.setNegativeButton(getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog
							TemplateTagsDialogFragment.this.getDialog()
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

	@Override
	public void onFilePicked(File directory) {
		Log.d("CreateNewTemplateActivity", "export:"+directory.getAbsolutePath());
		FileBrowser.removeAsPopup(getFragmentManager());
		// copy file
		final File templateFile = new File(currentTemplate.getFileAbsPath());
		final File targetFile = new File(directory, templateFile.getName());
		try {
			JacksonInterface.exportTemplate(CreateNewTemplateActivity.this,templateFile, targetFile, false);
		}
		catch (FileTargetIsSourceException e) {
			// should not occur
			e.printStackTrace();
		}
		catch(FileWouldOverwriteException e) {
			e.printStackTrace();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.question_overwrite_file).setTitle(R.string.msg_file_with_same_name_already_exists);
			builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// do nothing
					dialog.dismiss();
				}
			});
			builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					// overwrite file
					try {
						JacksonInterface.exportTemplate(CreateNewTemplateActivity.this, templateFile, targetFile, true);						
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			});
			builder.create().show();
			// show dialog
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Calls startAcitivty if the provided intent is not null. Otherwise calls super.onBackPressed().
	 * Before this Activity is left, a check will be performed to determine if the template has changed.
	 * If we are editing a Template and it has changed it will be saved before leaving. 
	 * @param intentToStart intent to use for startAcitivity or null to call super.onBackPressed().
	 */
	private void leaveActivityAndSaveTemplateIfNeeded(Intent intentToStart) {
		checkAndTakeOverChanges();
		if(editTemplate && templateEdited) {			
				SaveEditetTemplateTask saveTask = new SaveEditetTemplateTask();
				saveTask.intentToStart = intentToStart;
				saveTask.execute();
		}
		else {
			if(intentToStart != null) {
				startActivity(intentToStart);
			}
			else {
				superOnBackPressed();
			}
		}
	}
	
	/**
	 * Saves the changes made to the currentTemplate to file system.
	 * If intentToStart is set (!=null), startAcitivity(intentToStart) will be called after saving.
	 * If intentToStart is not set (=null), superOnBackPressed() will be called after saving.
	 * After execution we will leave this activity!
	 */
	private class SaveEditetTemplateTask extends AsyncTaskWithProgressDialog<Void, Void, Void> {
		public Intent intentToStart = null;

		@Override
		public void onPreExecute() {
			super.onPreExecute(CreateNewTemplateActivity.this, "Please wait", "saving changes");
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				File templateFile = new File (currentTemplate.getFileAbsPath());
				if(!templateFile.isFile()) {
					return null;
				}
				Template loadedTemplate = JacksonInterface.loadTemplate(templateFile, false);
				loadedTemplate.setDescription(currentTemplate.getDescription());
				loadedTemplate.setTagString(currentTemplate.getTagString());
				loadedTemplate.setTemplateName(currentTemplate.getTemplateName());
				loadedTemplate.setIconPath(currentTemplate.getIconPath());
				loadedTemplate.setGameName(currentTemplate.getGameName());
				JacksonInterface.saveTemplate(loadedTemplate, templateFile);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		public void onPostExecute(Void param) {
			templateEdited = false;
			super.onPostExecute(param);
			if(intentToStart != null) {
				startActivity(intentToStart);
			}
			else {
				(CreateNewTemplateActivity.this).superOnBackPressed();
			}
		}
	}

	/**
	 * Calls super.onBackPressed();
	 */
	private void superOnBackPressed() {
		super.onBackPressed();
	}
	
	/**
	 * Checks if the template infos have changed. Also takes over any changes to currentTemplate.
	 * @return true if something has changed, false otherwise.
	 */
	private boolean checkAndTakeOverChanges() {
		// check template name
		if(!tvTemplateName.getEditableText().toString().equals(currentTemplate.getTemplateName())) {
			templateEdited = true;
			currentTemplate.setTemplateName(tvTemplateName.getEditableText().toString());
		}
		// check world name
		if(!tvGameName.getEditableText().toString().equals(currentTemplate.getGameName())) {
			templateEdited = true;
			currentTemplate.setGameName(tvGameName.getEditableText().toString());
		}
		// check description
		if(!tvDescription.getEditableText().toString().equals(currentTemplate.getDescription())) {
			templateEdited = true;
			currentTemplate.setDescription(tvDescription.getEditableText().toString());
		}
		// icon is handled by onActivityResult
		// tag list is handled by popup
		return templateEdited;
	}
}
