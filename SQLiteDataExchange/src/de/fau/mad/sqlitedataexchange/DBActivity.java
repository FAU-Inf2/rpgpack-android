package de.fau.mad.sqlitedataexchange;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlitedataexchange.R;

import de.fau.mad.sqlite.DatabaseHandler;
import de.fau.mad.sqlite.KoboldDBContract;
import de.fau.mad.sqlite.KoboldEntryDAO;
import de.fau.mad.sqlite.impl.AllJoynReceiver;
import de.fau.mad.sqlite.interfaces.IAllJoynReceiver;

public class DBActivity extends ActionBarActivity implements OnClickListener {

	// GUI components
	private TextView resultsText; // DB entries text field

	private Button showDBButton; // show DB entries button
	private Button backButton; // Back button
	private Button receiveButton; // Receive new data from another user button

	private DatabaseHandler dbHandler;

	private String resultString;
	
	// feature is unnecessary, only koboldentryid and value
	private final static String JSON_DATA = "{"
			+ "\"koboldentryid\":\"2\",\"feature\":\"health\",\"value\":\"500\"" + "}"; // feature is unnecessary

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_db);

		resultsText = (TextView) findViewById(R.id.textView1);

		showDBButton = (Button) findViewById(R.id.button1);
		backButton = (Button) findViewById(R.id.button2);
		receiveButton = (Button) findViewById(R.id.button3);

		showDBButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
		receiveButton.setOnClickListener(this);
		
		resultsText.setText("");

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public void onClick(View v) {
		// If show DB button was clicked
		if (showDBButton.isPressed()) {

			dbHandler = new DatabaseHandler(DBActivity.this);
		
			resultsText.setText("Hello DB!!!");
			
			Log.w("Tag!!", "Now reading from database.");
			
			List<KoboldEntryDAO> resultList ;
//			// Get entries from DB
			resultList = dbHandler.readKoboldEntries();
			Log.w("", "Number of entries from db: " + resultList.size());

			resultString = "";
			for (KoboldEntryDAO dao : resultList)
				resultString = resultString + dao.getFeature() + " "
						+ dao.getValue() + " " + dao.getDescription() + "\n";
			resultsText.setText(resultString);

			// Display success information
			Toast.makeText(getApplicationContext(), "Read from DB!",
					Toast.LENGTH_LONG).show();
		
			// When back button is pressed
		} else if (backButton.isPressed()) {
			// Create an intent
			Intent intent = new Intent(DBActivity.this, MainActivity.class);
			// Start activity
			startActivity(intent);
			// Finish this activity
			this.finish();

			// Close the database
			// dao.close();
			
		} else if (receiveButton.isPressed()) {
			// Update some DB fields with received data (koboldentryid : value)
			dbHandler = new DatabaseHandler(DBActivity.this);
			
			IAllJoynReceiver receiver = new AllJoynReceiver();
			
			Map<String, String> receivedDataMap = new TreeMap<String, String>();
			receivedDataMap = receiver.receive(JSON_DATA);
			
			for (String key : receivedDataMap.keySet())
				dbHandler.update(key, KoboldDBContract.KoboldEntry.COLUMN_NAME_VALUE, receivedDataMap.get(key));
			
			// Display success information
			Toast.makeText(getApplicationContext(), "Updated!",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.db, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_db, container,
					false);
			return rootView;
		}
	}

}
