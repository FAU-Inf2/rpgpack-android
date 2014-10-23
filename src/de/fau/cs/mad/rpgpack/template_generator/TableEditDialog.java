package de.fau.cs.mad.rpgpack.template_generator;

import de.fau.cs.mad.rpgpack.R;
import de.fau.cs.mad.rpgpack.SlideoutNavigationActivity;
import de.fau.cs.mad.rpgpack.template_generator.SessionMonitorEditText.OnEditSessionCompleteListener;
import de.fau.cs.mad.rpgpack.template_generator.TableFragment.content_type;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class TableEditDialog extends DialogFragment {
	
	TableLayout dialogTable;
	TableLayout storedDialogTable = null;
	TableFragment targetFragment;
	AlertDialog dialog;
	AlertDialog.Builder alertDialogBuilder;
	SessionMonitorEditText dialogRowCounter;
	boolean newlyShown = true;
	View.OnFocusChangeListener headerNameFocusListener = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		Log.d("onCreateDialog", "onCreate OFFFFFFFFFFFFFFFFFFFFFFF Dialog!!!!");
		this.setCancelable(true);
		setRetainInstance(true);
        //create the dialog-builder
//		View dialogViewTableView = getActivity().getLayoutInflater().inflate(R.layout.alertdialog_template_generator_tableview, null);
//		dialogTable = ((TableLayout) dialogViewTableView.findViewById(R.id.tableView_alert_table));
//		adaptDialogTable(dialogTable, 3);

//		storedDialogTable = new TableLayout(getActivity());

        alertDialogBuilder = new AlertDialog.Builder(SlideoutNavigationActivity.theActiveActivity);
        alertDialogBuilder
        .setCancelable(false)
        .setPositiveButton(SlideoutNavigationActivity.theActiveActivity.getResources().getString(R.string.save_table),new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog,int id) {
        		// surrounded by a try: if the input field is empty we get a numberformatexception. so catch it and do nothing
        		try {
        			//TODO
        			dialogRowCounter.notifyOnDemand();
        			final int columnCount =	Integer.parseInt(dialogRowCounter.getText().toString());
        			targetFragment.setAmountOfColumns(columnCount);
            		adaptHeaderTable(dialogTable);
            		targetFragment.setTypeContents(dialogTable);
        		}
        		catch(NumberFormatException e) {
        			e.printStackTrace();
        		}
        		dialog.dismiss();
        	}
        })
        .setNegativeButton(SlideoutNavigationActivity.theActiveActivity.getResources().getString(R.string.go_back),new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog,int id) {
        		dialog.dismiss();
        	}
        });

        headerNameFocusListener = new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
