package de.fau.cs.mad.gamekobold.template_generator;

import de.fau.cs.mad.gamekobold.MainActivity;
import de.fau.cs.mad.gamekobold.matrix.MatrixViewActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;

public class WarningLeaveDialog extends DialogFragment{
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setMessage("Wollen Sie den Template-Generator wirklich verlassen?")
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
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ja & verwerfen", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//TODO: hier sollte verworfen werden
				// JACKSON START
				TemplateGeneratorActivity.skipNextOnPauseSave = true;
				// JACKSON END
				((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).superBackPressed();
			} });
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Nein", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}}); 
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ja & speichern", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				/*
				 * JACKSON START
				 */ 
				TemplateGeneratorActivity.forceSaveOnNextOnPause = true;
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
