package de.fau.cs.mad.gamekobold.template_generator;

import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;

import java.io.IOException;
import java.util.ArrayList;




import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;


//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
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

public class FolderFragment extends GeneralFragment {
	/*
	 * JACKSON START
	 */
	// could be done nicer if adapter could be created in constructor.
	public ContainerTable myTable;
	/*
	 * JACKSON END
	 */
	
	View view;
	ListView lView;
	protected ArrayList<DataHolder> allData;
	
	Button buttonAdd;
	View mainView;
//	FolderFragment fragment_parent = null;
//	ArrayList<TemplateGeneratorFragment> children = new ArrayList<>();
	DataAdapter dataAdapter;
	//TODO: add relations to other fragments -> e.g. parent to go back to
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        allData = new ArrayList<DataHolder>();
//      DataHolder data1 = new DataHolder((MainTemplateGenerator)getActivity());
//      allData.add(data1);
//      DataHolder data2 = new DataHolder((MainTemplateGenerator)getActivity());
//      allData.add(data2);
        dataAdapter = new DataAdapter((MainTemplateGenerator)getActivity(), R.layout.initialrow, allData);
        dataAdapter.myTable = myTable;
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
//		if(mainView == null){
			view = (LinearLayout) inflater.inflate(R.layout.activity_template_generator, null);
			mainView = view;
//		}
//		else{
//			view = mainView;
//		}
//		view = (LinearLayout) inflater.inflate(R.layout.activity_template_generator, null);
//		mainView = view;
		
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
        
        lView.setAdapter(dataAdapter);
        return view;
    }
	
	public void addItemList() {
		DataHolder newDataItem = new DataHolder((MainTemplateGenerator)getActivity());
		allData.add(newDataItem);
//		allData.add(0, newDataItem);
//		dataAdapter.add(newDataItem);
		dataAdapter.notifyDataSetChanged();
//		dataAdapter.add(newDataItem);
//		dataAdapter = new DataAdapter((MainTemplateGenerator)getActivity(), R.layout.initialrow, allData.toArray(new DataHolder[allData.size()]));
//		ListView listView = (ListView) mainView.findViewById(R.id.listView_items);
//		listView.setAdapter(dataAdapter);
		Log.d("addItemList","ADD ITEM. now amount == " + allData.size());
	}
	
	public void addItemList(int selected) {
		DataHolder newDataItem = new DataHolder((MainTemplateGenerator)getActivity());
		Toast.makeText(getActivity(), "selected: " + selected ,Toast.LENGTH_LONG).show();
		newDataItem.setSelected(selected);
		
		/*
		 * JACKSON START
		 */
		if(newDataItem.getSelectedText().equals("Ordner")) {
			newDataItem.myTable = dataAdapter.myTable.createAndAddNewContainerTable();
			try {
				MainTemplateGenerator.myTemplate.saveToJSON(getActivity(), "testTemplate.json");
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/*
		 * JACKSON END
		 */
		
		allData.add(newDataItem);
		dataAdapter.notifyDataSetChanged();

//		dataAdapter.add(newDataItem);
//		ListView listView = (ListView) mainView.findViewById(R.id.listView_items);
//		dataAdapter = new DataAdapter((MainTemplateGenerator)getActivity(), R.layout.initialrow, allData.toArray(new DataHolder[allData.size()]));
//		listView.setAdapter(dataAdapter);
		Log.d("addItemList","ADD ITEM. now amount == " + allData.size());
	}
	
	public void setJacksonTable(ContainerTable table) {
		if(dataAdapter != null) {
			dataAdapter.myTable = table;
		}
		myTable = table;
	}
}
