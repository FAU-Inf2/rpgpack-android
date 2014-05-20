package de.fau.mad.clickdummy;

import java.util.ArrayList;

import com.example.kobold.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class DataAdapter extends ArrayAdapter<DataHolder> {
	private Activity myContext;
//	ArrayList<DataHolder> theData;
	

    public DataAdapter(Activity context, int textViewResourceId, DataHolder[] objects, ArrayList<DataHolder> allData) {
        super(context, textViewResourceId, objects);
        myContext = context;
//        theData = allData;
//        adapter = new ArrayAdapter[allData.size()];
//        for(int i=0; i<theData.size(); ++i){
//        	adapter = ArrayAdapter.createFromResource(context, R.array.choices, android.R.layout.simple_spinner_item);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        }
    }

    // We keep this ViewHolder object to save time. It's quicker than findViewById() when repainting.
    static class ViewHolder {
        protected DataHolder data;
        protected EditText text;
        protected EditText text2;
        protected Spinner spin;
//        protected ArrayAdapter<CharSequence> adapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        // Check to see if this row has already been painted once.
        if (convertView == null) {

            // If it hasn't, set up everything:
            LayoutInflater inflator = myContext.getLayoutInflater();
            view = inflator.inflate(R.layout.initialrow, null);

            // Make a new ViewHolder for this row, and modify its data and spinner:
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (EditText) view.findViewById(R.id.text);
        	viewHolder.text.setVisibility(View.INVISIBLE);
//            viewHolder.text.setSelectAllOnFocus(true);
            viewHolder.text2 = (EditText) view.findViewById(R.id.text2);
            //viewHolder.text.setText("TEST");
            viewHolder.data = new DataHolder(myContext);
            viewHolder.spin = (Spinner) view.findViewById(R.id.spin);
            viewHolder.spin.setAdapter(viewHolder.data.getAdapter());
//            viewHolder.spin.setAdapter(((MainActivity)myContext).getAdapter(viewHolder.data.getID()));

            // Used to handle events when the user changes the Spinner selection:
            viewHolder.spin.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    viewHolder.data.setSelected(arg2);
                    viewHolder.text.setText(viewHolder.data.getText());
//                    if(((MainActivity)myContext).getAdapter(viewHolder.data.getID()).getItem(viewHolder.data.getSelected()).toString().equals("Ordner")){
                    if(viewHolder.data.getSelectedText().equals("Ordner")){
//                    	MainActivity ma = (MainActivity) myContext;
//                    	ma.addItemList();
//                    	ma.allData.remove(viewHolder.data);
//                    	arg1.setClickable(true);
                    	myContext.findViewById(R.id.listView_items);
//                    	Toast.makeText(myContext, myContext.getResources().getResourceEntryName(((View) arg1.getParent().getParent()).getId()), Toast.LENGTH_LONG).show();

//                    	AdapterView<?> row = ((AdapterView<?>) arg1.getParent().getParent().getParent());
//                    	Toast.makeText(myContext, myContext.getResources().getResourceEntryName(((View) arg1.getParent().getParent()).getId()), Toast.LENGTH_LONG).show();
////                    	row.setClickable(true);
//                    	row.setOnItemClickListener(new OnItemClickListener() {
//							@Override
//							public void onItemClick(AdapterView<?> arg0,
//									View arg1, int arg2, long arg3) {
//								// TODO Auto-generated method stub
//								Toast.makeText(myContext, "ONCLICK!!!", Toast.LENGTH_LONG).show();
//								Intent intent = new Intent(myContext, MainActivity.class);
//                    			myContext.startActivity(intent);
//							}
//                    	});
                    	View row = ((View) arg1.getParent().getParent());
                    	Toast.makeText(myContext, myContext.getResources().getResourceEntryName(((View) arg1.getParent().getParent()).getId()), Toast.LENGTH_LONG).show();
//                    	row.setClickable(true);
                    	row.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Toast.makeText(myContext, "ONCLICK!!!", Toast.LENGTH_LONG).show();
								Intent intent = new Intent(myContext, MainActivity.class);
                    			myContext.startActivity(intent);
							}
                    	});
                    }
                    else if(arg0.getItemAtPosition(arg2).toString().equals("Text")){
                    	viewHolder.text2.setVisibility(View.VISIBLE);
//                    	arg1.findViewById(R.id.listView_items).invalidate();
//                    	viewHolder.text2.setText(arg1.getId());
//                    	Toast.makeText(myContext, myContext.getResources().getResourceEntryName(((View) arg1.getParent().getParent().getParent()).getId()), Toast.LENGTH_LONG).show();
                    	
//                    	((ListView) arg1.getParent()).getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
//                    	this.notifyDataSetChanged();
                    	((ListView) arg1.getParent().getParent().getParent()).invalidate();
                    }
                    else{
                    	viewHolder.text2.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }

            });

            // Update the TextView to reflect what's in the Spinner
//            viewHolder.text.setText(viewHolder.data.getText());
//            viewHolder.text.setText("WOEFIOFWLIFOF");
            
            view.setTag(viewHolder);

            Log.d("DBGINF", viewHolder.text.getText() + "");
        } else {
            view = convertView;
        }

        // This is what gets called every time the ListView refreshes
        ViewHolder holder = (ViewHolder) view.getTag();
//        holder.text.setText(getItem(position).getText());
        holder.text2.setText("test");
        holder.spin.setSelection(getItem(position).getSelected());
        
        return view;
    }
}
