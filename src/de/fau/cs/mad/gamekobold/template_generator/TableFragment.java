package de.fau.cs.mad.gamekobold.template_generator;


import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.R.color;
import de.fau.cs.mad.gamekobold.jackson.ColumnHeader;
import de.fau.cs.mad.gamekobold.jackson.StringClass;
import de.fau.cs.mad.gamekobold.jackson.Table;
import de.fau.cs.mad.gamekobold.template_generator.DataAdapter.ViewHolder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

@SuppressLint("NewApi")
public class TableFragment extends GeneralFragment implements NumberPicker.OnValueChangeListener{
	/*
	 * JACKSON START
	 */
	Table jacksonTable;
	boolean jacksonHasBeenInflated = false;
	/*
	 * JACKSON END
	 */
	
	View view;
	TableLayout table;
	TableLayout headerTable;
	int amountColumns = 2;
	AlertDialog dialogTableView;
	View dialogViewTableView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainTemplateGenerator.theActiveActivity);
        LayoutInflater inflater = MainTemplateGenerator.theActiveActivity.getLayoutInflater();
        dialogViewTableView = inflater.inflate(R.layout.alertdialog_template_generator_tableview, null);
        alertDialogBuilder.setView(dialogViewTableView);
        final NumberPicker np = ((NumberPicker) dialogViewTableView.findViewById(R.id.numberPicker1));
        np.setMaxValue(99);
        np.setMinValue(0);
        np.setOnValueChangedListener(this);
        //		 np.setValue(((TableFragment) currentFragment).amountColumns);
        // set dialog message
        alertDialogBuilder
        .setCancelable(false)
        .setPositiveButton("Tabelle speichern",new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog,int id) {
        		setAmountOfColumns(np.getValue());
        		TableLayout otherTable = ((TableLayout) dialogViewTableView.findViewById(R.id.tableView_alert_table));
//        		todo
        		adaptHeaderTable(otherTable);
        	}
        })
        .setNegativeButton("Zurück",new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog,int id) {
				 dialog.cancel();
			 }
		 });
		 // create alert dialog
		 dialogTableView = alertDialogBuilder.create();
    }
	
	protected void adaptHeaderTable(TableLayout otherTable){
//		TODO: make following lines work -> adapt original Table
		int amountRows = otherTable.getChildCount()-1;
		for(int i=1; i<amountRows+1; i++){
			String otherString = ((EditText) ((TableRow) otherTable.getChildAt(i)).getChildAt(1)).getText().toString();
			Log.d("String got:", "offset=" + i + ", name=" + otherString);
			EditText textToChange = ((EditText) ((TableRow) headerTable.getChildAt(0)).getChildAt(i-1));
			textToChange.setText(otherString);
			setHeaderTableStyle(textToChange);
		}
	}
	
	protected TableLayout createTableHeader() {
		headerTable = (TableLayout) view.findViewById(R.id.header_table);
//		private static TableLayout addRowToTable(TableLayout table, String contentCol1, String contentCol2) {
		Context context = getActivity();
		final TableRow row = new TableRow(context);

		TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
		// Wrap-up the content of the row
		rowParams.height = TableRow.LayoutParams.MATCH_PARENT;
		rowParams.width = TableRow.LayoutParams.MATCH_PARENT;

		// The simplified version of the table of the picture above will have two columns
		// FIRST COLUMN
		TableRow.LayoutParams colParams = new TableRow.LayoutParams();
//		// Wrap-up the content of the row
		colParams.height = TableRow.LayoutParams.MATCH_PARENT;
		colParams.width = TableRow.LayoutParams.MATCH_PARENT;
		// Set the gravity to center the gravity of the column
//		col1Params.gravity = TableRow.Gravity.CENTER;
		final ResizingEditText col1 = new ResizingEditText(context, row, this);
//		col1.setText("Headline1");
		col1.setHint("Headline1");
		setHeaderTableStyle(col1);

		col1.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				checkResize(0, 0, col1, row);
				/*
				 *  JACKSON START
				 */
				jacksonTable.setColumnTitle(0, s.toString());
				Log.d("TABLE_FRAGMENT", "title changed - saved");
				MainTemplateGenerator.saveTemplate();
				/*
				 *  JACKSON END
				 */
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}
			public void onTextChanged(CharSequence s, int start, int before, int count){
			}
		});
		row.addView(col1);

		// SECOND COLUMN
//		TableRow.LayoutParams col2Params = new TableRow.LayoutParams();
//		// Wrap-up the content of the row
//		col2Params.height = TableRow.LayoutParams.WRAP_CONTENT;
//		col2Params.width = TableRow.LayoutParams.WRAP_CONTENT;
		// Set the gravity to center the gravity of the column
//		col2Params.gravity = Gravity.CENTER;
		final ResizingEditText col2 = new ResizingEditText(context, row, this);
		setHeaderTableStyle(col2);
		col2.setHint("Headline2");
		col2.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				checkResize(0, 0, col2, row);
				/*
				 *  JACKSON START
				 */
				jacksonTable.setColumnTitle(1, s.toString());
				Log.d("TABLE_FRAGMENT", "title changed - saved");
				MainTemplateGenerator.saveTemplate();
				/*
				 *  JACKSON END
				 */
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}
			public void onTextChanged(CharSequence s, int start, int before, int count){
			}
		});
		
		row.addView(col2);
		

		headerTable.addView(row);
		
		
