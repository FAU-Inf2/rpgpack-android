package de.fau.cs.mad.gamekobold.game;

import java.io.File;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ToolboxActivity extends Activity {

	private CharacterSheet[] characterSheets;
	public static String EXTRA_CHARACTER_ABS_PATH = "EXTRA_CHARACTER_ABS_PATH";
	public static String INFLATE_CHARACTER_NUMBER = "INFLATE_CHARACTER_NUMBER";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle(R.string.action_tools);
		setContentView(R.layout.fragment_game_toolbox);
		Intent intent = getIntent();
		final String[] characterAbsPaths = intent
				.getStringArrayExtra(EXTRA_CHARACTER_ABS_PATH);
		if (savedInstanceState != null) {
			characterSheets = (CharacterSheet[]) savedInstanceState
					.getParcelableArray("characterSheets");
		} else {
			characterSheets = new CharacterSheet[characterAbsPaths.length];
			if (characterAbsPaths != null) {
				Log.d("Toolbox", "characterAbsPath != null");
				try {
					int index = 0;
					for (String onePath : characterAbsPaths) {
						characterSheets[index++] = JacksonInterface
								.loadCharacterSheet(new File(onePath), false);
					}

					// characterSheet = JacksonInterface.loadCharacterSheet(new
					// File(
					// characterAbsPath), false);
					Log.d("CharacterPlayActivity", "loaded sheets");
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	private void notImplemented() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Add the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.setMessage("Not Implemented yet!");
		AlertDialog dialog = builder.create();
		dialog.show();

	}

	public void openTimer(View view) {
		Intent intent = new Intent(ToolboxActivity.this, ToolboxTimerActivity.class);
		startActivity(intent);
	}

	public void openTactical(View view) {
		Intent intent = ToolboxMapActivity.createIntentForStarting(this, characterSheets);	
		startActivity(intent);
	}

	public void openDice(View view) {
		Intent intent = new Intent(ToolboxActivity.this,
				ToolboxDiceActivity.class);
		startActivity(intent);
	}
	
	public void openRandomList(View view) {
		Intent intent = new Intent(ToolboxActivity.this,
				ToolboxRandomListActivity.class);
		startActivity(intent);
	}
	
	public static Intent createIntentForStarting(Context packageContext,
			CharacterSheet[] sheets) {
		Log.i("ToolboxActivity",
				"createIntentForStarting: sheets.length == " + sheets.length);
		Intent intent = new Intent(packageContext, ToolboxActivity.class);
		String[] filePaths = new String[sheets.length];
		int index = 0;
		for (CharacterSheet oneSheet : sheets) {
			filePaths[index++] = oneSheet.getFileAbsolutePath();
		}
		intent.putExtra(ToolboxActivity.EXTRA_CHARACTER_ABS_PATH,
				filePaths);
		return intent;
	}

}