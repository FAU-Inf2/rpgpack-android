package de.fau.cs.mad.gamekobold.templatebrowser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import de.fau.cs.mad.gamekobold.AsyncTaskWithProgressDialog;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.template_generator.TemplateGeneratorActivity;

public class TemplateBrowserActivity extends ListActivity {
	public static final String CREATE_CHAR_DIRECT = "CREATE_CHAR_DIRECT";
	
	private List<Template> templateList = null;
	// time stamp of the template directory
	// with this we can determine if a template has been deleted / created
	private long templateFolderTimeStamp = 0;
	// flag to distinguish if we are picking a template for character creation
	private boolean mode_pickTemplateForCharacterCreation = false;

	public TemplateBrowserActivity() {
	}

	// this constructor is only used for TemplateListActivity
	public TemplateBrowserActivity(Boolean mode_pickTemplateForCharacterCreation) {
		this.mode_pickTemplateForCharacterCreation = mode_pickTemplateForCharacterCreation;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_template_browser);

		// check if we came here from character browser
		// and want to create a character from this template directly
		final Intent intent = getIntent();
		final Bundle extras = intent.getExtras();
		if (extras != null) {
			mode_pickTemplateForCharacterCreation = extras.getBoolean(CREATE_CHAR_DIRECT, false);
		}

		Log.e("d", "On create!!!");
		if (templateList == null) {
			templateList = new ArrayList<Template>();
		}
		// have to make it final because of adapter.getCount()-method for
		// newTemplate-intent
		final TemplateBrowserArrayAdapter adapter = new TemplateBrowserArrayAdapter(
				this, templateList);
		adapter.setModeOnlyTemplates(mode_pickTemplateForCharacterCreation);
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// if we are only picking a tempalte for character creation
				if (mode_pickTemplateForCharacterCreation) {
					Intent i = new Intent(TemplateBrowserActivity.this,
							CreateNewCharacterActivity.class);
					i.putExtra("templateFileName", templateList.get(position).getFileName());
					startActivity(i);
					return;
				}
				// normal browser mode
				// is it last row?
				// JACKSON start : for editing last created template
				if (position == adapter.getCount() - 1) {
					// get shared preferences
					SharedPreferences pref = getSharedPreferences(
							SlideoutNavigationActivity.SHARED_PREFERENCES_FILE_NAME,
							Activity.MODE_PRIVATE);
					// get last edited template file name
					String templateFileName = pref
							.getString(
									SlideoutNavigationActivity.PREFERENCE_LAST_EDITED_TEMPLATE_NAME,
									"");
					// check for existence
					if (templateFileName.equals("")) {
						Toast.makeText(TemplateBrowserActivity.this, getString(R.string.toast_no_last_edited_tempalte),
								Toast.LENGTH_LONG).show();
					} else {
						startEditingOfTemplate(templateFileName);
					}
				}
				// JACKSON end
				else {
					Intent i = new Intent(TemplateBrowserActivity.this,
							CreateNewTemplateActivity.class);
					Log.e("er", "position: " + position);
					try {
						final de.fau.cs.mad.gamekobold.jackson.Template jacksonTemplate = 
							JacksonInterface.loadTemplate(new File(templateList.get(position).fileAbsolutePath), true);
						i.putExtra(de.fau.cs.mad.gamekobold.jackson.Template.PARCELABLE_STRING, jacksonTemplate);
						startActivity(i);
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		});

