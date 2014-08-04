package de.fau.cs.mad.gamekobold.template_generator;

import de.fau.cs.mad.gamekobold.MainActivity;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class WarningLeaveDialog extends DialogFragment{
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
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
				//TODO: hier sollte verworfen werden
				// JACKSON START
				SlideoutNavigationActivity.skipNextOnPauseSave = true;
				// JACKSON END
				((TemplateGeneratorActivity) SlideoutNavigationActivity.theActiveActivity).superBackPressed();
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
				SlideoutNavigationActivity.forceSaveOnNextOnPause = true;
				/*
				 * JACKSON END
				 */
				Intent intent = new Intent(getActivity(), MainActivity.class);
				startActivity(intent);
				//((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).superBackPressed();
			}});
		
		// when you done
		return alertDialog;
	}
	
	public static WarningLeaveDialog newInstance() {
		WarningLeaveDialog customAD = new WarningLeaveDialog();
		return customAD;
	}
}
