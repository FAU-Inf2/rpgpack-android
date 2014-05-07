package de.fau.mad.sqlitedataexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sqlitedataexchange.R;

import de.fau.mad.sqlite.DatabaseHandler;
import de.fau.mad.sqlite.KoboldEntryDAO;

public class MainActivity extends ActionBarActivity implements OnClickListener {
	// GUI components
	private EditText idText; // Text field
	private EditText featureText; // Text field
	private EditText valueText; // Text field
	private EditText descriptionText; // Text field

	private Button saveButton; // save to DB button
	private Button backButton; // Back button

	// DAO
	private KoboldEntryDAO dao;
	private DatabaseHandler dbHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		idText = (EditText) findViewById(R.id.editText1);
		featureText = (EditText) findViewById(R.id.editText2);
		valueText = (EditText) findViewById(R.id.editText3);
		descriptionText = (EditText) findViewById(R.id.editText4);

		saveButton = (Button) findViewById(R.id.button1);
		backButton = (Button) findViewById(R.id.button2);

		saveButton.setOnClickListener(this);
		backButton.setOnClickListener(this);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	public void onClick(View v) {
		// If save button was clicked
		if (saveButton.isPressed()) {

			dbHandler = new DatabaseHandler(MainActivity.this);
			// Get entered text
			dao = new KoboldEntryDAO(idText.getText().toString(), featureText
					.getText().toString(), valueText.getText().toString(),
					descriptionText.getText().toString());
			// add to DB
			dbHandler.insertKoboldEntry(dao);

			// Display success information
			Toast.makeText(getApplicationContext(), "New Entry added!",
					Toast.LENGTH_LONG).show();

			idText.setText("");
			featureText.setText("");
			valueText.setText("");
			descriptionText.setText("");

		// When back button is pressed
		} else if (backButton.isPressed()) {

			// Create an intent
			Intent intent = new Intent(MainActivity.this, DBActivity.class);
			// Start activity
			startActivity(intent);
			// Finish this activity
			this.finish();

			// Close the database
			// dbHandler.close();
			// dao.close();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
