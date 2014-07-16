package de.fau.cs.mad.gamekobold.colorpicker;

import de.fau.cs.mad.gamekobold.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class ColorPickerDialog extends DialogFragment implements View.OnClickListener{
	private ColorPickerDialogInterface receiver = null;
	private ImageButton[] buttons;
	private int[] colors;
	
	@Override
	public void onAttach(Activity receiver) {
		super.onAttach(receiver);
		try {
			this.receiver = (ColorPickerDialogInterface)receiver;
		}
		catch(ClassCastException e) {
	         // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(receiver.toString() + " must implement NoticeDialogListener");
		}
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    final LayoutInflater inflater = getActivity().getLayoutInflater();
	    View view = inflater.inflate(R.layout.color_picker_layout, null);
	    // create color list
	    colors = new int[12];
	    colors[0] = Color.BLACK;
	    colors[1] = Color.WHITE;
	    colors[2] = Color.GRAY;
	    colors[3] = Color.GREEN;
	    colors[4] = Color.BLUE;
	    colors[5] = Color.BLACK;
	    colors[6] = Color.YELLOW;
	    colors[7] = Color.MAGENTA;
	    colors[8] = Color.CYAN;
	    colors[9] = Color.BLACK;
	    colors[10] = Color.BLACK;
	    colors[11] = Color.BLACK;
	    
	    //create button list
	    buttons = new ImageButton[12];
	    buttons[0] = (ImageButton)view.findViewById(R.id.imageButton1);
	    buttons[1] = (ImageButton)view.findViewById(R.id.imageButton2);
	    buttons[2] = (ImageButton)view.findViewById(R.id.imageButton3);
	    buttons[3] = (ImageButton)view.findViewById(R.id.imageButton4);
	    buttons[4] = (ImageButton)view.findViewById(R.id.imageButton5);
	    buttons[5] = (ImageButton)view.findViewById(R.id.imageButton6);
	    buttons[6] = (ImageButton)view.findViewById(R.id.imageButton7);
	    buttons[7] = (ImageButton)view.findViewById(R.id.imageButton8);
	    buttons[8] = (ImageButton)view.findViewById(R.id.imageButton9);
	    buttons[9] = (ImageButton)view.findViewById(R.id.imageButton10);
	    buttons[10] = (ImageButton)view.findViewById(R.id.imageButton11);
	    buttons[11] = (ImageButton)view.findViewById(R.id.imageButton12);
	    // set listener and color for all buttons
	    for(int i = 0 ; i < buttons.length; i++) {
	    	final ImageButton button = buttons[i];
	    	button.setOnClickListener(this);
	    	button.setColorFilter(colors[i], Mode.SRC_ATOP);
	    }
	   
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(view);
	    // disable background dimming
	    final AlertDialog dialog = builder.create();
	    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	    return dialog;
	}
	
	@Override
	public void onClick(View v) {
		if(receiver != null) {
			// find index of button
			for(int i = 0; i < buttons.length; i++) {
				if(buttons[i] == v) {
					// pass picked color
					receiver.onColorPicked(colors[i]);
					// close dialog
					getDialog().dismiss();		
				}
			}
		}
	}
}
