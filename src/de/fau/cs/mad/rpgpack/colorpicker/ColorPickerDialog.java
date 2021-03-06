package de.fau.cs.mad.rpgpack.colorpicker;

import de.fau.cs.mad.rpgpack.R;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ColorPickerDialog extends DialogFragment implements View.OnClickListener{
	// our color list
	private static int[] colors = null;
	// receiver for picked color
	private ColorPickerDialogInterface receiver = null;
	// ImageButtons for displaying the colors
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
//	    final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
		final Dialog dialog = new Dialog(getActivity());
	    // Get the layout inflater
	    final LayoutInflater inflater = getActivity().getLayoutInflater();
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    final View view = inflater.inflate(R.layout.color_picker_layout, null);
	    ImageView triangle = (ImageView)view.findViewById(R.id.imageView1);
	    triangle.setColorFilter(getActivity().getResources().getColor(R.color.background_light), Mode.SRC_ATOP);
	    // create button list
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
//	    dialog.setView(view, 0, 0, 0, 0);
	    // disable background dimming
	    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	    // disable title bar
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    // set background color to be transparent
	    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	    // set content view
	    dialog.setContentView(view);
	    // set cancelable
	    dialog.setCancelable(true);
	    // cancel dialog when touch outside
	    dialog.setCanceledOnTouchOutside(true);
	    // position dialog under the target button
	    positionDialog(dialog, view);
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
	
	/**
	 * Positions the dialog under the target button
	 * @param dialog The dialog
	 * @param view View which the dialog displays
	 */
	private void positionDialog(Dialog dialog, View view) {
		if(targetButton == null) {
			return;
		}
		view.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		//Log.d("ColorPicker", "view W:"+view.getMeasuredWidth());
		//Log.d("ColorPicker", "view H:"+view.getMeasuredHeight());
		int[] targetLoc = new int[2];
		targetButton.getLocationInWindow(targetLoc);
	    final Window window = dialog.getWindow();
	    final WindowManager.LayoutParams wmlp = window.getAttributes();
		wmlp.gravity = Gravity.TOP | Gravity.LEFT;
		wmlp.y = targetLoc[1];
		wmlp.x = targetLoc[0] - (int)(view.getMeasuredWidth()*0.66);
	    window.setAttributes(wmlp);
	}

	public static ColorPickerDialog newInstance(Button targetButton) {
		ColorPickerDialog newInstance = new ColorPickerDialog();
		newInstance.targetButton = targetButton;
		return newInstance;
	}
}
