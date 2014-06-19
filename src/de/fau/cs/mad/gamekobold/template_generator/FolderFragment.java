package de.fau.cs.mad.gamekobold.template_generator;

import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.jackson.AbstractTable;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.Table;
import de.fau.cs.mad.gamekobold.template_generator.DataHolder.element_type;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
	DataAdapter dataAdapter;
	AlertDialog dialogCreateElement;
	View dialogViewCreateElement;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // nullcheck needed for jackson inflation. creates allData before onCreate is called
        if(allData == null) {
        	allData = new ArrayList<DataHolder>();
        }
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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainTemplateGenerator.theActiveActivity);
        LayoutInflater inflater = MainTemplateGenerator.theActiveActivity.getLayoutInflater();
        dialogViewCreateElement = inflater.inflate(R.layout.alertdialog_template_generator_add_new_element, null);
        alertDialogBuilder.setView(dialogViewCreateElement);
        alertDialogBuilder.setCancelable(true);
        ImageButton createTable = (ImageButton) dialogViewCreateElement.findViewById(R.id.create_table);
        ImageButton createCollection= (ImageButton) dialogViewCreateElement.findViewById(R.id.create_collection);
        ImageButton createFolder = (ImageButton) dialogViewCreateElement.findViewById(R.id.create_folder);
        createTable.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addItemList(element_type.table);
				dialogCreateElement.cancel();
			}
		});
        createCollection.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addItemList(element_type.matrix);
				dialogCreateElement.cancel();
			}
		});
        createFolder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addItemList(element_type.folder);
				dialogCreateElement.cancel();
			}
		});
        dialogCreateElement = alertDialogBuilder.create();
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
      
      TextView addRowBelow = (TextView)view.findViewById(R.id.add_below);
		addRowBelow.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				addItemList();
				dialogCreateElement.show();
			}
		});
		setAddButtonStyle(addRowBelow);
		
		//setting up the list view with an item
        lView = (ListView) view.findViewById(R.id.listView_items);
        lView.setAdapter(dataAdapter);
        
        return view;
    }
	
	@Override
	public void showDialog() {
		dialogCreateElement.show();
	}
	
	@SuppressLint("NewApi")
	protected void setAddButtonStyle(TextView text){
		TableRow.LayoutParams textViewParams = new TableRow.LayoutParams();
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		String uri = "@drawable/cell_shape_add_folder";
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
	
	public void addItemList() {
		addItemList(element_type.folder);
//		DataHolder newDataItem = new DataHolder((MainTemplateGenerator)getActivity());
//		/*
//		 * JACKSON START
//		 */
//		newDataItem.jacksonDoSaveOnNextChance = true;
//		/*
//		 * JACKSON END
//		 */
//		allData.add(newDataItem);
////		allData.add(0, newDataItem);
////		dataAdapter.add(newDataItem);
//		dataAdapter.notifyDataSetChanged();
//		final ListView myListView = (ListView) view.findViewById(R.id.listView_items);
//		myListView.post(new Runnable() {
//	        @Override
//	        public void run() {
//	            // Select the last row so it will scroll into view...
//	            myListView.setSelection(dataAdapter.getCount() - 1);
//	        }
//	    });
////		dataAdapter.add(newDataItem);
////		dataAdapter = new DataAdapter((MainTemplateGenerator)getActivity(), R.layout.initialrow, allData.toArray(new DataHolder[allData.size()]));
////		ListView listView = (ListView) mainView.findViewById(R.id.listView_items);
////		listView.setAdapter(dataAdapter);
//		Log.d("addItemList","ADD ITEM. now amount == " + allData.size());
	}
	
	public void addItemList(element_type selected) {
		DataHolder newDataItem = new DataHolder((MainTemplateGenerator)getActivity(), selected);
		Toast.makeText(getActivity(), "selected: " + selected ,Toast.LENGTH_LONG).show();
//		newDataItem.setSelected(selected);
		
		/*
		 * JACKSON START
		 */
		// not needed because we save right here
		// we do it here because if the new created item is not visible it would not
		// be saved
		// newDataItem.jacksonDoSaveOnNextChance = true;
		if(newDataItem.getSelectedText().equals("Ordner")) {
			newDataItem.jacksonTable = dataAdapter.jacksonTable.createAndAddNewContainerTable();
			MainTemplateGenerator.saveTemplateAsync();
		}
		/*
		 * JACKSON END
		 */
		
		allData.add(newDataItem);
		dataAdapter.notifyDataSetChanged();
		final ListView myListView = (ListView) view.findViewById(R.id.listView_items);
		myListView.post(new Runnable() {
	        @Override
	        public void run() {
	            // Select the last row so it will scroll into view...
	            myListView.setSelection(dataAdapter.getCount() - 1);
	        }
	    });

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
		if(dataAdapter == null) {
			dataAdapter = new DataAdapter(activity, R.layout.initialrow, allData);
		}
		for(final AbstractTable subTable : jacksonTable.subTables) {
			// check table type
			// get type id from choice array
			String[] choices = activity.getResources().getStringArray(R.array.choices);
			int selected = 0;
			// create new data holder
			//XXX: @Benni: ich (Julian) habe das 2. Argument fuer new DataHolder hinzugefuegt
			//wusste nicht welcher da jetzt richtig waere...
			DataHolder newDataItem = new DataHolder(activity, element_type.folder);
			if(subTable.tableName == null) {
				newDataItem.text.setText("");	
			}
			else {
				newDataItem.text.setText(subTable.tableName);
			}
			if(subTable instanceof ContainerTable) {
				for(final String string : choices) {
					if(string.equals("Ordner")) {
						break;
					}
					selected++;
				}
				// container table				
				newDataItem.selected = selected;
				// add data holder to adapter
				allData.add(newDataItem);
				dataAdapter.notifyDataSetChanged();
				// set data holder jackson Table
				newDataItem.jacksonTable = subTable;
				// create fragment
				newDataItem.childFragment = new FolderFragment();
				newDataItem.childFragment.elementName = subTable.tableName;
				newDataItem.childFragment.fragment_parent = this;
				newDataItem.childFragment.jacksonTable = (ContainerTable)subTable;
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
				newDataItem.selected = selected;
				// add data holder to adapter
				allData.add(newDataItem);
				dataAdapter.notifyDataSetChanged();
				// set data holder jackson Table
				newDataItem.jacksonTable = subTable;
				// create fragment
				newDataItem.table = new TableFragment();
				newDataItem.table.elementName = subTable.tableName;
				newDataItem.table.fragment_parent = this;
				newDataItem.table.jacksonInflate((Table)subTable, activity);
				// not working this way
				/*FragmentManager fragmentManager = activity.getFragmentManager();
				FragmentTransaction transaction = fragmentManager.beginTransaction();
				// add and commit fragment
				transaction.add(R.id.main_view_empty, newDataItem.table).commit();*/
			}
		}
	}
	/*
	 * JACKSON END
	 */
}
