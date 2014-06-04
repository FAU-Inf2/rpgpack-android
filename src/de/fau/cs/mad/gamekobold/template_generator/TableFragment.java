package de.fau.cs.mad.gamekobold.template_generator;


import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.jackson.Table;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint("NewApi")
public class TableFragment extends GeneralFragment {
	/*
	 * JACKSON START
	 */
	Table myTable;
	/*
	 * JACKSON END
	 */
	
	View view;
	TableLayout table;
	TableLayout headerTable;
	int amountColumns = 2;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
	
	protected TableLayout createTableHeader(){
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
//		TableRow.LayoutParams col1Params = new TableRow.LayoutParams();
//		// Wrap-up the content of the row
//		col1Params.height = TableRow.LayoutParams.WRAP_CONTENT;
//		col1Params.width = TableRow.LayoutParams.WRAP_CONTENT;
		// Set the gravity to center the gravity of the column
//		col1Params.gravity = TableRow.Gravity.CENTER;
		final ResizingEditText col1 = new ResizingEditText(context, row, this);
//		TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//		col1.setLayoutParams(lp);
		col1.setMaxLines(1);
		col1.setText("Headline1");
		col1.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}
			public void onTextChanged(CharSequence s, int start, int before, int count){
				checkResize(0, 0, col1, row);
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
		row.addView(col1);

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
		col2.setMaxLines(1);
		col2.setText("Headline2");
		col2.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}
			public void onTextChanged(CharSequence s, int start, int before, int count){
				checkResize(0, 0, col2, row);
			}
		});
		if (android.os.Build.VERSION.SDK_INT >= 16){
			col2.setBackground(res);
		}
		else{
			col2.setBackgroundDrawable(res);
		}
		row.addView(col2);

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
		for (int i = 0; i < usedTable.getChildCount(); i++) {
			View child = usedTable.getChildAt(i);
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
		Log.d("width", "to adapt width  == " + width);
		View singleRow = headerTable.getChildAt(0);
		if(singleRow instanceof TableRow){
			View theChildToResize = ((TableRow) singleRow).getChildAt(column);
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
	
	protected void addColumnToRow(final TableRow row){
		final ResizingEditText oneColumn = new ResizingEditText(getActivity(), row, this);
		String uri = "@drawable/cell_shape";
		int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
		Drawable res = getResources().getDrawable(imageResource);
//		oneColumn.setPadding(34, 34, 3, 3);
//		TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//		oneColumn.setLayoutParams(lp);
		if (android.os.Build.VERSION.SDK_INT >= 16){
			oneColumn.setBackground(res);
		}
		else{
			oneColumn.setBackgroundDrawable(res);
		}
		oneColumn.setMaxLines(1);
//		oneColumn.setSingleLine();
//		oneColumn.setMinLines(1);
		View theText = oneColumn;
//		oneColumn.setMaxLines(2);
//		inputType="textMultiLine"
//		oneColumn.setInputType(te);Row.LayoutParams.WRAP_CONTENT);
//		oneColumn.setLayoutParams(lp);
//		oneColumn.setOnEditorActionListener(new EditText.OnEditorActionListener() {
		oneColumn.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
//				int indexOfRow = table.indexOfChild(row);
//				Log.d("index", "index  == " + indexOfRow);
//				int indexOfColumn = -1;
//				if(table.getChildAt(indexOfRow) instanceof TableRow){
//					indexOfColumn = ((TableRow) table.getChildAt(indexOfRow)).indexOfChild(oneColumn);
//				}
//				int longestRow = 0;
//				int ownColumnNumber = indexOfColumn;
//				View longestView = null;
//				for (int i = 0; i < table.getChildCount(); i++) {
//					View child = table.getChildAt(i);
//					TableRow oneRow = (TableRow) child;
//					if (child instanceof TableRow) {
//						View view = oneRow.getChildAt(ownColumnNumber);
//						int lengthRow = 0;
//						if(view instanceof EditText){
//							lengthRow = ((EditText) view).length();
//							if(lengthRow > longestRow){
//								longestRow = lengthRow;
//								longestView = view;
//							}
//						}
//						//no need to traverse over all columns, the column we just changed is enough to adapt
////						for (int x = 0; x < row.getChildCount(); x++) {
////							View view = row.getChildAt(x);
////							int lengthRow = 0;
////							if(view instanceof EditText){
////								lengthRow = ((EditText) view).length();
////								if(lengthRow > longestRow){
////									longestRow = lengthRow;
////									columnNumber = x;
////									longestView = view;
////								}
////							}
////						}
//					}
//				}
//				if(longestView != null){
//					//force layout is needed! view has old size if we don't do it!
//					//but problem: edittext doesnt resize anymore if we do it
////					longestView.forceLayout();
//					longestView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//					int width = longestView.getMeasuredWidth();
//					int height = longestView.getMeasuredHeight();
//					Log.d("width", "widht == " + width);
//					Log.d("text", "text == " + ((EditText) longestView).getText());
//					Log.d("textlength", "textLENGHT == " + ((EditText) longestView).getText().toString().length());
//					adaptAllColumnsToSize(ownColumnNumber, width, height);
//				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}
			public void onTextChanged(CharSequence s, int start, int before, int count){
				checkResize(0, 0, oneColumn, row);
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
			Log.d("columns", "amount == " + amountColumns);
		}
	}
	
}