//				Log.d("TABLE_EDIT_DIALOG", "focus change, view:"+v+" focus:"+hasFocus);
				final EditText editText = (EditText)v;
				// get localized headline string
				final String HEADLINE_STRING = getResources().getString(R.string.headline);
				// if has focus
				if(hasFocus) {
					// our regex to match
					final String regex = "^"+HEADLINE_STRING+" [0-9]+$";
					// check match
					if(editText.getEditableText().toString().matches(regex)) {
//						Log.d("TABLE_EDIT_DIALOG", "regex match");
						// if match, clear text
						editText.setText("");
					}
//					else {
//						Log.d("TABLE_EDIT_DIALOG", "regex DID NOT match");
//					}
				}
				else {
					// check if the title is empty
					if(editText.getEditableText().toString().isEmpty()) {
						// get parent = row
						final TableRow row = (TableRow)v.getParent();
						// counter for column index
						int columnNumberCounter = 1;
						// get column index for our view
						for(;columnNumberCounter < dialogTable.getChildCount(); ++columnNumberCounter) {
							if(dialogTable.getChildAt(columnNumberCounter).equals(row)) {
								break;
							}
						}
						// set correct title
						editText.setText(HEADLINE_STRING+" "+String.valueOf(columnNumberCounter));
					}
				}
			}
		};
        
		super.onCreate(savedInstanceState);
	}
	
	static TableEditDialog newInstance() {
		return new TableEditDialog();
    }

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		Log.d("onCreateDialog", "onCreateDialog!!!!");
		View dialogViewTableView = getActivity().getLayoutInflater().inflate(R.layout.alertdialog_template_generator_tableview, new LinearLayout(getActivity()), false);
		dialogTable = ((TableLayout) dialogViewTableView.findViewById(R.id.tableView_alert_table));

		dialogRowCounter = (SessionMonitorEditText) dialogViewTableView.findViewById(R.id.edit_spaltenanzahl);
        dialogRowCounter.setOnEditSessionCompleteListener(new OnEditSessionCompleteListener() {
        	@Override
        	public void onEditSessionComplete(TextView v) {
        		// surrounded by a try: if the input field is empty we get a numberformatexception. so catch it and do nothing
        		try {
        			final int valueGiven = (Integer.parseInt(v.getText().toString()));
            		if(valueGiven < 0){
            			v.setText(Integer.toString(1));
            		}
            		else if(valueGiven > 99){
            			v.setText(Integer.toString(99));
            		}
					Log.d("TableEditDialog", "adaptDialogTable FALSE");
            		adaptDialogTable(dialogTable, valueGiven, false);	
        		}
        		catch(NumberFormatException e) {
        			e.printStackTrace();
        		}
        	}
        });
        //create add and subtract buttons for the dialog
        ImageButton addButton = (ImageButton) dialogViewTableView.findViewById(R.id.button_add_column);
        addButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		int oldValue;
        		// surrounded by a try: if the input field is empty we get a numberformatexception. so catch it and do nothing
        		try {
        			oldValue = (Integer.parseInt(dialogRowCounter.getText().toString()));
        		}
        		catch(NumberFormatException e) {
        			return;
        		}
        		int newValue = oldValue+1;
        		dialogRowCounter.setText(Integer.toString(newValue));
        		adaptDialogTable(dialogTable, (Integer.parseInt(dialogRowCounter.getText().toString())), false);
        	}
        });
        ImageButton subtractButton = (ImageButton) dialogViewTableView.findViewById(R.id.button_remove_column);
        subtractButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		int oldValue;
        		// surrounded by a try: if the input field is empty we get a numberformatexception. so catch it and do nothing
        		try {
            		oldValue = (Integer.parseInt(dialogRowCounter.getText().toString()));        			
        		}
        		catch(NumberFormatException e) {
        			return;
        		}
        		int newValue = oldValue-1;
        		// preventing negativ row count and deletion of table header
        		if(newValue < 1) {
        			return;
        		}
        		dialogRowCounter.setText(Integer.toString(newValue));
        		adaptDialogTable(dialogTable, (Integer.parseInt(dialogRowCounter.getText().toString())), false);
        	}
        });

        if(storedDialogTable != null){
			Log.d("storedDialogTable","storedDialogTable != null");
	        dialogRowCounter.setText(Integer.toString(storedDialogTable.getChildCount()-1));
        	copyDialogTable(dialogTable, storedDialogTable);
        }
        else{
			Log.d("storedDialogTable","storedDialogTable == null");
			TableRow headerRow = (TableRow) targetFragment.headerTable.getChildAt(0);
        	int rows = headerRow.getChildCount();
        	dialogRowCounter.setText(Integer.toString(rows));
			Log.d("storedDialogTable","rows == " + rows);
    		adaptDialogTable(dialogTable, rows);
        }
		
//		if(newlyShown){
//        	adaptDialogTableNames(dialogTable, 0, amountCurrent);
//		}
//		adaptDialogTable(dialogTable, (Integer.parseInt(dialogRowCounter.getText().toString())));

		alertDialogBuilder.setView(dialogViewTableView);
		dialog = alertDialogBuilder.create();
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		newlyShown = false;

        return dialog;
    }

