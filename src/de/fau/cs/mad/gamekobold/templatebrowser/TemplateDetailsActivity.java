package de.fau.cs.mad.gamekobold.templatebrowser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.template_generator.TemplateGeneratorActivity;

public class TemplateDetailsActivity extends Activity {
	private Template curTemplate;
	private CharacterGridArrayAdapter adapter;
	private GridView gridView;
	private static Activity myActivity;
	private boolean templateChanged = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_template_details);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		myActivity = this;

		List<CharacterSheet> characters = new ArrayList<CharacterSheet>();
		adapter = new CharacterGridArrayAdapter(this, characters);

		gridView = (GridView) findViewById(R.id.gridView1);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Log.d("TemplateDetailsActivity", "character grid click:"
						+ position);
				if (position != adapter.getCount() - 1) {
					Intent i = new Intent(TemplateDetailsActivity.this,
							CharacterDetailsActivity.class);
					i.putExtra("CharacterSheet", adapter.getItem(position));
					i.putExtra("templateFileName", getFileName());
					startActivity(i);
				}
			}
		});

		TemplateIcons templateIcons = TemplateIcons.getInstance();

		ImageView ivIcon = (ImageView) findViewById(R.id.icon1);
		TextView tvTempalteName = (TextView) findViewById(R.id.textView1);
		TextView tvWorldName = (TextView) findViewById(R.id.textView4);
		TextView tvInfo = (TextView) findViewById(R.id.textView3);
		// TextView tvDescription = (TextView) findViewById(R.id.textView2);
		Button backButton = (Button) findViewById(R.id.button1);
		Button editButton = (Button) findViewById(R.id.button2);
		Button infoButton = (Button) findViewById(R.id.buttonInfo);

		curTemplate = null;
		final Intent intent = getIntent();
		final Bundle extras = intent.getExtras();
		if (extras != null) {
			final Template template = (Template) extras
					.getSerializable("template");
			if (template != null) {
				curTemplate = template;
				tvTempalteName.setText(curTemplate.getTemplateName());
				tvWorldName.setText(curTemplate.getWorldName());
				tvInfo.setText("Von: " + curTemplate.getAuthor() + ", "
						+ curTemplate.getDate());

				ivIcon.setImageResource(Integer.valueOf(templateIcons
						.getTempalteIcon(curTemplate.getIconID())));

				if (curTemplate.fileAbsolutePath == null) {
					editButton.setEnabled(false);
				}
				setTitle(curTemplate.getTemplateName());
				// load all characters for this template
				CharacterListLoaderTask loadingTask = new CharacterListLoaderTask();
				loadingTask.execute(new Template[] { curTemplate });
			}
		}

		infoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// show popup with TemplateInfo
				showPopup();
			}
		});

		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goBackToBrowser();
			}
		});

		editButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (curTemplate != null) {
					if (curTemplate.fileAbsolutePath != null) {
						String fileName = getFileName();
						Intent intent = new Intent(
								TemplateDetailsActivity.this,
								TemplateGeneratorActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						// flag to distinguish between editing and creating
						intent.putExtra(
								SlideoutNavigationActivity.MODE_CREATE_NEW_TEMPLATE,
								false);
						intent.putExtra(
								SlideoutNavigationActivity.EDIT_TEMPLATE_FILE_NAME,
								fileName);
						startActivity(intent);
					}
				}
			}
		});

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	protected String getFileName(){
		int lastSlashPos = curTemplate.fileAbsolutePath
				.lastIndexOf("/");
		if (lastSlashPos == -1) {
			return curTemplate.fileAbsolutePath;
		} else {
			return curTemplate.fileAbsolutePath
					.substring(lastSlashPos + 1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.template_details, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == android.R.id.home) {
//			onBackPressed();
			goBackToBrowser();
			return true;
		} else if (id == R.id.action_delete_template) {
			final AlertDialog.Builder dialogBuilder = new Builder(this);
			dialogBuilder.setTitle(getResources().getString(
					R.string.msg_want_to_delete));
			dialogBuilder.setMessage(getResources().getString(
					R.string.msg_yes_to_delete));
			dialogBuilder.setNegativeButton(
					getResources().getString(R.string.no),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			dialogBuilder.setPositiveButton(
					getResources().getString(R.string.yes),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (curTemplate.fileAbsolutePath != null) {
								if (!curTemplate.fileAbsolutePath.isEmpty()) {
									final File file = curTemplate.getTemplateFile();
									if (file != null) {
										if (file.delete()) {
											// check if we removed the last
											// edited template
											SharedPreferences pref = getSharedPreferences(
													SlideoutNavigationActivity.SHARED_PREFERENCES_FILE_NAME,
													MODE_PRIVATE);
											String lastEditedTemplate = pref
													.getString(
															SlideoutNavigationActivity.LAST_EDITED_TEMPLATE_NAME,
															"");
											if (lastEditedTemplate.equals(file
													.getName())) {
												// if so we remove it from the
												// saved preference
												SharedPreferences.Editor editor = pref
														.edit();
												editor.remove(SlideoutNavigationActivity.LAST_EDITED_TEMPLATE_NAME);
												editor.commit();
											}
										}
									}
								}
							}
							goBackToBrowser();
						}
					});
			dialogBuilder.create().show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showPopup() {
		TemplateInfoDialogFragment popupTemplateInfoFragment = TemplateInfoDialogFragment
				.newInstance(this, curTemplate);
		popupTemplateInfoFragment.show(getFragmentManager(),
				"popupTemplateInfoFragment");

	}

	private class CharacterListLoaderTask extends
			AsyncTask<Template, Void, List<CharacterSheet>> {
		private ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(myActivity);
			pd.setTitle("Loading character list...");
			pd.setMessage("Please wait...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

		@Override
		protected List<CharacterSheet> doInBackground(Template... params) {
			List<CharacterSheet> characterList = new ArrayList<CharacterSheet>();
			// testing data

			CharacterSheet sheet = new CharacterSheet("Hodor");
			sheet.color = Color.parseColor("#9b59b6");
			characterList.add(sheet);
			sheet = new CharacterSheet("Thorodin");
			sheet.color = Color.parseColor("#e74c3c");
			characterList.add(sheet);
			sheet = new CharacterSheet("Finn");
			sheet.color = Color.parseColor("#2980b9");
			characterList.add(sheet);
			//
			File dir = de.fau.cs.mad.gamekobold.jackson.Template
					.getDirectoryForCharacters(myActivity, params[0], false);
			if (dir != null) {
				Log.d("TemplateDetails",
						"character Folder:" + dir.getAbsolutePath());
				if (dir.isDirectory()) {
					final File[] characters = dir.listFiles();
					for (final File character : characters) {
						if (character.isFile()) {
							try {
								sheet = CharacterSheet
										.loadForCharacterList(character);
								characterList.add(sheet);
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			return characterList;
		}

		@Override
		protected void onPostExecute(List<CharacterSheet> characterList) {
			// should not happen but who knows
			if (characterList == null) {
				characterList = new ArrayList<CharacterSheet>();
			}
			// entry for creating a new character
			characterList.add(new CharacterSheet("Create Character"));
			((TemplateDetailsActivity) myActivity)
					.setCharacterList(characterList);
			if (pd != null) {
				pd.dismiss();
			}
		}
	}

	public void setCharacterList(List<CharacterSheet> characterList) {
		if (characterList != null) {
			adapter.clear();
			adapter.addAll(characterList);
			adapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * Starts the TemplateBrowser Activity.
	 */
	private void goBackToBrowser() {
		Intent i = new Intent(TemplateDetailsActivity.this,
				TemplateBrowserActivity.class);
		startActivity(i);
	}

	@Override
	public void onBackPressed() {
		goBackToBrowser();
		//super.onBackPressed();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		// check if we changed the template
		if(templateChanged) {
			templateChanged = false;
			// load template from file
			final File templateFile = curTemplate.getTemplateFile();
			// check for null pointer
			if(templateFile != null) {
				de.fau.cs.mad.gamekobold.jackson.Template jacksonTemplate = null;
				//TODO maybe show a progress dialog but saving in ui thread ?!
				try {
					//load template
					jacksonTemplate = de.fau.cs.mad.gamekobold.jackson.Template.loadFromJSONFile( templateFile,
																								false);
				// take over all changes
					jacksonTemplate.takeOverValues(curTemplate);
				// save template again.
					jacksonTemplate.saveToFile(templateFile);
				}
				catch(Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static class TemplateInfoDialogFragment extends DialogFragment {
		private EditText templateInfo;
		public TemplateDetailsActivity templateDetailsActivity;
		private Template myTemplate;

		public static TemplateInfoDialogFragment newInstance(
				TemplateDetailsActivity templateDetailsActivity,
				Template template) {
			TemplateInfoDialogFragment fragment = new TemplateInfoDialogFragment();
			fragment.templateDetailsActivity = templateDetailsActivity;
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
			// get all EditTexts
			templateInfo = (EditText) view
					.findViewById(R.id.editTextAdditionalInformation);
			if (!myTemplate.getDescription().isEmpty()) {
				templateInfo.setText(myTemplate.getDescription());
			} else {
				templateInfo.setText(getActivity().getString(
						R.string.no_description_found));
			}

			builder.setView(view);

			// set Dialog characteristics
			// get right button text
			String positiveButtonText;

			positiveButtonText = getString(R.string.save_changes);

			builder.setMessage(getString(R.string.popup_template_details_info_titel));
			builder.setPositiveButton(positiveButtonText,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							if(!myTemplate.getDescription().equals(templateInfo.getEditableText().toString())) {
								myTemplate.setDescription(templateInfo.getEditableText().toString());
								templateDetailsActivity.templateChanged = true;
							}
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
					R.layout.fragment_template_details, container, false);
			return rootView;
		}
	}
}
