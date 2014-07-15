package de.fau.cs.mad.gamekobold.templatebrowser;

import de.fau.cs.mad.gamekobold.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CharacterDetailsActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_character_details);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		final RelativeLayout relLayout = (RelativeLayout)findViewById(R.id.relativeLayout1);
		final EditText description = (EditText)findViewById(R.id.editText1);
		final Button colorChangeButton = (Button)findViewById(R.id.button2);
		final TextView characterName = (TextView)findViewById(R.id.textView1);
		final TextView levelLabel = (TextView)findViewById(R.id.textView3);
		
		final Intent intent = getIntent();
		final Bundle extras = intent.getExtras();
		// remove next line later when we got a character
		setTitle("Character name");
		if (extras != null) {
			// get character here
			// set to character color
			//relLayout.setBackgroundColor(Color.CYAN);
			setTitle("Character name");
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		/*if (id == R.id.action_settings) {
			return true;
		}*/
		if(id == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