//		col1.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//		int elementSize = col1.getMeasuredWidth();
//		Log.d("width", "INITAL WDTH: " + elementSize);
				
		return headerTable;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		view = (RelativeLayout) inflater.inflate(R.layout.template_generator_table_view, null);
        createTableHeader();
        
		Button buttonAdd = (Button)view.findViewById(R.id.add);
		buttonAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addItemList();
			}
		});
		
		table = (TableLayout) view.findViewById(R.id.template_generator_table);
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
		//addItemList();
		
		TextView addRowBelow = (TextView)view.findViewById(R.id.add_below);
		addRowBelow.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addItemList();
			}
		});
		setAddButtonStyle(addRowBelow);
		
		/*
		 * JACKSON START
		 */
		Log.d("TableFragment", "jacksonHasBeenInflated:"+jacksonHasBeenInflated);
		final int jacksonTableColumnNumber = jacksonTable.numberOfColumns;
		// check if we have inflated the table with some data
		// BUT also check if we got any saved columns (they are only created if user goes into table!)
		// so if there are no saved columns or we didn't load any data we add the default columns 
		if(jacksonHasBeenInflated && (jacksonTableColumnNumber > 0)) {
			
			// create the right amount of columns
			Log.d("jackson table inflating","jacksonNumberCol:"+jacksonTableColumnNumber);
			Log.d("jackson table inflating","amountCol:"+amountColumns);
			while(amountColumns < jacksonTableColumnNumber) {
				Log.d("jackson table inflating","addColumn()");
				addColumn();
			}
			
			while(amountColumns > jacksonTableColumnNumber) {
				Log.d("jackson table inflating","removeColumn()");
				removeColumn();
			}
			// set titles
			TableRow headerRow = (TableRow) headerTable.getChildAt(0);
			for(int i = 0; i < amountColumns; i++) {
				View view = headerRow.getChildAt(i);
				Log.d("TABLE INFLATING", "setting column header title:"+jacksonTable.columnHeaders.get(i).name);
				((EditText)view).setText(jacksonTable.columnHeaders.get(i).name);
				setHeaderTableStyle((EditText)view);
				// check size
				checkResize(0, 0, (EditText)view, headerRow);
		        int width = getNeededWidth(i);
				int height = getNeededHeight(0, headerRow);
				final LayoutParams lparams = new LayoutParams(width, height);
			    view.setLayoutParams(lparams);
				view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			}
			Log.d("TABLE_FRAGMENT", "loaded table header data");
			jacksonHasBeenInflated = false;
		}
		else {
			// add the 2 default columns
			jacksonTable.addColumn(new ColumnHeader("Headline1", StringClass.TYPE_STRING));
			jacksonTable.addColumn(new ColumnHeader("Headline2", StringClass.TYPE_STRING));
			// save template
			Log.d("TableFragment", "added default columns");
			MainTemplateGenerator.saveTemplate();
		}
		/*
		 * JACKSON END
		 */
		addItemList();
		return view;
	}
	
	protected TableRow contextMenuRow;
	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if(v instanceof TableRow){
			contextMenuRow = (TableRow) v;
		}
		MenuInflater inflater = MainTemplateGenerator.theActiveActivity.getMenuInflater();
		inflater.inflate(R.menu.template_generator_remove_table_item, menu);
	}
	
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.removeTableRow:
			removeRow(contextMenuRow);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	protected void removeRow(TableRow row) {
        table.removeView(row);
}

	//adds a new row to the listview
	protected void addItemList() {
	        TableRow row= new TableRow(getActivity());
//	        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//	        row.setLayoutParams(lp);
	        registerForContextMenu(row);
	        
	        for(int i=0; i<amountColumns; ++i){
	        	addColumnToRow(row);
	        }
	        table.addView(row);
	        final ScrollView sv = (ScrollView) view.findViewById(R.id.table_scroll);
	        //invalidating didnt change anything
//	        table.invalidate();
//	        table.refreshDrawableState();
//	        sv.invalidate();
//	        sv.requestLayout();
//	        row.invalidate();
//	        row.requestLayout();
//	        sv.scrollTo(0, sv.getBottom());
//	        sv.fullScroll(ScrollView.FOCUS_DOWN);
	        //we have to do scrolling in seperate thread to make sure the new item is already inserted
	        sv.post(new Runnable() {
	            @Override
	            public void run() {
	            	sv.fullScroll(ScrollView.FOCUS_DOWN);
	            }
	        });
	        for(int i=0; i<((TableRow) headerTable.getChildAt(0)).getChildCount(); i++){
	        	setHeaderTableStyle((EditText) ((TableRow) headerTable.getChildAt(0)).getChildAt(i));
	        }
	}

	//adds a new row to the listview
	@Override
	protected void addItemList(int selected) {
		addItemList();
	}
	
	private int getNeededWidth(int columnIndex) {
		int longestWidth = -1;
		for (int i = 0; i < headerTable.getChildCount(); i++) {
			View child = headerTable.getChildAt(i);
			TableRow oneRow = (TableRow) child;
			if (child instanceof TableRow) {
				View view = oneRow.getChildAt(columnIndex);
				if(view != null){
					view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					int elementSize = view.getMeasuredWidth();
					if(elementSize > longestWidth){
						longestWidth = elementSize;
					}
				}
			}
		}
		for (int i = 0; i < table.getChildCount(); i++) {
			View child = table.getChildAt(i);
			TableRow oneRow = (TableRow) child;
			if (child instanceof TableRow) {
				View view = oneRow.getChildAt(columnIndex);
				if(view != null){
					view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					int elementSize = view.getMeasuredWidth();
					if(elementSize > longestWidth){
						longestWidth = elementSize;
					}
				}
			}
		}
		return longestWidth;
	}
	
	private int getNeededHeight(int rowIndex, TableRow row) {
		int maxHeight = -1;
		for (int i = 0; i < row.getChildCount(); i++) {
			View child = row.getChildAt(i);
			if(child != null){
				child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				int elementSize = child.getMeasuredHeight();
				if(elementSize > maxHeight){
					maxHeight = elementSize;
				}
			}
		}
		return maxHeight;
	}
	
	
	protected void checkResize(int width, int height, EditText textEdited, TableRow row){
		TableLayout usedTable;
		usedTable = table;
		int indexOfRow = table.indexOfChild(row);
		if(indexOfRow == -1){
			indexOfRow = headerTable.indexOfChild(row);
			if(indexOfRow == -1){
				Log.d("critical", "table to search for element not known!");
			}
			else{
				usedTable = headerTable;
			}
		}
		Log.d("index", "index  == " + indexOfRow);
		int indexOfColumn = -1;
		if(usedTable.getChildAt(indexOfRow) instanceof TableRow){
			indexOfColumn = ((TableRow) usedTable.getChildAt(indexOfRow)).indexOfChild(textEdited);
		}
		int longestWidth = 0;
		int ownColumnNumber = indexOfColumn;
		View longestView = null;
		for (int i = 0; i < table.getChildCount(); i++) {
			View child = table.getChildAt(i);
			TableRow oneRow = (TableRow) child;
			if (child instanceof TableRow) {
				View view = oneRow.getChildAt(ownColumnNumber);
				int elementSize = 0;
				if(view instanceof EditText){
					//todo
					view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					elementSize = view.getMeasuredWidth();
					if(elementSize > longestWidth){
						longestWidth = elementSize;
						longestView = view;
					}
				}
				//no need to traverse over all columns, the column we just changed is enough to adapt
//				for (int x = 0; x < row.getChildCount(); x++) {
//					View view = row.getChildAt(x);
//					int lengthRow = 0;
//					if(view instanceof EditText){
//						lengthRow = ((EditText) view).length();
//						if(lengthRow > longestRow){
//							longestRow = lengthRow;
//							columnNumber = x;
//							longestView = view;
//						}
//					}
//				}
			}
		}
		for (int i = 0; i < headerTable.getChildCount(); i++) {
			View child = headerTable.getChildAt(i);
			TableRow oneRow = (TableRow) child;
			if (child instanceof TableRow) {
				View view = oneRow.getChildAt(ownColumnNumber);
				int elementSize = 0;
				if(view instanceof EditText){
					//todo
					view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					elementSize = view.getMeasuredWidth();
					if(elementSize > longestWidth){
						longestWidth = elementSize;
						longestView = view;
					}
				}
			}
		}
		if(longestView != null){
			//force layout is needed! view has old size if we don't do it!
			//but problem: edittext doesnt resize anymore if we do it
//			longestView.forceLayout();
			longestView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//			int width = longestView.getMeasuredWidth();
			int longestHeight = longestView.getMeasuredHeight();
			if(longestWidth < width){
				longestWidth = width;
			}
			if(longestHeight < height){
				longestHeight = height;
			}
			adaptAllColumnsToSize(ownColumnNumber, longestWidth, longestHeight);
		}
	}
	
	protected void adaptAllColumnsToSize(int column, int width, int height){
//		Log.d("width", "to adapt width  == " + width);
		View singleRow = headerTable.getChildAt(0);
		if(singleRow instanceof TableRow){
			View theChildToResize = ((TableRow) singleRow).getChildAt(column);
			if(theChildToResize instanceof EditText){
				Log.d("width", "to adapt width  == " + width);
				Log.d("width", "headline width before  == " + ((EditText) theChildToResize).getWidth());
				final TableRow.LayoutParams lparams = new TableRow.LayoutParams(width,((EditText) theChildToResize).getHeight()); // Width , height
			    ((EditText) theChildToResize).setLayoutParams(lparams);
				Log.d("width", "headline width after  == " + ((EditText) theChildToResize).getWidth());
				setHeaderTableStyle((EditText) theChildToResize);
//			    textField.invalidate();
//			    textField.forceLayout();
				Log.d("header table", "size is set!!!");
			}
		}
		for(int i=0; i<table.getChildCount(); i++){
			View oneRow = table.getChildAt(i);
			if(oneRow instanceof TableRow){
				View theChildToResize = ((TableRow) oneRow).getChildAt(column);
				if(theChildToResize instanceof EditText){
					final TableRow.LayoutParams lparams = new TableRow.LayoutParams(width,((EditText) theChildToResize).getHeight()); // Width , height
				    ((EditText) theChildToResize).setLayoutParams(lparams);
				    setTableStyle((EditText) theChildToResize);
				}
				
			}
		}
	}
	
	protected void setAmountOfColumns(int amount){
		int amountToAdd = amount - amountColumns;
		if(amountToAdd > 0){
			for(int i=0; i<amountToAdd; i++){
				addColumn();
			}
		}
		else{
			for(int i=amountToAdd; i<0; i++){
				removeColumn();
			}
		}
	}
	
	protected void addColumnToRow(final TableRow row){
		final ResizingEditText oneColumn = new ResizingEditText(getActivity(), row, this);
		oneColumn.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				checkResize(0, 0, oneColumn, row);
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}
			public void onTextChanged(CharSequence s, int start, int before, int count){
			}
		});
		/*
		 * JACKSON START
		 */
		// only add to header cells
		if(row == headerTable.getChildAt(0)) {
			oneColumn.addTextChangedListener(new TextWatcher(){
				// column index for this header cell  !index! no + 1 needed
				private final int columnIndex = ((TableRow)headerTable.getChildAt(0)).getChildCount();
				// callback
				public void afterTextChanged(Editable s) {
					if(!jacksonHasBeenInflated) {
						jacksonTable.setColumnTitle(columnIndex, s.toString());
					}
					Log.d("TABLE_FRAGMENT", "title changed - save");
					MainTemplateGenerator.saveTemplate();
				}
				public void beforeTextChanged(CharSequence s, int start, int count, int after){
				}
				public void onTextChanged(CharSequence s, int start, int before, int count){
				}
			});
		}
		/*
		 * JACKSON END
		 */
        
        if(headerTable.indexOfChild(row) != -1){
        	setHeaderTableStyle(oneColumn);
            oneColumn.setText("Headline " + (((TableRow) headerTable.getChildAt(0)).getChildCount()+1));
//            oneColumn.setHint(oneColumn.getEditableText().toString());
        }
        else{
        	setTableStyle(oneColumn);
        	oneColumn.setText("---");
        }
        row.addView(oneColumn);
        int columnIndex = row.indexOfChild(oneColumn);
        int rowIndex = table.indexOfChild(row);
        if(rowIndex == -1){
        	rowIndex = headerTable.indexOfChild(row);
        	if(rowIndex == -1)         Log.d("critical", "cant find row!");
        }
        int width = getNeededWidth(columnIndex);
		int height = getNeededHeight(rowIndex, row);
		int oldWidth = oneColumn.getMeasuredWidth();
		int oldHeight = oneColumn.getMeasuredWidth();
		Log.d("width", "before w : " + oldWidth);
		Log.d("width", "before h : " + oldHeight);
