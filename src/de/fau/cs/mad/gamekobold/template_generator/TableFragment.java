package de.fau.cs.mad.gamekobold.template_generator;



import java.util.ArrayList;

import com.nhaarman.supertooltips.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.ReattachingPopup;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.character.CharacterEditActivity;
import de.fau.cs.mad.gamekobold.character.CustomExpandableListAdapter;
import de.fau.cs.mad.gamekobold.jackson.CheckBoxClass;
import de.fau.cs.mad.gamekobold.jackson.ColumnHeader;
import de.fau.cs.mad.gamekobold.jackson.IEditableContent;
import de.fau.cs.mad.gamekobold.jackson.PopupClass;
import de.fau.cs.mad.gamekobold.jackson.StringClass;
import de.fau.cs.mad.gamekobold.jackson.Table;
import de.fau.cs.mad.gamekobold.matrix.MatrixFragment;
import de.fau.cs.mad.gamekobold.matrix.MatrixItem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;

@SuppressLint("NewApi")
public class TableFragment extends GeneralFragment implements OnCheckedChangeListener{
	/*
	 * JACKSON START
	 */
	Table jacksonTable;
	boolean jacksonInflateWithData = false;
	/*
	 * JACKSON END
	 */
	
	public enum content_type{
		editText, checkbox, popup;
	}
	
	View mainView;
	TableLayout table;
	TableLayout headerTable;
	// also set in createTableHeader !! onCreate is called when orientation is rotated ->
	// amountColumns = #before change but we recreate the header table with 2 columns
	int amountColumns = 2;
//	AlertDialog dialog;
//	TableLayout dialogTable;
	SessionMonitorEditText dialogRowCounter = null;
	//menu shown when long clicking a row
	protected TableRow contextMenuRow;
//	protected ArrayList<View> popupViewList = new ArrayList<>();
//	protected ArrayList<PopupWindow> popupList = new ArrayList<>();
	public Map<Pair<Integer,Integer>, ReattachingPopup> popupList = new ConcurrentHashMap<Pair<Integer,Integer>, ReattachingPopup>();
	public List<ReattachingPopup> currentlyShownPopups = new ArrayList<ReattachingPopup>();

	private final View.OnFocusChangeListener editTextFocusListener = new View.OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
//			Log.d("FOCUS", "focus changed:"+hasFocus);
			final TextView tv = (TextView)v;
			if(hasFocus) {
				if(tv.getEditableText().toString().equals(getResources().getString(R.string.blank))) {
					tv.setText("");
				}
			}
			else {
				if(tv.getEditableText().toString().isEmpty()) {
					tv.setText(R.string.blank);
				}
			}
		}
	};

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        setRetainInstance(true);
    	LayoutInflater inflater = (LayoutInflater) SlideoutNavigationActivity.theActiveActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(SlideoutNavigationActivity.theActiveActivity instanceof TemplateGeneratorActivity){
//			Log.d("TableFragment", "inflated for TemplateGenerator");
        	//create the table that will be shown in the dialog
        	mainView = (RelativeLayout) inflater.inflate(R.layout.template_generator_table_view, new LinearLayout(getActivity()), false);
        	table = (TableLayout) mainView.findViewById(R.id.template_generator_table);
        	createTableHeader();
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
        }
        else if(SlideoutNavigationActivity.theActiveActivity instanceof CharacterEditActivity){
			Log.d("TableFragment", "inflated for CharacterEditActivity");
        	mainView = (RelativeLayout) inflater.inflate(R.layout.character_edit_table_view, new LinearLayout(getActivity()), false);
        }
		// removed because we could otherwise not save/load tables with 0 rows
		//addItemList();
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		// JACKSON START
		if(SlideoutNavigationActivity.theActiveActivity instanceof TemplateGeneratorActivity){
			// check for saved rows
			table.removeAllViews();
			if(jacksonTable.getRowCount() > 0) {
				jacksonInflateWithData = true;
				// adjust row count
				final int jacksonRowNum = jacksonTable.getRowCount();
				while(table.getChildCount() < jacksonRowNum) {
					final int columnCount = jacksonTable.getNumberOfColumns();
			        TableRow row = new TableRow(getActivity());
			        registerForContextMenu(row);
			        table.addView(row);
					for(int i = 0 ; i < columnCount; i++) {
						final int rowIndex = table.getChildCount()-1;
						final ColumnHeader header = jacksonTable.getColumnHeader(i);
						View newElement = null;
						if(header.isString()) {
							newElement = initTextField(row, jacksonTable.getEntry(i, rowIndex));
						}
						else if(header.isCheckBox()) {
							newElement = initCheckBox(jacksonTable.getEntry(i, rowIndex));
						}
						else {
							newElement = initPopup(row, jacksonTable.getEntry(i, rowIndex), i, rowIndex);
						}
						row.addView(newElement);
						final int width = getNeededWidth(i);
						final int height = getNeededHeight(rowIndex, row);
						final LayoutParams lparams = new LayoutParams(width, height);
					    newElement.setLayoutParams(lparams);
					}
					Log.d("TableFragment", "added row");
				}
				while(table.getChildCount() > jacksonRowNum) {
					removeRow((TableRow)table.getChildAt(table.getChildCount()-1));
					Log.d("TableFragment", "removed row");
				}
				jacksonInflateWithData = false;
			}
			else {
				addItemList();
			}
		}
		else if(SlideoutNavigationActivity.theActiveActivity instanceof CharacterEditActivity){
			// check for saved rows
			fillListView();
		}
		//
		// JACKSON END
		//
		
		//show all previously shown popups
