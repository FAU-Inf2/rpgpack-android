package de.fau.cs.mad.gamekobold.template_generator;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.template_generator.SessionMonitorEditText.OnEditSessionCompleteListener;
import de.fau.cs.mad.gamekobold.template_generator.TableFragment.content_type;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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

        alertDialogBuilder = new AlertDialog.Builder(TemplateGeneratorActivity.theActiveActivity);
        alertDialogBuilder
        .setCancelable(false)
        .setPositiveButton(TemplateGeneratorActivity.theActiveActivity.getResources().getString(R.string.save_table),new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog,int id) {
        		targetFragment.setAmountOfColumns(Integer.parseInt(dialogRowCounter.getText().toString()));
        		adaptHeaderTable(dialogTable);
        		targetFragment.setTypeContents(dialogTable);
        		dialog.dismiss();
        	}
        })
        .setNegativeButton(TemplateGeneratorActivity.theActiveActivity.getResources().getString(R.string.go_back),new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog,int id) {
        		dialog.dismiss();
        	}
        });
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
        		int valueGiven = (Integer.parseInt(v.getText().toString()));
        		if(valueGiven < 0){
        			v.setText(Integer.toString(1));
        		}
        		else if(valueGiven > 99){
        			v.setText(Integer.toString(99));
        		}
        		adaptDialogTable(dialogTable, valueGiven);
        	}
        });
        //create add and subtract buttons for the dialog
        ImageButton addButton = (ImageButton) dialogViewTableView.findViewById(R.id.button_add_column);
        addButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		int oldValue = (Integer.parseInt(dialogRowCounter.getText().toString()));
        		int newValue = oldValue+1;
        		dialogRowCounter.setText(Integer.toString(newValue));
        		adaptDialogTable(dialogTable, (Integer.parseInt(dialogRowCounter.getText().toString())));
        	}
        });
        ImageButton subtractButton = (ImageButton) dialogViewTableView.findViewById(R.id.button_remove_column);
        subtractButton.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		int oldValue = (Integer.parseInt(dialogRowCounter.getText().toString()));
        		int newValue = oldValue-1;
        		dialogRowCounter.setText(Integer.toString(newValue));
        		adaptDialogTable(dialogTable, (Integer.parseInt(dialogRowCounter.getText().toString())));
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
		newlyShown = false;

        return dialog;
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
					Spinner spin = new Spinner(TemplateGeneratorActivity.theActiveActivity);
					String [] spin_arry = TemplateGeneratorActivity.theActiveActivity.getResources().getStringArray(R.array.spaltentypen);
					spin.setAdapter(targetFragment.new DialogSpinnerAdapter<CharSequence>(TemplateGeneratorActivity.theActiveActivity, spin_arry));
					theView = spin;
					targetFragment.setSpinnerStyle(spin);
					spin.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
							((TextView) parent.getChildAt(0)).setTextColor(TemplateGeneratorActivity.theActiveActivity.getResources().getColor(R.color.background));
							
						}
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							
						}
					});
					Log.d("i,k", "i==" + i+ ", k==" +k);
					spin.setSelection(((Spinner) storedRow.getChildAt(k)).getSelectedItemPosition());
				}
				else{
					final EditText oneColumn = new EditText(TemplateGeneratorActivity.theActiveActivity);
					targetFragment.setTableStyle((EditText) oneColumn);
					if (i == -1) {
						if (k == 0) {
							oneColumn.setText(TemplateGeneratorActivity.theActiveActivity.getResources().getString(R.string.number));
						} else if (k == 1) {
							oneColumn.setText(TemplateGeneratorActivity.theActiveActivity.getResources().getString(R.string.column_name));
						} else {
							oneColumn.setText(TemplateGeneratorActivity.theActiveActivity.getResources().getString(R.string.column_type));
						}
					}
					// insert the number of the column in the first row
					else if (k == 0) {
						oneColumn.setText((i + 1) + TemplateGeneratorActivity.theActiveActivity.getResources().getString(R.string.dot));
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
		int amountRows = dialogTable.getChildCount()-1;
		for(int i=1; i<amountRows+1; i++){
			String otherString = ((EditText) ((TableRow) dialogTable.getChildAt(i)).getChildAt(1)).getText().toString();
			EditText textToChange = ((EditText) ((TableRow) targetFragment.headerTable.getChildAt(0)).getChildAt(i-1));
			if(!otherString.equals("")){
				textToChange.setText(otherString);
			}
		}
	}
	
	/**
	 * adapt the dialog table to have exactly rowsToShow rows
	 * @param dialogTable
	 * @param rowsToShow
	 */
	protected void adaptDialogTable(TableLayout dialogTable, int rowsToShow){
		int firstRowToAdd = dialogTable.getChildCount();
		int rowsNeeded = rowsToShow;
		//first step: adapt all needed Column-names from headerTable
		int oldColumnsToAdept = (firstRowToAdd<rowsNeeded? firstRowToAdd:rowsNeeded);
		for(int i=1; i<oldColumnsToAdept; i++){
			TableRow headerRow = (TableRow) targetFragment.headerTable.getChildAt(0);
			EditText headerText = (EditText) headerRow.getChildAt(i-1);
			EditText textToSet = (EditText) ((TableRow) dialogTable.getChildAt(i)).getChildAt(1);
			if(headerText == null){
				textToSet.setText("");
			}
			else{
				textToSet.setText(headerText.getText());
			}
			//dont adapt spinner now, it might have been modified and we want to keep that
//			Spinner spinnerToSet = (Spinner) ((TableRow) dialogTable.getChildAt(i)).getChildAt(2);
//			setSpinner(spinnerToSet, targetFragment.getType(i));
		}
		//second step: add rows if needed
		for(int i=firstRowToAdd-1; i<rowsNeeded; i++){
			final TableRow row = new TableRow(TemplateGeneratorActivity.theActiveActivity);
			TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
			rowParams.height = TableRow.LayoutParams.WRAP_CONTENT;
			rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;
			//use k columns
			for(int k=0; k<3; k++){
				View theView = null;
				if(k==2 && i != -1){
					Spinner spin = new Spinner(TemplateGeneratorActivity.theActiveActivity);
					String [] spin_arry = TemplateGeneratorActivity.theActiveActivity.getResources().getStringArray(R.array.spaltentypen);
					spin.setAdapter(targetFragment.new DialogSpinnerAdapter<CharSequence>(TemplateGeneratorActivity.theActiveActivity, spin_arry));
					theView = spin;
					targetFragment.setSpinnerStyle(spin);
					spin.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
							((TextView) parent.getChildAt(0)).setTextColor(TemplateGeneratorActivity.theActiveActivity.getResources().getColor(R.color.background));
							
						}
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							
						}
					});
					setSpinner(spin, targetFragment.getType(i));
				}
				else{
					final EditText oneColumn = new EditText(TemplateGeneratorActivity.theActiveActivity);
					targetFragment.setTableStyle((EditText) oneColumn);
					if (i == -1) {
						if (k == 0) {
							oneColumn.setText(TemplateGeneratorActivity.theActiveActivity.getResources().getString(R.string.number));
						} else if (k == 1) {
							oneColumn.setText(TemplateGeneratorActivity.theActiveActivity.getResources().getString(R.string.column_name));
						} else {
							oneColumn.setText(TemplateGeneratorActivity.theActiveActivity.getResources().getString(R.string.column_type));
						}
					}
					// insert the number of the column in the first row
					else if (k == 0) {
						oneColumn.setText((i + 1) + TemplateGeneratorActivity.theActiveActivity.getResources().getString(R.string.dot));
					} else if (k == 1) {
						TableRow headerRow = (TableRow) targetFragment.headerTable
								.getChildAt(0);
						EditText headerText = (EditText) headerRow
								.getChildAt(i);
						// Log.d("i,k", "i==" + ", k==" +k);
						if (headerText == null) {
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
