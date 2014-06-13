package de.fau.cs.mad.gamekobold.template_generator;


import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.jackson.ColumnHeader;
import de.fau.cs.mad.gamekobold.jackson.StringClass;
import de.fau.cs.mad.gamekobold.jackson.Table;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
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
	
	protected TableLayout createTableHeader() {
		headerTable = (TableLayout) view.findViewById(R.id.header_table);
//		private static TableLayout addRowToTable(TableLayout table, String contentCol1, String contentCol2) {
		Context context = getActivity();
		final TableRow row = new TableRow(context);

		TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
		// Wrap-up the content of the row
		rowParams.height = TableRow.LayoutParams.WRAP_CONTENT;
		rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;

		// The simplified version of the table of the picture above will have two columns
		// FIRST COLUMN
		TableRow.LayoutParams col1Params = new TableRow.LayoutParams();
//		// Wrap-up the content of the row
		col1Params.height = TableRow.LayoutParams.WRAP_CONTENT;
		col1Params.width = TableRow.LayoutParams.WRAP_CONTENT;
		// Set the gravity to center the gravity of the column
//		col1Params.gravity = TableRow.Gravity.CENTER;
		final ResizingEditText col1 = new ResizingEditText(context, row, this);
//		TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//		col1.setLayoutParams(col1Params);
		col1.setSingleLine();
//		col1.setMaxLines(1);
		col1.setText("Headline1");
		col1.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				checkResize(0, 0, col1, row);
				// JACKSON START
				jacksonTable.setColumnTitle(0, s.toString());
				try {
					MainTemplateGenerator.myTemplate.saveToJSON(getActivity(), "testTemplate.json");
					Log.d("TABLE_FRAGMENT", "title changed - saved");
				} catch (JsonGenerationException | JsonMappingException e) {
				} catch(IOException e) {
					
				}
				// JACKSON END
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}
			public void onTextChanged(CharSequence s, int start, int before, int count){
			}
		});
		
		
		String uri = "@drawable/cell_shape";
		int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
		Drawable res = getResources().getDrawable(imageResource);
		if (android.os.Build.VERSION.SDK_INT >= 16){
			col1.setBackground(res);
		}
		else{
			col1.setBackgroundDrawable(res);
		}
		row.addView(col1, col1Params);

		// SECOND COLUMN
//		TableRow.LayoutParams col2Params = new TableRow.LayoutParams();
//		// Wrap-up the content of the row
//		col2Params.height = TableRow.LayoutParams.WRAP_CONTENT;
//		col2Params.width = TableRow.LayoutParams.WRAP_CONTENT;
		// Set the gravity to center the gravity of the column
//		col2Params.gravity = Gravity.CENTER;
		final ResizingEditText col2 = new ResizingEditText(context, row, this);
//		TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//		col2.setLayoutParams(lp);
//		col2.setMaxLines(1);
		col2.setSingleLine();
		col2.setText("Headline2");
		col2.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				checkResize(0, 0, col2, row);
				/*
				 *  JACKSON START
				 */
				jacksonTable.setColumnTitle(1, s.toString());
				try {
					MainTemplateGenerator.myTemplate.saveToJSON(getActivity(), "testTemplate.json");
					Log.d("TABLE_FRAGMENT", "title changed - saved");
				} catch (JsonGenerationException | JsonMappingException e) {
				} catch(IOException e) {
					
				}
				/*
				 *  JACKSON END
				 */
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}
			public void onTextChanged(CharSequence s, int start, int before, int count){
			}
		});
		if (android.os.Build.VERSION.SDK_INT >= 16){
			col2.setBackground(res);
		}
		else{
			col2.setBackgroundDrawable(res);
		}
		row.addView(col2);

		headerTable.addView(row);
		
