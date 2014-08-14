package de.fau.cs.mad.gamekobold.template_generator;

import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.character.CharacterEditActivity;
import de.fau.cs.mad.gamekobold.jackson.AbstractTable;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.MatrixTable;
import de.fau.cs.mad.gamekobold.jackson.Table;
import de.fau.cs.mad.gamekobold.matrix.MatrixFragment;
import de.fau.cs.mad.gamekobold.template_generator.FolderElementData.element_type;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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
	public ListView lView;
	public ArrayList<FolderElementData> allData;
	
	Button buttonAdd;
	View mainView;
	public FolderElementAdapter dataAdapter;
	AlertDialog dialogCreateElement;
	View dialogViewCreateElement;
    boolean editable = true;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editable = (getActivity() instanceof CharacterEditActivity? false:true);
		Log.d("FolderFragment","onCreate; editable set to: " + editable);
        // nullcheck needed for jackson inflation. creates allData before onCreate is called
        if(allData == null) {
        	allData = new ArrayList<FolderElementData>();
        }
        // nullcheck needed for jackson inflation. creates dataAdapter before onCreate is called
        if(dataAdapter == null) {
        	dataAdapter = new FolderElementAdapter((SlideoutNavigationActivity)getActivity(), editable, R.layout.template_listview_row, allData);
            /*
             * JACKSON START
             */
            dataAdapter.jacksonTable = jacksonTable;
            /*
             * JACKSON END
             */
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SlideoutNavigationActivity.theActiveActivity);
        LayoutInflater inflater = SlideoutNavigationActivity.theActiveActivity.getLayoutInflater();
        dialogViewCreateElement = inflater.inflate(R.layout.alertdialog_template_generator_add_new_element, null);
        final EditText nameInput = (EditText) dialogViewCreateElement.findViewById(R.id.enter_name_of_element);
        nameInput.setSingleLine();
        alertDialogBuilder.setView(dialogViewCreateElement);
        alertDialogBuilder.setCancelable(true);
        ImageButton createTable = (ImageButton) dialogViewCreateElement.findViewById(R.id.create_table);
        ImageButton createCollection= (ImageButton) dialogViewCreateElement.findViewById(R.id.create_collection);
        ImageButton createFolder = (ImageButton) dialogViewCreateElement.findViewById(R.id.create_folder);
        createTable.setOnClickListener(new View.OnClickListener() {
       	//	private final EditText elementName = (EditText)view.findViewById(R.id.enter_name_of_element);
			@Override
			public void onClick(View v) {
				addItemList(editable, element_type.table, nameInput.getText().toString());
				dialogCreateElement.cancel();
				nameInput.setText("");
			}
		});
        createCollection.setOnClickListener(new View.OnClickListener() {
        	//private final EditText elementName = (EditText)view.findViewById(R.id.enter_name_of_element);
			@Override
			public void onClick(View v) {
				addItemList(editable, element_type.matrix, nameInput.getText().toString());
				dialogCreateElement.cancel();
				nameInput.setText("");
			}
		});
        createFolder.setOnClickListener(new View.OnClickListener() {
        	//private final EditText elementName = (EditText)view.findViewById(R.id.enter_name_of_element);
			@Override
			public void onClick(View v) {
				addItemList(editable, element_type.folder, nameInput.getText().toString());
				dialogCreateElement.cancel();
				nameInput.setText("");
			}
		});
        dialogCreateElement = alertDialogBuilder.create();
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		super.onCreateView(inflater, container, savedInstanceState);
		if(SlideoutNavigationActivity.theActiveActivity instanceof TemplateGeneratorActivity){
			view = (LinearLayout) inflater.inflate(R.layout.activity_template_generator_add_button, null);
			TextView addRowBelow = (TextView)view.findViewById(R.id.add_below);
			addRowBelow.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					dialogCreateElement.show();
				}
			});
			setAddButtonStyle(addRowBelow);
		}
		else{
			view = (LinearLayout) inflater.inflate(R.layout.activity_template_generator, null);
		}
		mainView = view;
        lView = (ListView) view.findViewById(R.id.listView_items);
        lView.setAdapter(dataAdapter);
        return view;
    }
	
	@Override
	public void showDialog() {
		dialogCreateElement.show();
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	protected void setAddButtonStyle(TextView text){
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
		addItemList(true, element_type.folder, "");
	}
	
	public void addItemList(boolean editable) {
		addItemList(editable, element_type.folder, "");
	}
	
	public void addItemList(boolean editable, element_type selected, String name) {
		FolderElementData newDataItem = new FolderElementData(
				(SlideoutNavigationActivity)getActivity(), editable, selected);
		newDataItem.text.setText(name);
		Toast.makeText(getActivity(), "selected: " + selected ,Toast.LENGTH_LONG).show();
		/*
		 * JACKSON START
		 */
		// not needed because we save right here
		// we do it here because if the new created item is not visible it would not
		// be saved
		// newDataItem.jacksonDoSaveOnNextChance = true;
		if(newDataItem.getType() == element_type.folder){
			newDataItem.jacksonTable = dataAdapter.jacksonTable.createAndAddNewContainerTable();
		}
		else if(newDataItem.getType() == element_type.table) {
			newDataItem.jacksonTable = dataAdapter.jacksonTable.createAndAddNewTable();
		}
		else if(newDataItem.getType() == element_type.matrix) {
			newDataItem.jacksonTable = dataAdapter.jacksonTable.createAndAddNewMatrixTable();
		}
		newDataItem.jacksonTable.tableName = name;
		//TemplateGeneratorActivity.saveTemplateAsync();
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
		//default: not given if editable; take value from class variable
		inflateWithJacksonData(myTable, activity, editable);
	}
	
	public void inflateWithJacksonData(ContainerTable myTable, Activity activity, boolean editable) {
		Log.d("FolderFragment","inflating jackson data; editable: " + editable);
		// set jackson table for this fragment
		jacksonTable = myTable;
		// check if there are any sub tables in this folder
		if(myTable.subTables == null) {
			return;
		}
		// initialize fields for this FolderFragment, so we can add data
		if(allData == null) {
			allData = new ArrayList<FolderElementData>();
		}
		if(dataAdapter == null) {
			dataAdapter = new FolderElementAdapter(activity, editable, R.layout.template_listview_row, allData);
			dataAdapter.jacksonTable = jacksonTable;
		}
		// add data
		for(final AbstractTable subTable : jacksonTable.subTables) {
			FolderElementData newDataItem;
			// switch type if sub table
			if(subTable instanceof ContainerTable) {
				// create new folder element
				newDataItem = new FolderElementData(activity, editable, element_type.folder);
				// set its name
				if(subTable.tableName == null) {
					newDataItem.text.setText("");	
				}
				else {
					newDataItem.text.setText(subTable.tableName);
				}
				// add data holder to adapter
				allData.add(newDataItem);
				// set data holder jackson Table
				newDataItem.jacksonTable = subTable;
				// create fragment
				newDataItem.childFragment = new FolderFragment();
				newDataItem.childFragment.elementName = subTable.tableName;
				newDataItem.childFragment.fragment_parent = this;
				//((FolderFragment) newDataItem.childFragment).jacksonTable = (ContainerTable)subTable;
				// call recursive
				//note: editable must be given because the onCreate was not yet called
				//and therefore variable "editable" not yet initialized
				((FolderFragment) newDataItem.childFragment).inflateWithJacksonData((ContainerTable)subTable, activity, editable);
			}
			else if(subTable instanceof Table) {
				// create new folder element
				newDataItem = new FolderElementData(activity, editable, element_type.table);
				// set its name
				if(subTable.tableName == null) {
					newDataItem.text.setText("");	
				}
				else {
					newDataItem.text.setText(subTable.tableName);
				}
				// add data holder to adapter
				allData.add(newDataItem);
				// set data holder jackson Table
				newDataItem.jacksonTable = subTable;
				// create fragment
				newDataItem.childFragment = new TableFragment();
				newDataItem.childFragment.elementName = subTable.tableName;
				newDataItem.childFragment.fragment_parent = this;
				((TableFragment) newDataItem.childFragment).jacksonInflate((Table)subTable, activity);
			}
			else if(subTable instanceof MatrixTable) {
				// create new folder element
				newDataItem = new FolderElementData(activity, editable, element_type.matrix);
				// set its name
				if(subTable.tableName == null) {
					newDataItem.text.setText("");	
				}
				else {
					newDataItem.text.setText(subTable.tableName);
				}
				// add data holder to adapter
				allData.add(newDataItem);
				// set data holder jackson Table
				newDataItem.jacksonTable = subTable;
				// create fragment
				// TODO add matrix inflation
				newDataItem.childFragment = new MatrixFragment();
				newDataItem.childFragment.elementName = subTable.tableName;
				newDataItem.childFragment.fragment_parent = this;
				((MatrixFragment) newDataItem.childFragment).jacksonInflate((MatrixTable)subTable, activity);
			}
			dataAdapter.notifyDataSetChanged();
		}
	}
	/*
	 * JACKSON END
	 */
}
