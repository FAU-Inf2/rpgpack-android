package de.fau.cs.mad.rpgpack.template_generator;

import de.fau.cs.mad.rpgpack.R;
import de.fau.cs.mad.rpgpack.MainMenu;
import de.fau.cs.mad.rpgpack.SlideoutNavigationActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class WarningLeaveDialog extends DialogFragment{
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Log.d("WarningLeaveDialog","onCreate");
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setMessage(getString(R.string.question_exit))
		.setCancelable(true);
//		.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog,int id) {
//				((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).superBackPressed();
//			}
//		  })
//		.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog,int id) {
//				dialog.cancel();
//			}
//		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.yes_and_trash), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// JACKSON START
				SlideoutNavigationActivity.saveOnNextOnPause = false;
				// JACKSON END
//				((TemplateGeneratorActivity) SlideoutNavigationActivity.theActiveActivity).superBackPressed();
				Intent intent = new Intent(getActivity(), MainMenu.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			} });
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}}); 
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes_and_save), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				/*
				 * JACKSON START
				 */ 
				SlideoutNavigationActivity.saveOnNextOnPause = true;
				/*
				 * JACKSON END
				 */
				Intent intent = new Intent(getActivity(), MainMenu.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}});
		// when you done
		return alertDialog;
	}
	
	public static WarningLeaveDialog newInstance() {
		WarningLeaveDialog customAD = new WarningLeaveDialog();
		return customAD;
	}
}