//		col1.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//		int elementSize = col1.getMeasuredWidth();
//		Log.d("width", "INITAL WDTH: " + elementSize);
				
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
//		TableRow row= new TableRow(getActivity());
//		TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//		row.setLayoutParams(lp);
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
		addItemList();
		
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
				// check size
				checkResize(0, 0, (EditText)view, headerRow);
		        int width = getNeededWidth(i);
				int height = getNeededHeight(0, headerRow);
				final LayoutParams lparams = new LayoutParams(width, height);
			    view.setLayoutParams(lparams);
			    view.requestLayout();
			    view.forceLayout();
			    view.invalidate();
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
    		try {
    			MainTemplateGenerator.myTemplate.saveToJSON(getActivity(), "testTemplate.json");
    			Log.d("JSON_DATA_ADAPTER", "saved template");
    		} catch (JsonGenerationException
    				| JsonMappingException e) {
    			e.printStackTrace();
    		}
    		catch(IOException e) {
    			e.printStackTrace();
    		}
			Log.d("TableFragment", "added default columns");
		}
		/*
		 * JACKSON END
		 */

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
//			Log.d("width", "widht == " + width);
//			Log.d("text", "text == " + ((EditText) longestView).getText());
//			Log.d("textlength", "textLENGHT == " + ((EditText) longestView).getText().toString().length());
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
//			    ((EditText) theChildToResize).setWidth(width);
				Log.d("width", "headline width after  == " + ((EditText) theChildToResize).getWidth());

			    String uri = "@drawable/cell_shape";
				int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
				Drawable res = getResources().getDrawable(imageResource);
				if (android.os.Build.VERSION.SDK_INT >= 16){
					((EditText) theChildToResize).setBackground(res);
				}
				else{
					((EditText) theChildToResize).setBackgroundDrawable(res);
				}
//			    textField.invalidate();
//			    textField.forceLayout();
				Log.d("header table", "size is set!!!");
