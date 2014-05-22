package de.fau.cs.mad.gamekobold.template_generator;

import de.fau.cs.mad.gamekobold.*;

import java.util.ArrayList;

import android.app.Fragment;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class TemplateGeneratorFragment extends Fragment {
	
	View view;
	ListView lView;
	protected ArrayList<DataHolder> allData;
	Button buttonAdd;
	View mainView;
	//TODO: add relations to other fragments -> e.g. parent to go back to
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		view = (LinearLayout) inflater.inflate(R.layout.activity_template_generator, null);
		mainView = view;
		
		 //set up view of line with add-button
//      EditText textIn = (EditText)getView().findViewById(R.id.textin);
//      textIn.setOnKeyListener(new View.OnKeyListener() {
//
//          public boolean onKey(View v, int keyCode, KeyEvent event) {
//              // TODO Auto-generated method stub
//
//              if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                  addItemList();
//              }
//              return true;
//          }
//      });
//      note: adapter for spinner item atm not instantiated (probably not needed at all)
      Button buttonAdd = (Button)view.findViewById(R.id.add);
      buttonAdd.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
          	addItemList();
          }
      });
		
		//setting up the list view with an item
        lView = (ListView) view.findViewById(R.id.listView_items);
        allData = new ArrayList<DataHolder>();
        DataHolder data1 = new DataHolder((MainTemplateGenerator)getActivity());
        allData.add(data1);
        DataAdapter dataAdapter = new DataAdapter((MainTemplateGenerator)getActivity(), R.layout.initialrow, allData.toArray(new DataHolder[allData.size()]));
        lView.setAdapter(dataAdapter);
        return view;
    }
	
	public DataHolder addItemList() {
		DataHolder newDataItem = new DataHolder((MainTemplateGenerator)getActivity());
		allData.add(newDataItem);
		DataAdapter dataAdapter = new DataAdapter((MainTemplateGenerator)getActivity(), R.layout.initialrow, allData.toArray(new DataHolder[allData.size()]));
		ListView listView = (ListView) mainView.findViewById(R.id.listView_items);
		listView.setAdapter(dataAdapter);
		Log.d("addItemList","ADD ITEM. now amount == " + allData.size());
		return newDataItem;
	}
	
	public DataHolder addItemList(int selected) {
		DataHolder newDataItem = new DataHolder((MainTemplateGenerator)getActivity());
		Toast.makeText(getActivity(), "selected: " + selected ,Toast.LENGTH_LONG).show();
		newDataItem.setSelected(selected);
		allData.add(newDataItem);
		ListView listView = (ListView) mainView.findViewById(R.id.listView_items);
		DataAdapter dataAdapter = new DataAdapter((MainTemplateGenerator)getActivity(), R.layout.initialrow, allData.toArray(new DataHolder[allData.size()]));
		listView.setAdapter(dataAdapter);
		Log.d("addItemList","ADD ITEM. now amount == " + allData.size());
		return newDataItem;
	}
}
