package de.fau.cs.mad.gamekobold;

import de.fau.cs.mad.gamekobold.matrix.MatrixViewActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.TemplateBrowserActivity;
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	/* Functions */
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

	public void settings(View view) {
		this.notImplemented();
	}

	public void loadGame(View view) {
		this.notImplemented();
	}

	public void startGame(View view) {
		this.notImplemented();
	}

	public void manageCharackter(View view) {
		//Charakter Kurz-Übersicht
		Intent intent = new Intent(MainActivity.this,
				MatrixViewActivity.class);
		startActivity(intent);
		
	}

	public void templateGenerator(View view) {
		Intent intent = new Intent(MainActivity.this,
				TemplateBrowserActivity.class);
		startActivity(intent);

		// Intent intent = new Intent(MainActivity.this,
		// de.fau.cs.mad.gamekobold.template_generator.MainTemplateGenerator.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		// startActivity(intent);
	}

	/* this functions starts an alljoyn test activity */
	public void alljoyn(View view) {
		Intent intent = new Intent(this, AlljoynTest.class);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.main_actionbar);
		setContentView(R.layout.activity_main2);
	}

}
