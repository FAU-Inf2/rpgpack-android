package de.fau.cs.mad.gamekobold.template_generator;


import java.util.ArrayList;

import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.Table;
import de.fau.cs.mad.gamekobold.matrix.MatrixFragment;
import de.fau.cs.mad.gamekobold.template_generator.FolderElementData.element_type;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;


public class FolderElementAdapter extends ArrayAdapter<FolderElementData> {
	/*
	 * JACKSON START
	 */
	// DataAdapter only used in FolderFragments. one per fragment, so use DataAdapter for table info
	public ContainerTable jacksonTable;
	/*
	 * JACKSON END
	 */
	
	
	ArrayList<FolderElementData> allData;
	

	public FolderElementAdapter(Activity context, int textViewResourceId, ArrayList<FolderElementData> objects) {
		super(context, textViewResourceId, objects);
        allData = new ArrayList<FolderElementData>();
        allData = objects;
    }

    // We keep this ViewHolder object to save time. It's quicker than findViewById() when repainting.
	static class ViewHolder {
		EditText elementName;
		protected View row;
		TextWatcher jacksonTableNameChangeWatcher = null;
	}
	
	@Override
	public int getCount() {
	    return allData.size();
	}

	@Override
	public FolderElementData getItem(int position) {
	    return allData.get(position);
	}

	@Override
	public boolean hasStableIds(){
		return true;
	}

