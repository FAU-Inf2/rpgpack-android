package de.fau.cs.mad.gamekobold.template_generator;


import de.fau.cs.mad.gamekobold.R;
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
		final EditText oneColumn = new EditText(getActivity());
//		oneColumn.setPadding(0, 55, 0, 0);
		String uri = "@drawable/cell_shape";
		int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());
		Drawable res = getResources().getDrawable(imageResource);
//		oneColumn.setPadding(34, 34, 3, 3);
//		oneColumn.setLayoutParams(params);
//		TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//		oneColumn.setLayoutParams(lp);
		if (android.os.Build.VERSION.SDK_INT >= 16){
			oneColumn.setBackground(res);
		}
		else{
			oneColumn.setBackgroundDrawable(res);
		}
//		oneColumn.setMaxLines(1);
//		oneColumn.setSingleLine();
		oneColumn.setMinLines(1);
//		oneColumn.setMaxLines(2);
//		inputType="textMultiLine"
//		oneColumn.setInputType(te);
//		oneColumn.setOnEditorActionListener(new EditText.OnEditorActionListener() {
		oneColumn.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){

//				Log.d("class", "class of Editable: " + s.getClass());
//				s.getClass()
				int index = table.indexOfChild(oneColumn);
				Log.d("index", "index  == " + index);
				int longestRow = 0;
				int columnNumber = 0;
				View longestView = null;
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
									longestView = view;
								}
							}
						}
					}
				}
				if(longestView != null){
					longestView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					int width = longestView.getMeasuredWidth();
					int height = longestView.getMeasuredHeight();
					Log.d("width", "widht == " + width);
					View singleRow = headerTable.getChildAt(0);
					if(singleRow instanceof TableRow){
						View textField = ((TableRow) singleRow).getChildAt(columnNumber);
						if(textField instanceof EditText){
							((EditText) textField).setWidth(width);
						}
					}
				}
				
//				View oneRow = headerTable.getChildAt(0);
//				View element = null;
//				if(oneRow instanceof TableRow){
//					element = ((TableRow) oneRow).getChildAt(columnNumber);
//					if(!(element instanceof EditText)){
//						Log.d("critical", "exception in TableFragment!");
//						System.exit(-1);
//					}
//				}
//				else{
//					Log.d("critical", "exception in TableFragment!");
//					System.exit(-1);
//				}
//				String resultingString = "";
//				for(int i=0; i<longestRow; ++i){
//					resultingString = resultingString + "-";
//				}
//				((EditText) element).setText(resultingString);
			}
			public void onTextChanged(CharSequence s, int start, int before, int count){
				
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
