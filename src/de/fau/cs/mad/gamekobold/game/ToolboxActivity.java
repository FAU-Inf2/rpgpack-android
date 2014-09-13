package de.fau.cs.mad.gamekobold.game;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

public class ToolboxActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new ToolboxFragment();
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
		Intent intent = new Intent(ToolboxActivity.this,
				ToolboxMapActivity.class);
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

}