    @Override
    public View getView(int viewPosition, View convertView, ViewGroup parent) {
    	final FolderElementData data = this.getItem(viewPosition);
        View view = null;
        // Check to see if this row has already been painted once.
        if (convertView == null) {
            // If it hasn't, set up everything:
            LayoutInflater inflator = TemplateGeneratorActivity.theActiveActivity.getLayoutInflater();
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
        	//Log.d("folder","viewpos:"+viewPosition+" convertView:"+convertView+" parten:"+parent);
        	/*if (data.jacksonTable == null) {
        		data.jacksonTable = jacksonTable.createAndAddNewContainerTable();
        	}*/
        	//data.text.setText(data.jacksonTable.tableName);
        //	holder.elementName.setText(data.text.getText());
        	/*
        	 * JACKSON START
        	 */
        	// we changed type to folder, so check if childFragment exists
        	/*if(data.childFragment == null) {
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
        	}*/
        	// TODO noch mal schauen ob man das net wo anders hinpacken kann
        	//holder.elementName.removeTextChangedListener(data.jacksonTable.tableNameTextWatcher);
        	//holder.elementName.addTextChangedListener(data.jacksonTable.tableNameTextWatcher);
        	

        	// onItemSelected gets called when setting data while loading a template. use this flag to eliminate
        	// saving bug ( template is saved when data gets added)
        	if(data.jacksonDoSaveOnNextChance) {
        		data.jacksonDoSaveOnNextChance = false;
        		// save template
        		Log.d("JSON_DATA_ADAPTER", "Container Table -> save template");
        		TemplateGeneratorActivity.saveTemplateAsync();                		
        	}
        	/*
        	 * JACKSON END
        	 */

        	holder.row.setOnClickListener(new OnClickListener() {
        		@Override
        		public void onClick(View v) {
        			if(data.childFragment == null) {
        				FragmentTransaction fragmentTransaction = ((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).getFragmentManager().beginTransaction();
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
        				fragmentTransaction.add(R.id.frame_layout_container, newFragment);
        				GeneralFragment oldFragment = ((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment;
        				fragmentTransaction.detach(oldFragment);
        				newFragment.backStackElement = oldFragment;
        				fragmentTransaction.addToBackStack(null);
        				fragmentTransaction.commit();
    					Log.d("backstack", "backstack filled!");
        				newFragment.fragment_parent = oldFragment;
        				data.childFragment = newFragment;
        				((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment = newFragment;
        				((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).mDrawerLayout.closeDrawers();
        				((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).invalidateOptionsMenu();
        			}
        			//fragment already exisits -> show it
        			else{
        				FragmentTransaction fragmentTransaction = ((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).getFragmentManager().beginTransaction();
        				GeneralFragment oldFragment = ((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment;
        				fragmentTransaction.detach(oldFragment);
        				data.childFragment.backStackElement = oldFragment;
        				/*
        				 * JACKSON START
        				 * needed if template is edited, because we can create but we cannot add the fragment during inflation
        				 */
        				if(!data.childFragment.isAdded()) {
        					fragmentTransaction.add(R.id.frame_layout_container, data.childFragment);
        					Log.d("jackson", "jackson did add to fragmentTransaction!");
        				}
        				/*
        				 * JACKSON END
        				 */
        				fragmentTransaction.attach(data.childFragment);
        				((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment = data.childFragment;
        				fragmentTransaction.addToBackStack(null);
        				fragmentTransaction.commit();
        				((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).mDrawerLayout.closeDrawers();
        				((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).invalidateOptionsMenu();
        			}
        			data.childFragment.elementName = holder.elementName.getText().toString();
        		}
        	});
        }
        else if(data.type == element_type.table) {
			/*
			 * JACKSON START
			 */
        	/*
        	if(data.childFragment == null){	
           		// if it does not exist we check if we already have created a table
        		// if not we create a new one
        		// when the user clicks on the row we attach the table to the fragment
        		// TODO anstatt instanceof (langsam?!) typ info in abstract table
        		if(!(data.jacksonTable instanceof Table)) {
        			jacksonTable.removeTable(data.jacksonTable);
        			data.jacksonTable = jacksonTable.createAndAddNewTable();
        			//set table header
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
        	*/
        	// save template
        	if(data.jacksonDoSaveOnNextChance) {
        		data.jacksonDoSaveOnNextChance = false;
        		Log.d("JSON_DATA_ADAPTER", "Table -> saved template");
        		TemplateGeneratorActivity.saveTemplateAsync();
        	}
			/*
			 * JACKSON END
			 */
        	
        	holder.row.setOnClickListener(new OnClickListener() {
        		//hier bei Klick neues Fragment anzeigen (== Unterordner)
				@Override
				public void onClick(View v) {
					if(((TableFragment) data.childFragment) == null) {
						FragmentTransaction fragmentTransaction = ((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).getFragmentManager().beginTransaction();
						TableFragment newFragment = new TableFragment();
						/*
						 * JACKSON START
						 */
						newFragment.jacksonTable = (Table) data.jacksonTable;
						/*
						 * JACKSON END
						 */
						
						fragmentTransaction.add(R.id.frame_layout_container, newFragment);
						GeneralFragment oldFragment = ((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment;
						fragmentTransaction.detach(oldFragment);
						newFragment.backStackElement = oldFragment;
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
						newFragment.fragment_parent = oldFragment;
						data.childFragment = newFragment;
						((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment = newFragment;
        				((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).mDrawerLayout.closeDrawers();
						((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).invalidateOptionsMenu();
					}
					//fragment already exisits -> show it
					else{
						FragmentTransaction fragmentTransaction = ((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).getFragmentManager().beginTransaction();
						GeneralFragment oldFragment = ((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment;
						fragmentTransaction.detach(oldFragment);
						/*
						 * JACKSON START
						 * needed if template is edited, because we can create but we cannot add the fragment during inflation
						 */
						if(!((TableFragment) data.childFragment).isAdded()) {
							fragmentTransaction.add(R.id.frame_layout_container, ((TableFragment) data.childFragment));
						}
						/*
						 * JACKSON END
						 */
						
						fragmentTransaction.attach(((TableFragment) data.childFragment));
						data.childFragment.backStackElement = oldFragment;
						((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment = ((TableFragment) data.childFragment);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
        				((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).mDrawerLayout.closeDrawers();
						((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).invalidateOptionsMenu();
					}
					((TableFragment) data.childFragment).elementName = holder.elementName.getText().toString();
				}
        	});
        }
        
      //Matrix element
        else if(data.type == element_type.matrix) {
			
        	holder.row.setOnClickListener(new OnClickListener() {
        		//hier bei Klick neues Fragment anzeigen (== Matrixansicht)
				@Override
				public void onClick(View v) {
					if(((MatrixFragment) data.childFragment) == null) {
						FragmentTransaction fragmentTransaction = ((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).getFragmentManager().beginTransaction();
						MatrixFragment newFragment = new MatrixFragment();
						fragmentTransaction.add(R.id.main_view_empty, newFragment);
						GeneralFragment oldFragment = ((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment;
						fragmentTransaction.detach(oldFragment);
						newFragment.backStackElement = oldFragment;
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
						newFragment.fragment_parent = oldFragment;
						data.childFragment = newFragment;
						((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment = newFragment;
        				((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).mDrawerLayout.closeDrawers();
						((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).invalidateOptionsMenu();
					}
					//fragment already exists -> show it
					else{
						FragmentTransaction fragmentTransaction = ((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).getFragmentManager().beginTransaction();
						GeneralFragment oldFragment = ((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment;
						fragmentTransaction.detach(oldFragment);
						fragmentTransaction.attach(((MatrixFragment) data.childFragment));
						data.childFragment.backStackElement = oldFragment;
						((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment = ((MatrixFragment) data.childFragment);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.commit();
        				((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).mDrawerLayout.closeDrawers();
						((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).invalidateOptionsMenu();
					}
					//((FolderFragment) data.childFragment).elementName = holder.elementName.getText().toString();
				//	((MatrixFragment) data.childFragment).elementName = holder.elementName.getText().toString();

				}
        	});
        }
        
        /*else{
        	// if a table was attached then remove it because we are no longer a folder nor table
        	if(data.jacksonTable != null) {
        		holder.elementName.removeTextChangedListener(data.jacksonTable.tableNameTextWatcher);
        		jacksonTable.removeTable(data.jacksonTable);
        		data.jacksonTable = null;
        		Log.d("JSON_DATA_ADAPTER", "Else -> saved template");
        		TemplateGeneratorActivity.saveTemplateAsync();
        	}
        }*/
        /*
         * JACKSON START
         */
        // check if we are converting a view
        if(convertView != null) {
        	// if so we need to remove the old TextWatcher
        	holder.elementName.removeTextChangedListener(holder.jacksonTableNameChangeWatcher);
        	//Log.d("TableNameChangeWatcher", "convert View");
        }
        // set view.elementName according to data
        holder.elementName.setText(data.text.getText());
        // add TextWatcher
        holder.elementName.addTextChangedListener(data.nameChangeWatcher);
        // save which TextWatcher is currently added. needed for removal
        holder.jacksonTableNameChangeWatcher = data.nameChangeWatcher;
        // long click listener for removal of items
        holder.row.setOnLongClickListener(new View.OnLongClickListener() {
        	public final FolderElementData myDataItem = data;
			@Override
			public boolean onLongClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				builder.setTitle("Delete item?");
				builder.setMessage("Click yes to delete the item.");
				builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						jacksonTable.removeTable(myDataItem.jacksonTable);
						remove(myDataItem);
						notifyDataSetChanged();
					}
				});
				builder.create().show();
				return true;
			}
		});
       // Log.d("TableNameChangeWatcher", "set watcher");
        /*
         * JACKSON END
         */
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
        return view;
    }
    
    
}
