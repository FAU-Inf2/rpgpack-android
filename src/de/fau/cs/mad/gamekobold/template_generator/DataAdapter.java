package de.fau.cs.mad.gamekobold.template_generator;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.Table;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class DataAdapter extends ArrayAdapter<DataHolder> {
	/*
	 * JACKSON START
	 */
	// DataAdapter only used in FolderFragments. one per fragment, so use DataAdapter for table info
	public ContainerTable jacksonTable;
	/*
	 * JACKSON END
	 */
	
	
//	private Activity MainTemplateGenerator.theActiveActivity;
	ArrayList<DataHolder> allData;
	

	public DataAdapter(Activity context, int textViewResourceId, ArrayList<DataHolder> objects) {
		super(context, textViewResourceId, objects);
//		MainTemplateGenerator.theActiveActivity = context;
        allData = new ArrayList<DataHolder>();
        allData = objects;
    }

    // We keep this ViewHolder object to save time. It's quicker than findViewById() when repainting.
	static class ViewHolder {
		EditText text;
		EditText invisibleText;
		protected Spinner spin;
		protected View row;
	}
	
	@Override
	public int getCount() {
	    return allData.size();
	}

	@Override
	public DataHolder getItem(int position) {
	    return allData.get(position);
	}

	@Override
	public long getItemId(int position) {
	    return allData.get(position).ID;
	}
	
	@Override
	public boolean hasStableIds(){
		return true;
	}

    @Override
    public View getView(int viewPosition, View convertView, ViewGroup parent) {
    	final DataHolder data = this.getItem(viewPosition);
        View view = null;
        // Check to see if this row has already been painted once.
        if (convertView == null) {
            Log.d("set tag", "convertview is NULL, " + this.getItemId(viewPosition));

            // If it hasn't, set up everything:
            LayoutInflater inflator = MainTemplateGenerator.theActiveActivity.getLayoutInflater();
            view = inflator.inflate(R.layout.initialrow, null);

            final ViewHolder holder = new ViewHolder();
            holder.text = (EditText) view.findViewById(R.id.text);
            holder.invisibleText = (EditText) view.findViewById(R.id.text2);
            holder.spin = (Spinner) view.findViewById(R.id.spin);
            holder.row = view;

            view.setTag(holder);
        } else {
            view = convertView;
        }
        //setting of onItemSelectedListener or other adapters needs to be done here! (because recycling of views in android)
        final ViewHolder holder = (ViewHolder) view.getTag();
        ((Spinner) view.findViewById(R.id.spin)).setAdapter(data.getAdapter());
        ((Spinner) view.findViewById(R.id.spin)).setOnItemSelectedListener(new OnItemSelectedListener() {
        	@Override
            public void onItemSelected(AdapterView<?> parent, View view, int itemPosition, long id) {
        		data.setSelected(itemPosition);
                if(data.getSelectedText().equals("Ordner")) {
                	// check if data.text is not empty, so we can use this name
                	if(data.text.getEditableText().toString().isEmpty()) {
                		data.text.setText("UFO");
                		data.jacksonTable.tableName = "UFO";
                	}
                	holder.text.setText(data.text.getText());
                	
                	/*
                	 * JACKSON START
                	 */
                	// we changed type to folder, so check if childFragment exists
                	if(data.childFragment == null) {
                		// if it does not exist we check if we already have created a container table
                		// if not we create a new one
                		// when the user clicks on the row we attach the table to the fragment
                		// TODO anstatt instanceof (langsam?!) typ info in abstract table
                		if(!(data.jacksonTable instanceof ContainerTable)) {
                			jacksonTable.removeTable(data.jacksonTable);
                			data.jacksonTable = jacksonTable.createAndAddNewContainerTable();
                			Log.d("JSON_DATA_ADAPTER", "created new container table");
                			data.jacksonDoSaveOnNextChance = true;
                		}
                		else  {
                			// remove here because we are going to add it again
                			holder.text.removeTextChangedListener(data.jacksonTable.tableNameTextWatcher);
                		}
                	}
                	else {
                		// if exists
						// childFragment was not null, so folder Fragment already exists -> jackson table exists
						// replace current table with the table stored in fragment
                		if(data.jacksonTable != data.childFragment.jacksonTable) {
                			holder.text.removeTextChangedListener(data.jacksonTable.tableNameTextWatcher);
                			jacksonTable.removeTable(data.jacksonTable);
                			jacksonTable.addTable(data.childFragment.jacksonTable);
                			data.jacksonTable = data.childFragment.jacksonTable;
                			Log.d("JSON_DATA_ADAPTER", "replaced container table");
                			data.jacksonDoSaveOnNextChance = true;
                		}
                	}
                	// TODO noch mal schauen ob man das net wo anders hinpacken kann
        			holder.text.addTextChangedListener(data.jacksonTable.tableNameTextWatcher);
           		 
                	// onItemSelected gets called when setting data while loading a template. use this flag to eliminate
                	// saving bug ( template is saved when data gets added)
                	if(data.jacksonDoSaveOnNextChance) {
                		data.jacksonDoSaveOnNextChance = false;
                		// save template
                		Log.d("JSON_DATA_ADAPTER", "Container Table -> save template");
                		MainTemplateGenerator.saveTemplateAsync();                		
                	}
                	/*
                	 * JACKSON END
                	 */
                	
                	holder.row.setOnClickListener(new OnClickListener() {
                		//hier bei Klick neues Fragment anzeigen (== Unterordner)
						@Override
						public void onClick(View v) {
					        Log.d("data", "view == " + v.getParent());
							//new subfolder -> create new fragment for it
							if(data.childFragment == null) {
								FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).getFragmentManager().beginTransaction();
								FolderFragment newFragment = new FolderFragment();
								
								/*
								 * JACKSON START
								 */
								// childFragment was null, so we need to create a new table (childFragment == FolderFragment)
								// data.jacksonTable should be a Container Table, created when type changed
								newFragment.setJacksonTable((ContainerTable)data.jacksonTable);
								/*
								 * JACKSON END
								 */
//								fragmentTransaction.repl
								fragmentTransaction.add(R.id.main_view_empty, newFragment);
//								((MainTemplateGenerator) getContext()).getApplicationContext().getCurrentActivity();
								GeneralFragment oldFragment = ((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).currentFragment;
								fragmentTransaction.hide(oldFragment);
								fragmentTransaction.commit();
								newFragment.fragment_parent = oldFragment;
								data.childFragment = newFragment;
								((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).currentFragment = newFragment;
								((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).invalidateOptionsMenu();
							}
							//fragment already exisits -> show it
							else{
								FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).getFragmentManager().beginTransaction();
								GeneralFragment oldFragment = ((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).currentFragment;
								fragmentTransaction.hide(oldFragment);
								/*
								 * JACKSON START
								 * needed if template is edited, because we can create but we cannot add the fragment during inflation
								 */
								if(!data.childFragment.isAdded()) {
									fragmentTransaction.add(R.id.main_view_empty, data.childFragment);
							        Log.d("jackson", "jackson did add to fragmentTransaction!");
								}
								/*
								 * JACKSON END
								 */
								fragmentTransaction.show(data.childFragment);
								((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).currentFragment = data.childFragment;
								fragmentTransaction.commit();
								((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).invalidateOptionsMenu();
						        Log.d("data", "data child NOT null!");
							}
							data.childFragment.elementName = holder.text.getText().toString();
						}
                	});
                }
                else if(parent.getItemAtPosition(itemPosition).toString().equals("Text")){
                	data.setVisibility(View.VISIBLE);
                	holder.invisibleText.setVisibility(View.VISIBLE);
                	/*
                	 * JACKSON START
                	 */
                	// if a table was attached remove it
                	if(data.jacksonTable != null) {
                		holder.text.removeTextChangedListener(data.jacksonTable.tableNameTextWatcher);
                		jacksonTable.removeTable(data.jacksonTable);
                		data.jacksonTable = null;
                		// save template
                		Log.d("JSON_DATA_ADAPTER", "Text -> saved template");
                		MainTemplateGenerator.saveTemplateAsync();
                	}
                	/*
                	 * JACKSON END
                	 */
                }
                else if(data.getSelectedText().equals("Tabelle")) {
					/*
					 * JACKSON START
					 */
                	if(data.table == null){	
                   		// if it does not exist we check if we already have created a table
                		// if not we create a new one
                		// when the user clicks on the row we attach the table to the fragment
                		// TODO anstatt instanceof (langsam?!) typ info in abstract table
                		if(!(data.jacksonTable instanceof Table)) {
                			jacksonTable.removeTable(data.jacksonTable);
                			data.jacksonTable = jacksonTable.createAndAddNewTable();
                			//set table header
                			/*ColumnHeader[] headers = { 
	            					new ColumnHeader("spalte1", StringClass.TYPE_STRING),
	            					new ColumnHeader("spalte2", StringClass.TYPE_STRING),
                			};*/
                			//((Table)data.jacksonTable).columnHeaders = new ArrayList<ColumnHeader>(Arrays.asList(headers)) ;
                			//set table column number
                			//((Table)data.jacksonTable).numberOfColumns = 2;
                			// log for info
                			Log.d("NEW TABLE","created new table");
                			data.jacksonDoSaveOnNextChance = true;
                		}
            			// remove here because we are going to add it again
                		else {
                			holder.text.removeTextChangedListener(data.jacksonTable.tableNameTextWatcher);
                		}
					}
					else{
						// if exists
						// data.table was not null, so table Fragment already exists -> jackson table exists
						// replace current table with the table stored in fragment
                		if(data.jacksonTable != data.table.jacksonTable) {
                			holder.text.removeTextChangedListener(data.jacksonTable.tableNameTextWatcher);
                			jacksonTable.removeTable(data.jacksonTable);
                			jacksonTable.addTable(data.table.jacksonTable);
                			data.jacksonTable = data.table.jacksonTable;
                			Log.d("JSON_DATA_ADAPTER", "replaced table");
                			data.jacksonDoSaveOnNextChance = true;
                		}
					}
        			holder.text.addTextChangedListener(data.jacksonTable.tableNameTextWatcher);
                	
                	// save template
                	if(data.jacksonDoSaveOnNextChance) {
                		data.jacksonDoSaveOnNextChance = false;
                		Log.d("JSON_DATA_ADAPTER", "Table -> saved template");
                		MainTemplateGenerator.saveTemplateAsync();
                	}
					/*
					 * JACKSON END
					 */
                	
                	holder.row.setOnClickListener(new OnClickListener() {
                		//hier bei Klick neues Fragment anzeigen (== Unterordner)
						@Override
						public void onClick(View v) {
					        Log.d("data", "view == " + v.getParent());
							//new subfolder -> create new fragment for it
							if(data.table == null) {
								FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).getFragmentManager().beginTransaction();
								TableFragment newFragment = new TableFragment();
//								newFragment.elementName = holder.text.getText().toString();
								
								/*
								 * JACKSON START
								 */
								newFragment.jacksonTable = (Table) data.jacksonTable;
								/*
								 * JACKSON END
								 */
								
								fragmentTransaction.add(R.id.main_view_empty, newFragment);
								GeneralFragment oldFragment = ((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).currentFragment;
								fragmentTransaction.hide(oldFragment);
								fragmentTransaction.commit();
								newFragment.fragment_parent = oldFragment;
								data.table = newFragment;
								((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).currentFragment = newFragment;
								((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).invalidateOptionsMenu();
							}
							//fragment already exisits -> show it
							else{
								FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).getFragmentManager().beginTransaction();
								GeneralFragment oldFragment = ((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).currentFragment;
								fragmentTransaction.hide(oldFragment);
								/*
								 * JACKSON START
								 * needed if template is edited, because we can create but we cannot add the fragment during inflation
								 */
								if(!data.table.isAdded()) {
									fragmentTransaction.add(R.id.main_view_empty, data.table);
								}
								/*
								 * JACKSON END
								 */
								
								fragmentTransaction.show(data.table);
								((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).currentFragment = data.table;

								fragmentTransaction.commit();
								((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).invalidateOptionsMenu();
							}
							data.table.elementName = holder.text.getText().toString();
						}
                	});
                	
                	
//                	if(data.table == null){
//						FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).getFragmentManager().beginTransaction();
//						TableFragment newFragment = new TableFragment();
//						fragmentTransaction.add(R.id.main_view_empty, newFragment);
//						TemplateGeneratorFragment oldFragment = ((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).currentFragment;
//						fragmentTransaction.hide(oldFragment);
//						fragmentTransaction.commit();
//						newFragment.fragment_parent = oldFragment;
//						data.table = newFragment;
//						((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).currentFragment = newFragment;
//					}
//					//fragment already exisits -> show it
//					else{
//						FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).getFragmentManager().beginTransaction();
//						TableFragment oldFragment = ((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).currentFragment;
//						fragmentTransaction.hide(oldFragment);
//						fragmentTransaction.show(data.childFragment);
//						((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).currentFragment = data.childFragment;
//						fragmentTransaction.commit();
//				        Log.d("data", "data child NOT null!");
//					}
                }
                else{
                	data.setVisibility(View.GONE);
                	holder.invisibleText.setVisibility(View.GONE);
                	/*
                	 * JACKSON START
                	 */
                	// if a table was attached then remove it because we are no longer a folder nor table
                	if(data.jacksonTable != null) {
                		holder.text.removeTextChangedListener(data.jacksonTable.tableNameTextWatcher);
                		jacksonTable.removeTable(data.jacksonTable);
                		data.jacksonTable = null;
                		Log.d("JSON_DATA_ADAPTER", "Else -> saved template");
                		MainTemplateGenerator.saveTemplateAsync();
                	}
                	/*
                	 * JACKSON END
                	 */
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        

        holder.text.setText(data.text.getText());
        holder.spin.setSelection(data.getSelected());
        
        return view;
    }
}
