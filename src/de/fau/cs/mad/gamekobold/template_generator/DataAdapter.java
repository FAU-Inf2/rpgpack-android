package de.fau.cs.mad.gamekobold.template_generator;


import java.util.ArrayList;
import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.Table;
import de.fau.cs.mad.gamekobold.template_generator.DataHolder.element_type;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;


public class DataAdapter extends ArrayAdapter<DataHolder> {
	/*
	 * JACKSON START
	 */
	// DataAdapter only used in FolderFragments. one per fragment, so use DataAdapter for table info
	public ContainerTable jacksonTable;
	/*
	 * JACKSON END
	 */
	
	
	ArrayList<DataHolder> allData;
	

	public DataAdapter(Activity context, int textViewResourceId, ArrayList<DataHolder> objects) {
		super(context, textViewResourceId, objects);
        allData = new ArrayList<DataHolder>();
        allData = objects;
    }

    // We keep this ViewHolder object to save time. It's quicker than findViewById() when repainting.
	static class ViewHolder {
		EditText elementName;
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
	public boolean hasStableIds(){
		return true;
	}

    @Override
    public View getView(int viewPosition, View convertView, ViewGroup parent) {
    	final DataHolder data = this.getItem(viewPosition);
        View view = null;
        // Check to see if this row has already been painted once.
        if (convertView == null) {
            // If it hasn't, set up everything:
            LayoutInflater inflator = MainTemplateGenerator.theActiveActivity.getLayoutInflater();
            view = inflator.inflate(R.layout.initialrow, null);
            final ViewHolder holder = new ViewHolder();
            holder.elementName = (EditText) view.findViewById(R.id.text);
            holder.row = view;
            view.setTag(holder);
        } else {
            view = convertView;
        }
        //setting of onItemSelectedListener or other adapters needs to be done here! (because recycling of views in android)
        final ViewHolder holder = (ViewHolder) view.getTag();
        if(data.type == element_type.folder){
        	if (data.jacksonTable == null) {
        		data.jacksonTable = jacksonTable.createAndAddNewContainerTable();
        	}
        	holder.elementName.setText(data.text.getText());
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
        			holder.elementName.removeTextChangedListener(data.jacksonTable.tableNameTextWatcher);
        		}
        	}
        	else {
        		// if exists
        		// childFragment was not null, so folder Fragment already exists -> jackson table exists
        		// replace current table with the table stored in fragment
        		if((data.jacksonTable != ((FolderFragment) data.childFragment).jacksonTable)) {
        			holder.elementName.removeTextChangedListener(data.jacksonTable.tableNameTextWatcher);
        			jacksonTable.removeTable(data.jacksonTable);
        			jacksonTable.addTable(((FolderFragment)data.childFragment).jacksonTable);
        			data.jacksonTable = ((FolderFragment) data.childFragment).jacksonTable;
        			Log.d("JSON_DATA_ADAPTER", "replaced container table");
        			data.jacksonDoSaveOnNextChance = true;
        		}
        	}
        	// TODO noch mal schauen ob man das net wo anders hinpacken kann
        	holder.elementName.addTextChangedListener(data.jacksonTable.tableNameTextWatcher);

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
        		@Override
        		public void onClick(View v) {
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
        				fragmentTransaction.add(R.id.main_view_empty, newFragment);
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
        			}
        			data.childFragment.elementName = holder.elementName.getText().toString();
        		}
        	});
        }
        else if(data.type == element_type.table) {
			/*
			 * JACKSON START
			 */
        	if(data.childFragment == null){	
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
        			holder.elementName.removeTextChangedListener(data.jacksonTable.tableNameTextWatcher);
        		}
			}
			else{
				// if exists
				// data.table was not null, so table Fragment already exists -> jackson table exists
				// replace current table with the table stored in fragment
        		if(data.jacksonTable != ((TableFragment) data.childFragment).jacksonTable) {
        			holder.elementName.removeTextChangedListener(data.jacksonTable.tableNameTextWatcher);
        			jacksonTable.removeTable(data.jacksonTable);
        			jacksonTable.addTable(((TableFragment) data.childFragment).jacksonTable);
        			data.jacksonTable = ((TableFragment) data.childFragment).jacksonTable;
        			Log.d("JSON_DATA_ADAPTER", "replaced table");
        			data.jacksonDoSaveOnNextChance = true;
        		}
			}
			holder.elementName.addTextChangedListener(data.jacksonTable.tableNameTextWatcher);
        	
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
					if(((TableFragment) data.childFragment) == null) {
						FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).getFragmentManager().beginTransaction();
						TableFragment newFragment = new TableFragment();
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
						if(!((TableFragment) data.childFragment).isAdded()) {
							fragmentTransaction.add(R.id.main_view_empty, ((TableFragment) data.childFragment));
						}
						/*
						 * JACKSON END
						 */
						
						fragmentTransaction.show(((TableFragment) data.childFragment));
						((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).currentFragment = ((TableFragment) data.childFragment);

						fragmentTransaction.commit();
						((MainTemplateGenerator) MainTemplateGenerator.theActiveActivity).invalidateOptionsMenu();
					}
					((TableFragment) data.childFragment).elementName = holder.elementName.getText().toString();
				}
        	});
        }
        else{
        	/*
        	 * JACKSON START
        	 */
        	// if a table was attached then remove it because we are no longer a folder nor table
        	if(data.jacksonTable != null) {
        		holder.elementName.removeTextChangedListener(data.jacksonTable.tableNameTextWatcher);
        		jacksonTable.removeTable(data.jacksonTable);
        		data.jacksonTable = null;
        		Log.d("JSON_DATA_ADAPTER", "Else -> saved template");
        		MainTemplateGenerator.saveTemplateAsync();
        	}
        	/*
        	 * JACKSON END
        	 */
        }
        //set image left of the element
        ImageView elementSymbol = (ImageView) view.findViewById(R.id.element_symbol);
        switch (this.getItem(viewPosition).getType()){
        case table:
        	elementSymbol.setImageResource(R.drawable.table_icon);
        	break;
        case folder:
        	elementSymbol.setImageResource(R.drawable.folder_icon);
        	break;
        case matrix:
        	elementSymbol.setImageResource(R.drawable.collection_icon);
        	break;
        }
        holder.elementName.setText(data.text.getText());
        
        return view;
    }
}
