package de.fau.cs.mad.gamekobold;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.characterbrowser.CharacterBrowserActivity;
import de.fau.cs.mad.gamekobold.game.GameBrowserActivity;
import de.fau.cs.mad.gamekobold.jackson.MatrixTable;
import de.fau.cs.mad.gamekobold.matrix.MatrixViewActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.TemplateBrowserActivity;

public class MainActivity extends Activity {

	Toast leaveToast = null;

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
		// Spiel erstellen
		Intent intent = new Intent(MainActivity.this, GameBrowserActivity.class);
		startActivity(intent);
	}

	public void manageCharackter(View view) {
		Intent intent = new Intent(MainActivity.this,
				CharacterBrowserActivity.class);
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
		// needed for jackson
		MatrixTable.appContext = getApplicationContext();
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.main_actionbar);
		//leaveToast = Toast.makeText(this, "Press back again to leave",Toast.LENGTH_SHORT);
		setContentView(R.layout.activity_main4);
	}

	public void onBackPressed() {
		if (!leaveToast.getView().isShown()) {
			leaveToast.show();
		} else {
			super.onBackPressed();
		}
	}
}