//		Stack<ReattachingPopup> copyStack = (Stack<ReattachingPopup>) currentPopups.clone();
//		currentPopups.clear();
//		while(!copyStack.isEmpty()){
//			ReattachingPopup toShow = copyStack.pop();
//			toShow.show();
//		}
		int popupsToShow = currentlyShownPopups.size();
		for(int i=0; i<popupsToShow; i++){
			final ReattachingPopup toShow = currentlyShownPopups.remove(0);
			if(SlideoutNavigationActivity.theActiveActivity.findViewById(android.R.id.content) == null){
				Log.d("SlideoutNavigationActivity", "IS NULL!!!!!");
			}
			mainView.post(new Runnable() {
		        public void run() {
		        	toShow.showAtLocation(SlideoutNavigationActivity.theActiveActivity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
		        }
		    });
//			toShow.show(this, SlideoutNavigationActivity.theActiveActivity.findViewById(android.R.id.content));
		}
		return mainView;
	}
	
	@Override
	public void onResume(){
		super.onResume();
//		int popupsToShow = currentlyShownPopups.size();
//		for(int i=0; i<popupsToShow; i++){
//			ReattachingPopup toShow = currentlyShownPopups.remove(0);
//			if(SlideoutNavigationActivity.theActiveActivity.findViewById(android.R.id.content) == null){
//				Log.d("SlideoutNavigationActivity", "IS NULL!!!!!");
//			}
//			toShow.showAtLocation(SlideoutNavigationActivity.theActiveActivity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
////			toShow.show(this, SlideoutNavigationActivity.theActiveActivity.findViewById(android.R.id.content));
//		}
	}
	
	private void fillListView(){
		final int jacksonTableColumnNumber = jacksonTable.getNumberOfColumns();
		final int jacksonRowNum = jacksonTable.getRowCount();

		// check if we got any saved columns (they are only created if user goes into table!)
		// so if there are no saved columns or we didn't load any data we add the default columns 
		
//		jacksonTable.getEntry(columnIndex, rowIndex)
//		List<String> your_array_list = new ArrayList<String>();
//        your_array_list.add("foo");
//		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                this, 
//                android.R.layout.simple_list_item_1,
//                your_array_list );
		String[] headlines = new String[jacksonTableColumnNumber];
		for(int i=0; i<jacksonTableColumnNumber; i++){
//			IEditableContent jacksonEntry = jacksonTable.getEntry(i, 0);
			headlines[i] = jacksonTable.getColumnHeader(i).getContent();
		}
		
		String[] groupList = new String[jacksonRowNum];
		String[][] itemValues = new String[jacksonRowNum][];
		content_type[][] contentList = new content_type[jacksonRowNum][];
		for(int rowIndex=0; rowIndex<jacksonRowNum; rowIndex++){
			groupList[rowIndex] = jacksonTable.getEntry(0, rowIndex).getContent();
		}
		
		for(int rowIndex=0; rowIndex<jacksonRowNum; rowIndex++){
			itemValues[rowIndex] = new String[jacksonTableColumnNumber];
			contentList[rowIndex] = new content_type[jacksonTableColumnNumber];
			for(int columnIndex=0; columnIndex<jacksonTableColumnNumber; columnIndex++){
				IEditableContent jacksonEntry = null;
				jacksonEntry = jacksonTable.getEntry(columnIndex, rowIndex);
				
				final ColumnHeader header = jacksonTable.getColumnHeader(columnIndex);
				if(header.isCheckBox()) {
					contentList[rowIndex][columnIndex] = content_type.checkbox;
				}
				else if(header.isPopup()){
					contentList[rowIndex][columnIndex] = content_type.popup;
				}
				else{
					contentList[rowIndex][columnIndex] = content_type.editText;
				}
				if(jacksonEntry != null) {
					String content = jacksonEntry.getContent();
					itemValues[rowIndex][columnIndex] = content;
				}
			}
		}
		ExpandableListView expListView = (ExpandableListView) mainView.findViewById(R.id.exp_table_list_view);
        final ExpandableListAdapter expListAdapter = new CustomExpandableListAdapter(
                getActivity(), jacksonTable, this, headlines, contentList, groupList, itemValues);
        expListView.setAdapter(expListAdapter);
	}
	
	protected void setTypeContents(TableLayout dialogTable){
		Log.d("TableFragment", "setTypeContents");
		final int jsonOldNumberOfColumns = jacksonTable.getNumberOfColumns();
		for(int i=1; i<dialogTable.getChildCount(); i++){
			int indexOfTable = i-1;
			TableRow dialogTableRow = (TableRow) dialogTable.getChildAt(i);
			final Spinner spin = (Spinner) dialogTableRow.getChildAt(2);
			final String choice = spin.getSelectedItem().toString();
			//
			// JSON START
			// check index
			if(indexOfTable > jsonOldNumberOfColumns) {
				// we have to create a new column
				if(choice.equals("Textfeld")){
					jacksonTable.addColumn(new ColumnHeader("", StringClass.TYPE_STRING));
				}
				else if(choice.equals("CheckBox")){
					jacksonTable.addColumn(new ColumnHeader("", CheckBoxClass.TYPE_STRING));
				}
				else if(choice.equals("PopUp")){
					jacksonTable.addColumn(new ColumnHeader("", PopupClass.TYPE_STRING));
				}
			}
			else {
				// we have to change the type
				// change column type in json
				if(choice.equals("Textfeld")){
					jacksonTable.changeColumnType(indexOfTable, StringClass.TYPE_STRING);
				}
				else if(choice.equals("CheckBox")){
					jacksonTable.changeColumnType(indexOfTable, CheckBoxClass.TYPE_STRING);
				}
				else if(choice.equals("PopUp")){
					jacksonTable.changeColumnType(indexOfTable, PopupClass.TYPE_STRING);
				}				
			}
			// JSON END
			//
			for(int k=0; k<table.getChildCount(); k++){
				boolean isModified = false;
				View newElement = null;
				final TableRow tableRow = (TableRow) table.getChildAt(k);
				View elementToAdapt = tableRow.getChildAt(indexOfTable);
				// moved before jackson part above
//				Spinner spin = (Spinner) dialogTableRow.getChildAt(2);
//				String choice = spin.getSelectedItem().toString();
				if(choice.equals("Textfeld")){
					if(!(elementToAdapt instanceof EditText)){
						isModified = true;
						tableRow.removeView(elementToAdapt);
						newElement = initTextField(tableRow, jacksonTable.getEntry(indexOfTable, k));
						tableRow.addView(newElement, indexOfTable);
					}
				}
				else if(choice.equals("CheckBox")){
					if(!(elementToAdapt instanceof LinearLayout)){
						isModified = true;
						tableRow.removeView(elementToAdapt);
						newElement = initCheckBox(jacksonTable.getEntry(indexOfTable, k));
						tableRow.addView(newElement, indexOfTable);
						Log.d("TABLE_FRAGMENT", "FFFFFFFFF");
					}
					else if(!(((LinearLayout) elementToAdapt).getChildAt(0) instanceof CheckBox)){
						isModified = true;
						tableRow.removeView(elementToAdapt);
						newElement = initCheckBox(jacksonTable.getEntry(indexOfTable, k));
						tableRow.addView(newElement, indexOfTable);
						Log.d("TABLE_FRAGMENT", "GGGGGGGGGG");
					}
				}
				else if(choice.equals("PopUp")){
					if(!(elementToAdapt instanceof TextView) || elementToAdapt instanceof EditText){
						isModified = true;
						tableRow.removeView(elementToAdapt);
						newElement = initPopup(tableRow, jacksonTable.getEntry(indexOfTable, k), indexOfTable, k);
						tableRow.addView(newElement, indexOfTable);
					}
					else if(((LinearLayout) elementToAdapt).getChildAt(0) instanceof CheckBox){
						isModified = true;
						tableRow.removeView(elementToAdapt);
						newElement = initPopup(tableRow, jacksonTable.getEntry(indexOfTable, k), indexOfTable, k);
						tableRow.addView(newElement, indexOfTable);
					}
				}
				if(isModified){
					newElement.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					int width = newElement.getMeasuredWidth();
					int height = newElement.getMeasuredHeight();
					checkResize(width, height, newElement, tableRow);
//					int width = getNeededWidth(i);
//					int height = getNeededHeight(0, tableRow);
//					final LayoutParams lparams = new LayoutParams(width, height);
//				    newElement.setLayoutParams(lparams);
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
			TextView textToChange = ((TextView) ((TableRow) headerTable.getChildAt(0)).getChildAt(i-1));
			if(!otherString.equals("")){
				textToChange.setText(otherString);
			}
		}
	}
	
	protected TableLayout createTableHeader() {
		headerTable = (TableLayout) mainView.findViewById(R.id.header_table);
		final TableRow row = new TableRow(getActivity());
		TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
		// Wrap-up the content of the row
		rowParams.height = TableRow.LayoutParams.MATCH_PARENT;
		rowParams.width = TableRow.LayoutParams.MATCH_PARENT;
		TableRow.LayoutParams colParams = new TableRow.LayoutParams();
		colParams.height = TableRow.LayoutParams.MATCH_PARENT;
		colParams.width = TableRow.LayoutParams.MATCH_PARENT;
		//TODO:
//		((View) row).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				showDialog();
//			}
//		});
		headerTable.addView(row);
		jacksonLoadTableHeader(row);
		return headerTable;
	}
	
	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		Log.d("TableFragment", "onCreateContextMenu view:"+v);
		if(v instanceof TableRow){
			contextMenuRow = (TableRow) v;
			// only show if v is a TableRow. We get double calls because
			// we have to register also checkbox and popup
			MenuInflater inflater = SlideoutNavigationActivity.theActiveActivity.getMenuInflater();
			inflater.inflate(R.menu.template_generator_remove_table_item, menu);
		}
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
		if(table.getChildCount() <= 1) {
			return;
		}
		// JACKSON
		if(!jacksonInflateWithData) {
			jacksonTable.removeRow(table.indexOfChild(row));
		}
		// JACKSON END
        table.removeView(row);
	}

	/**
	 * adds a new row to the table
	 */
	protected void addItemList() {
			//
			// JACKSON
			// create jackson row first, so we can use it in addColumnToRow
        	if(!jacksonInflateWithData) {
        		jacksonTable.addNewRow();
        	}
        	//
        	// JACKSON END
        	//
	        TableRow row = new TableRow(getActivity());
	        registerForContextMenu(row);
	        table.addView(row);
	        for(int i=0; i<amountColumns; ++i){
	        	addColumnToRow(row);
	        }
//	        table.addView(row);
	        final ScrollView sv = (ScrollView) mainView.findViewById(R.id.table_scroll);
	        //note: we have to do scrolling in seperate thread to make sure the new item is already inserted
	        sv.post(new Runnable() {
	            @Override
	            public void run() {
	            	sv.fullScroll(ScrollView.FOCUS_DOWN);
	            }
	        });
	        for(int i=0; i<((TableRow) headerTable.getChildAt(0)).getChildCount(); i++){
	        	setHeaderTableStyle((TextView) ((TableRow) headerTable.getChildAt(0)).getChildAt(i));
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
//		Log.d("index", "indexOfRow  == " + indexOfRow);
		int indexOfColumn = -1;
		if(usedTable.getChildAt(indexOfRow) instanceof TableRow){
			indexOfColumn = ((TableRow) usedTable.getChildAt(indexOfRow)).indexOfChild(textEdited);
		}
//		Log.d("index", "indexOfColumn  == " + indexOfColumn);
		int largestWidth = 0;
		//traverse over all columns to get biggest width
		//first columns of normal table
		for (int i = 0; i < table.getChildCount(); i++) {
			View child = table.getChildAt(i);
			TableRow oneRow = (TableRow) child;
			if (child instanceof TableRow) {
				View view = oneRow.getChildAt(indexOfColumn);
				if(view != null){
					int localWidth = 0;
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
				if(view != null){
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
			if(theChildToResize != null){
//				setHeaderTableStyle((EditText) theChildToResize);
				theChildToResize.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				final TableRow.LayoutParams lparams = new TableRow.LayoutParams(width,getNeededHeight(0, ((TableRow) singleRow))); // Width , height
			    theChildToResize.setLayoutParams(lparams);
			}
		}
		for(int i=0; i<table.getChildCount(); i++){
			View oneRow = table.getChildAt(i);
			if(oneRow instanceof TableRow){
				View theChildToResize = ((TableRow) oneRow).getChildAt(columnIndex);
				if(theChildToResize != null){
					theChildToResize.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					final TableRow.LayoutParams lparams = new TableRow.LayoutParams(width, getNeededHeight(i, ((TableRow) oneRow))); // Width , height
				    theChildToResize.setLayoutParams(lparams);
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
	
	private TextView initTextField(final TableRow row, final IEditableContent jacksonEntry){
		//standard: use edittext
		return initTextField(row, jacksonEntry, true);
	}
	
	private TextView initTextField(final TableRow row, final IEditableContent jacksonEntry, boolean editable){
//		Log.d("initEditText", "jacksonEntry:"+jacksonEntry.hashCode());
		TextView tv;
		if(editable){
			tv = new EditText(getActivity());
		}
		else{
			tv = new TextView(getActivity());
		}
		final TextView newElement = tv;
		newElement.setGravity(Gravity.CENTER);
			newElement.addTextChangedListener(new TextWatcher(){
				public void afterTextChanged(Editable s) {
					if(row !=null){
						checkResize(0, 0, newElement, row);
					}
				}
				public void beforeTextChanged(CharSequence s, int start, int count, int after){
				}
				public void onTextChanged(CharSequence s, int start, int before, int count){
				}
			});
		if(row !=null){
			if(row == headerTable.getChildAt(0)) {
				setHeaderTableStyle((TextView) newElement);
			}
			else {
				setTableStyle(newElement);
			}
		}
		//
		// JACKSON START
		//
		if(jacksonEntry != null) {
			newElement.setText(jacksonEntry.getContent());
			newElement.addTextChangedListener(new TextWatcher() {
				private final IEditableContent content = jacksonEntry;
				public void afterTextChanged(Editable s) {
					// guard. only set new content if we are not currently inflating with data.
					if(!jacksonInflateWithData) {
						content.setContent(s.toString());
					}
					if(row !=null){
						checkResize(0, 0, newElement, row);
					}
				}
				public void beforeTextChanged(CharSequence s, int start, int count, int after){
				}
				public void onTextChanged(CharSequence s, int start, int before, int count){
				}
			});
		}
		//
		// JACKSON END
		//
		newElement.setOnFocusChangeListener(editTextFocusListener);
		// we can do it like this with a check, or we change the default value in jackson model
		// check is needed, because we would otherwise overwrite the loaded value
		if(newElement.getText().toString().isEmpty()) {
			if(row !=null){
				if(row == headerTable.getChildAt(0)) {
					newElement.setText(getResources().getString(R.string.headline)
							+ " "
							+ (((TableRow) headerTable.getChildAt(0))
									.getChildCount()+1));
				}	
				else {
					((TextView) newElement).setText(getResources().getString(R.string.blank));
				}
			}
		}
//		((EditText) newElement).clearFocus();
//		setTableStyle(newElement);
		return newElement;
	}
	
    int styleStart = 0;
    int cursorLoc = 0;
    
    private ArrayList<MatrixItem> getAllMatrixReferences(FolderFragment fragmentToSearch){
    	ArrayList<MatrixItem> results = new ArrayList<MatrixItem>();
		Log.d("popupReferences", "subdirs: " + fragmentToSearch.dataAdapter.getAll().length);
        for(FolderElementData currentDatum  : fragmentToSearch.dataAdapter.getAll()){
        	GeneralFragment currentFragment = currentDatum.childFragment;
        	if(currentFragment instanceof FolderFragment){
    			Log.d("popupReferences", "folderfragment found, descending now");
        		ArrayList<MatrixItem> toAdd = getAllMatrixReferences((FolderFragment) currentFragment);
        		results.addAll(toAdd);
        	}
        	else if(currentFragment instanceof TableFragment){
    			Log.d("popupReferences", "tableview found");
        	}
        	else if(currentFragment instanceof MatrixFragment){
    			Log.d("popupReferences", "matrix found. Elements:" + (((MatrixFragment) currentFragment).itemsList).size());
        		for(MatrixItem oneItem : ((MatrixFragment) currentFragment).itemsList){
        			results.add(oneItem);
        		}
        	}
        	else{
    			Log.d("popupReferences", "unhandled element found!!!");
        	}
        }
        return results;
    }
    
    private ArrayList<String> getAllElementsToRef(FolderFragment fragmentToSearch){
    	ArrayList<String> results = new ArrayList<String>();
		Log.d("popupReferences", "subdirs: " + fragmentToSearch.dataAdapter.getAll().length);
        for(FolderElementData currentDatum  : fragmentToSearch.dataAdapter.getAll()){
        	GeneralFragment currentFragment = currentDatum.childFragment;
        	if(currentFragment instanceof FolderFragment){
    			Log.d("popupReferences", "folderfragment found, descending now");
        		ArrayList<String> toAdd = getAllElementsToRef((FolderFragment) currentFragment);
        		results.addAll(toAdd);
        	}
        	else if(currentFragment instanceof TableFragment){
    			Log.d("popupReferences", "tableview found");
        	}
        	else if(currentFragment instanceof MatrixFragment){
    			Log.d("popupReferences", "matrix found. Elements:" + (((MatrixFragment) currentFragment).itemsList).size());
        		for(MatrixItem oneItem : ((MatrixFragment) currentFragment).itemsList){
        			String oneName = oneItem.getItemName();
        			results.add(oneName);
        		}
        	}
        	else{
    			Log.d("popupReferences", "unhandled element found!!!");
        	}
        }
        return results;
    }
	
    boolean alreadyChangingText = false;
    
    private LinearLayout initPopup(final TableRow row, final IEditableContent jacksonEntry, int columnIndex, int rowIndex){
		Log.d("TABLE_FRAGMENT", "init_popup");
		final LinearLayout ll = new LinearLayout(getActivity());
		ll.setFocusable(true);
		ll.setFocusableInTouchMode(true);
		final TextView newElement = new TextView(getActivity());
		//
		// JACKSON START
		//
		//temporarily disabled, have to take a closer look to popups
//		if(jacksonEntry != null) {
//			newElement.setText(jacksonEntry.toString());
//			newElement.addTextChangedListener(new TextWatcher() {
//				final IEditableContent myJacksonEntry = jacksonEntry;
//				// callback
//				public void afterTextChanged(Editable s) {
//					// guard. only set column title if we are not currently inflating with data
//					if(!jacksonInflateWithData) {
//						myJacksonEntry.setContent(s.toString());
//					}
//				}
//				public void beforeTextChanged(CharSequence s, int start, int count, int after){
//				}
//				public void onTextChanged(CharSequence s, int start, int before, int count){
//				}
//			});
//		}
		//
		// JACKSON END
		//
		final TextView txt = (TextView) newElement;
		txt.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				if(row != null){
					checkResize(0, 0, txt, row);
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}
			public void onTextChanged(CharSequence s, int start, int before, int count){
			}
		});
        LayoutInflater inflater = (LayoutInflater) SlideoutNavigationActivity.theActiveActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.table_view_popup, null);
//        final View layoutContainingHeadline = (View) popupView.findViewById(R.id.popup_content);
        final TextView popupHeadline = (TextView) popupView.findViewById(R.id.popup_headline);
        popupHeadline.setText(((SlideoutNavigationActivity) SlideoutNavigationActivity.theActiveActivity).getCurrentFragment().elementName);
        final EditText inputPopup = (EditText) popupView.findViewById(R.id.popup_editText);
        
        
        
        final ToggleButton toggleBold = (ToggleButton) popupView.findViewById(R.id.toggle_bold);
        toggleBold.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
//        		int selectionStart = inputPopup.getSelectionStart();
//        		styleStart = selectionStart;
                int selectionStart = inputPopup.getSelectionEnd();

                styleStart = selectionStart;

//                int selectionEnd = inputPopup.getSelectionEnd();
//
//                if (selectionStart > selectionEnd){
//                    int temp = selectionEnd;
//                    selectionEnd = selectionStart;
//                    selectionStart = temp;
//                }
//
//
//                if (selectionEnd > selectionStart)
//                {
//                    Spannable str = inputPopup.getText();
//                    StyleSpan[] ss = str.getSpans(selectionStart, selectionEnd, StyleSpan.class);
//
//                    boolean exists = false;
//                    for (int i = 0; i < ss.length; i++) {
//                        if (ss[i].getStyle() == android.graphics.Typeface.BOLD){
//                            str.removeSpan(ss[i]);
//                            exists = true;
//                        }
//                    }
//
//                    if (!exists){
//                        str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            			Log.d("textstyle", "setSpan!!!");
//                    }
//
//                    toggleBold.setChecked(false);
//                }
        	}
        });
        final ToggleButton toggleItalic = (ToggleButton) popupView.findViewById(R.id.toggle_italic);
        toggleItalic.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
//        		int selectionStart = inputPopup.getSelectionStart();
//        		styleStart = selectionStart;
        		int selectionStart = inputPopup.getSelectionEnd();

                styleStart = selectionStart;

//                int selectionEnd = inputPopup.getSelectionEnd();
//
//                if (selectionStart > selectionEnd){
//                    int temp = selectionEnd;
//                    selectionEnd = selectionStart;
//                    selectionStart = temp;
//                }
//
//
//                if (selectionEnd > selectionStart)
//                {
//                    Spannable str = inputPopup.getText();
//                    StyleSpan[] ss = str.getSpans(selectionStart, selectionEnd, StyleSpan.class);
//
//                    boolean exists = false;
//                    for (int i = 0; i < ss.length; i++) {
//                        if (ss[i].getStyle() == android.graphics.Typeface.BOLD){
//                            str.removeSpan(ss[i]);
//                            exists = true;
//                        }
//                    }
//
//                    if (!exists){
//                        str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            			Log.d("textstyle", "setSpan!!!");
//                    }
//
//                    toggleItalic.setChecked(false);
//                }
        	}
        });
        final ToggleButton toggleUnderlined = (ToggleButton) popupView.findViewById(R.id.toggle_underline);
        toggleUnderlined.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        toggleUnderlined.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
//        		int selectionStart = inputPopup.getSelectionStart();
//        		styleStart = selectionStart;
        		int selectionStart = inputPopup.getSelectionEnd();

                styleStart = selectionStart;

//                int selectionEnd = inputPopup.getSelectionEnd();
//
//                if (selectionStart > selectionEnd){
//                    int temp = selectionEnd;
//                    selectionEnd = selectionStart;
//                    selectionStart = temp;
//                }
//
//
//                if (selectionEnd > selectionStart)
//                {
//                    Spannable str = inputPopup.getText();
//                    StyleSpan[] ss = str.getSpans(selectionStart, selectionEnd, StyleSpan.class);
//
//                    boolean exists = false;
//                    for (int i = 0; i < ss.length; i++) {
//                        if (ss[i].getStyle() == android.graphics.Typeface.BOLD){
//                            str.removeSpan(ss[i]);
//                            exists = true;
//                        }
//                    }
//
//                    if (!exists){
//                        str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            			Log.d("textstyle", "setSpan!!!");
//                    }
//
//                    toggleUnderlined.setChecked(false);
//                }
        	}
        });
        
        
        inputPopup.addTextChangedListener(new TextWatcher() { 
        	final IEditableContent myJacksonEntry = jacksonEntry;
        	public void afterTextChanged(Editable s) { 
        	}
        	public void beforeTextChanged(CharSequence s, int start, int count, int after) { 
        		//unused
        	} 
        	public void onTextChanged(CharSequence s, int start, int before, int count) { 
        		if(alreadyChangingText){
//            		Log.d("textstyle", "alreadChanging!");
        			return;
        		}
        		else{
        			alreadyChangingText = true;
        		}
//        		Log.d("textstyle", "CHANGE!!!");
        		int position = inputPopup.getSelectionStart();
        		if (position < 0){
        			position = 0;
        		}
        		if (position > 0){
        			if (styleStart > position){
        				//user changed cursor location, reset
//                		Log.d("textstyle", "RESET!!!\n" + "position == "+ position + "; styleStart == " + styleStart
//                				+ "; cursorLoc == " + cursorLoc);
                		Log.d("textstyle", "RESET!!!");
        				styleStart = position - 1;
        			}
        			else if(position > (cursorLoc + 1)){
                		Log.d("textstyle", "RESET!!!");
        				styleStart = position - 1;
        			}
        			cursorLoc = position;
//        			SpannableStringBuilder highlightedText = (SpannableStringBuilder) s;
        			Editable text = inputPopup.getText();
////        			position++;
//        			if(text.toString().charAt(text.length()) != ' '){
//            			text.append(" ");
//        			}
        			Log.d("textstyle", "text to style: " + text.toString());
        			SpannableStringBuilder highlightedText = (SpannableStringBuilder) text;
//            		Log.d("textstyle", "position == "+ position + "; styleStart == " + styleStart
//            				+ "; cursorLoc == " + cursorLoc);
//            		Log.d("textstyle", "styleStart == " + styleStart + ", position == " + position);
            		Object[] spans = highlightedText.getSpans(styleStart, position, Object.class);
//        			highlightedText.clearSpans();
//        			Log.d("textstyle", "before has " + spans.length + " spans!");
//            		for(Object span : spans) {
//            			highlightedText.removeSpan(span);
////            			Log.d("textstyle", "remove from " + styleStart + " to " + position);
//            		}
            		spans = highlightedText.getSpans(styleStart, position, Object.class);
        			Log.d("textstyle", "before has " + spans.length + " spans! (" + styleStart + " to " + position + ")");
        			for (int i = 0; i < spans.length; i++) {
//            			Log.d("textstyle", "new span: " + spans[i].getClass().toString());
            			if(spans[i].getClass().toString().equals("class android.text.style.StyleSpan")){
//                			Log.d("textstyle", "stylespan found!");
            				StyleSpan ss = ((StyleSpan) spans[i]);
                			Log.d("textstyle", "stylespan: " + ss.getStyle() );
            			}
        			}
//        			boolean includeEnd = position==text.length();
//        			boolean longerOne = (position-styleStart)>1;
        			if (toggleUnderlined.isChecked()){
                    	UnderlineSpan[] ss = highlightedText.getSpans(styleStart, position, UnderlineSpan.class);
                        for (int i = 0; i < ss.length; i++) {
                                highlightedText.removeSpan(ss[i]);
                    			Log.d("textstyle", "removeSpan: UNDERLINED!!! (" + styleStart + " to " + position + ")");
                        }
                        highlightedText.setSpan(new UnderlineSpan(), styleStart, position,  33);
            			Log.d("textstyle", "setSpan: UNDERLINED!!! (" + styleStart + " to " + position + ")");
//            			Log.d("textstyle", "UNDERLINE from " + styleStart + " to " + position);
                    }
                    //XXX:note: i think recursive calls to listener are the problem
            		spans = text.getSpans(styleStart, position, Object.class);
        			Log.d("textstyle", "setting text with " + spans.length + " spans! (" + styleStart + " to " + position + ")");
        			for (int i = 0; i < spans.length; i++) {
//            			Log.d("textstyle", "new span: " + spans[i].getClass().toString());
            			if(spans[i].getClass().toString().equals("class android.text.style.StyleSpan")){
//                			Log.d("textstyle", "stylespan found!");
            				StyleSpan ss = ((StyleSpan) spans[i]);
                			Log.d("textstyle", "stylespan: " + ss.getStyle() );
            			}
        			}
        			if (toggleBold.isChecked()){
//        				if(!toggleItalic.isChecked()){
        					StyleSpan[] ss = highlightedText.getSpans(styleStart, position, StyleSpan.class);
        					for (int i = 0; i < ss.length; i++) {
        						if (ss[i].getStyle() == android.graphics.Typeface.BOLD){
        							highlightedText.removeSpan(ss[i]);
                        			Log.d("textstyle", "removeSpan: BOLD!!! (" + styleStart + " to " + position + ")");
        						}
        					}
        					highlightedText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), styleStart, position,  33);
                			Log.d("textstyle", "setSpan: BOLD!!! (" + styleStart + " to " + position + ")");
//        					Log.d("textstyle", "BOLD from " + styleStart + " to " + position);
//        				}
//        				else{
//        					StyleSpan[] ss = highlightedText.getSpans(styleStart, position, StyleSpan.class);
//        					for (int i = 0; i < ss.length; i++) {
//        						if (ss[i].getStyle() == android.graphics.Typeface.BOLD_ITALIC){
//        							highlightedText.removeSpan(ss[i]);
//                        			Log.d("textstyle", "removeSpan: BOLD_ITALIC!!! (" + styleStart + " to " + position + ")");
//        						}
//        					}
//        					highlightedText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), styleStart, position,  33);
//                			Log.d("textstyle", "setSpan: BOLD_ITALIC!!! (" + styleStart + " to " + position + ")");
//        					Log.d("textstyle", "BOLD_ITALIC from " + styleStart + " to " + position);
//        				}
                    }
        			if (toggleItalic.isChecked()){
                        StyleSpan[] ss = highlightedText.getSpans(styleStart, position, StyleSpan.class);
                        for (int i = 0; i < ss.length; i++) {
                            if (ss[i].getStyle() == android.graphics.Typeface.ITALIC){
                                highlightedText.removeSpan(ss[i]);
                    			Log.d("textstyle", "removeSpan: ITALIC (" + styleStart + " to " + position + ")");
                            }
                        }
                        highlightedText.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), styleStart, position,  33);
            			Log.d("textstyle", "setSpan: ITALIC (" + styleStart + " to " + position + ")");
//                        Log.d("textstyle", "ITALIC from " + styleStart + " to " + position);
        			}
            		