//				((EditText) theChildToResize).setWidth(width);
//				((EditText) theChildToResize).setMinimumWidth(width);
//				textField.forceLayout();
//				textField.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//				int textFieldWidth = textField.getMeasuredWidth();
//				int textFieldHeight = textField.getMeasuredHeight();
//				((EditText) textField).setPadding(width-textFieldWidth, textField.getPaddingTop(), textField.getPaddingRight(), textField.getPaddingBottom());
			}
		}
		for(int i=0; i<table.getChildCount(); i++){
			View oneRow = table.getChildAt(i);
			if(oneRow instanceof TableRow){
				View theChildToResize = ((TableRow) oneRow).getChildAt(column);
				if(theChildToResize instanceof EditText){
					final TableRow.LayoutParams lparams = new TableRow.LayoutParams(width,height); // Width , height
				    ((EditText) theChildToResize).setLayoutParams(lparams);
				    String uri = "@drawable/cell_shape";
					int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
					Drawable res = getResources().getDrawable(imageResource);
					if (android.os.Build.VERSION.SDK_INT >= 16){
						((EditText) theChildToResize).setBackground(res);
					}
					else{
						((EditText) theChildToResize).setBackgroundDrawable(res);
					}
//					((EditText) theChildToResize).setWidth(width);
//					((EditText) theChildToResize).setMinimumWidth(width);
//					((EditText) theChildToResize).setHeight(height);
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
		String uri = "@drawable/cell_shape";
		int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
		Drawable res = getResources().getDrawable(imageResource);
//		oneColumn.setPadding(34, 34, 3, 3);
//		final LayoutParams lparams = new LayoutParams(190, 74);
//	    oneColumn.setLayoutParams(lparams);
		
		if (android.os.Build.VERSION.SDK_INT >= 16){
			oneColumn.setBackground(res);
		}
		else{
			oneColumn.setBackgroundDrawable(res);
		}
//		oneColumn.setMaxLines(1);
		oneColumn.setSingleLine();
//		oneColumn.setMinLines(1);
		View theText = oneColumn;
//		oneColumn.setMaxLines(2);
//		inputType="textMultiLine"
//		oneColumn.setInputType(te);Row.LayoutParams.WRAP_CONTENT);
//		oneColumn.setLayoutParams(lp);
//		oneColumn.setOnEditorActionListener(new EditText.OnEditorActionListener() {
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
					try {
						MainTemplateGenerator.myTemplate.saveToJSON(getActivity(), "testTemplate.json");
						Log.d("TABLE_FRAGMENT", "title changed - saved");
					} catch (JsonGenerationException | JsonMappingException e) {
					} catch(IOException e) {
						
					}
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
        oneColumn.setText("---");
        if(headerTable.indexOfChild(row) != -1){
            oneColumn.setText("Headline " + (((TableRow) headerTable.getChildAt(0)).getChildCount()+1));
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
	    oneColumn.requestLayout();
	    oneColumn.forceLayout();
	    oneColumn.invalidate();
		oneColumn.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		int newWidth = oneColumn.getMeasuredWidth();
		int newHeight = oneColumn.getMeasuredWidth();
		Log.d("width", "after w : " + newWidth);
		Log.d("width", "after h : " + newHeight);
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
			try {
				MainTemplateGenerator.myTemplate.saveToJSON(getActivity(), "testTemplate.json");
				Log.d("TABLE_FRAGMENT", "added column - saved");
			} catch (JsonGenerationException | JsonMappingException e) {
			} catch(IOException e) {
			}
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
				try {
					MainTemplateGenerator.myTemplate.saveToJSON(getActivity(), "testTemplate.json");
					Log.d("TABLE_FRAGMENT", "removed column - saved");
				} catch (JsonGenerationException | JsonMappingException e) {
				} catch(IOException e) {
				}
			}
			/*
			 *  JACKSON END
			 */
			Log.d("columns", "amount == " + amountColumns);
		}
	}

	/**
	 * @param dialogTable
	 * Method to set up the table for showing it in AlertDialog
	 */
	protected void prepareTableForDialog(TableLayout dialogTable) {
		dialogTable.removeAllViews();
		for(int i=-1; i<amountColumns; i++){
			final TableRow row = new TableRow(MainTemplateGenerator.theActiveActivity);
			TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
			rowParams.height = TableRow.LayoutParams.WRAP_CONTENT;
			rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;
			//use k columns
			for(int k=0; k<3; k++){
				final EditText oneColumn = new EditText(MainTemplateGenerator.theActiveActivity);
				String uri = "@drawable/cell_shape";
				int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
				Drawable res = getResources().getDrawable(imageResource);
				if (android.os.Build.VERSION.SDK_INT >= 16){
					oneColumn.setBackground(res);
				}
				else{
					oneColumn.setBackgroundDrawable(res);
				}
				oneColumn.setSingleLine();
				View theText = oneColumn;
				//TODO: how to resize the table if too big?
				oneColumn.addTextChangedListener(new TextWatcher(){
					public void afterTextChanged(Editable s) {
						//					checkResize(0, 0, oneColumn, row);
					}
					public void beforeTextChanged(CharSequence s, int start, int count, int after){
					}
					public void onTextChanged(CharSequence s, int start, int before, int count){
					}
				});
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
					oneColumn.setText(i+".");
				}
				else if(k==1){
					TableRow headerRow = (TableRow) headerTable.getChildAt(0);
					EditText headerText = (EditText) headerRow.getChildAt(i);
					Log.d("i,k", "i==" + i + ", k==" +k);
					oneColumn.setText(headerText.getText());					
				}
				row.addView(oneColumn);
			}
			dialogTable.addView(row);
		}
//		return table;
	}
	
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
	
	protected void initializeDialogTable(TableLayout dialogTable){
		final TableRow row = new TableRow(MainTemplateGenerator.theActiveActivity);
		TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
		rowParams.height = TableRow.LayoutParams.WRAP_CONTENT;
		rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;
		//use k columns
		for(int k=0; k<3; k++){
			final EditText oneColumn = new EditText(MainTemplateGenerator.theActiveActivity);
			String uri = "@drawable/cell_shape";
			int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
			Drawable res = getResources().getDrawable(imageResource);
			if (android.os.Build.VERSION.SDK_INT >= 16){
				oneColumn.setBackground(res);
			}
			else{
				oneColumn.setBackgroundDrawable(res);
			}
			oneColumn.setSingleLine();
			View theText = oneColumn;
			//TODO: how to resize the table if too big?
			oneColumn.addTextChangedListener(new TextWatcher(){
				public void afterTextChanged(Editable s) {
					//					checkResize(0, 0, oneColumn, row);
				}
				public void beforeTextChanged(CharSequence s, int start, int count, int after){
				}
				public void onTextChanged(CharSequence s, int start, int before, int count){
				}
			});
			//first row -> insert column descriptions
			if(k==0){
				oneColumn.setText("Nr.");
			}
			else if(k==1){
				oneColumn.setText("Spaltenname");
			}
			else{
				oneColumn.setText("Spaltentyp");
			}
			row.addView(oneColumn);
		}
		dialogTable.addView(row);
	}
	
