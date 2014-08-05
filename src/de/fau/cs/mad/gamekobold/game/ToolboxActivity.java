package de.fau.cs.mad.gamekobold.game;

import de.fau.cs.mad.gamekobold.MainActivity;
import de.fau.cs.mad.gamekobold.R;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
		Intent intent = new Intent(ToolboxActivity.this, TimerActivity.class);
		startActivity(intent);
	}

	public void openTactical(View view) {
		Intent intent = new Intent(ToolboxActivity.this,
				ToolboxMapActivity.class);
		startActivity(intent);
	}

	public void openRandomGenerator(View view) {
		Intent intent = new Intent(ToolboxActivity.this,
				ToolboxRandomGenerator.class);
		startActivity(intent);
	}

}