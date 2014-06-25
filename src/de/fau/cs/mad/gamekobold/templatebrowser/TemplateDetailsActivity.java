package de.fau.cs.mad.gamekobold.templatebrowser;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.template_generator.TemplateGeneratorActivity;

public class TemplateDetailsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_template_details);

		TemplateIcons templateIcons = TemplateIcons.getInstance();

		ImageView ivIcon = (ImageView) findViewById(R.id.icon1);
		TextView tvTempalteName = (TextView) findViewById(R.id.textView1);
		TextView tvGameName = (TextView) findViewById(R.id.textView4);
		TextView tvInfo = (TextView) findViewById(R.id.textView3);
		TextView tvDescription = (TextView) findViewById(R.id.textView2);
		Button backButton = (Button) findViewById(R.id.button1);
		Button editButton = (Button) findViewById(R.id.button2);

		int position = 1;

		// take position to show details
		if (getIntent().hasExtra("position") == true) {
			position = getIntent().getExtras().getInt("position");
		}

		final Template curTemplate = TemplateBrowserActivity.getDataForListView(this)
				.get(position);
		tvTempalteName.setText(curTemplate.getTemplateName());
		tvGameName.setText(curTemplate.getGameName());
		tvInfo.setText("Von: " + curTemplate.getAuthor() + ", "
				+ curTemplate.getDate());

		ivIcon.setImageResource(Integer.valueOf(templateIcons.getTempalteIcon(curTemplate
				.getIconID())));

		if(curTemplate.getDescription().equals("")) {
			tvDescription.setText("No description found!");
		}
		else {
			tvDescription.setText(curTemplate.getDescription());	
		}
		

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(TemplateDetailsActivity.this,
						TemplateBrowserActivity.class);
				startActivity(i);
			}
		});

		// vllt in den TemplateBrowser irgendwie integrieren
		editButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(curTemplate.absoluteFilePath != null) {
					int lastSlashPos = curTemplate.absoluteFilePath.lastIndexOf("/");
					String fileName = null;
					if(lastSlashPos == -1) {
						fileName = curTemplate.absoluteFilePath;
					}
					else {
						fileName = curTemplate.absoluteFilePath.substring(lastSlashPos+1);
					}
					Intent intent = new Intent(TemplateDetailsActivity.this,
							TemplateGeneratorActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					// flag to distinguish between editing and creating 
					intent.putExtra(TemplateGeneratorActivity.MODE_CREATE_NEW_TEMPLATE, false);
					intent.putExtra(TemplateGeneratorActivity.EDIT_TEMPLATE_FILE_NAME, fileName);
					startActivity(intent);	
				}
			}
		});

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.template_details, menu);
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
					R.layout.fragment_template_details, container, false);
			return rootView;
		}
	}

}
