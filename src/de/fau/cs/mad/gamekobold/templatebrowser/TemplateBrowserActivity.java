package de.fau.cs.mad.gamekobold.templatebrowser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.template_generator.TemplateGeneratorActivity;

public class TemplateBrowserActivity extends ListActivity {

	List<Template> templateList = null;
	private static Activity myActivity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_template_browser);
		Log.e("d", "On create!!!");
		myActivity = this;
		//templateList = getDataForListView(this);
		if(templateList == null) {
			templateList = new ArrayList<Template>();
		}
		TemplateListLoaderTask loaderTask = new TemplateListLoaderTask();
		loaderTask.execute();
		
//		Template newTemplate;
//		
//		// take new Template object if it was sent from
//		// CreateNewTemplate-Activity
//		if (getIntent().hasExtra("newtemplate") == true) {
//			newTemplate = (Template) getIntent().getExtras().getSerializable(
//					"newtemplate");
//			
//			templateList.add(newTemplate);
//		}
/*
		// JACKSON add a new entry for editing the last created template
		templateList.add(new Template("Edit last template...", "", "", "", -1));
		// set create new template row to the end of the list
		Template addNewTemplateRow = new Template("Create New Template...", "",
				"", "", -1);
		templateList.add(addNewTemplateRow);
*/
		// have to make it final because of adapter.getCount()-method for
		// newTemplate-intent
		final TemplateBrowserArrayAdapter adapter = new TemplateBrowserArrayAdapter(
				this, templateList);
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				
				//is it last row?
				if (position == adapter.getCount() - 1) {
					
					Log.e("er", "Position, getCount: " + adapter.getCount());
					
					Intent i = new Intent(TemplateBrowserActivity.this,
							CreateNewTemplateActivity.class);
					startActivity(i);

				}
				// JACKSON start : for editing last created template
				else if(position == adapter.getCount() -2) {
					// get shared preferences
					SharedPreferences pref = getSharedPreferences(TemplateGeneratorActivity.SHARED_PREFERENCES_FILE_NAME,  Activity.MODE_PRIVATE);
					// get last edited template file name
					String templateFileName = pref.getString(TemplateGeneratorActivity.LAST_EDITED_TEMPLATE_NAME, "");
					// check for existence
					if(templateFileName.equals("")) {
						Toast.makeText(myActivity, "No template edited", Toast.LENGTH_LONG).show();
					}
					else {
						startEditingOfTemplate(templateFileName);
					}
				}
				// JACKSON end
				else {
					Intent i = new Intent(TemplateBrowserActivity.this,
							TemplateDetailsActivity.class);
					
					Log.e("er", "position: " + position);
					
					i.putExtra("position", position);
					startActivity(i);
				}
			}
		});
		
		// set onItemClickListener. if the user long clicks on an item we show a dialog
		// the user then gets the option to delete the template
		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
				final Template longClickedTemplate = templateList.get(pos);
				
				Log.d("TemplateBrowser","longClickOn:"+longClickedTemplate.absoluteFilePath);
				
				if(longClickedTemplate.absoluteFilePath != null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
					builder.setTitle("Delete Template?");
					builder.setMessage("Click yes to delete the template.");
					builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							File file = new File(longClickedTemplate.absoluteFilePath);
							if(file != null) {
								Log.d("TempalteBrowser", "delete template:"+longClickedTemplate);
								//removeItem(longClickedTemplate);
								if(file.delete()) {
									// check if we removed the last edited template
									SharedPreferences pref = getSharedPreferences(TemplateGeneratorActivity.SHARED_PREFERENCES_FILE_NAME, MODE_PRIVATE);
									String lastEditedTemplate = pref.getString(TemplateGeneratorActivity.LAST_EDITED_TEMPLATE_NAME, "");
									if(lastEditedTemplate.equals(file.getName())) {
										// if so we remove it from the saved preference
										SharedPreferences.Editor editor = pref.edit();
										editor.remove(TemplateGeneratorActivity.LAST_EDITED_TEMPLATE_NAME);
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

	// TODO reload data. we could be comming back from the generator. otherwise the new template
	// won't show
	@Override
	protected void onResume() {
		super.onResume();
		Log.e("d", "On resume!!!");

		// Template newTemplate;
		// // take new Template object if it was sent from
		// // CreateNewTemplateActivity
		// if (getIntent().hasExtra("newtemplate")) {
		//
		// Log.e("d", "NewTemplate is da!!!");
		//
		// newTemplate = (Template) getIntent().getExtras().getSerializable(
		// "newtemplate");
		// templateList.add(newTemplate);
		// }
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.template_browser, menu);
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

	// TODO replace with real data, now it is just stub for real data from DB or
	// json file?
	public static List<Template> getDataForListView(Context context) {
		// temp list
		List<Template> templateList = new ArrayList<Template>();

		// add dummy data
		Template template1 = new Template(
				"My First Template",
				"Dungeons and Dragons",
				"Anna",
				"20.05.2014",
				2,
				"This is my first try to make my own template! D&D departs from traditional wargaming and assigns each player a specific character to play instead of a military formation. These characters embark upon imaginary adventures within a fantasy setting.");
		Template template2 = new Template("The Best Template",
				"Vampire the Masquerade", "Anna", "20.05.2014", 3);
		Template template3 = new Template("Schwarze Auge Template",
				"Das Schwarze Auge", "Anna", "21.05.2014", 4);

		templateList.add(template1);
		templateList.add(template2);
		templateList.add(template3);
		//
		/*
		 * JACKSON START
		 * We iterate over all files in the template directory and load the data into the list
		 */
		File templateDir = de.fau.cs.mad.gamekobold.jackson.Template.getTemplateDirectory(myActivity);
		if(templateDir != null) {
			if(templateDir.isDirectory()) {
				final File[] fileList = templateDir.listFiles();
				de.fau.cs.mad.gamekobold.jackson.Template loadedTemplate = null;
				for(final File file : fileList) {
					try {
						loadedTemplate = de.fau.cs.mad.gamekobold.jackson.Template.loadFromJSONFileForTemplateBrowser(file);
					} catch (JsonParseException | JsonMappingException e) {
						e.printStackTrace();
					}
					catch(IOException e) {
						e.printStackTrace();
					}
					if(loadedTemplate != null) {
						Template temp = new Template(loadedTemplate.templateName,
								loadedTemplate.gameName,
								loadedTemplate.author,
								loadedTemplate.date,
								loadedTemplate.iconID,
								loadedTemplate.description);
						if(temp.getTemplateName().equals("")) {
							temp.setTemplateName(file.getName());
						}
						temp.absoluteFilePath = file.getAbsolutePath();
						templateList.add(temp);
					}
				}
			}
		}
		/*
		 * JACKSON END
		 */
		return templateList;
	}
	
	private void removeItem(Template template) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<Template> adapter = (ArrayAdapter<Template>)getListAdapter();
		adapter.remove(template);
		adapter.notifyDataSetChanged();
	}
	
	private void startEditingOfTemplate(Template template) {
		if(template.absoluteFilePath != null) {
			int lastSlashPos = template.absoluteFilePath.lastIndexOf("/");
			String fileName = null;
			if(lastSlashPos == -1) {
				fileName = template.absoluteFilePath;
			}
			else {
				fileName = template.absoluteFilePath.substring(lastSlashPos+1);
			}
			startEditingOfTemplate(fileName);
		}
	}
	
	private void startEditingOfTemplate(String fileName) {
		if(fileName.equals("")){
			return;
		}
		Intent intent = new Intent(TemplateBrowserActivity.this,
				TemplateGeneratorActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		// flag to distinguish between editing and creating 
		intent.putExtra(TemplateGeneratorActivity.MODE_CREATE_NEW_TEMPLATE, false);
		intent.putExtra(TemplateGeneratorActivity.EDIT_TEMPLATE_FILE_NAME, fileName);
		startActivity(intent);	
	}
	
	private class TemplateListLoaderTask extends AsyncTask<Void, Void, List<Template>> {
		private ProgressDialog pd;
		
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(myActivity);
    		pd.setTitle("Loading template list...");
    		pd.setMessage("Please wait...");
    		pd.setCancelable(false);
    		pd.setIndeterminate(true);
    		pd.show();
		}
		
		@Override
		protected List<Template> doInBackground(Void... params) {
			List<Template> templateList = new ArrayList<Template>();
			Template template1 = new Template(
					"My First Template",
					"Dungeons and Dragons",
					"Anna",
					"20.05.2014",
					2,
					"This is my first try to make my own template! D&D departs from traditional wargaming and assigns each player a specific character to play instead of a military formation. These characters embark upon imaginary adventures within a fantasy setting.");
			Template template2 = new Template("The Best Template",
					"Vampire the Masquerade", "Anna", "20.05.2014", 3);
			Template template3 = new Template("Schwarze Auge Template",
					"Das Schwarze Auge", "Anna", "21.05.2014", 4);

			templateList.add(template1);
			templateList.add(template2);
			templateList.add(template3);
			/*
			 * JACKSON START
			 * We iterate over all files in the template directory and load the data into the list
			 */
			File templateDir = null;
			if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
				templateDir = Environment.getExternalStorageDirectory();
			}
			else {
				templateDir = myActivity.getDir(de.fau.cs.mad.gamekobold.jackson.Template.FOLDER_NAME, Context.MODE_PRIVATE);
			}
			if(templateDir != null) {
				if(templateDir.isDirectory()) {
					final File[] fileList = templateDir.listFiles();
					de.fau.cs.mad.gamekobold.jackson.Template loadedTemplate = null;
					for(final File file : fileList) {
						try {
							loadedTemplate = de.fau.cs.mad.gamekobold.jackson.Template.loadFromJSONFileForTemplateBrowser(file);
						} catch (JsonParseException | JsonMappingException e) {
							e.printStackTrace();
						}
						catch(IOException e) {
							e.printStackTrace();
						}
						if(loadedTemplate != null) {
							Template temp = new Template(loadedTemplate.templateName,
									loadedTemplate.gameName,
									loadedTemplate.author,
									loadedTemplate.date,
									loadedTemplate.iconID,
									loadedTemplate.description);
							if(temp.getTemplateName().equals("")) {
								temp.setTemplateName(file.getName());
							}
							temp.absoluteFilePath = file.getAbsolutePath();
							templateList.add(temp);
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
			if(templateList != null) {
				// JACKSON add a new entry for editing the last created template
				templateList.add(new Template("Edit last template...", "", "", "", -1));
				// set create new template row to the end of the list
				templateList.add(new Template("Create New Template...", "", "", "", -1));
			}
			((TemplateBrowserActivity)myActivity).setTemplateList(templateList);
			if(pd != null) {
				pd.dismiss();
			}
		}
	}
	
	public void setTemplateList(List<Template> templateList) {
		if(templateList != null) {
			TemplateBrowserArrayAdapter adapter = (TemplateBrowserArrayAdapter)getListAdapter();
			adapter.clear();
			adapter.addAll(templateList);
			adapter.notifyDataSetChanged();
		}
	}
}