//        			String str = text.toString().trim();
//        			if(str.length()!=0){
//        			str  = str.substring( 0, str.length() - 1 ); 
////        			    yourEditText.setText ( str );
//        			}
//        			position--;
//        			text.replace(position, position+1, "");
        			inputPopup.setText(highlightedText);
//                    inputPopup.setText((Spanned)inputPopup.getText().delete(position , position+1));
                    myJacksonEntry.setContent(s.toString());
                    
                    inputPopup.setSelection(position);
//                    inputPopup.clearFocus();
//                    ll.requestFocus();
//                    inputPopup.setSelection(position);
//        			text.replace(position, position+1, "");
//        			inputPopup.setText(text);
        		}
        		alreadyChangingText = false;
        	} 
        });
        
        Log.d("TableFragment", "VOR Popup Inhalt!");
        // TEST
        if(!jacksonEntry.getContent().isEmpty()) {
	        Log.d("TableFragment", "set Popup Inhalt!");
        	inputPopup.setText(jacksonEntry.getContent());
        	//XXX: references activation
        	if(SlideoutNavigationActivity.theActiveActivity instanceof CharacterEditActivity){
    	        Log.d("TableFragment", "durchsuche Popup!");
        		String searchForReferences = inputPopup.getText().toString();
        		Pattern p = Pattern.compile("@");
        	    Matcher m = p.matcher(searchForReferences);
        	    while (m.find()){
//        	    	System.out.print("Start index: " + matcher.start());
        	    	//TODO: copy&paste for highlighting popup?
        	    	int startIndex = m.start();
        	    	int endIndex = startIndex;
        	    	while(searchForReferences.charAt(endIndex) != ' ' && searchForReferences.charAt(endIndex) != '\n'
        	    			&& searchForReferences.charAt(endIndex) != '\b'){
        	    		endIndex++;
        	    	}
        	    	String referenceString = searchForReferences.substring(startIndex, endIndex);
        	    	getAllMatrixReferences(((SlideoutNavigationActivity) SlideoutNavigationActivity.theActiveActivity).getRootFragment());
        	    	Spannable span = (Spannable) inputPopup;
        	    	span.setSpan(new MyClickableSpan(popupView), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        	        span.setSpan(new ClickableSpan() {  
//        	            @Override
//        	            public void onClick(View v) {  
////        	            	ToolTipRelativeLayout a = new ToolTipRelativeLayout(getActivity(), null);
//        	            	ToolTipRelativeLayout toolTipRelativeLayout = (ToolTipRelativeLayout) popupView.findViewById(R.id.activity_main_tooltipRelativeLayout);
////
//        	                ToolTip toolTip = new ToolTip()
//        	                                    .withText("A beautiful View")
//        	                                    .withColor(R.color.red)
//        	                                    .withShadow();
//        	                View myToolTipView = toolTipRelativeLayout.showToolTipForView(toolTip, this.);
////        	                myToolTipView.setOnToolTipViewClickedListener(MainActivity.this);
//        	            	
//        	                //TODO: tooltip
//        	            }
//        	        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        	        inputPopup.setText(span); 
        	    }
        	}
        	
        }
        // TEST END
        
        //before
//        toggleBold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                	if(toggleItalic.isChecked()){
////                		inputPopup.setTextAppearance(getActivity(), R.style.italic_bold);
//                		inputPopup.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
//                	}
//                	else{
//                		inputPopup.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//                	}
//                } else {
//                	if(toggleItalic.isChecked()){
////                		inputPopup.setTextAppearance(getActivity(), R.style.italic);
//                		inputPopup.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
//                	}
//                	else{
////                		inputPopup.setTextAppearance(getActivity(), R.style.normal_text);
//                		inputPopup.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
//                	}
//                }
//            }
//        });
        
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
//        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
//        int popupHeight = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.popup_height), getResources().getDisplayMetrics()));
//        int popupWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.popup_width), getResources().getDisplayMetrics()));
//		int popupHeight = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (dpHeight*0.8), getResources().getDisplayMetrics()));
		final int popupWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) (dpWidth*0.9), getResources().getDisplayMetrics()));

