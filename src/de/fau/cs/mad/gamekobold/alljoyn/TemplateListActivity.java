package de.fau.cs.mad.gamekobold.alljoyn;

import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;
import de.fau.cs.mad.gamekobold.templatebrowser.TemplateBrowserActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.TemplateBrowserArrayAdapter;
import de.fau.cs.mad.gamekobold.templatebrowser.TemplateBrowserActivity.PlaceholderFragment;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TemplateListActivity extends ListActivity {
	private List<Template> templateList = null;
	private Boolean mode_pickTemplateForCharacterCreation = true;
	private TemplateBrowserActivity a = new TemplateBrowserActivity(
			mode_pickTemplateForCharacterCreation);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_template_browser);

		if (templateList == null) {
			templateList = new ArrayList<Template>();
		}

		final TemplateListArrayAdapter adapter = new TemplateListArrayAdapter(
				this, templateList);
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				Toast.makeText(TemplateListActivity.this,
						templateList.get(position).getTemplateName(),
						Toast.LENGTH_LONG).show();
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
		TemplateListArrayAdapter adapter = (TemplateListArrayAdapter) getListAdapter();
		a.loadTemplateList(this, adapter);
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
}
