package de.fau.cs.mad.gamekobold.template_generator;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.template_generator.FolderElementData.element_type;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class CreateElementDialog extends DialogFragment{

	private static final String DIALOG_TEXT_BUNDLE= "dialogText";
    private static final String DIALOG_TEXT_SELECTION_START_BUNDLE = "dialogTextSelectionStart";
    private static final String DIALOG_TEXT_SELECTION_END_BUNDLE = "dialogTextSelectionEnd";
	
	boolean editable = false;
	EditText nameInput = null;
	
	public static CreateElementDialog newInstance(boolean editable) {
		CreateElementDialog dialog = new CreateElementDialog();
		dialog.editable = editable;
		return dialog;
	}
	
	@Override
	public void onCreate(Bundle icicle) {
	    this.setCancelable(true);
	    super.onCreate(icicle);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState); // <-- must call this if you want to retain dialogFragment upon rotation
	    outState.putString(DIALOG_TEXT_BUNDLE, nameInput.getText().toString());
	    outState.putInt(DIALOG_TEXT_SELECTION_START_BUNDLE, nameInput.getSelectionStart());
	    outState.putInt(DIALOG_TEXT_SELECTION_END_BUNDLE, nameInput.getSelectionEnd());
    
	}
	
	@Override
	public void onDestroyView() {
	    if (getDialog() != null && getRetainInstance())
	        getDialog().setDismissMessage(null);
	    super.onDestroyView();

	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SlideoutNavigationActivity.theActiveActivity);
		LayoutInflater inflater = SlideoutNavigationActivity.theActiveActivity.getLayoutInflater();
		View dialogViewCreateElement = inflater.inflate(R.layout.alertdialog_template_generator_add_new_element, null);
		nameInput = (EditText) dialogViewCreateElement.findViewById(R.id.enter_name_of_element);
		nameInput.setSingleLine();
		alertDialogBuilder.setView(dialogViewCreateElement);
		ImageButton createTable = (ImageButton) dialogViewCreateElement.findViewById(R.id.create_table);
		ImageButton createCollection= (ImageButton) dialogViewCreateElement.findViewById(R.id.create_collection);
		ImageButton createFolder = (ImageButton) dialogViewCreateElement.findViewById(R.id.create_folder);
		createTable.setOnClickListener(new View.OnClickListener() {
			//	private final EditText elementName = (EditText)view.findViewById(R.id.enter_name_of_element);
			@Override
			public void onClick(View v) {
				// check if a name has been set
				if(nameInput.getText().toString().isEmpty()) {
					Toast.makeText(getActivity(), getResources().getString(R.string.alert_set_element_name), Toast.LENGTH_SHORT).show();
					return;
				}
				((FolderFragment) getTargetFragment()).addItemList(editable, element_type.table, nameInput.getText().toString());
				//				dialogCreateElement.cancel();
				dismiss();
				nameInput.setText("");
			}
		});
		createCollection.setOnClickListener(new View.OnClickListener() {
			//private final EditText elementName = (EditText)view.findViewById(R.id.enter_name_of_element);
			@Override
			public void onClick(View v) {
				// check if a name has been set
				if(nameInput.getText().toString().isEmpty()) {
					Toast.makeText(getActivity(), getResources().getString(R.string.alert_set_element_name), Toast.LENGTH_SHORT).show();
					return;
				}
				((FolderFragment) getTargetFragment()).addItemList(editable, element_type.matrix, nameInput.getText().toString());
				//				dialogCreateElement.cancel();
				dismiss();
				nameInput.setText("");
			}
		});
		createFolder.setOnClickListener(new View.OnClickListener() {
			//private final EditText elementName = (EditText)view.findViewById(R.id.enter_name_of_element);
			@Override
			public void onClick(View v) {
				// check if a name has been set
				if(nameInput.getText().toString().isEmpty()) {
					Toast.makeText(getActivity(), getResources().getString(R.string.alert_set_element_name), Toast.LENGTH_SHORT).show();
					return;
				}
				((FolderFragment) getTargetFragment()).addItemList(editable, element_type.folder, nameInput.getText().toString());
				//				dialogCreateElement.cancel();
//                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
				dismiss();
				nameInput.setText("");
			}
		});

		if (savedInstanceState != null) {
			Log.d("CreateElementDialog","savedInstanceState != null");
            String text = savedInstanceState.getString(DIALOG_TEXT_BUNDLE);
            int selectionStart = savedInstanceState.getInt(DIALOG_TEXT_SELECTION_START_BUNDLE);
            int selectionEnd = savedInstanceState.getInt(DIALOG_TEXT_SELECTION_END_BUNDLE);

            nameInput.setText(text);
            nameInput.setSelection(selectionStart, selectionEnd);
		}

        return alertDialogBuilder.create();
		
	}
}
