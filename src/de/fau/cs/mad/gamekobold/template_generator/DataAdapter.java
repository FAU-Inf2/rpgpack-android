package de.fau.cs.mad.gamekobold.template_generator;


import java.util.ArrayList;

import de.fau.cs.mad.gamekobold.*;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class DataAdapter extends ArrayAdapter<DataHolder> {
	private Activity myContext;
	ArrayList<DataHolder> allData;
	

//    public DataAdapter(Activity context, int textViewResourceId, DataHolder[] objects) {
	public DataAdapter(Activity context, int textViewResourceId, ArrayList<DataHolder> objects) {
		super(context, textViewResourceId, objects);
		myContext = context;
        allData = new ArrayList<DataHolder>();
        allData = objects;
    }

//    maybe enable in future: fragment may addData instead of recreating DataAdapter
//    public boolean addData(DataHolder data){
//    	allData.add(data);
//    	return true;
//    }
    
    // We keep this ViewHolder object to save time. It's quicker than findViewById() when repainting.
//    maybe move this data to DataHolder?!
//    atm DataHolder only has the adapter for the spinner item
//    static DataHolder data;
	
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
      Log.d("set tag", "int ==" + viewPosition);
    	final DataHolder data = this.getItem(viewPosition);
        View view = null;
        // Check to see if this row has already been painted once.
        if (convertView == null) {
//        	Log.d("position == ", "NEW VIEW, pos = " + viewPosition + "!!!");

            // If it hasn't, set up everything:
            LayoutInflater inflator = myContext.getLayoutInflater();
            view = inflator.inflate(R.layout.initialrow, null);

//            data = this.getItem(position);
            
            final ViewHolder holder = new ViewHolder();
            holder.text = (EditText) view.findViewById(R.id.text);
            holder.invisibleText = (EditText) view.findViewById(R.id.text2);
            holder.spin = (Spinner) view.findViewById(R.id.spin);
            holder.row = view;
//            data = super.getItem(viewPosition);
//            Log.d("position", Integer.toString(position));

            data.text.setText("A");
            // Make a new ViewHolder for this row, and modify its data and spinner:
//            EditText text = (EditText) view.findViewById(R.id.text);
//            EditText invisibleTextField = (EditText) view.findViewById(R.id.text2);
////            TODO: use DataHolder that were given at creation of DataAdapter
////            because we need the stored valus (e.g. spinner item)
////            viewHolder.data = new DataHolder(myContext);
//            Spinner spin = (Spinner) view.findViewById(R.id.spin);
            ((Spinner) view.findViewById(R.id.spin)).setAdapter(data.getAdapter());


            // Used to handle events when the user changes the Spinner selection:
//            ((Spinner) view.findViewById(R.id.spin)).setOnItemSelectedListener(new OnItemSelectedListener() {
//
//            	//XXX: note: whatever is set insid onItemSelected must be set to data AND to the findViewById()-item
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int itemPosition, long id){
//                    data.setSelected(itemPosition);
////                    data.invisibleTextField.setVisibility(View.VISIBLE);
////                    data.setVisibility(View.VISIBLE);
//                    
////                    data.text.setText("UFO");
////                	Toast.makeText(myContext, "selected: " + data.getSelectedText(), Toast.LENGTH_LONG).show();
//                    if(data.getSelectedText().equals("Ordner")){
////                    if(((String) data.adapter.getItem(itemPosition)).equals("Ordner")){
////                	if((Spinner) arg1.findViewById(R.id.spin)).getSelectedText().equals("Ordner")){
////                    	((EditText) arg1.findViewById(R.id.text)).setText("Ordner");
////                    	Toast.makeText(myContext, "set Ordner!", Toast.LENGTH_LONG).show();
////                    	((EditText) myContext.findViewById(R.id.text)).setText("V");
////                    	data.invisibleTextField = (EditText) myContext.findViewById(R.id.text2);
////                    	((EditText) myContext.findViewById(R.id.text2)).setVisibility(View.VISIBLE);
//                    	data.text.setText("UFO");
//                    	holder.text.setText(data.text.getText());
//
//
////                    	myContext.findViewById(R.id.listView_items);
//                    	//taking whole element from listView and make it clickable
////                    	View row = ((View) view.getParent().getParent());
//                    	Toast.makeText(myContext, myContext.getResources().getResourceEntryName(((View) view.getParent().getParent()).getId()), Toast.LENGTH_LONG).show();
////                    	row.setOnClickListener(new OnClickListener() {
//                    	holder.row.setOnClickListener(new OnClickListener() {
//							@Override
//							public void onClick(View v) {
////								Toast.makeText(myContext, "ONCLICK!!!", Toast.LENGTH_LONG).show();
//								FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) myContext).getFragmentManager().beginTransaction();
//								TemplateGeneratorFragment newFragment = new TemplateGeneratorFragment();
//								fragmentTransaction.add(R.id.main_view_empty, newFragment);
////								fragmentTransaction.remove(((MainTemplateGenerator) myContext).fragment);
//								TemplateGeneratorFragment oldFragment = ((MainTemplateGenerator) myContext).fragment;
//								fragmentTransaction.hide(oldFragment);
//								fragmentTransaction.commit();
//								newFragment.fragment_parent = oldFragment;
//								((MainTemplateGenerator) myContext).fragment = newFragment;
//							}
//                    	});
//                    }
//                    else if(parent.getItemAtPosition(itemPosition).toString().equals("Text")){
////                    	data.invisibleTextField.setVisibility(View.VISIBLE);
//                    	data.setVisibility(View.VISIBLE);
//                    	//TODO: whole listview field must get larger if we show this text
//                    	//doesn't seem to work with the following line (tried to invalidate listview to request rebuild)
//                    	((ListView) view.getParent().getParent().getParent()).invalidate();
//                    }
//                    //if any other spinner item is selected
//                    else{
////                    	data.invisibleTextField.setVisibility(View.GONE);
//                    	data.setVisibility(View.GONE);
//                    }
//
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> arg0) {
//                }
//
//            });

//            view.setTag(viewHolder);
            view.setTag(holder);
//            Log.d("DBGINF", viewHolder.text.getText() + "");
        } else {
//        	Log.d("CALL", "View known!");
            view = convertView;
            
        }

        final ViewHolder holder = (ViewHolder) view.getTag();
//        final DataHolder data = super.getItem(viewPosition);

        //XXX: set EVERYTHING according to the data stored to prevent displaying data from wrong rows ("listView-item recycling")
//        data = (DataHolder) view.getTag();
//        storedData.text = (EditText) view.findViewById(R.id.text);
//        holder.invisibleText.setVisibility(data.isInvisible());
//        data.invisibleTextField = (EditText) view.findViewById(R.id.text2);
//        data.invisibleTextField.setVisibility(View.VISIBLE);
//        
//        data.spin = (Spinner) view.findViewById(R.id.spin);
//        data.text.setText("F");
        
        holder.text.setText(data.text.getText());
        holder.spin.setSelection(data.getSelected());
        
        ((Spinner) view.findViewById(R.id.spin)).setAdapter(data.getAdapter());
        data.setSelected(data.getSelected());
//        ((Spinner) view.findViewById(R.id.spin)).setAdapter(data.getAdapter());
        ((Spinner) view.findViewById(R.id.spin)).setOnItemSelectedListener(new OnItemSelectedListener() {

        	//XXX: note: whatever is set insid onItemSelected must be set to data AND to the findViewById()-item
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int itemPosition, long id){
                data.setSelected(itemPosition);
//                data.invisibleTextField.setVisibility(View.VISIBLE);
//                data.setVisibility(View.VISIBLE);
                
//                data.text.setText("UFO");
//            	Toast.makeText(myContext, "selected: " + data.getSelectedText(), Toast.LENGTH_LONG).show();
                if(data.getSelectedText().equals("Ordner")){
//                if(((String) data.adapter.getItem(itemPosition)).equals("Ordner")){
//            	if((Spinner) arg1.findViewById(R.id.spin)).getSelectedText().equals("Ordner")){
//                	((EditText) arg1.findViewById(R.id.text)).setText("Ordner");
//                	Toast.makeText(myContext, "set Ordner!", Toast.LENGTH_LONG).show();
//                	((EditText) myContext.findViewById(R.id.text)).setText("V");
//                	data.invisibleTextField = (EditText) myContext.findViewById(R.id.text2);
//                	((EditText) myContext.findViewById(R.id.text2)).setVisibility(View.VISIBLE);
                	data.text.setText("UFO");
//                	holder.text.setText(data.text.getText());


//                	myContext.findViewById(R.id.listView_items);
                	//taking whole element from listView and make it clickable
//                	View row = ((View) view.getParent().getParent());
                	Toast.makeText(myContext, myContext.getResources().getResourceEntryName(((View) view.getParent().getParent()).getId()), Toast.LENGTH_LONG).show();
//                	row.setOnClickListener(new OnClickListener() {
                	holder.row.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
//							Toast.makeText(myContext, "ONCLICK!!!", Toast.LENGTH_LONG).show();
							FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) myContext).getFragmentManager().beginTransaction();
							TemplateGeneratorFragment newFragment = new TemplateGeneratorFragment();
							fragmentTransaction.add(R.id.main_view_empty, newFragment);
//							fragmentTransaction.remove(((MainTemplateGenerator) myContext).fragment);
							TemplateGeneratorFragment oldFragment = ((MainTemplateGenerator) myContext).fragment;
							fragmentTransaction.hide(oldFragment);
							fragmentTransaction.commit();
							newFragment.fragment_parent = oldFragment;
							((MainTemplateGenerator) myContext).fragment = newFragment;
						}
                	});
                }
                else if(parent.getItemAtPosition(itemPosition).toString().equals("Text")){
//                	data.invisibleTextField.setVisibility(View.VISIBLE);
                	data.setVisibility(View.VISIBLE);
                	//TODO: whole listview field must get larger if we show this text
                	//doesn't seem to work with the following line (tried to invalidate listview to request rebuild)
                	((ListView) view.getParent().getParent().getParent()).invalidate();
                }
                //if any other spinner item is selected
                else{
//                	data.invisibleTextField.setVisibility(View.GONE);
                	data.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });
        
        holder.spin.setSelection(data.getSelected());
        // This is what gets called every time the ListView refreshes
//        ViewHolder holder = (ViewHolder) view.getTag();

//        Log.d("position", Integer.toString(position) + ", selected: " + getItem(position).getSelected());
//        data.spin.setSelection(getItem(position).getSelected());
//        holder.invisibleTextField.setText("test");
        
        return view;
    }
}