//	protected void adaptDialogTable(TableLayout dialogTable){
//		int rowsNeeded = ((TableRow) headerTable.getChildAt(0)).getChildCount();
//		adaptDialogTable(dialogTable, rowsNeeded);
//	}
	
	protected void adaptDialogTable(TableLayout dialogTable){
		int firstRowToAdd = dialogTable.getChildCount();
		int rowsNeeded = ((TableRow) headerTable.getChildAt(0)).getChildCount();;
		//first step: adapt all needed Column-names from headerTable
		int oldColumnsToAdept = (firstRowToAdd<rowsNeeded? firstRowToAdd:rowsNeeded);
		for(int i=1; i<oldColumnsToAdept; i++){
			TableRow headerRow = (TableRow) headerTable.getChildAt(0);
			EditText headerText = (EditText) headerRow.getChildAt(i-1);
			EditText nameFromHeaderColumn = (EditText) ((TableRow) dialogTable.getChildAt(i)).getChildAt(1);
			nameFromHeaderColumn.setText(headerText.getText());
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
				final EditText oneColumn = new EditText(MainTemplateGenerator.theActiveActivity);
				String uri = "@drawable/cell_shape";
				int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
				Drawable res = getResources().getDrawable(imageResource);
				if (android.os.Build.VERSION.SDK_INT >= 16){
					oneColumn.setBackground(res);
				}
				else{
					oneColumn.setBackgroundDrawable(res);
				}
				oneColumn.setSingleLine();
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
					oneColumn.setText(headerText.getText());
					final int index = i;
					oneColumn.addTextChangedListener(new TextWatcher(){
						public void afterTextChanged(Editable s) {
							TableRow headerRow = (TableRow) headerTable.getChildAt(0);
							EditText headerText = (EditText) headerRow.getChildAt(index);
							headerText.setText(s);
							//					checkResize(0, 0, oneColumn, row);
						}
						public void beforeTextChanged(CharSequence s, int start, int count, int after){
						}
						public void onTextChanged(CharSequence s, int start, int before, int count){
						}
					});
				}
				row.addView(oneColumn);
			}
			dialogTable.addView(row);
		}
		//last step: remove rows if needed
		for(int i=rowsNeeded+1; i<firstRowToAdd; i++){
			Log.d("dialog", "remove");
			dialogTable.removeView(dialogTable.getChildAt(dialogTable.getChildCount()-1));
		}
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		setAmountOfColumns(newVal);
//		adaptDialogTable(((TableLayout) dialogViewTableView.findViewById(R.id.tableView_alert_table)), newVal);
		adaptDialogTable(((TableLayout) dialogViewTableView.findViewById(R.id.tableView_alert_table)));
	}
}
