package de.fau.cs.mad.gamekobold.templatebrowser;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.colorpicker.ColorPickerDialog;
import de.fau.cs.mad.gamekobold.colorpicker.ColorPickerDialogInterface;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CharacterDetailsActivity extends Activity implements ColorPickerDialogInterface{
	private RelativeLayout relLayout;
	private CharacterSheet sheet;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_character_details);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		relLayout = (RelativeLayout)findViewById(R.id.relativeLayout1);
		final EditText description = (EditText)findViewById(R.id.editText1);
		final Button colorChangeButton = (Button)findViewById(R.id.button2);
		final TextView characterName = (TextView)findViewById(R.id.textView1);
		final TextView levelLabel = (TextView)findViewById(R.id.textView3);
		
		colorChangeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final ColorPickerDialog dialog = new ColorPickerDialog();
				dialog.setTargetButton(colorChangeButton);
				dialog.show(getFragmentManager(), "ColorPickerDialog");
			}
		});
		
		final Intent intent = getIntent();
		final Bundle extras = intent.getExtras();		
		if (extras != null) {
			sheet = (CharacterSheet)extras.getParcelable("CharacterSheet");
			// remove next line later when we got a character
			if(sheet != null) {
				setTitle(sheet.name);
				characterName.setText(sheet.name);
				// set to character color
				relLayout.setBackgroundColor(sheet.color);
			}
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

	@Override
	public void onColorPicked(int color) {
		Log.d("CharacterDetails", "picked color:"+color);
		setCharacterColor(color);
	}
	
	private void setCharacterColor(int color) {
		relLayout.setBackgroundColor(color);
		if(sheet != null) {
			sheet.color = color;
		}
	}
}