//		Log.d("width", "before w: " + oneColumn.getWidth());
//		Log.d("width", "before h: " + oneColumn.getHeight());
		Log.d("width", "setting w: " + width);
		Log.d("width", "setting h: " + height);
//		oneColumn.setWidth(width);
		final LayoutParams lparams = new LayoutParams(width, height);
	    oneColumn.setLayoutParams(lparams);
		oneColumn.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//		Log.d("width", "after h: " + oneColumn.getHeight());
//		final TableRow.LayoutParams lparams = new TableRow.LayoutParams(width, height);
//	    ((EditText) oneColumn).setLayoutParams(lparams);
	}
	
	
	
//	protected void removeColumnFromRow(TableRow row){
//		int elements = row.getChildCount();
//	}
	
	

	

	protected void addColumn() {
		amountColumns++;
		View headerRow = headerTable.getChildAt(0);
		TableRow row = (TableRow) headerRow;
		/*
		 *  JACKSON START
		 */
		// only add column if were are not loading our inflated data
		if(!jacksonHasBeenInflated) {
			jacksonTable.addColumn(new ColumnHeader("", StringClass.TYPE_STRING));
			Log.d("TABLE_FRAGMENT", "added column - save");
			MainTemplateGenerator.saveTemplate();
		}
		/*
		 *  JACKSON END
		 */
        addColumnToRow(row);
		for (int i = 0; i < table.getChildCount(); i++) {
		    View child = table.getChildAt(i);

		    if (child instanceof TableRow) {
		        row = (TableRow) child;
//		        EditText newColumn = new EditText(getActivity());
//		        newColumn.setText("empty");
//		        newColumn.setSingleLine();
		        addColumnToRow(row);
//		        row.addView(newColumn);
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
			View headerRow = headerTable.getChildAt(0);
			TableRow row = (TableRow) headerRow;
			row.removeView(row.getChildAt(row.getChildCount()-1));
			/*
			 *  JACKSON START
			 */
			// only remove column if we are not inflating
			if(!jacksonHasBeenInflated) {
				jacksonTable.removeColumn();
				Log.d("TABLE_FRAGMENT", "removed column - save");
				MainTemplateGenerator.saveTemplate();
			}
			/*
			 *  JACKSON END
			 */
			Log.d("columns", "amount == " + amountColumns);
		}
	}

//	/**
//	 * @param dialogTable
//	 * Method to set up the table for showing it in AlertDialog
//	 */
//	protected void prepareTableForDialog(TableLayout dialogTable) {
//		dialogTable.removeAllViews();
//		for(int i=-1; i<amountColumns; i++){
//			final TableRow row = new TableRow(MainTemplateGenerator.theActiveActivity);
//			TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
//			rowParams.height = TableRow.LayoutParams.WRAP_CONTENT;
//			rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;
//			//use k columns
//			for(int k=0; k<3; k++){
//				final EditText oneColumn = new EditText(MainTemplateGenerator.theActiveActivity);
//				setTableStyle(oneColumn);
//				//TODO: how to resize the table if too big?
//				oneColumn.addTextChangedListener(new TextWatcher(){
//					public void afterTextChanged(Editable s) {
//						//					checkResize(0, 0, oneColumn, row);
//					}
//					public void beforeTextChanged(CharSequence s, int start, int count, int after){
//					}
//					public void onTextChanged(CharSequence s, int start, int before, int count){
//					}
//				});
//				//first row -> insert column descriptions
//				if(i==-1){
//					if(k==0){
//						oneColumn.setText("Nr.");
//					}
//					else if(k==1){
//						oneColumn.setText("Spaltenname");
//					}
//					else{
//						oneColumn.setText("Spaltentyp");
//					}
//				}
//				//insert the number of the column in the first row
//				else if(k ==0){
//					oneColumn.setText(i+".");
//				}
//				else if(k==1){
//					TableRow headerRow = (TableRow) headerTable.getChildAt(0);
//					EditText headerText = (EditText) headerRow.getChildAt(i);
//					Log.d("i,k", "i==" + i + ", k==" +k);
//					oneColumn.setText(headerText.getText());					
//				}
//				row.addView(oneColumn);
//			}
//			dialogTable.addView(row);
//		}
////		return table;
//	}
	
	/*
	 * JACKSON START
	 */
	public void jacksonInflate(Table myTable, Activity activity) {
		jacksonTable = myTable;
		jacksonHasBeenInflated = true;
	}
	/*
	 * JACKSON END
	 */

	@Override
	public void showDialog() {
//		dialogTableView.setTitle(elementName);
		NumberPicker np = ((NumberPicker) dialogViewTableView.findViewById(R.id.numberPicker1));
		np.setValue(amountColumns);
		TableLayout table = ((TableLayout) dialogViewTableView.findViewById(R.id.tableView_alert_table));
//		prepareTableForDialog(table);
		adaptDialogTable(table);
		dialogTableView.show();
	}
	
//	protected void initializeDialogTable(TableLayout dialogTable){
//		final TableRow row = new TableRow(MainTemplateGenerator.theActiveActivity);
//		TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
//		rowParams.height = TableRow.LayoutParams.WRAP_CONTENT;
//		rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;
//		//use k columns
//		for(int k=0; k<3; k++){
//			final EditText oneColumn = new EditText(MainTemplateGenerator.theActiveActivity);
//			String uri = "@drawable/cell_shape";
//			int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
//			Drawable res = getResources().getDrawable(imageResource);
//			if (android.os.Build.VERSION.SDK_INT >= 16){
//				oneColumn.setBackground(res);
//			}
//			else{
//				oneColumn.setBackgroundDrawable(res);
//			}
//			oneColumn.setSingleLine();
//			View theText = oneColumn;
//			//TODO: how to resize the table if too big?
//			oneColumn.addTextChangedListener(new TextWatcher(){
//				public void afterTextChanged(Editable s) {
//					//					checkResize(0, 0, oneColumn, row);
//				}
//				public void beforeTextChanged(CharSequence s, int start, int count, int after){
//				}
//				public void onTextChanged(CharSequence s, int start, int before, int count){
//				}
//			});
//			//first row -> insert column descriptions
//			if(k==0){
//				oneColumn.setText("Nr.");
//			}
//			else if(k==1){
//				oneColumn.setText("Spaltenname");
//			}
//			else{
//				oneColumn.setText("Spaltentyp");
//			}
//			row.addView(oneColumn);
//		}
//		dialogTable.addView(row);
//	}
	
//	protected void adaptDialogTable(TableLayout dialogTable){
//		int rowsNeeded = ((TableRow) headerTable.getChildAt(0)).getChildCount();
//		adaptDialogTable(dialogTable, rowsNeeded);
//	}
	
	protected void setTableStyle(View view){
		
		String uri = "@drawable/cell_shape";
		int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
		Drawable res = getResources().getDrawable(imageResource);
		if (android.os.Build.VERSION.SDK_INT >= 16){
			view.setBackground(res);
		}
		else{
			view.setBackgroundDrawable(res);
		}
		if(view instanceof EditText){
			EditText text = (EditText) view;
			text.setTextColor(getResources().getColor(R.color.background));
			text.setSingleLine();
		}
	}
	
	class DialogSpinnerAdapter<T> extends ArrayAdapter<T>
	{
		public DialogSpinnerAdapter(Context ctx, T [] objects) {
			super(ctx, android.R.layout.simple_spinner_item, objects);
		}
		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent)
		{
			View view = super.getView(position, convertView, parent);
			TextView text = (TextView)view.findViewById(android.R.id.text1);
			text.setTextColor(getResources().getColor(R.color.background));//choose your color :)         
			/*}*/

			return view;

		}
	}
	
	protected void setSpinnerStyle(Spinner spinner){
		String uri = "@drawable/cell_shape";
		int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
		Drawable res = getResources().getDrawable(imageResource);
		spinner.setPopupBackgroundResource(imageResource);
		spinner.setBackground(res);
	}
	
	protected void setAddButtonStyle(TextView text){
		TableRow.LayoutParams textViewParams = new TableRow.LayoutParams();
//		View theLayout = view.findViewById(R.id.main_view);
//		theLayout.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//		int width = theLayout.getMeasuredWidth();
//		int height = theLayout.getMeasuredHeight();
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		textViewParams.height = height;
		textViewParams.width = width;
		Log.d("width", "SCREEN.x == " + width);
		text.setMaxWidth(width);
//		text.setLayoutParams(textViewParams);
		
//		text.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
		String uri = "@drawable/cell_shape_add";
		int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
		Drawable res = getResources().getDrawable(imageResource);
		if (android.os.Build.VERSION.SDK_INT >= 16){
			text.setBackground(res);
		}
		else{
			text.setBackgroundDrawable(res);
		}
		text.setTextColor(getResources().getColor(R.color.own_grey));
		text.setSingleLine();
	}
	
	protected void setHeaderTableStyle(EditText text){
		Log.d("setHeaderTableStyle", "setHeaderTableStyle!");
		text.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
		String uri = "@drawable/cell_shape_green";
		int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
		Drawable res = getResources().getDrawable(imageResource);
		if (android.os.Build.VERSION.SDK_INT >= 16){
			text.setBackground(res);
		}
		else{
			text.setBackgroundDrawable(res);
		}
		text.setTextColor(getResources().getColor(R.color.white));
		text.setSingleLine();
	}
	
	protected void adaptDialogTable(TableLayout dialogTable){
		int firstRowToAdd = dialogTable.getChildCount();
		int rowsNeeded = ((TableRow) headerTable.getChildAt(0)).getChildCount();
		//first step: adapt all needed Column-names from headerTable
		int oldColumnsToAdept = (firstRowToAdd<rowsNeeded? firstRowToAdd:rowsNeeded);
		for(int i=1; i<oldColumnsToAdept; i++){
			TableRow headerRow = (TableRow) headerTable.getChildAt(0);
			EditText headerText = (EditText) headerRow.getChildAt(i-1);
			EditText textToSet = (EditText) ((TableRow) dialogTable.getChildAt(i)).getChildAt(1);
			textToSet.setText(headerText.getText());
		}
		//test if header is set
//		if(dialogTable.getChildCount() == 0){
////			Log.e("TableFragment.java", "dialog table of Table Fragment should be initialisized, but already has children!");
////			dialogTable.removeAllViews();
//			initializeDialogTable(dialogTable);
//		}
		//second step: add rows if needed
		for(int i=firstRowToAdd-1; i<rowsNeeded; i++){
			Log.d("dialog", "add");
			final TableRow row = new TableRow(MainTemplateGenerator.theActiveActivity);
			TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
			rowParams.height = TableRow.LayoutParams.WRAP_CONTENT;
			rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;
			//use k columns
			for(int k=0; k<3; k++){
				View theView = null;
				if(k==2 && i != -1){
//					DialogSpinnerAdapter<CharSequence> adapter = (DialogSpinnerAdapter<CharSequence>) ArrayAdapter.createFromResource(MainTemplateGenerator.theActiveActivity, R.array.choices, android.R.layout.simple_spinner_item);
//				    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					Spinner spin = new Spinner(MainTemplateGenerator.theActiveActivity);
//					spin.setAdapter(adapter);
					
					String [] spin_arry = getResources().getStringArray(R.array.choices);
					spin.setAdapter(new DialogSpinnerAdapter<CharSequence>(MainTemplateGenerator.theActiveActivity, spin_arry));
//		        ((Spinner) view.findViewById(R.id.spin)).setOnItemSelectedListener(new OnItemSelectedListener() {
					theView = spin;
					setSpinnerStyle(spin);
					spin.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
//							String item = (String) parent.getItemAtPosition(arg2);
							((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.background));
						}
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					});
//					EditText fieldToMeasure = (EditText) row.getChildAt(1);
//					fieldToMeasure.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//					int elementSize = fieldToMeasure.getMeasuredHeight();
//					Spinner.LayoutParams spinnerParams = new TableRow.LayoutParams();
//					rowParams.height = elementSize;
//					rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;
//					spin.setLayoutParams(spinnerParams);
				}
				else{
					final EditText oneColumn = new EditText(MainTemplateGenerator.theActiveActivity);
					setTableStyle((EditText) oneColumn);
					//				View theText = oneColumn;
					//				//TODO: how to resize the table if too big?
					//				oneColumn.addTextChangedListener(new TextWatcher(){
					//					public void afterTextChanged(Editable s) {
					//						//					checkResize(0, 0, oneColumn, row);
					//					}
					//					public void beforeTextChanged(CharSequence s, int start, int count, int after){
					//					}
					//					public void onTextChanged(CharSequence s, int start, int before, int count){
					//					}
					//				});
					//first row -> insert column descriptions
					if(i==-1){
						if(k==0){
							oneColumn.setText("Nr.");
						}
						else if(k==1){
							oneColumn.setText("Spaltenname");
						}
						else{
							oneColumn.setText("Spaltentyp");
						}
					}
					//insert the number of the column in the first row
					else if(k ==0){
						oneColumn.setText((i+1)+".");
					}
					else if(k==1){
						TableRow headerRow = (TableRow) headerTable.getChildAt(0);
						EditText headerText = (EditText) headerRow.getChildAt(i);
						//					Log.d("i,k", "i=="  + ", k==" +k);
						if(headerText == null){
							oneColumn.setText("");
						}
						else{
							oneColumn.setText(headerText.getText());
						}
						final int index = i;
						oneColumn.addTextChangedListener(new TextWatcher(){
							public void afterTextChanged(Editable s) {
								//dont edit it now, but after pressing "tabelle speichern"
								//							TableRow headerRow = (TableRow) headerTable.getChildAt(0);
								//							EditText headerText = (EditText) headerRow.getChildAt(index);
								//							headerText.setText(s);
								//					checkResize(0, 0, oneColumn, row);
							}
							public void beforeTextChanged(CharSequence s, int start, int count, int after){
							}
							public void onTextChanged(CharSequence s, int start, int before, int count){
							}
						});
					}
					theView = oneColumn;
				}
				row.addView(theView);
			}
			dialogTable.addView(row);
		}
		//last step: remove rows if needed
		for(int i=rowsNeeded+1; i<firstRowToAdd; i++){
			Log.d("dialog", "remove");
			dialogTable.removeView(dialogTable.getChildAt(dialogTable.getChildCount()-1));
		}
	}
	
	protected void adaptDialogTable(TableLayout dialogTable, int rowsToShow){
		int firstRowToAdd = dialogTable.getChildCount();
		int rowsNeeded = rowsToShow;
		//first step: adapt all needed Column-names from headerTable
		int oldColumnsToAdept = (firstRowToAdd<rowsNeeded? firstRowToAdd:rowsNeeded);
		for(int i=1; i<oldColumnsToAdept; i++){
			TableRow headerRow = (TableRow) headerTable.getChildAt(0);
			EditText headerText = (EditText) headerRow.getChildAt(i-1);
			EditText textToSet = (EditText) ((TableRow) dialogTable.getChildAt(i)).getChildAt(1);
			if(headerText == null){
				textToSet.setText("");
			}
			else{
				textToSet.setText(headerText.getText());
			}
		}
		//test if header is set
//		if(dialogTable.getChildCount() == 0){
////			Log.e("TableFragment.java", "dialog table of Table Fragment should be initialisized, but already has children!");
////			dialogTable.removeAllViews();
//			initializeDialogTable(dialogTable);
//		}
		//second step: add rows if needed
		for(int i=firstRowToAdd-1; i<rowsNeeded; i++){
			Log.d("dialog", "add");
			final TableRow row = new TableRow(MainTemplateGenerator.theActiveActivity);
			TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
			rowParams.height = TableRow.LayoutParams.WRAP_CONTENT;
			rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;
			//use k columns
			for(int k=0; k<3; k++){
				View theView = null;
				if(k==2 && i != -1){
//					DialogSpinnerAdapter<CharSequence> adapter = (DialogSpinnerAdapter<CharSequence>) ArrayAdapter.createFromResource(MainTemplateGenerator.theActiveActivity, R.array.choices, android.R.layout.simple_spinner_item);
//				    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					Spinner spin = new Spinner(MainTemplateGenerator.theActiveActivity);
//					spin.setAdapter(adapter);
					
					String [] spin_arry = getResources().getStringArray(R.array.choices);
					spin.setAdapter(new DialogSpinnerAdapter<CharSequence>(MainTemplateGenerator.theActiveActivity, spin_arry));
//		        ((Spinner) view.findViewById(R.id.spin)).setOnItemSelectedListener(new OnItemSelectedListener() {
					theView = spin;
					setSpinnerStyle(spin);
					spin.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
//							String item = (String) parent.getItemAtPosition(arg2);
							((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.background));
						}
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					});
//					EditText fieldToMeasure = (EditText) row.getChildAt(1);
//					fieldToMeasure.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//					int elementSize = fieldToMeasure.getMeasuredHeight();
//					Spinner.LayoutParams spinnerParams = new TableRow.LayoutParams();
//					rowParams.height = elementSize;
//					rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;
//					spin.setLayoutParams(spinnerParams);
				}
				else{
					final EditText oneColumn = new EditText(MainTemplateGenerator.theActiveActivity);
					setTableStyle((EditText) oneColumn);
					//				View theText = oneColumn;
					//				//TODO: how to resize the table if too big?
					//				oneColumn.addTextChangedListener(new TextWatcher(){
					//					public void afterTextChanged(Editable s) {
					//						//					checkResize(0, 0, oneColumn, row);
					//					}
					//					public void beforeTextChanged(CharSequence s, int start, int count, int after){
					//					}
					//					public void onTextChanged(CharSequence s, int start, int before, int count){
					//					}
					//				});
					//first row -> insert column descriptions
					if(i==-1){
						if(k==0){
							oneColumn.setText("Nr.");
						}
						else if(k==1){
							oneColumn.setText("Spaltenname");
						}
						else{
							oneColumn.setText("Spaltentyp");
						}
					}
					//insert the number of the column in the first row
					else if(k ==0){
						oneColumn.setText((i+1)+".");
					}
					else if(k==1){
						TableRow headerRow = (TableRow) headerTable.getChildAt(0);
						EditText headerText = (EditText) headerRow.getChildAt(i);
						//					Log.d("i,k", "i=="  + ", k==" +k);
						if(headerText == null){
							oneColumn.setText("");
						}
						else{
							oneColumn.setText(headerText.getText());
						}
						final int index = i;
						oneColumn.addTextChangedListener(new TextWatcher(){
							public void afterTextChanged(Editable s) {
								//dont edit it now, but after pressing "tabelle speichern"
								//							TableRow headerRow = (TableRow) headerTable.getChildAt(0);
								//							EditText headerText = (EditText) headerRow.getChildAt(index);
								//							headerText.setText(s);
								//					checkResize(0, 0, oneColumn, row);
							}
							public void beforeTextChanged(CharSequence s, int start, int count, int after){
							}
							public void onTextChanged(CharSequence s, int start, int before, int count){
							}
						});
					}
					theView = oneColumn;
				}
				row.addView(theView);
			}
			dialogTable.addView(row);
		}
		//last step: remove rows if needed
		for(int i=rowsNeeded+1; i<firstRowToAdd; i++){
			Log.d("dialog", "remove");
			dialogTable.removeView(dialogTable.getChildAt(dialogTable.getChildCount()-1));
		}
	}
	
