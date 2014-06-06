package de.fau.cs.mad.gamekobold.template_generator;


import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.jackson.ColumnHeader;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.StringClass;
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
	/*
	 * JACKSON START
	 */
	// DataAdapter only used in FolderFragments. one per fragment, so use DataAdapter for table info
	public ContainerTable jacksonTable;
	/*
	 * JACKSON END
	 */
	
	
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
                	data.text.setText("UFO");
                	holder.text.setText(data.text.getText());
                	
                	/*
                	 * JACKSON START
                	 */
                	// we changed type to folder, so check if childFragment exists
                	if(data.childFragment == null) {
                		// it does not exist, so we create a table and store it in data holder
                		// when the user clicks on the row we attach the table to the fragment
                		jacksonTable.removeTable(data.jacksonTable);
                		data.jacksonTable = jacksonTable.createAndAddNewContainerTable();
                		Log.d("JSON_DATA_ADAPTER", "created new container table");
                		try {
							MainTemplateGenerator.myTemplate.saveToJSON(myContext, "testTemplate.json");
							Log.d("JSON_DATA_ADAPTER", "saved template");
						} catch (JsonGenerationException
								| JsonMappingException e) {
							e.printStackTrace();
						}
                		catch(IOException e) {
                			e.printStackTrace();
                		}
                	}
                	else {
                		// if exists
						// childFragment was not null, so folder Fragment already exists -> jackson table exists
						// replace current table with the table stored in fragment
                		jacksonTable.removeTable(data.jacksonTable);
                		jacksonTable.addTable(data.childFragment.jacksonTable);
						data.jacksonTable = data.childFragment.jacksonTable;
						Log.d("JSON_DATA_ADAPTER", "replaced table");
						try {
							MainTemplateGenerator.myTemplate.saveToJSON(myContext, "testTemplate.json");
							Log.d("JSON_DATA_ADAPTER", "saved template");
						} catch (JsonGenerationException
								| JsonMappingException e) {
							e.printStackTrace();
						}
                		catch(IOException e) {
                			e.printStackTrace();
                		}
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
								FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) myContext).getFragmentManager().beginTransaction();
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
								GeneralFragment oldFragment = ((MainTemplateGenerator) myContext).currentFragment;
								fragmentTransaction.hide(oldFragment);
								fragmentTransaction.commit();
								newFragment.fragment_parent = oldFragment;
								data.childFragment = newFragment;
								((MainTemplateGenerator) myContext).currentFragment = newFragment;
							}
							//fragment already exisits -> show it
							else{
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
                	// if a table was attached remove it
                	if(data.jacksonTable != null) {
                		jacksonTable.removeTable(data.jacksonTable);
                		data.jacksonTable = null;
                		try {
                			MainTemplateGenerator.myTemplate.saveToJSON(myContext, "testTemplate.json");
                			Log.d("JSON_DATA_ADAPTER", "saved template");
                		} catch (JsonGenerationException
                				| JsonMappingException e) {
                			e.printStackTrace();
                		}
                		catch(IOException e) {
                			e.printStackTrace();
                		}
                	}
                	/*if(data.jacksonTable != null) {
                		// TODO research if parentTable is nec. parentTable should be jacksonTable of this adapter
                		jacksonTable.removeTable(data.jacksonTable);
                		//data.jacksonTable.parentTable.removeTable(data.jacksonTable);
                	}*/
                	/*
                	 * JACKSON END
                	 */
                }
                else if(parent.getItemAtPosition(itemPosition).toString().equals("Tabelle")){                	
                	if(data.table == null){
						FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) myContext).getFragmentManager().beginTransaction();
						TableFragment newFragment = new TableFragment();
						
						/*
						 * JACKSON START
						 */
						// data.table was null, so create new jackson table and add it to the current tree
						newFragment.jacksonTable = jacksonTable.createAndAddNewTable();
						// remove old table from tree. e.g. when changing type from folder to table
						if(data.jacksonTable != null) {
							jacksonTable.removeTable(data.jacksonTable);
						}
						// update reference in data holder
						data.jacksonTable = newFragment.jacksonTable;
	                	// set table header
	                	newFragment.jacksonTable.columnHeaders = new ColumnHeader[]{ 
	            				new ColumnHeader("spalte1", StringClass.TYPE_STRING),
	            				new ColumnHeader("spalte2", StringClass.TYPE_STRING),
	            				};
	                	// set table column number
	            		newFragment.jacksonTable.numberOfColumns = 2;
	            		// log for info
	            		Log.d("NEW TABLE","created new table");
	            		try {
							MainTemplateGenerator.myTemplate.saveToJSON(myContext, "testTemplate.json");
							Log.d("JSON_DATA_ADAPTER", "saved template");
						} catch (JsonGenerationException
								| JsonMappingException e) {
							e.printStackTrace();
						}
                		catch(IOException e) {
                			e.printStackTrace();
                		}
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
						// data.table was not null, so tableFragment already exists -> jackson table exists
						// replace current table with the table stored in fragment
						jacksonTable.replaceTable(data.jacksonTable, data.table.jacksonTable);
						data.jacksonTable = data.table.jacksonTable;
						Log.d("JSON_DATA_ADAPTER", "replaced table");
						try {
							MainTemplateGenerator.myTemplate.saveToJSON(myContext, "testTemplate.json");
							Log.d("JSON_DATA_ADAPTER", "saved template");
						} catch (JsonGenerationException
								| JsonMappingException e) {
							e.printStackTrace();
						}
                		catch(IOException e) {
                			e.printStackTrace();
                		}
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
                	// if a table was attached then remove it because we are no longer a folder nor table
                	if(data.jacksonTable != null) {
                		jacksonTable.removeTable(data.jacksonTable);
                		data.jacksonTable = null;
                		try {
                			MainTemplateGenerator.myTemplate.saveToJSON(myContext, "testTemplate.json");
                			Log.d("JSON_DATA_ADAPTER", "saved template");
                		} catch (JsonGenerationException
                				| JsonMappingException e) {
                			e.printStackTrace();
                		}
                		catch(IOException e) {
                			e.printStackTrace();
                		}
                	}
                	/*if(data.jacksonTable != null) {
                		jacksonTable.removeTable(data.jacksonTable);
                		//data.jacksonTable.parentTable.removeTable(data.jacksonTable);
                	}*/
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
