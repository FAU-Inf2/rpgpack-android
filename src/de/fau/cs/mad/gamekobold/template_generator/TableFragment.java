package de.fau.cs.mad.gamekobold.template_generator;



import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.jackson.ColumnHeader;
import de.fau.cs.mad.gamekobold.jackson.StringClass;
import de.fau.cs.mad.gamekobold.jackson.Table;
import de.fau.cs.mad.gamekobold.template_generator.SessionMonitorEditText.OnEditSessionCompleteListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

@SuppressLint("NewApi")
public class TableFragment extends GeneralFragment {
	/*
	 * JACKSON START
	 */
	Table jacksonTable;
	boolean jacksonInflateWithData = false;
	/*
	 * JACKSON END
	 */
	
	View mainView;
	TableLayout table;
	TableLayout headerTable;
	// also set in createTableHeader !! onCreate is called when orientation is rotated ->
	// amountColumns = #before change but we recreate the header table with 2 columns
	int amountColumns = 2;
	AlertDialog dialog;
	TableLayout dialogTable;
	SessionMonitorEditText dialogRowCounter = null;
	//menu shown when long clicking a row
	protected TableRow contextMenuRow;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        setRetainInstance(true);
        //create the table that will be shown in the dialog
        LayoutInflater inflater = (LayoutInflater) TemplateGeneratorActivity.theActiveActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogViewTableView = inflater.inflate(R.layout.alertdialog_template_generator_tableview, null);
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
        		adaptDialogTable(dialogTable, (Integer.parseInt(v.getText().toString())));
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
        //create the dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TemplateGeneratorActivity.theActiveActivity);
        alertDialogBuilder.setView(dialogViewTableView);
        alertDialogBuilder
        .setCancelable(false)
        .setPositiveButton("Tabelle speichern",new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog,int id) {
        		setAmountOfColumns(Integer.parseInt(dialogRowCounter.getText().toString()));
        		setTypeContents();
        		adaptHeaderTable(dialogTable);
        	}
        })
        .setNegativeButton("Zurück",new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog,int id) {
        		dialog.cancel();
        	}
        });
        dialog = alertDialogBuilder.create();
    }
	
	protected void setTypeContents(){
		Log.d("TableFragment", "setTypeContents");
		for(int i=1; i<dialogTable.getChildCount(); i++){
			int indexOfTable = i-1;
			TableRow dialogTableRow = (TableRow) dialogTable.getChildAt(i);
			for(int k=0; k<table.getChildCount(); k++){
				boolean isModified = false;
				View newElement = null;
				TableRow tableRow = (TableRow) table.getChildAt(k);
				View elementToAdapt = tableRow.getChildAt(indexOfTable);
				Spinner spin = (Spinner) dialogTableRow.getChildAt(2);
				String choice = spin.getSelectedItem().toString();
				if(choice.equals("Textfeld")){
					if(!(elementToAdapt instanceof EditText)){
						isModified = true;
						tableRow.removeView(elementToAdapt);
						newElement = new EditText(getActivity());
						setTableStyle(newElement);
						tableRow.addView(newElement);
					}
				}
				else if(choice.equals("CheckBox")){
					if(!(elementToAdapt instanceof CheckBox)){
						isModified = true;
//						Log.d("TableFragment", "setTypeContents: remove Elementt");
						tableRow.removeView(elementToAdapt);
						newElement = new CheckBox(getActivity());
						setTableStyle(newElement);
//						tableRow.addView(newElement);
						tableRow.addView(newElement, indexOfTable);
					}
				}
				else if(choice.equals("PopUp")){
					if(!(elementToAdapt instanceof TextView)){
						isModified = true;
						tableRow.removeView(elementToAdapt);
						newElement = new TextView(getActivity());
						((TextView) newElement).setText("...");
						AlertDialog.Builder alertCreater = new AlertDialog.Builder(getActivity());
						alertCreater.setTitle("PopUp");
						alertCreater.setMessage("Message");
						// Set an EditText view to get user input 
						final EditText input = new EditText(getActivity());
						alertCreater.setView(input);
						alertCreater.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						  // Do something with value!
						  }
						});

						alertCreater.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						  public void onClick(DialogInterface dialog, int whichButton) {
						    // Canceled.
						  }
						});
						final AlertDialog alert = alertCreater.create();
						newElement.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								alert.show();
							}
						});