//	protected void adaptDialogTableFast(TableLayout dialogTable, int rowsToShow){
//		int firstRowToAdd = dialogTable.getChildCount();
//		int rowsNeeded = rowsToShow;
//		for(int i=firstRowToAdd-1; i<rowsNeeded; i++){
//			Log.d("dialog", "add");
//			final TableRow row = new TableRow(MainTemplateGenerator.theActiveActivity);
//			TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
//			rowParams.height = TableRow.LayoutParams.WRAP_CONTENT;
//			rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;
//			//use k columns
//			for(int k=0; k<3; k++){
//				final EditText oneColumn = new EditText(MainTemplateGenerator.theActiveActivity);
//				setTableStyle(oneColumn);
//				if(i==-1){
//					if(k==0){
//						oneColumn.setText("Nr.");
//					}
//					else if(k==1){
//						oneColumn.setText("Spaltenname");
//					}
//					else{
//						oneColumn.setText("Spaltentyp");
//					}
//				}
//				//insert the number of the column in the first row
//				else if(k ==0){
//					oneColumn.setText((i+1)+".");
//				}
//				else if(k==1){
//					TableRow headerRow = (TableRow) headerTable.getChildAt(0);
//					EditText headerText = (EditText) headerRow.getChildAt(i);
////					Log.d("i,k", "i=="  + ", k==" +k);
//					if(headerText == null){
//						oneColumn.setText("");
//					}
//					else{
//						oneColumn.setText(headerText.getText());
//					}
//					final int index = i;
//					oneColumn.addTextChangedListener(new TextWatcher(){
//						public void afterTextChanged(Editable s) {
//							//dont edit it now, but after pressing "tabelle speichern"
////							TableRow headerRow = (TableRow) headerTable.getChildAt(0);
////							EditText headerText = (EditText) headerRow.getChildAt(index);
////							headerText.setText(s);
//							//					checkResize(0, 0, oneColumn, row);
//						}
//						public void beforeTextChanged(CharSequence s, int start, int count, int after){
//						}
//						public void onTextChanged(CharSequence s, int start, int before, int count){
//						}
//					});
//				}
//				row.addView(oneColumn);
//			}
//			dialogTable.addView(row);
//		}
//		//last step: remove rows if needed
//		for(int i=rowsNeeded+1; i<firstRowToAdd; i++){
//			Log.d("dialog", "remove");
//			dialogTable.removeView(dialogTable.getChildAt(dialogTable.getChildCount()-1));
//		}
//	}
	
	private class Runner implements Runnable {
		int newValue = -1;
		public Runner(int newVal){
			newValue = newVal;
		}
	     @Override
	     public void run() {
//	    	 setAmountOfColumns(newValue);
	    	 adaptDialogTable(((TableLayout) dialogViewTableView.findViewById(R.id.tableView_alert_table)), newValue);
	    }
	};
	
	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		if(oldVal == newVal){
			return;
		}
//		new AdaptColumns().execute(newVal);
		final int newValue = newVal;
		MainTemplateGenerator.theActiveActivity.runOnUiThread(new Runner(newVal));
		
//		setAmountOfColumns(newVal);
//		adaptDialogTable(((TableLayout) dialogViewTableView.findViewById(R.id.tableView_alert_table)));
	}
	
	private class AdaptColumns extends AsyncTask<Integer, Void, Void> {
	     protected Void doInBackground(Integer... params) {
//	    	setAmountOfColumns(params[0]);
//	 		adaptDialogTable(((TableLayout) dialogViewTableView.findViewById(R.id.tableView_alert_table)));
	 		return null;
	     }

	     protected void onProgressUpdate(Integer... progress) {
	     }

	     protected void onPostExecute(Long result) {
	     }
	 }

}
