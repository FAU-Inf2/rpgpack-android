package de.fau.cs.mad.gamekobold.template_generator;


import de.fau.cs.mad.gamekobold.R;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TableFragment extends GeneralFragment {

	View view;
	TableLayout table;
	int amountColumns = 2;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
	
	protected TableLayout createTableHeader(){
		TableLayout headerTable = (TableLayout) view.findViewById(R.id.header_table);
//		private static TableLayout addRowToTable(TableLayout table, String contentCol1, String contentCol2) {
		Context context = getActivity();
		TableRow row = new TableRow(context);

		TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
		// Wrap-up the content of the row
		rowParams.height = TableRow.LayoutParams.WRAP_CONTENT;
		rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;

		// The simplified version of the table of the picture above will have two columns
		// FIRST COLUMN
		TableRow.LayoutParams col1Params = new TableRow.LayoutParams();
		// Wrap-up the content of the row
		col1Params.height = TableRow.LayoutParams.WRAP_CONTENT;
		col1Params.width = TableRow.LayoutParams.WRAP_CONTENT;
		// Set the gravity to center the gravity of the column
//		col1Params.gravity = TableRow.Gravity.CENTER;
		EditText col1 = new EditText(context);
		col1.setSingleLine();
		col1.setText("test123");
		row.addView(col1, col1Params);

		// SECOND COLUMN
		TableRow.LayoutParams col2Params = new TableRow.LayoutParams();
		// Wrap-up the content of the row
		col2Params.height = TableRow.LayoutParams.WRAP_CONTENT;
		col2Params.width = TableRow.LayoutParams.WRAP_CONTENT;
		// Set the gravity to center the gravity of the column
//		col2Params.gravity = Gravity.CENTER;
		EditText col2 = new EditText(context);
		col2.setSingleLine();
		col2.setText("test456");
		row.addView(col2, col2Params);

		headerTable.addView(row, rowParams);
		

		return headerTable;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		view = (LinearLayout) inflater.inflate(R.layout.template_generator_table_view, null);
        createTableHeader();
		Button buttonAdd = (Button)view.findViewById(R.id.add);
		buttonAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addItemList();
			}
		});
		
		table = (TableLayout) view.findViewById(R.id.template_generator_table);
		TableRow row= new TableRow(getActivity());
		TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
		row.setLayoutParams(lp);
		Button addColumnButton = (Button)view.findViewById(R.id.add_Column);
		addColumnButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addColumn();
			}
		});
		Button removeColumnButton = (Button)view.findViewById(R.id.remove_Column);
		removeColumnButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				removeColumn();
			}
		});

		return view;
	}

	//adds a new row to the listview
	protected void addItemList() {
	        TableRow row= new TableRow(getActivity());
	        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
	        row.setLayoutParams(lp);
	        table.addView(row);
	        for(int i=0; i<amountColumns; ++i){
	        	addColumnToRow(row);
	        }
	}

	//adds a new row to the listview
	@Override
	protected void addItemList(int selected) {
		addItemList();
		
	}
	
	protected void addColumnToRow(TableRow row){
		EditText oneColumn = new EditText(getActivity());
		oneColumn.setSingleLine();
		oneColumn.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//		oneColumn.addTextChangedListener(new TextWatcher(){
//	        public void afterTextChanged(Editable s) {
//	        	int longestRow = 0;
//	        	int columnNumber = 0;
//	        	for (int i = 0; i < table.getChildCount(); i++) {
//					View child = table.getChildAt(i);
//					TableRow row = (TableRow) child;
//					if (child instanceof TableRow) {
//						for (int x = 0; x < row.getChildCount(); x++) {
//							View view = row.getChildAt(x);
//							int lengthRow = 0;
//							if(view instanceof EditText){
//								lengthRow = ((EditText) view).length();
//								if(lengthRow > longestRow){
//									longestRow = lengthRow;
//									columnNumber = x;
//								}
//							}
//						}
//					}
//				}
//	        	TableLayout headerTable = (TableLayout) view.findViewById(R.id.header_table);
//	        	//get first child -> is the one TextRow == header
//	        	View oneRow = headerTable.getChildAt(0);
//	        	View element = null;
//	        	if(oneRow instanceof TableRow){
//	        		element = ((TableRow) oneRow).getChildAt(columnNumber);
//	        	}
//	        	else{
//	                Log.d("critical", "exception in TableFragment!");
//	                System.exit(-1);
//	        	}
//	        	String resultingString = "";
//	        	for(int i=0; i<longestRow; ++i){
//	        		resultingString.concat("-");
//	        	}
//	        	((EditText) element).setText(resultingString);
////				headerTable = addRowToTable(headerTable, longestRow.split("-")[0], longestRow.split("-")[1]);
//	        	
//	        }
//	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
//	        public void onTextChanged(CharSequence s, int start, int before, int count){}
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				int longestRow = 0;
	        	int columnNumber = 0;
	        	for (int i = 0; i < table.getChildCount(); i++) {
					View child = table.getChildAt(i);
					TableRow row = (TableRow) child;
					if (child instanceof TableRow) {
						for (int x = 0; x < row.getChildCount(); x++) {
							View view = row.getChildAt(x);
							int lengthRow = 0;
							if(view instanceof EditText){
								lengthRow = ((EditText) view).length();
								if(lengthRow > longestRow){
									longestRow = lengthRow;
									columnNumber = x;
								}
							}
						}
					}
				}
	        	TableLayout headerTable = (TableLayout) view.findViewById(R.id.header_table);
	        	//get first child -> is the one TextRow == header
	        	View oneRow = headerTable.getChildAt(0);
	        	View element = null;
	        	if(oneRow instanceof TableRow){
	        		element = ((TableRow) oneRow).getChildAt(columnNumber);
	        	}
	        	else{
	                Log.d("critical", "exception in TableFragment!");
	                System.exit(-1);
	        	}
	        	String resultingString = "";
	        	for(int i=0; i<longestRow; ++i){
	        		resultingString.concat("-");
	        	}
	        	((EditText) element).setText(resultingString);
				return false;
			}
	    }); 
		
		
		
        oneColumn.setText("empty");
        row.addView(oneColumn);
	}
	
//	protected void removeColumnFromRow(TableRow row){
//		int elements = row.getChildCount();
//	}
	
	protected void addColumn(){
		amountColumns++;
		for (int i = 0; i < table.getChildCount(); i++) {
		    View child = table.getChildAt(i);

		    if (child instanceof TableRow) {
		        TableRow row = (TableRow) child;
		        EditText newColumn = new EditText(getActivity());
		        newColumn.setText("empty");
		        newColumn.setSingleLine();
		        row.addView(newColumn);
//		        table.addView(row);		        
//		        for (int x = 0; x < row.getChildCount(); x++) {
//		            View view = row.getChildAt(x);
//		            view.setEnabled(false);
//		        }
		    }
		}
        Log.d("columns", "amount == " + amountColumns);

	}
	
	protected void removeColumn(){
		amountColumns--;
		if(amountColumns < 1){
			amountColumns = 1;
		}
		else{
			for (int i = 0; i < table.getChildCount(); i++) {
				View child = table.getChildAt(i);

				if (child instanceof TableRow) {
					TableRow row = (TableRow) child;
					row.removeView(row.getChildAt(row.getChildCount()-1));
					//		        for (int x = 0; x < row.getChildCount(); x++) {
					//		            View view = row.getChildAt(x);
					//		            row.removeView(view);
					////		            view.setEnabled(false);
					//		        }
				}
			}
			Log.d("columns", "amount == " + amountColumns);
		}
	}
	
}