//						setTableStyle(newElement);
						tableRow.addView(newElement, indexOfTable);
					}
				}
				if(isModified){
					newElement.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					int width = newElement.getMeasuredWidth();
					int height = newElement.getMeasuredHeight();
					checkResize(width, height, newElement, tableRow);
				}
			}
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
			EditText textToChange = ((EditText) ((TableRow) headerTable.getChildAt(0)).getChildAt(i-1));
			if(!otherString.equals("")){
				textToChange.setText(otherString);
			}
		}
	}
	
	protected TableLayout createTableHeader() {
		amountColumns = 2;
		headerTable = (TableLayout) mainView.findViewById(R.id.header_table);
		final TableRow row = new TableRow(getActivity());
		TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
		// Wrap-up the content of the row
		rowParams.height = TableRow.LayoutParams.MATCH_PARENT;
		rowParams.width = TableRow.LayoutParams.MATCH_PARENT;
		// The simplified version of the table of the picture above will have two columns
		// FIRST COLUMN
		TableRow.LayoutParams colParams = new TableRow.LayoutParams();
		colParams.height = TableRow.LayoutParams.MATCH_PARENT;
		colParams.width = TableRow.LayoutParams.MATCH_PARENT;
		final EditText col1 = new EditText(getActivity());
		col1.setText("Headline1");
		setHeaderTableStyle(col1);
		col1.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				checkResize(0, 0, col1, row);
				/*
				 *  JACKSON START
				 */
				jacksonTable.setColumnTitle(0, s.toString());
				Log.d("TABLE_FRAGMENT", "column title changed");
				//TemplateGeneratorActivity.saveTemplateAsync();
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
		final EditText col2 = new EditText(getActivity());
		setHeaderTableStyle(col2);
		col2.setText("Headline2");
		col2.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				checkResize(0, 0, col2, row);
				/*
				 *  JACKSON START
				 */
				jacksonTable.setColumnTitle(1, s.toString());
				Log.d("TABLE_FRAGMENT", "column title changed");
				//TemplateGeneratorActivity.saveTemplateAsync();
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
		// TODO research if more and more rows are added when rotating a device!
		headerTable.addView(row);
		return headerTable;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = (RelativeLayout) inflater.inflate(R.layout.template_generator_table_view, null);
        createTableHeader();
        table = (TableLayout) mainView.findViewById(R.id.template_generator_table);
        //set the 3 buttons
		Button buttonAdd = (Button)mainView.findViewById(R.id.add_row);
		buttonAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addItemList();
			}
		});
		Button addColumnButton = (Button)mainView.findViewById(R.id.add_column);
		addColumnButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addColumn();
			}
		});
		Button removeColumnButton = (Button)mainView.findViewById(R.id.remove_column);
		removeColumnButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				removeColumn();
			}
		});
		TextView addRowBelow = (TextView)mainView.findViewById(R.id.add_below);
		addRowBelow.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addItemList();
			}
		});
		setAddButtonStyle(addRowBelow);
		/*
		 * JACKSON START
		 */
		Log.d("TableFragment", "jacksonInflateWithData:"+jacksonInflateWithData);
		final int jacksonTableColumnNumber = jacksonTable.numberOfColumns;
		// check if we have inflated the table with some data
		// BUT also check if we got any saved columns (they are only created if user goes into table!)
		// so if there are no saved columns or we didn't load any data we add the default columns 
		if(jacksonTableColumnNumber > 0) {
			// set flag, so we don't add new columns to the jackson table while loading
			jacksonInflateWithData = true;
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
			Log.d("TableFragment-onCreateView", "headerRow:"+headerRow);
			Log.d("TableFragment-onCreateView", "Column#:"+amountColumns);
			for(int i = 0; i < amountColumns; i++) {
				View view = headerRow.getChildAt(i);
				Log.d("TableFragment-onCreateView", "view:"+view);
				Log.d("TABLE INFLATING", "setting column("+i+") header title:"+jacksonTable.columnHeaders.get(i).name);
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
			jacksonInflateWithData = false;
		}
		else {
			// add the 2 default columns
			jacksonTable.addColumn(new ColumnHeader("", StringClass.TYPE_STRING));
			jacksonTable.addColumn(new ColumnHeader("", StringClass.TYPE_STRING));
			jacksonInflateWithData = false;
			// save template
			Log.d("TableFragment", "added default columns");
			//TemplateGeneratorActivity.saveTemplateAsync();
		}
		/*
		 * JACKSON END
		 */
		addItemList();
		return mainView;
	}
	
	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if(v instanceof TableRow){
			contextMenuRow = (TableRow) v;
		}
		MenuInflater inflater = TemplateGeneratorActivity.theActiveActivity.getMenuInflater();
		inflater.inflate(R.menu.template_generator_remove_table_item, menu);
	}
	
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.removeTableRow:
			removeRow(contextMenuRow);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	
	/**
	 * removes one row of the table
	 * @param row
	 */
	protected void removeRow(TableRow row) {
        table.removeView(row);
	}

	/**
	 * adds a new row to the table
	 */
	protected void addItemList() {
	        TableRow row= new TableRow(getActivity());
	        registerForContextMenu(row);
	        for(int i=0; i<amountColumns; ++i){
	        	addColumnToRow(row);
	        }
	        table.addView(row);
	        final ScrollView sv = (ScrollView) mainView.findViewById(R.id.table_scroll);
	        //note: we have to do scrolling in seperate thread to make sure the new item is already inserted
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

	/**
	 * @param columnIndex
	 * @return width of biggest element in column with given columnIndex
	 */
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
	
	/**
	 * 
	 * @param rowIndex
	 * @param row
	 * @return height of biggest element in column with given rowIndex
	 */
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
	
	/**
	 * checks if it is needed to resize the given row (does it if textEdited is bigger now)
	 * @param width
	 * @param height
	 * @param textEdited
	 * @param row
	 */
	protected void checkResize(int width, int height, View textEdited, TableRow row){
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
//		Log.d("index", "index  == " + indexOfRow);
		int indexOfColumn = -1;
		if(usedTable.getChildAt(indexOfRow) instanceof TableRow){
			indexOfColumn = ((TableRow) usedTable.getChildAt(indexOfRow)).indexOfChild(textEdited);
		}
		int largestWidth = 0;
		//traverse over all columns to get biggest width
		//first columns of normal table
		for (int i = 0; i < table.getChildCount(); i++) {
			View child = table.getChildAt(i);
			TableRow oneRow = (TableRow) child;
			if (child instanceof TableRow) {
				View view = oneRow.getChildAt(indexOfColumn);
				int localWidth = 0;
				if(view instanceof EditText){
					view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					localWidth = view.getMeasuredWidth();
					if(localWidth > largestWidth){
						largestWidth = localWidth;
					}
				}
			}
		}
		//than compare with header table also
		for (int i = 0; i < headerTable.getChildCount(); i++) {
			View child = headerTable.getChildAt(i);
			TableRow oneRow = (TableRow) child;
			if (child instanceof TableRow) {
				View view = oneRow.getChildAt(indexOfColumn);
				int localWidth = 0;
				if(view instanceof EditText){
					//todo
					view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					localWidth = view.getMeasuredWidth();
					if(localWidth > largestWidth){
						largestWidth = localWidth;
					}
				}
			}
		}
		/* Code that measures the height of one row
		int largestHeight = 0;
		if (row instanceof TableRow) {
			for(int i=0; i<((TableRow) row).getChildCount(); i++){
				View view = ((TableRow) row).getChildAt(i);
				int localHeight = 0;
				if(view instanceof EditText){
					view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					localHeight = view.getMeasuredHeight();
					if(localHeight > largestHeight){
						largestHeight = localHeight;
					}
				}
			}
		}
		*/
		adaptAllColumnsToSize(indexOfColumn, largestWidth);
	}
	
	/**
	 * adapts all columns of the table with given columnIndex to the given width
	 * @param columnIndex
	 * @param width
	 */
	protected void adaptAllColumnsToSize(int columnIndex, int width){
		View singleRow = headerTable.getChildAt(0);
		if(singleRow instanceof TableRow){
			View theChildToResize = ((TableRow) singleRow).getChildAt(columnIndex);
			if(theChildToResize instanceof EditText){
//				setHeaderTableStyle((EditText) theChildToResize);
				//TODO: extract measurement of height to match the hight of the biggest element in the row
				theChildToResize.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				int height = theChildToResize.getMeasuredHeight();
				final TableRow.LayoutParams lparams = new TableRow.LayoutParams(width,height); // Width , height
			    ((EditText) theChildToResize).setLayoutParams(lparams);
//			    textField.invalidate();
//			    textField.forceLayout();
			}
		}
		for(int i=0; i<table.getChildCount(); i++){
			View oneRow = table.getChildAt(i);
			if(oneRow instanceof TableRow){
				View theChildToResize = ((TableRow) oneRow).getChildAt(columnIndex);
				if(theChildToResize instanceof EditText){
					//TODO: extract measurement of height to match the hight of the biggest element in the row
					theChildToResize.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					int height = theChildToResize.getMeasuredHeight();
					final TableRow.LayoutParams lparams = new TableRow.LayoutParams(width, height); // Width , height
				    ((EditText) theChildToResize).setLayoutParams(lparams);
//				    setTableStyle((EditText) theChildToResize);
				}
				
			}
		}
	}
	
	/**
	 * changes amount of table columns to the given amountOfColumns
	 * @param amountOfColumns
	 */
	protected void setAmountOfColumns(int amountOfColumns){
		int amountToAdd = amountOfColumns - amountColumns;
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
	
	/**
	 * adds one column to the given row 
	 * @param row
	 */
	protected void addColumnToRow(final TableRow row){
		final EditText oneColumn = new EditText(getActivity());
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
			// guard. only set column title to default if we are not currently inflating with data
			/*if(!jacksonInflateWithData) {
			jacksonTable.setColumnTitle(((TableRow)headerTable.getChildAt(0)).getChildCount(),
							"Headline " + (((TableRow) headerTable.getChildAt(0)).getChildCount()+1));
			}*/
			oneColumn.addTextChangedListener(new TextWatcher() {
				// column index for this header cell  !index! no + 1 needed
				private final int columnIndex = ((TableRow)headerTable.getChildAt(0)).getChildCount();
				// callback
				public void afterTextChanged(Editable s) {
					// guard. only set column title if we are not currently inflating with data
					if(!jacksonInflateWithData) {
						// only save if the title changed
						if(jacksonTable.setColumnTitle(columnIndex, s.toString())) {
							Log.d("TABLE_FRAGMENT", "column title changed");
							//TemplateGeneratorActivity.saveTemplateAsync();	
						}
					}
					checkResize(0, 0, oneColumn, row);
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
        }
        else{
        	setTableStyle(oneColumn);
        	oneColumn.setText("-");
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
//		int oldWidth = oneColumn.getMeasuredWidth();
//		int oldHeight = oneColumn.getMeasuredWidth();
		final LayoutParams lparams = new LayoutParams(width, height);
	    oneColumn.setLayoutParams(lparams);
		oneColumn.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
	}
	
	/**
	 * adds one column to the table
	 */
	protected void addColumn() {
		amountColumns++;
		View headerRow = headerTable.getChildAt(0);
		TableRow row = (TableRow) headerRow;
		/*
		 *  JACKSON START
		 */
		// guard. only add column if we are not lnflating with data
		if(!jacksonInflateWithData) {
			jacksonTable.addColumn(new ColumnHeader("", StringClass.TYPE_STRING));
			Log.d("TABLE_FRAGMENT", "added column");
			//TemplateGeneratorActivity.saveTemplateAsync();
		}
		/*
		 *  JACKSON END
		 */
        addColumnToRow(row);
		for (int i = 0; i < table.getChildCount(); i++) {
		    View child = table.getChildAt(i);

		    if (child instanceof TableRow) {
		        row = (TableRow) child;
		        addColumnToRow(row);
		    }
		}
	}
	
	/**
	 * removes one column of the table
	 */
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
				}
			}
			View headerRow = headerTable.getChildAt(0);
			TableRow row = (TableRow) headerRow;
			row.removeView(row.getChildAt(row.getChildCount()-1));
			/*
			 *  JACKSON START
			 */
			// guard. only remove column if we are not currently inflating with data
			if(!jacksonInflateWithData) {
				jacksonTable.removeColumn();
				Log.d("TABLE_FRAGMENT", "removed column");
				//TemplateGeneratorActivity.saveTemplateAsync();
			}
			/*
			 *  JACKSON END
			 */
		}
	}

	/*
	 * JACKSON START
	 */
	public void jacksonInflate(Table myTable, Activity activity) {
		// set table
		jacksonTable = myTable;
		// set flag, so that we are inflating the views with data from jackson model
		jacksonInflateWithData = true;
	}
	/*
	 * JACKSON END
	 */

	@Override
	public void showDialog() {
		adaptDialogTable(dialogTable);
		dialogRowCounter.setText(Integer.toString(amountColumns));
		dialog.show();
	}
	
	@SuppressWarnings("deprecation")
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
	
	@SuppressWarnings("deprecation")
	protected void setAddButtonStyle(TextView text){
		TableRow.LayoutParams textViewParams = new TableRow.LayoutParams();
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		textViewParams.height = height;
		textViewParams.width = width;
		Log.d("width", "SCREEN.x == " + width);
		text.setMaxWidth(width);
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
	
	@SuppressWarnings("deprecation")
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
	
	/**
	 * adapt the table to match the amount and names of rows given in the mainView
	 * @param dialogTable
	 */
	protected void adaptDialogTable(TableLayout dialogTable){
//		int firstRowToAdd = dialogTable.getChildCount();
		int rowsNeeded = ((TableRow) headerTable.getChildAt(0)).getChildCount();
		adaptDialogTable(dialogTable, rowsNeeded);
//		//first step: adapt all needed Column-names from headerTable
//		int oldColumnsToAdept = (firstRowToAdd<rowsNeeded? firstRowToAdd:rowsNeeded);
//		for(int i=1; i<oldColumnsToAdept; i++){
//			TableRow headerRow = (TableRow) headerTable.getChildAt(0);
//			EditText headerText = (EditText) headerRow.getChildAt(i-1);
//			EditText textToSet = (EditText) ((TableRow) dialogTable.getChildAt(i)).getChildAt(1);
//			textToSet.setText(headerText.getText());
//		}
//		//second step: add rows if needed
//		for(int i=firstRowToAdd-1; i<rowsNeeded; i++){
//			Log.d("dialog", "add");
//			final TableRow row = new TableRow(TemplateGeneratorActivity.theActiveActivity);
//			TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
//			rowParams.height = TableRow.LayoutParams.WRAP_CONTENT;
//			rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;
//			//use k columns
//			for(int k=0; k<3; k++){
//				View theView = null;
//				if(k==2 && i != -1){
//					Spinner spin = new Spinner(TemplateGeneratorActivity.theActiveActivity);
//					String [] spin_arry = getResources().getStringArray(R.array.spaltentypen);
//					spin.setAdapter(new DialogSpinnerAdapter<CharSequence>(TemplateGeneratorActivity.theActiveActivity, spin_arry));
//					theView = spin;
//					setSpinnerStyle(spin);
//					spin.setOnItemSelectedListener(new OnItemSelectedListener() {
//						@Override
//						public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
//							((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.background));
//						}
//						@Override
//						public void onNothingSelected(AdapterView<?> arg0) {
//							//return
//						}
//					});
//				}
//				else{
//					final EditText oneColumn = new EditText(TemplateGeneratorActivity.theActiveActivity);
//					setTableStyle((EditText) oneColumn);
//					if(i==-1){
//						if(k==0){
//							oneColumn.setText("Nr.");
//						}
//						else if(k==1){
//							oneColumn.setText("Spaltenname");
//						}
//						else{
//							oneColumn.setText("Spaltentyp");
//						}
//					}
//					//insert the number of the column in the first row
//					else if(k ==0){
//						oneColumn.setText((i+1)+".");
//					}
//					else if(k==1){
//						TableRow headerRow = (TableRow) headerTable.getChildAt(0);
//						EditText headerText = (EditText) headerRow.getChildAt(i);
//						if(headerText == null){
//							oneColumn.setText("");
//						}
//						else{
//							oneColumn.setText(headerText.getText());
//						}
//					}
//					theView = oneColumn;
//				}
//				row.addView(theView);
//			}
//			dialogTable.addView(row);
//		}
//		//last step: remove rows if needed
//		for(int i=rowsNeeded+1; i<firstRowToAdd; i++){
//			Log.d("dialog", "remove");
//			dialogTable.removeView(dialogTable.getChildAt(dialogTable.getChildCount()-1));
//		}
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
		//second step: add rows if needed
		for(int i=firstRowToAdd-1; i<rowsNeeded; i++){
			Log.d("dialog", "add");
			final TableRow row = new TableRow(TemplateGeneratorActivity.theActiveActivity);
			TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
			rowParams.height = TableRow.LayoutParams.WRAP_CONTENT;
			rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;
			//use k columns
			for(int k=0; k<3; k++){
				View theView = null;
				if(k==2 && i != -1){
					Spinner spin = new Spinner(TemplateGeneratorActivity.theActiveActivity);
					String [] spin_arry = getResources().getStringArray(R.array.spaltentypen);
					spin.setAdapter(new DialogSpinnerAdapter<CharSequence>(TemplateGeneratorActivity.theActiveActivity, spin_arry));
					theView = spin;
					setSpinnerStyle(spin);
					spin.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View arg1, int arg2, long arg3) {
							((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.background));
							
						}
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							
						}
					});
				}
				else{
					final EditText oneColumn = new EditText(TemplateGeneratorActivity.theActiveActivity);
					setTableStyle((EditText) oneColumn);
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
	
	//not needed atm -> async task to adapt dialog table
	/*
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
	 */

}
