package de.fau.cs.mad.gamekobold.template_generator;


import java.util.ArrayList;

import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.MatrixTable;
import de.fau.cs.mad.gamekobold.jackson.Table;
import de.fau.cs.mad.gamekobold.matrix.MatrixFragment;
import de.fau.cs.mad.gamekobold.template_generator.FolderElementData.element_type;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.v4.view.GravityCompat;
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
	
	private void setOnClickListener(final ViewHolder holder, final FolderElementData data){
		holder.row.setOnClickListener(new OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			GeneralFragment newFragment;
    			GeneralFragment oldFragment;
    			if(data.childFragment == null) {
    				FragmentTransaction fragmentTransaction = ((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).getFragmentManager().beginTransaction();
    				/*
    				 * JACKSON START
    				 */
    				// childFragment was null, so we need to create a new table (childFragment == FolderFragment)
    				// data.jacksonTable should be a Container Table, created when type changed
    				if(data.type == element_type.folder){
    					newFragment = new FolderFragment();
    					((FolderFragment) newFragment).setJacksonTable((ContainerTable)data.jacksonTable);
    				}
    				else if(data.type == element_type.table){
    					newFragment = new TableFragment();
    					((TableFragment) newFragment).jacksonTable = (Table) data.jacksonTable;
    				}
    				else {//if(data.type == element_type.matrix){
    					newFragment = new MatrixFragment();
    					((MatrixFragment) newFragment).jacksonTable = (MatrixTable) data.jacksonTable;
    				}
    				/*
    				 * JACKSON END
    				 */
    				fragmentTransaction.add(R.id.frame_layout_container, newFragment);
    				oldFragment = ((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment;
    				fragmentTransaction.detach(oldFragment);
    				newFragment.backStackElement = oldFragment;
    				fragmentTransaction.addToBackStack(null);
    				fragmentTransaction.commit();
    			}
    			//fragment already exisits -> show it
    			else{
    				newFragment = data.childFragment;
    				FragmentTransaction fragmentTransaction = ((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).getFragmentManager().beginTransaction();
    				oldFragment = ((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment;
    				fragmentTransaction.detach(oldFragment);
    				newFragment.backStackElement = oldFragment;
    				/*
    				 * JACKSON START
    				 * needed if template is edited, because we can create but we cannot add the fragment during inflation
    				 */
    				if(!newFragment.isAdded()) {
    					fragmentTransaction.add(R.id.frame_layout_container, newFragment);
    					Log.d("jackson", "jackson did add to fragmentTransaction!");
    				}
    				/*
    				 * JACKSON END
    				 */
    				fragmentTransaction.attach(newFragment);
    				((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment = data.childFragment;
    				fragmentTransaction.addToBackStack(null);
    				fragmentTransaction.commit();
    			}
    			newFragment.fragment_parent = oldFragment;
				data.childFragment = newFragment;
    			((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment = newFragment;
				if(((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
					//drawer is open -> new fragment will be top fragment
					((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).topFragment = newFragment;
				}
				((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).mDrawerLayout.closeDrawers();
				((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).invalidateOptionsMenu();
    			data.childFragment.elementName = holder.elementName.getText().toString();
    		}
    	});
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
        setOnClickListener(holder, data);
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
