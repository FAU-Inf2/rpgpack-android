package de.fau.cs.mad.gamekobold.colorpicker;

import java.util.Random;
import de.fau.cs.mad.gamekobold.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class ColorPickerDialog extends DialogFragment implements View.OnClickListener{
	// our color list
	private static int[] colors = null;
	// receiver for picked color
	private ColorPickerDialogInterface receiver = null;
	//
	private ImageButton[] buttons;
	// where to put this color picker
	private Button targetButton = null;
	
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 // check if colors already loaded
	    if(colors == null) {
		    final Resources res = getResources();
		    // create color list
		    colors = new int[12];
		    colors[0] = res.getColor(R.color.light_yellow);
		    colors[1] = res.getColor(R.color.orange);
		    colors[2] = res.getColor(R.color.light_red);
		    colors[3] = res.getColor(R.color.light_red2);
		    colors[4] = res.getColor(R.color.light_purple);
		    colors[5] = res.getColor(R.color.dark_purple);
		    colors[6] = res.getColor(R.color.stone);
		    colors[7] = res.getColor(R.color.dark_stone);
		    colors[8] = res.getColor(R.color.light_green);
		    colors[9] = res.getColor(R.color.dark_green);
		    colors[10] = res.getColor(R.color.peter_river);
		    colors[11] = res.getColor(R.color.belize_hole);	
	    }
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Get the layout inflater
	    final LayoutInflater inflater = getActivity().getLayoutInflater();
	    View view = inflater.inflate(R.layout.color_picker_layout, null);
	    // shuffle colors
	    shuffleArray(colors);
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
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
	
	@Override
	public void onStart() {
		if(targetButton != null) {
			int[] coordinates = new int[2];
			targetButton.getLocationOnScreen(coordinates);
			Log.d("Colorpicker", "targetX:"+coordinates[0]);
			Log.d("Colorpicker", "targetY:"+coordinates[1]);
			final int height = targetButton.getHeight();
			final Dialog dlg = getDialog();
			Log.d("Colorpicker", "dlg:"+dlg);
			final Window window = dlg.getWindow();
			Log.d("Colorpicker", "window:"+window);
			final WindowManager.LayoutParams dialogLayout = window.getAttributes();
			Log.d("Colorpicker", "popupX:"+dialogLayout.x);
			Log.d("Colorpicker", "popupY:"+dialogLayout.y);
			Log.d("Colorpicker", "popupWidth:"+dialogLayout.width);
			Log.d("Colorpicker", "popupHeight:"+dialogLayout.height);
			dialogLayout.gravity = Gravity.TOP | Gravity.LEFT;
			//dialogLayout.gravity = Gravity.TOP;
			dialogLayout.x = coordinates[0]+targetButton.getWidth()-dialogLayout.width;
			dialogLayout.y = coordinates[1];
			Log.d("Colorpicker", "popupX:"+dialogLayout.x);
			Log.d("Colorpicker", "popupY:"+dialogLayout.y);
		}
		super.onStart();
	}
	
	public void setTargetButton(Button targetButton) {
		this.targetButton = targetButton;
	}
	
	// Implementing Fisher–Yates shuffle
	// From http://stackoverflow.com/a/1520212
	private static void shuffleArray(int[] ar)
	{
		Random rnd = new Random();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	    	int index = rnd.nextInt(i + 1);
	    	// Simple swap
	    	int a = ar[index];
	    	ar[index] = ar[i];
	    	ar[i] = a;
	    }
	}
	
	
}
