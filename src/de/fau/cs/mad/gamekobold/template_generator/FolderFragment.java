package de.fau.cs.mad.gamekobold.template_generator;

import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.jackson.AbstractTable;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.Table;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.Spinner;
import android.widget.Toast;

public class FolderFragment extends GeneralFragment {
	/*
	 * JACKSON START
	 */
	public ContainerTable jacksonTable;
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
        // nullcheck needed for jackson inflation. creates allData before onCreate is called
        if(allData == null) {
        	allData = new ArrayList<DataHolder>();
        }
//      DataHolder data1 = new DataHolder((MainTemplateGenerator)getActivity());
//      allData.add(data1);
//      DataHolder data2 = new DataHolder((MainTemplateGenerator)getActivity());
//      allData.add(data2);
        // nullcheck needed for jackson inflation. creates dataAdapter before onCreate is called
        if(dataAdapter == null) {
        	dataAdapter = new DataAdapter((MainTemplateGenerator)getActivity(), R.layout.initialrow, allData);
        }
        /*
         * JACKSON START
         */
        dataAdapter.jacksonTable = jacksonTable;
        /*
         * JACKSON END
         */
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
			newDataItem.jacksonTable = dataAdapter.jacksonTable.createAndAddNewContainerTable();
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
	
	/*
	 * JACKSON START
	 */
	public void setJacksonTable(ContainerTable table) {
		if(dataAdapter != null) {
			dataAdapter.jacksonTable = table;
		}
		jacksonTable = table;
	}
	
	public void inflateWithJacksonData(ContainerTable myTable, Activity activity) {
		// set jackson table for this fragment
		jacksonTable = myTable;
		if(myTable.subTables == null) {
			return;
		}
		if(allData == null) {
			allData = new ArrayList<DataHolder>();
		}
		//Log.d("FOLDER_FRAGMENT", "isAdded:"+isAdded());
		//activity = getActivity();
		if(dataAdapter == null) {
			dataAdapter = new DataAdapter(activity, R.layout.initialrow, allData);
		}
		for(final AbstractTable subTable : jacksonTable.subTables) {
			// check table type
			// get type id from choice array
			String[] choices = activity.getResources().getStringArray(R.array.choices);
			int selected = 0;
			if(subTable instanceof ContainerTable) {
				for(final String string : choices) {
					if(string.equals("Ordner")) {
						break;
					}
					selected++;
				}
				// container table
				// create and add new data holder to adapter;
				DataHolder newDataItem = new DataHolder(activity);
				newDataItem.selected = selected;
				allData.add(newDataItem);
				dataAdapter.notifyDataSetChanged(); 
				// set data holder jackson Table
				newDataItem.jacksonTable = subTable;
				// create fragment
				newDataItem.childFragment = new FolderFragment();
				newDataItem.childFragment.fragment_parent = this;
				// not working this way
				/*FragmentManager fragmentManager = activity.getFragmentManager();
				FragmentTransaction transaction = fragmentManager.beginTransaction();
				// add and commit fragment
				transaction.add(R.id.main_view_empty, newDataItem.childFragment).commit();*/
				// call recursive
				newDataItem.childFragment.inflateWithJacksonData((ContainerTable)subTable, activity);
			}
			else if(subTable instanceof Table) {
				for(final String string : choices) {
					if(string.equals("Tabelle")) {
						break;
					}
					selected++;
				}
				// table
				// create and add new data holder to adapter;
				DataHolder newDataItem = new DataHolder(activity);
				newDataItem.selected = selected;
				allData.add(newDataItem);
				dataAdapter.notifyDataSetChanged();
				// set data holder jackson Table
				newDataItem.jacksonTable = subTable;
				// create fragment
				newDataItem.table = new TableFragment();
				newDataItem.table.fragment_parent = this;
				// not working this way
				/*FragmentManager fragmentManager = activity.getFragmentManager();
				FragmentTransaction transaction = fragmentManager.beginTransaction();
				// add and commit fragment
				transaction.add(R.id.main_view_empty, newDataItem.table).commit();*/
				// TODO set right column stuff
			}
		}
	}
	/*
	 * JACKSON END
	 */
}
