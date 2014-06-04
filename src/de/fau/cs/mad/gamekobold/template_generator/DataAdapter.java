package de.fau.cs.mad.gamekobold.template_generator;


import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.jackson.ColumnHeader;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.StringClass;
import de.fau.cs.mad.gamekobold.jackson.Table;
import android.app.Activity;
import android.app.FragmentTransaction;
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
	private Activity myContext;
	ArrayList<DataHolder> allData;
	

	public DataAdapter(Activity context, int textViewResourceId, ArrayList<DataHolder> objects) {
		super(context, textViewResourceId, objects);
		myContext = context;
        allData = new ArrayList<DataHolder>();
        allData = objects;
    }

    // We keep this ViewHolder object to save time. It's quicker than findViewById() when repainting.
	static class ViewHolder {
		TextView text;
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
            LayoutInflater inflator = myContext.getLayoutInflater();
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
            public void onItemSelected(AdapterView<?> parent, View view, int itemPosition, long id){
                data.setSelected(itemPosition);
                if(data.getSelectedText().equals("Ordner")){
                	
                	/*
                	 * JACKSON START
                	 */
                	if(data.myTable != null) {
                		data.myTable = data.myTable.parentTable.replaceTableWithContainerTable(data.myTable);
                		try {
							MainTemplateGenerator.myTemplate.saveToJSON(myContext, "testTemplate.json");
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
                	
                	data.text.setText("UFO");
                	holder.text.setText(data.text.getText());
                	holder.row.setOnClickListener(new OnClickListener() {
                		//hier bei Klick neues Fragment anzeigen (== Unterordner)
						@Override
						public void onClick(View v) {
					        Log.d("data", "view == " + v.getParent());
							//new subfolder -> create new fragment for it
							if(data.childFragment == null){
								FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) myContext).getFragmentManager().beginTransaction();
								FolderFragment newFragment = new FolderFragment();
								
								/*
								 * JACKSON START
								 */
								newFragment.myTable = (ContainerTable) data.myTable;
								/*
								 * JACKSON END
								 */
								
								fragmentTransaction.add(R.id.main_view_empty, newFragment);
								GeneralFragment oldFragment = ((MainTemplateGenerator) myContext).currentFragment;
								fragmentTransaction.hide(oldFragment);
								fragmentTransaction.commit();
								newFragment.fragment_parent = oldFragment;
								data.childFragment = newFragment;
								((MainTemplateGenerator) myContext).currentFragment = newFragment;
							}
							//fragment already exisits -> show it
							else{
								
								/*
								 * JACKSON START
								 */
								data.childFragment.myTable = (ContainerTable) data.myTable;
								/*
								 * JACKSON END
								 */
								
								FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) myContext).getFragmentManager().beginTransaction();
								GeneralFragment oldFragment = ((MainTemplateGenerator) myContext).currentFragment;
								fragmentTransaction.hide(oldFragment);
								fragmentTransaction.show(data.childFragment);
								((MainTemplateGenerator) myContext).currentFragment = data.childFragment;
								fragmentTransaction.commit();
						        Log.d("data", "data child NOT null!");
							}
						}
                	});
                }
                else if(parent.getItemAtPosition(itemPosition).toString().equals("Text")){
                	data.setVisibility(View.VISIBLE);
                	holder.invisibleText.setVisibility(View.VISIBLE);
                	/*
                	 * JACKSON START
                	 */
                	if(data.myTable != null) {
                		data.myTable.parentTable.removeTable(data.myTable);
                	}
                	/*
                	 * JACKSON END
                	 */
                }
                else if(parent.getItemAtPosition(itemPosition).toString().equals("Tabelle")){
                	
                   	/*
                	 * JACKSON START
                	 */
                	if(data.myTable != null) {
                		data.myTable = data.myTable.parentTable.replaceTableWithTable(data.myTable);
                		((Table)data.myTable).columnHeaders = new ColumnHeader[]{ 
                				new ColumnHeader("spalte1", StringClass.TYPE_STRING),
                				new ColumnHeader("spalte2", StringClass.TYPE_STRING),
                				};
                		((Table)data.myTable).numberOfColumns = 2;
                		Log.d("NEW TABLE","new table");
                		try {
							MainTemplateGenerator.myTemplate.saveToJSON(myContext, "testTemplate.json");
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
                	
                	if(data.table == null){
						FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) myContext).getFragmentManager().beginTransaction();
						TableFragment newFragment = new TableFragment();
						
						/*
						 * JACKSON START
						 */
						newFragment.myTable = (Table) data.myTable;
						/*
						 * JACKSON END
						 */
						
						fragmentTransaction.add(R.id.main_view_empty, newFragment);
						GeneralFragment oldFragment = ((MainTemplateGenerator) myContext).currentFragment;
						fragmentTransaction.hide(oldFragment);
						fragmentTransaction.commit();
						newFragment.fragment_parent = oldFragment;
						data.table = newFragment;
						((MainTemplateGenerator) myContext).currentFragment = newFragment;
					}
					//fragment already exisits -> show it
					else{
						FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) myContext).getFragmentManager().beginTransaction();
						GeneralFragment oldFragment = ((MainTemplateGenerator) myContext).currentFragment;
						fragmentTransaction.hide(oldFragment);
						fragmentTransaction.show(data.table);
						((MainTemplateGenerator) myContext).currentFragment = data.table;
						
						/*
						 * JACKSON START
						 */
						data.table.myTable = (Table) data.myTable;
						/*
						 * JACKSON END
						 */
						
						fragmentTransaction.commit();
				        Log.d("data", "data child NOT null!");
					}
                	
//                	if(data.table == null){
//						FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) myContext).getFragmentManager().beginTransaction();
//						TableFragment newFragment = new TableFragment();
//						fragmentTransaction.add(R.id.main_view_empty, newFragment);
//						TemplateGeneratorFragment oldFragment = ((MainTemplateGenerator) myContext).currentFragment;
//						fragmentTransaction.hide(oldFragment);
//						fragmentTransaction.commit();
//						newFragment.fragment_parent = oldFragment;
//						data.table = newFragment;
//						((MainTemplateGenerator) myContext).currentFragment = newFragment;
//					}
//					//fragment already exisits -> show it
//					else{
//						FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) myContext).getFragmentManager().beginTransaction();
//						TableFragment oldFragment = ((MainTemplateGenerator) myContext).currentFragment;
//						fragmentTransaction.hide(oldFragment);
//						fragmentTransaction.show(data.childFragment);
//						((MainTemplateGenerator) myContext).currentFragment = data.childFragment;
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
                	if(data.myTable != null) {
                		data.myTable.parentTable.removeTable(data.myTable);
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
