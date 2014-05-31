package de.fau.cs.mad.gamekobold.template_generator;


import de.fau.cs.mad.gamekobold.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class TableFragment extends GeneralFragment {

	View view;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		view = (LinearLayout) inflater.inflate(R.layout.template_generator_table_view, null);
		Button buttonAdd = (Button)view.findViewById(R.id.add);
		buttonAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				addItemList();
			}
		});

		return view;
	}

	protected void addItemList() {
		TableLayout ll = (TableLayout) view.findViewById(R.id.template_generator_table);
//		if(ll == null){
//			System.exit(-1);
//		}

//	    for (int i = 0; i <2; i++) {
	        TableRow row= new TableRow(getActivity());
	        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
	        row.setLayoutParams(lp);
	        EditText oneRow = new EditText(getActivity());
	        oneRow.setText("hello");
	        EditText sameRow = new EditText(getActivity());
	        sameRow.setText("world");
	        row.addView(oneRow);
	        row.addView(sameRow);
	        ll.addView(row);
//	    }
	}

	@Override
	protected void addItemList(int selected) {
		addItemList();
		
	}
	
}
