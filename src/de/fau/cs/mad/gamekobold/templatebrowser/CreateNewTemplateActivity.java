package de.fau.cs.mad.gamekobold.templatebrowser;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.jackson.Template;
import de.fau.cs.mad.gamekobold.template_generator.MainTemplateGenerator;

public class CreateNewTemplateActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_template);

		ImageButton addImageButton = (ImageButton) findViewById(R.id.imageButton1);

		final TextView tvTempalteName = (TextView) findViewById(R.id.editText1);
		final TextView tvGameName = (TextView) findViewById(R.id.editText2);
		final TextView tvDescription = (TextView) findViewById(R.id.editText3);
		Button createTemplateButton = (Button) findViewById(R.id.button2);

		addImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// open OpenPictureDialog

			}
		});

		createTemplateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// add new template to list on template browser-view
				Toast.makeText(getApplicationContext(),
						"Dein Template wird erstellt!", Toast.LENGTH_SHORT)
						.show();

//				String currentDate = new SimpleDateFormat("dd.MM.yyyy")
//						.format(new Date());
//				
//				// TODO Have to store Tempalte-data!
//				// TODO What is the template author's name?
//				// now it is just stub: "Registered Author"
//				// TODO 0 stands for default icon id
//				Template newTemplate = new Template(tvTempalteName.getText()
//						.toString(), tvGameName.getText().toString(),
//						"Registered Author", currentDate, 0, tvDescription
//								.getText().toString());
//	
//				Intent i = new Intent(CreateNewTemplateActivity.this,
//						TemplateBrowserActivity.class);
//
//				// he currently running instance of activity B in the above
//				// example will either receive the new intent you are starting
//				// here in its onNewIntent() method, or be itself finished and
//				// restarted with the new intent. If it has declared its launch
//				// mode to be "multiple" (the default) and you have not set
//				// FLAG_ACTIVITY_SINGLE_TOP in the same intent, then it will be
//				// finished and re-created; for all other launch modes or if
//				// FLAG_ACTIVITY_SINGLE_TOP is set then this Intent will be
//				// delivered to the current instance's onNewIntent().
//				i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//				// TODO we have to save newTempalte!! or it will be lost!
//				// (detailsView->Browser... got lost)
//				i.putExtra("newtemplate", newTemplate);
//				startActivity(i);

				
				// TODO we have to save newTempalte-data!! or it will be lost!
				
				Intent intent = new Intent(CreateNewTemplateActivity.this,
						MainTemplateGenerator.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				
				/*
				 * JACKSON START
				 */
				// flag to distinguish between editing and creating 
				intent.putExtra(MainTemplateGenerator.MODE_CREATE_NEW_TEMPLATE, true);
				// create template for data transfer
				de.fau.cs.mad.gamekobold.jackson.Template jTemplate = new Template();
				// set data
				jTemplate.templateName = tvTempalteName.getText().toString();
				jTemplate.gameName = tvGameName.getText().toString();
				//TODO author
				jTemplate.author = "Registered Author";
				jTemplate.date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
				//TODO icon id
				jTemplate.iconID = 0;
				jTemplate.description = tvDescription.getText().toString();
				intent.putExtra(de.fau.cs.mad.gamekobold.jackson.Template.PARCELABLE_STRING, jTemplate);
				/*
				 * JACKSON END 
				 */
				
				startActivity(intent);
				
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

}
