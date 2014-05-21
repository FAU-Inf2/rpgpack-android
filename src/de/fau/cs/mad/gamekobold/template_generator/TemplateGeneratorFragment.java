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
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		view = (LinearLayout) inflater.inflate(R.layout.activity_template_generator, null);
		mainView = view;
		 //setUpView
//      EditText textIn = (EditText)getView().findViewById(R.id.textin);
      Button buttonAdd = (Button)view.findViewById(R.id.add);
      ListView lvItem = (ListView)view.findViewById(R.id.listView_items);
      if(lvItem == null){
      	System.exit(0);
      }
//      ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>();
//      lvItem.setAdapter(itemAdapter);
      buttonAdd.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
          	addItemList();
          }
      });
		
		
//        RelativeLayout pasteLayout=(RelativeLayout)view.findViewById(R.id.template_generated_element);
//        pasteLayout.setVisibility(RelativeLayout.GONE);
//        Context context = getActivity();
        lView = (ListView) view.findViewById(R.id.listView_items);
        allData = new ArrayList<DataHolder>();
//        DataHolder data1 = new DataHolder((MainActivity)getActivity());
//        allData.add(data1);theData
//        DataHolder data2 = new DataHolder((MainActivity)getActivity());
//        allData.add(data2);
        DataAdapter dataAdapter = new DataAdapter(getActivity(), R.layout.initialrow, allData.toArray(new DataHolder[allData.size()]));
        
        lView.setAdapter(dataAdapter);
        return view;
    }
	
	public DataHolder addItemList() {
		DataHolder data4 = new DataHolder((MainTemplateGenerator)getActivity());
		allData.add(data4);
		//  	dataAdapter.notifyDataSetChanged();
		ListView listView = (ListView) mainView.findViewById(R.id.listView_items);
		DataAdapter dataAdapter = new DataAdapter((MainTemplateGenerator)getActivity(), R.layout.initialrow, allData.toArray(new DataHolder[allData.size()]));
		listView.setAdapter(dataAdapter);
		Log.d("PIEP","ADD ITEM. now amount == " + allData.size());
		return data4;
	}
	
	public DataHolder addItemList(int selected) {
		DataHolder data4 = new DataHolder((MainTemplateGenerator)getActivity());
		Toast.makeText(getActivity(), "selected: " + selected ,Toast.LENGTH_LONG).show();
		data4.setSelected(selected);
		allData.add(data4);
		//	  	dataAdapter.notifyDataSetChanged();
		ListView listView = (ListView) mainView.findViewById(R.id.listView_items);
		DataAdapter dataAdapter = new DataAdapter((MainTemplateGenerator)getActivity(), R.layout.initialrow, allData.toArray(new DataHolder[allData.size()]));
		listView.setAdapter(dataAdapter);
		Log.d("PIEP","ADD ITEM. now amount == " + allData.size());
		return data4;
	}
}