		// set onItemClickListener. if the user long clicks on an item we show a
		// dialog
		// the user then gets the option to delete the template
		getListView().setOnItemLongClickListener(
				new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int pos, long id) {
						final Template longClickedTemplate = templateList
								.get(pos);

						Log.d("TemplateBrowser", "longClickOn:"
								+ longClickedTemplate.fileAbsolutePath);

						if (longClickedTemplate.fileAbsolutePath != null) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									TemplateBrowserActivity.this);
							builder.setTitle(getResources().getString(
									R.string.msg_want_to_delete));
							builder.setMessage(getResources().getString(
									R.string.msg_yes_to_delete));
							builder.setNegativeButton(
									getResources().getString(R.string.no),

									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									});
							builder.setPositiveButton(
									getResources().getString(R.string.yes),
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											File file = longClickedTemplate
													.getTemplateFile();
											if (file != null) {
												Log.d("TempalteBrowser",
														"delete template:"
																+ longClickedTemplate);
												// delete characters
												final File characterDir = JacksonInterface
														.getDirectoryForCharacters(
																longClickedTemplate,
																TemplateBrowserActivity.this,
																false);
												if (characterDir != null) {
													// delete characters
													final File[] characterFiles = characterDir
															.listFiles();
													for (final File character : characterFiles) {
														character.delete();
													}
													// delete dircetory
													characterDir.delete();
												}
												if (file.delete()) {
													// check if we removed the
													// last edited template
													SharedPreferences pref = getSharedPreferences(
															SlideoutNavigationActivity.SHARED_PREFERENCES_FILE_NAME,
															MODE_PRIVATE);
													String lastEditedTemplate = pref
															.getString(
																	SlideoutNavigationActivity.PREFERENCE_LAST_EDITED_TEMPLATE_NAME,
																	"");
													if (lastEditedTemplate
															.equals(file
																	.getName())) {
														// if so we remove it
														// from the saved
														// preference
														SharedPreferences.Editor editor = pref
																.edit();
														editor.remove(SlideoutNavigationActivity.PREFERENCE_LAST_EDITED_TEMPLATE_NAME);
														editor.commit();
													}
													removeItem(longClickedTemplate);
												}
											}
										}
									});
							builder.create().show();
						}
						return true;
					}
				});

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("d", "On resume!!!");
		TemplateBrowserArrayAdapter adapter = (TemplateBrowserArrayAdapter) getListAdapter();
		loadTemplateList(this, adapter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.e("d", "On pause!!!");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.e("d", "On stop!!!");
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.template_browser, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}		
//		return super.onOptionsItemSelected(item);
//	}

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
					R.layout.fragment_template_browser, container, false);
			return rootView;
		}
	}

	private void removeItem(Template template) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<Template> adapter = (ArrayAdapter<Template>) getListAdapter();
		adapter.remove(template);
		adapter.notifyDataSetChanged();
	}

	private void startEditingOfTemplate(String fileName) {
		if (fileName.equals("")) {
			return;
		}
		Intent intent = new Intent(TemplateBrowserActivity.this,
				TemplateGeneratorActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		// flag to distinguish between editing and creating
		intent.putExtra(SlideoutNavigationActivity.EXTRA_MODE, SlideoutNavigationActivity.MODE_EDIT_TEMPLATE);
		intent.putExtra(SlideoutNavigationActivity.EXTRA_TEMPLATE_FILE_NAME, fileName);
		startActivity(intent);
	}

	/**
	 * Loads and updates the list of available templates.
	 */
	public void loadTemplateList(Context context, ArrayAdapter adapter) {
		if (!checkForTemplateDirectoryChange(context, adapter)) {
			// we only check for single template changes if we are not
			// reloading the whole list
			checkForTemplateChanges(context);
		}
	}

	/**
	 * Creates and executes a new {@link TemplateListUpdaterTask}.
	 */
	public void checkForTemplateChanges(Context context) {
		TemplateListUpdaterTask updaterTask = new TemplateListUpdaterTask(
				context);
		updaterTask.execute();
	}

	/**
	 * Checks the template directory for a change. Its time stamp is updated
	 * when a file is created or deleted.
	 * 
	 * @return true if the template list will be reloaded, false otherwise.
	 */
	public boolean checkForTemplateDirectoryChange(Context context,
			ArrayAdapter adapter) {
		final File templateDir = JacksonInterface
				.getTemplateRootDirectory(context);
		final long newTimeStamp = templateDir.lastModified();
		if (templateFolderTimeStamp < newTimeStamp) {
			templateFolderTimeStamp = newTimeStamp;
			// reload template list
			TemplateListLoaderTask loaderTask = new TemplateListLoaderTask(
					context, adapter);
			loaderTask.execute();
			return true;
		}
		return false;
	}

	public class TemplateListUpdaterTask extends
			AsyncTaskWithProgressDialog<Void, Void, Void> {
		private Context context;

		public TemplateListUpdaterTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute(context,
					getString(R.string.msg_updating_template_list),
					getString(R.string.msg_please_wait));
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean templateListChanged = false;
			// iterate over all loaded templates
			for (int i = 0; i < templateList.size(); i++) {
				Template templateToCheck;
				// check for change
				templateToCheck = templateList.get(i);
				if (templateToCheck.hasFileTimeStampChanged()) {
					// file changed so reload it
					try {
						// file for template
						final File templateFile = templateToCheck.getTemplateFile();
						// load template
						final de.fau.cs.mad.gamekobold.jackson.Template loadedTemplate = JacksonInterface
								.loadTemplate(templateFile, true);
						// check
						if (loadedTemplate != null) {
							// take over changes
							templateToCheck
									.setTemplateName(loadedTemplate.getTemplateName());
							templateToCheck.setWorldName(loadedTemplate.getGameName());
							templateToCheck.setAuthor(loadedTemplate.getAuthor());
							templateToCheck.setDate(loadedTemplate.getDate());
							templateToCheck.setIconPath(loadedTemplate.getIconPath());
							templateToCheck
									.setDescription(loadedTemplate.getDescription());
							templateToCheck.setTagString(loadedTemplate.getTagString());
							// update time stamp
							templateToCheck.setFileTimeStamp(templateFile
									.lastModified());
							templateListChanged = true;
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
			return templateListChanged;
		}

		protected void onPostExecute(Boolean listChanged) {
			// if a template changed
			if (listChanged) {
				// notify adapter that a change occurred
				TemplateBrowserArrayAdapter adapter = (TemplateBrowserArrayAdapter) getListAdapter();
				adapter.notifyDataSetChanged();
			}
			super.onPostExecute(listChanged);
		}
	};

	/**
	 * Loads all templates in the template directory and sets them as the new
	 * list to show.
	 */
	public class TemplateListLoaderTask extends
			AsyncTaskWithProgressDialog<Void, Void, List<Template>> {
		private Context context;
		private ArrayAdapter adapter;

		public TemplateListLoaderTask(Context context, ArrayAdapter adapter) {
			this.context = context;
			this.adapter = adapter;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute(context,
					context.getString(R.string.msg_loading_template_list),
					context.getString(R.string.msg_please_wait));
		}

		@Override
		protected List<Template> doInBackground(Void... params) {
			List<Template> templateList = new ArrayList<Template>();
			/*
			 * JACKSON START We iterate over all files in the template directory
			 * and load the data into the list
			 */
			File templateDir = JacksonInterface
					.getTemplateRootDirectory(context);
			if (templateDir != null) {
				Log.d("TemplateBrowser",
						"templateDir:" + templateDir.getAbsolutePath());
				if (templateDir.isDirectory()) {
					final File[] fileList = templateDir.listFiles();
					de.fau.cs.mad.gamekobold.jackson.Template loadedTemplate = null;
					for (final File file : fileList) {
						try {
							loadedTemplate = JacksonInterface.loadTemplate(file, true);
							if (loadedTemplate != null) {
								Template temp = new Template(
										loadedTemplate.getTemplateName(),
										loadedTemplate.getGameName(),
										loadedTemplate.getAuthor(),
										loadedTemplate.getDate(),
										loadedTemplate.getIconPath(),
										loadedTemplate.getDescription());
								temp.setTagString(loadedTemplate.getTagString());
								if (temp.getTemplateName().equals("")) {
									temp.setTemplateName(file.getName());
								}
								temp.fileAbsolutePath = file.getAbsolutePath();
								// set time stamp
								temp.setFileTimeStamp(file.lastModified());
								templateList.add(temp);
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}
			}
			/*
			 * JACKSON END
			 */
			return templateList;
		}

		@Override
		protected void onPostExecute(List<Template> templateList) {
			// discard progress dialog
			super.onPostExecute(templateList);
			// should not happen but who knows
			if (templateList == null) {
				templateList = new ArrayList<Template>();
			}
			if (!mode_pickTemplateForCharacterCreation) {
				// JACKSON add a new entry for editing the last created template
				templateList.add(new Template(getResources().getString(
						R.string.row_edit_last_template), "", "", "", -1));
			}
			// set the template list for the current activity
			setTemplateList(templateList, adapter);
		}
	}

	/**
	 * Clears the internal template list and adds all template from the new list. Updates views.
	 * @param templateList New List of templates.
	 */
	public void setTemplateList(List<Template> templateList,
			ArrayAdapter adapter) {
		if (templateList != null) {
			// TemplateBrowserArrayAdapter adapter =
			// (TemplateBrowserArrayAdapter) getListAdapter();
			adapter.clear();
			adapter.addAll(templateList);
			adapter.notifyDataSetChanged();
		}
	}
}