//        mainView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//		int mainWidth = mainView.getMeasuredWidth();
//		int mainHeight = mainView.getMeasuredHeight();
//        final PopupWindow popup = new PopupWindow(popupView, popupWidth, popupHeight, true);
		ReattachingPopup searchedPopup = null;
		ReattachingPopup savedPopup = null;
		if((savedPopup =
				popupList.get(new Pair<Integer, Integer>(Integer.valueOf(columnIndex), Integer.valueOf(rowIndex))))
				== null){
	        Log.d("searchedPopup", "new one");
			searchedPopup = new ReattachingPopup(this, popupView, popupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, true);
		}
		else{
	        Log.d("searchedPopup", "saved one");
			searchedPopup = savedPopup;
		}
		final ReattachingPopup popup = searchedPopup;
//		final ReattachingPopup popup = new ReattachingPopup(this, popupView, popupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popup.setBackgroundDrawable(new BitmapDrawable(getResources(),""));
//		popup.setOutsideTouchable(false);
        popup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        
        

        final Button addRefButton = (Button) popupView.findViewById(R.id.add_ref);
        final TableFragment tf = this; 
        addRefButton.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
//        		Animation slide_up = AnimationUtils.loadAnimation(TemplateGeneratorActivity.theActiveActivity, R.animator.slide_up);
                LayoutInflater inflater = (LayoutInflater) SlideoutNavigationActivity.theActiveActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupReferencesView = inflater.inflate(R.layout.table_view_references, null);
                LinearLayout reference_list = (LinearLayout) popupReferencesView.findViewById(R.id.reference_list);
                final ReattachingPopup popupReferences = new ReattachingPopup(tf, popupReferencesView, popupWidth, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupReferences.setBackgroundDrawable(new BitmapDrawable(getResources(),""));
                popupReferences.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//                popupReferences.setOnDismissListener(new OnDismissListener() {
//					@Override
//					public void onDismiss() {
//				        Log.d("inputPopup", "now selection set");
//	                    inputPopup.setSelection(inputPopup.getText().length());
//	                    inputPopup.requestFocus();
//					}
//				});
                ArrayList<String> allRefs = getAllElementsToRef(((SlideoutNavigationActivity) SlideoutNavigationActivity.theActiveActivity).getRootFragment());
//                for(String aReference : allRefs){
                //skip the last element (this is "new element")
                if(allRefs.size() > 1){
                	TextView oneLine = new TextView(SlideoutNavigationActivity.theActiveActivity);
                	//TODO: translate
            		oneLine.setText("Choose the value to reference");
            		oneLine.setGravity(Gravity.CENTER);
            		oneLine.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
            				getResources().getDimension(R.dimen.text_large));
            		setHeaderTableStyle(oneLine);
            		reference_list.addView(oneLine);
                	for(int i=0; i<(allRefs.size()-1); i++){
                		String aReference = allRefs.get(i);
                		oneLine = new TextView(SlideoutNavigationActivity.theActiveActivity);
                		oneLine.setText(aReference);
                		oneLine.setGravity(Gravity.CENTER);
                		oneLine.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
                				getResources().getDimension(R.dimen.text_large));
                		oneLine.setOnClickListener(new OnClickListener() {
                			@Override
                			public void onClick(View v) {
                				String lastCharOfPopup = inputPopup.getText().toString();
                				if(lastCharOfPopup.length() > 0){
                					lastCharOfPopup = lastCharOfPopup.substring(lastCharOfPopup.length() - 1);
                					if(!lastCharOfPopup.equals("\n") && !lastCharOfPopup.equals(" ")){
                						inputPopup.append(" ");
                					}
                				}
                				inputPopup.append("@" + ((TextView) v).getText()+ " ");
                				popupReferences.dismiss();
                			}
                		});
                		setTableStyle(oneLine);
                		//                	setAddButtonStyle(oneLine);
                		//                	oneLine.setBackground(R.drawable.cell_shape_white_borders);
                		reference_list.addView(oneLine);
                	}
                	popupReferences.showAtLocation(SlideoutNavigationActivity.theActiveActivity.findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
                	//                  reference_view.startAnimation(slide_up);

                }
                else{
                	//TODO: translation
            		Toast.makeText(getActivity(), "unfortunately nothing available to reference to" ,Toast.LENGTH_SHORT).show();
                }
        	}
        });
        //following does not work yet
