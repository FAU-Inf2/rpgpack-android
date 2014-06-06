package de.fau.cs.mad.gamekobold.templatebrowser;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.template_generator.MainTemplateGenerator;

public class TemplateBrowserActivity extends ListActivity {

	List<Template> templateList = getDataForListView();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_template_browser);

		Log.e("d", "On create!!!");

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

		// JACKSON add a new entry for editing the last created template
		templateList.add(new Template("Edit last template...", "", "", "", -1));
		// set create new template row to the end of the list
		Template addNewTemplateRow = new Template("Create New Template...", "",
				"", "", -1);
		templateList.add(addNewTemplateRow);

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
					Intent intent = new Intent(TemplateBrowserActivity.this,
							MainTemplateGenerator.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					// flag to distinguish between editing and creating 
					intent.putExtra("MODE_CREATE_NEW_TEMPLATE", false);
					intent.putExtra("TEMPLATE_FILENAME", "testTemplate.json");
					startActivity(intent);
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

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

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
	public static List<Template> getDataForListView() {
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

		return templateList;

	}

}