//	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	    getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	    super.onViewCreated(view, savedInstanceState);
	}
	
	private void copyDialogTable(TableLayout dialogTable,
			TableLayout storedDialogTable) {
//		int firstRowToAdd = dialogTable.getChildCount();
		int rowsNeeded = storedDialogTable.getChildCount()-1;
		for(int i=-1; i<rowsNeeded; i++){
			TableRow newRow = new TableRow(getActivity());
			TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
			rowParams.height = TableRow.LayoutParams.WRAP_CONTENT;
			rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;
			//use k columns
			TableRow storedRow = null;
			storedRow = (TableRow) storedDialogTable.getChildAt(i+1);
			for(int k=0; k<3; k++){
				View theView = null;
				if(k==2 && i != -1){
					Spinner spin = new Spinner(SlideoutNavigationActivity.theActiveActivity);
					String [] spin_arry = SlideoutNavigationActivity.theActiveActivity.getResources().getStringArray(R.array.spaltentypen);
					spin.setAdapter(targetFragment.new DialogSpinnerAdapter<CharSequence>(SlideoutNavigationActivity.theActiveActivity, spin_arry));
					theView = spin;
					targetFragment.setSpinnerStyle(spin);
					spin.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
							((TextView) parent.getChildAt(0)).setTextColor(SlideoutNavigationActivity.theActiveActivity.getResources().getColor(R.color.background));
							
						}
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							
						}
					});
					Log.d("i,k", "i==" + i+ ", k==" +k);
					spin.setSelection(((Spinner) storedRow.getChildAt(k)).getSelectedItemPosition());
				}
				else{
					final EditText oneColumn = new EditText(SlideoutNavigationActivity.theActiveActivity);
					targetFragment.setTableStyle((EditText) oneColumn);
					if (i == -1) {
						if (k == 0) {
							oneColumn.setText(SlideoutNavigationActivity.theActiveActivity.getResources().getString(R.string.number));
						} else if (k == 1) {
							oneColumn.setText(SlideoutNavigationActivity.theActiveActivity.getResources().getString(R.string.column_name));
						} else {
							oneColumn.setText(SlideoutNavigationActivity.theActiveActivity.getResources().getString(R.string.column_type));
						}
					}
					// insert the number of the column in the first row
					else if (k == 0) {
						oneColumn.setText((i + 1) + SlideoutNavigationActivity.theActiveActivity.getResources().getString(R.string.dot));
					} else if (k == 1) {
//						TableRow headerRow = (TableRow) targetFragment.headerTable
//								.getChildAt(0);
//						EditText headerText = (EditText) headerRow
//								.getChildAt(i);
//						// Log.d("i,k", "i==" + ", k==" +k);
//						if (headerText == null) {
//							oneColumn.setText("");
//						}
//						else{
//							oneColumn.setText(headerText.getText());
//						}
						oneColumn.setText(((TextView) storedRow.getChildAt(k)).getText());
					}
					theView = oneColumn;
				}
				newRow.addView(theView);
			}
			dialogTable.addView(newRow);
		}
	}
	
	private void setSpinner(Spinner spin, content_type type){
		if(type == content_type.editText){
			spin.setSelection(0);
		}
		else if(type == content_type.checkbox){
			spin.setSelection(1);
		}
		else{
			spin.setSelection(2);
		}
	}

	/**
	 * adapts the header table to match the caption of the given dialogTable
	 * @param dialogTable
	 */
	protected void adaptHeaderTable(TableLayout dialogTable){
		// get localized headline string
		final String HEADLINE_STRING = getResources().getString(R.string.headline);
		int amountRows = dialogTable.getChildCount()-1;
		for(int i=1; i<amountRows+1; i++){
			String otherString = ((EditText) ((TableRow) dialogTable.getChildAt(i)).getChildAt(1)).getText().toString();
			TextView textToChange = ((TextView) ((TableRow) targetFragment.headerTable.getChildAt(0)).getChildAt(i-1));
			if(otherString.isEmpty()) {
				otherString = HEADLINE_STRING+" "+String.valueOf(i);
			}
			textToChange.setText(otherString);
		}
	}
	
	
	protected void adaptDialogTable(TableLayout dialogTable, int rowsToShow){
		final Throwable throwable = new Throwable();
		throwable.printStackTrace();
		adaptDialogTable(dialogTable, rowsToShow, true);
	}
	
	/**
	 * adapt the dialog table to have exactly rowsToShow rows
	 * @param dialogTable
	 * @param rowsToShow
	 * @param readFromTable if true: values are read from underlying table <br>
	 * if false: values are taken as they are already stored in this temporary dialog-table
	 */
	protected void adaptDialogTable(TableLayout dialogTable, int rowsToShow, boolean readFromTable){
		int firstRowToAdd = dialogTable.getChildCount();
		int rowsNeeded = rowsToShow;
		//first step: adapt all needed Column-names from headerTable
		int oldColumnsToAdept = (firstRowToAdd<rowsNeeded? firstRowToAdd:rowsNeeded);
		for(int i=1; i<oldColumnsToAdept; i++){
			TableRow headerRow = (TableRow) targetFragment.headerTable.getChildAt(0);
			TextView headerText = (TextView) headerRow.getChildAt(i-1);
			EditText textToSet = (EditText) ((TableRow) dialogTable.getChildAt(i)).getChildAt(1);
			if(readFromTable){
				if(headerText == null){
					textToSet.setText("");
				}
				else{
					textToSet.setText(headerText.getText());
				}
			}
			//dont adapt spinner now, it might have been modified and we want to keep that
//			Spinner spinnerToSet = (Spinner) ((TableRow) dialogTable.getChildAt(i)).getChildAt(2);
//			setSpinner(spinnerToSet, targetFragment.getType(i));
		}
		//second step: add rows if needed
		for(int i=firstRowToAdd-1; i<rowsNeeded; i++){
			final TableRow row = new TableRow(SlideoutNavigationActivity.theActiveActivity);
			TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
			rowParams.height = TableRow.LayoutParams.WRAP_CONTENT;
			rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;
			//use k columns
			for(int k=0; k<3; k++){
				View theView = null;
				if(k==2 && i != -1){
					Spinner spin = new Spinner(SlideoutNavigationActivity.theActiveActivity);
					String [] spin_arry = SlideoutNavigationActivity.theActiveActivity.getResources().getStringArray(R.array.spaltentypen);
					spin.setAdapter(targetFragment.new DialogSpinnerAdapter<CharSequence>(SlideoutNavigationActivity.theActiveActivity, spin_arry));
					theView = spin;
					targetFragment.setSpinnerStyle(spin);
					spin.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
							((TextView) parent.getChildAt(0)).setTextColor(SlideoutNavigationActivity.theActiveActivity.getResources().getColor(R.color.background));
							
						}
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							
						}
					});
					setSpinner(spin, targetFragment.getType(i));
				}
				else{
					final EditText oneColumn = new EditText(SlideoutNavigationActivity.theActiveActivity);
					targetFragment.setTableStyle((EditText) oneColumn);
					if (i == -1) {
						if (k == 0) {
							oneColumn.setText(SlideoutNavigationActivity.theActiveActivity.getResources().getString(R.string.number));
						} else if (k == 1) {
							oneColumn.setText(SlideoutNavigationActivity.theActiveActivity.getResources().getString(R.string.column_name));
						} else {
							oneColumn.setText(SlideoutNavigationActivity.theActiveActivity.getResources().getString(R.string.column_type));
						}
					}
					// insert the number of the column in the first row
					else if (k == 0) {
						oneColumn.setText((i + 1) + SlideoutNavigationActivity.theActiveActivity.getResources().getString(R.string.dot));
					} else if (k == 1) {
						TableRow headerRow = (TableRow) targetFragment.headerTable
								.getChildAt(0);
						TextView headerText = (TextView) headerRow
								.getChildAt(i);
						// Log.d("i,k", "i==" + ", k==" +k);
						if (headerText == null) {
//							oneColumn.setText(SlideoutNavigationActivity.theActiveActivity.getResources().getString(R.string.headline)+" "+String.valueOf(i+1));
							oneColumn.setText("");
						}
						else{
							oneColumn.setText(headerText.getText());
						}
					}
					theView = oneColumn;
				}
				row.addView(theView);
			}
			dialogTable.addView(row);
		}
		//last step: remove rows if needed
		for(int i=rowsNeeded+1; i<firstRowToAdd; i++){
			dialogTable.removeView(dialogTable.getChildAt(dialogTable.getChildCount()-1));
		}
		// set focus listener to all column header title elements
        for(int i = 1; i < dialogTable.getChildCount(); ++i) {
        	View column = ((TableRow)dialogTable.getChildAt(i)).getChildAt(1);
        	column.setOnFocusChangeListener(headerNameFocusListener);
        }
	}

	@Override
	public void onDestroyView() {
	  if (getDialog() != null && getRetainInstance())
	    getDialog().setDismissMessage(null);
	  super.onDestroyView();
	}
	
	@Override
	public void onDismiss(DialogInterface dialog){
		getFragmentManager().beginTransaction().remove(this).commit(); 
		storedDialogTable = null;
		newlyShown = true;
	}
	
	@Override
	public void onSaveInstanceState(Bundle bundle){
//		Log.d("onCreateDialog", "onSaveInstanceStateeeeeeeeeeee!!!!");
		storedDialogTable = dialogTable;
		super.onSaveInstanceState(bundle);
	}

	public void setTargetFragment(TableFragment tableFragment,
			int dialogFragment) {
		targetFragment  = tableFragment;
	}
}