//        inputPopup.setOnFocusChangeListener(new OnFocusChangeListener() {
//			
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				InputMethodManager inputMgr = (InputMethodManager)TemplateGeneratorActivity.theActiveActivity.
//						getSystemService(Context.INPUT_METHOD_SERVICE);
//				if(hasFocus){
//					inputMgr.showSoftInput(inputPopup, InputMethodManager.SHOW_FORCED);
////					inputMgr.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
////							InputMethodManager.SHOW_IMPLICIT);
//					
////					TemplateGeneratorActivity.theActiveActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//			        Log.d("focus_change", "SHOW software-keyboard");
//				}
//				else{
//					inputMgr.hideSoftInputFromWindow(inputPopup.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
////					inputMgr.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
////							InputMethodManager.HIDE_IMPLICIT_ONLY);
////					TemplateGeneratorActivity.theActiveActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
////			        Log.d("focus_change", "HIDE software-keyboard");
//				}
//			}
//		});
        
		ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View headlineView = ((TableRow) headerTable.getChildAt(0)).getChildAt(getColumnIndex(ll));
				String headline = ((TextView) headlineView).getText().toString();
				popupHeadline.setText(headline);
				//old version... but we need to take the content as parent, not popupView
//				popup.showAtLocation(popupView, Gravity.CENTER, 0, 0);
				popup.showAtLocation(SlideoutNavigationActivity.theActiveActivity.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
				//XXX: might be needed?!
//				InputMethodManager inputMgr = (InputMethodManager)SlideoutNavigationActivity.theActiveActivity.
//						getSystemService(Context.INPUT_METHOD_SERVICE);
//					inputMgr.showSoftInput(inputPopup, InputMethodManager.SHOW_FORCED);
			}
		});
		popupView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				inputPopup.requestFocus();
			}
		});
		txt.setText(getResources().getString(R.string.new_element));
		txt.setTextColor(getResources().getColor(R.color.background));
		txt.setSingleLine();
		ll.addView(txt);
		ll.setGravity(Gravity.CENTER);
		// set it here because otherwise a long click on the cell
		// won't trigger the context menu
		ll.setOnCreateContextMenuListener(this);
		setTableStyle(ll);
		return ll;
	}
	
	private int getColumnIndex(View view){
		int columnIndex = -1;
		TableRow oneRow = (TableRow) headerTable.getChildAt(0);
		for(int i=0; i<oneRow.getChildCount(); i++){
			if(view.equals(oneRow.getChildAt(i))){
				columnIndex = i;
			}
		}
		for(int k=0; k<table.getChildCount(); k++){
			oneRow = (TableRow) table.getChildAt(k);
			for(int i=0; i<oneRow.getChildCount(); i++){
				if(view.equals(oneRow.getChildAt(i))){
					columnIndex = i;
				}
			}
		}
		return columnIndex;
	}
	
	private LinearLayout initCheckBox(final IEditableContent jacksonEntry){
		LinearLayout newElement = new LinearLayout(getActivity());
		CheckBox cb = new CheckBox(getActivity());
		if(jacksonEntry != null) {
			// sets the onCheckedChangeListener
			// this is needed so we can take over the changes to our jackson model
			cb.setOnCheckedChangeListener(this);
			// sets the associated jackson row to this 
			cb.setTag(R.id.jackson_row_tag_id, jacksonEntry);
			// set checked state to jackson state
			cb.setChecked(Boolean.parseBoolean(jacksonEntry.getContent()));
		}
		((LinearLayout) newElement).addView(cb);
		((LinearLayout) newElement).setGravity(Gravity.CENTER);
		cb.setButtonDrawable(R.drawable.custom_checkbox);
		setTableStyle(newElement);
		// set it here because otherwise a long click on the drawable/checkbox
		// won't trigger the context menu
		cb.setOnCreateContextMenuListener(this);
		return newElement;
	}
	
	/**
	 * adds one column to the given row 
	 * @param row
	 */
	protected void addColumnToRow(final TableRow row){
		int columnIndex = row.getChildCount();
        int rowIndex = table.indexOfChild(row);
        if(rowIndex == -1){
        	rowIndex = headerTable.indexOfChild(row);
        	if(rowIndex == -1)         Log.d("critical", "cant find row!");
        }
        View newElement = null;
        //get the needed content_type by choosing it to be the first rows type
        View firstRowView = null;
        TableRow firstRow = (TableRow) table.getChildAt(0);
        if(firstRow != null){
        	firstRowView = firstRow.getChildAt(columnIndex);
        }
        if(firstRowView instanceof EditText){
        	newElement = initTextField(row, jacksonTable.getEntry(columnIndex, rowIndex));
        }
        else if(firstRowView instanceof LinearLayout){
        	if(((LinearLayout) firstRowView).getChildAt(0) instanceof CheckBox){
        		newElement = initCheckBox(jacksonTable.getEntry(columnIndex, rowIndex));
        	}
        	else{// if(((LinearLayout) firstRowView).getChildAt(0) instanceof TextView){
            	newElement = initPopup(row, jacksonTable.getEntry(columnIndex, rowIndex), columnIndex, rowIndex);
            }
        }
        else{
        	//firstRowView should be null now -> its a new column
        	// could also be the first row inserted after an empty table
        	// quick temp fix
        	if(row == headerTable.getChildAt(0)) {
//        		Log.d("row==headerTable", "col:"+columnIndex+ " row:"+rowIndex);
        		newElement = initTextField(row, jacksonTable.getColumnHeader(columnIndex), false);
        	}
        	else {
//        		Log.d("row==ChildTable", "col:"+columnIndex+ " row:"+rowIndex);
        		newElement = initTextField(row, jacksonTable.getEntry(columnIndex, rowIndex), true);
        	}
        }
		row.addView(newElement);
		int width = getNeededWidth(columnIndex);
		int height = getNeededHeight(rowIndex, row);
//		int oldWidth = oneColumn.getMeasuredWidth();
//		int oldHeight = oneColumn.getMeasuredWidth();
		final LayoutParams lparams = new LayoutParams(width, height);
	    newElement.setLayoutParams(lparams);
	    // moved to initEditText
//        if (headerTable.indexOfChild(row) != -1) {
			//setHeaderTableStyle((EditText) newElement);
//			if(((EditText)newElement).getText().toString().isEmpty()) {
//				((EditText) newElement)
//						.setText(getResources().getString(R.string.headline)
//								+ " "
//								+ (((TableRow) headerTable.getChildAt(0))
//										.getChildCount()));
//			}
//		} else {
//			setTableStyle(newElement);
//		}

	}
	
	/**
	 * adds one column to the table
	 */
	protected void addColumn() {
		Log.d("addColumn()","addColumn()!!!");
		amountColumns++;
		View headerRow = headerTable.getChildAt(0);
		TableRow row = (TableRow) headerRow;
		//
		//  JACKSON START
		// guard. only add column if we are not lnflating with data
		if(!jacksonInflateWithData) {
			// add a new column. use standard type
			jacksonTable.addColumn();
		}
		//
		//  JACKSON END
		//
        addColumnToRow(row);
        //XXX: check if works
        ((View) row).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showDialog();
			}
		});
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
			//
			//  JACKSON START
			// guard. only remove column if we are not currently inflating with data
			if(!jacksonInflateWithData) {
				jacksonTable.removeColumn();
			}
			//
			//  JACKSON END
			//
		}
	}
	
	@Override
	public void onDetach(){
		super.onDetach();
		for(ReattachingPopup p: currentlyShownPopups){
			p.saveDismiss();
		}
	}

	//
	// JACKSON START
	//
	public void jacksonInflate(Table myTable, Activity activity) {
		// set table
		jacksonTable = myTable;
		// set flag, so that we are inflating the views with data from jackson model
		//jacksonInflateWithData = true;
	}
	
	/**
	 *  This is called when a checkbox changed its state.
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// get the associated entry
		IEditableContent entry = (IEditableContent) buttonView.getTag(R.id.jackson_row_tag_id);
		// set the new value
		entry.setContent(String.valueOf(isChecked));
	}

	/**
	 * Call this from createTableHeader. It checks if there any stored columns in the jackson model.
	 * If so we inflate the TableHeader with it. If not we set the two default columns.
	 * @param headerRow The TableRow for the header
	 */
	private void jacksonLoadTableHeader(final TableRow headerRow) {
		final int jacksonTableColumnNumber = jacksonTable.getNumberOfColumns();
		// check if we got any saved columns (they are only created if user goes into table!)
		// so if there are no saved columns or we didn't load any data we add the default columns 
		if(jacksonTableColumnNumber > 0) {
			amountColumns = 0;
			// set flag, so we don't add new columns to the jackson table while loading
			jacksonInflateWithData = true;
			// create the right amount of columns
			Log.d("jackson table inflating","jacksonNumberCol:"+jacksonTableColumnNumber);
			Log.d("jackson table inflating","amountCol:"+amountColumns);
			// TODO redo, we are starting with 0 columns every time
			while(amountColumns < jacksonTableColumnNumber) {
				Log.d("jackson table inflating","addColumn()");
				addColumn();
			}
			while(amountColumns > jacksonTableColumnNumber) {
				Log.d("jackson table inflating","removeColumn()");
				removeColumn();
			}
			// set titles
			Log.d("TableFragment-onCreateView", "headerRow:"+headerRow);
			Log.d("TableFragment-onCreateView", "Column#:"+amountColumns);
//			for(int i = 0; i < amountColumns; i++) {
//				View view = headerRow.getChildAt(i);
//				Log.d("TableFragment-onCreateView", "view:"+view);
//				Log.d("TABLE INFLATING", "setting column("+i+") header title:"+jacksonTable.getColumnHeader(i).name);
//				((EditText)view).setText(jacksonTable.getColumnHeader(i).name);
//				setHeaderTableStyle((EditText)view);
//				// check size
//				checkResize(0, 0, (EditText)view, headerRow);
//		        int width = getNeededWidth(i);
//				int height = getNeededHeight(0, headerRow);
//				final LayoutParams lparams = new LayoutParams(width, height);
//			    view.setLayoutParams(lparams);
//			}
			jacksonInflateWithData = false;
			Log.d("TABLE_FRAGMENT", "loaded table header data");
		}
		else {
			// add the 2 default columns
			//TODO do it with addColumn
			jacksonTable.addColumn(new ColumnHeader(getResources().getString(R.string.headline1),
					StringClass.TYPE_STRING));
			jacksonTable.addColumn(new ColumnHeader(getResources().getString(R.string.headline2),
					StringClass.TYPE_STRING));
			final TextView  col1 = new TextView(getActivity());
			((View) col1).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					showDialog();
				}
			});
			col1.setText(getResources().getString(R.string.headline1));
			col1.setGravity(Gravity.CENTER);
			setHeaderTableStyle(col1);
			col1.addTextChangedListener(new TextWatcher(){
				public void afterTextChanged(Editable s) {
					checkResize(0, 0, col1, headerRow);
					jacksonTable.setColumnTitle(0, s.toString());
				}
				public void beforeTextChanged(CharSequence s, int start, int count, int after){
				}
				public void onTextChanged(CharSequence s, int start, int before, int count){
				}
			});
			headerRow.addView(col1);
			// SECOND COLUMN
			final TextView col2 = new TextView(getActivity());
			((View) col2).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					showDialog();
				}
			});
			setHeaderTableStyle(col2);
			col2.setText(getResources().getString(R.string.headline2));
			col2.setGravity(Gravity.CENTER);
			col2.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable s) {
					checkResize(0, 0, col2, headerRow);
					jacksonTable.setColumnTitle(1, s.toString());
				}
				public void beforeTextChanged(CharSequence s, int start, int count, int after){
				}
				public void onTextChanged(CharSequence s, int start, int before, int count){
				}
			});
			headerRow.addView(col2);
	        amountColumns = 2;
			Log.d("TableFragment", "added default columns");
		}
	}
	//
	// JACKSON END
	//

	@Override
	public void showDialog() {
		Log.d("showDialog", "showDialog in TableFragment");
		TableEditDialog dialog = TableEditDialog.newInstance();
//		android.support.v4.app.FragmentManager fm = TemplateGeneratorActivity.theActiveActivity.getSupportFragmentManager();
		dialog.setTargetFragment(this, 0);
//		dialog.show(fm, "tableEditDialog");
//		
//		DialogFragment newFragment = TableEditDialog.newInstance();
//	    newFragment.show(getFragmentManager(), "dialog");
//	    
	    dialog.show(getFragmentManager(), "MyDialog");

//		adaptDialogTable(dialogTable);
//		dialogRowCounter.setText(Integer.toString(amountColumns));
//		dialog.show();
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
//		Log.d("width", "SCREEN.x == " + width);
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
		if(view instanceof TextView){
//			Log.d("setTableStyle", "now changed!");
			TextView text = (TextView) view;
			text.setTextColor(getResources().getColor(R.color.background));
			text.setSingleLine();
//			text.setGravity(Gravity.CENTER);
		}
	}
	
	@SuppressWarnings("deprecation")
	protected void setHeaderTableStyle(TextView text){
//		Log.d("setHeaderTableStyle", "setHeaderTableStyle!");
		text.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault);
		text.setPadding((int) getResources().getDimension(R.dimen.padding_textview_side),
				(int) getResources().getDimension(R.dimen.padding_below_small),
				(int) getResources().getDimension(R.dimen.padding_textview_side),
				(int) getResources().getDimension(R.dimen.padding_below_small));
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
	 * 
	 * @param row
	 * @return type of the row with index row
	 */
	protected content_type getType(int row){
		TableRow tableRow = (TableRow) table.getChildAt(0);
		View tableView = tableRow.getChildAt(row);
		if(tableView instanceof LinearLayout){
			View embeddedLayout = ((LinearLayout) tableView).getChildAt(0);
			if(embeddedLayout instanceof CheckBox){
				return content_type.checkbox;
			}
			else if(embeddedLayout instanceof TextView){
				return content_type.popup;
			}
		}
		return content_type.editText;
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
//			Log.d("dialog", "add");
			final TableRow row = new TableRow(SlideoutNavigationActivity.theActiveActivity);
			TableRow.LayoutParams rowParams = new TableRow.LayoutParams();
			rowParams.height = TableRow.LayoutParams.WRAP_CONTENT;
			rowParams.width = TableRow.LayoutParams.WRAP_CONTENT;
			//use k columns
			for(int k=0; k<3; k++){
				View theView = null;
				if(k==2 && i != -1){
					Spinner spin = new Spinner(SlideoutNavigationActivity.theActiveActivity);
					String [] spin_arry = getResources().getStringArray(R.array.spaltentypen);
					spin.setAdapter(new DialogSpinnerAdapter<CharSequence>(SlideoutNavigationActivity.theActiveActivity, spin_arry));
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
					final EditText oneColumn = new EditText(SlideoutNavigationActivity.theActiveActivity);
					setTableStyle((EditText) oneColumn);
					if (i == -1) {
						if (k == 0) {
							oneColumn.setText(getResources().getString(R.string.number));
						} else if (k == 1) {
							oneColumn.setText(getResources().getString(R.string.column_name));
						} else {
							oneColumn.setText(getResources().getString(R.string.column_type));
						}
					}
					// insert the number of the column in the first row
					else if (k == 0) {
						oneColumn.setText((i + 1) + getResources().getString(R.string.dot));
					} else if (k == 1) {
						TableRow headerRow = (TableRow) headerTable
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
