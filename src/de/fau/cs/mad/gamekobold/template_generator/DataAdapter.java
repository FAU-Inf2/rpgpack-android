package de.fau.cs.mad.gamekobold.template_generator;


import de.fau.cs.mad.gamekobold.*;
import android.app.Activity;
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
import android.widget.Toast;


public class DataAdapter extends ArrayAdapter<DataHolder> {
	private Activity myContext;
	DataHolder[] allData;
	

    public DataAdapter(Activity context, int textViewResourceId, DataHolder[] objects) {
        super(context, textViewResourceId, objects);
        myContext = context;
        allData = new DataHolder[objects.length];
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
    static class ViewHolder {
        protected DataHolder data;
        protected EditText text;
        protected EditText invisibleTextField;
        protected Spinner spin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        // Check to see if this row has already been painted once.
        if (convertView == null) {

            Toast.makeText(getContext(), "now creating new view: ",Toast.LENGTH_SHORT).show();

            // If it hasn't, set up everything:
            LayoutInflater inflator = myContext.getLayoutInflater();
            view = inflator.inflate(R.layout.initialrow, null);

            // Make a new ViewHolder for this row, and modify its data and spinner:
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (EditText) view.findViewById(R.id.text);
            viewHolder.invisibleTextField = (EditText) view.findViewById(R.id.text2);
//            TODO: use DataHolder that were given at creation of DataAdapter
//            because we need the stored valus (e.g. spinner item)
            viewHolder.data = new DataHolder(myContext);
            viewHolder.spin = (Spinner) view.findViewById(R.id.spin);
            viewHolder.spin.setAdapter(viewHolder.data.getAdapter());

            // Used to handle events when the user changes the Spinner selection:
            viewHolder.spin.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    viewHolder.data.setSelected(arg2);
                    if(viewHolder.data.getSelectedText().equals("Ordner")){
                    	myContext.findViewById(R.id.listView_items);
                    	View row = ((View) arg1.getParent().getParent());
                    	Toast.makeText(myContext, myContext.getResources().getResourceEntryName(((View) arg1.getParent().getParent()).getId()), Toast.LENGTH_LONG).show();
                    	row.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Toast.makeText(myContext, "ONCLICK!!!", Toast.LENGTH_LONG).show();
								//TODO: change to another fragment, not to another activity
								Intent intent = new Intent(myContext, MainTemplateGenerator.class);
                    			myContext.startActivity(intent);
							}
                    	});
                    }
                    else if(arg0.getItemAtPosition(arg2).toString().equals("Text")){
                    	viewHolder.invisibleTextField.setVisibility(View.VISIBLE);
                    	//TODO: whole listview field must get larger if we show this text
                    	//doesn't seem to work with the following line (tried to invalidate listview to request rebuild)
                    	((ListView) arg1.getParent().getParent().getParent()).invalidate();
                    }
                    //if any other spinner item is selected
                    else{
                    	viewHolder.invisibleTextField.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }

            });

            view.setTag(viewHolder);

            Log.d("DBGINF", viewHolder.text.getText() + "");
        } else {
            view = convertView;
        }

		

        // This is what gets called every time the ListView refreshes
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.invisibleTextField.setText("test");
        
        return view;
    }
}
