package de.fau.mad.clickdummy;

import java.util.ArrayList;
import java.util.LinkedList;

import com.example.kobold.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemSelectedListener{

	
	 EditText textIn;
	 Button buttonAdd;
	 //LinearLayout container;
	 //LinkedList<View> added_views = new LinkedList<View>();
	 private ListView lvItem;
	 private ArrayList<String> itemArrey;
	 private ArrayAdapter<String> itemAdapter;
	 DataAdapter dataAdapter;
	 private Spinner spinner;
	 private static final String[]paths = {"Typ", "Ordner", "Zahl", "Text"};
	 private ArrayList<DataHolder> allData;


	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);

//		older version
//		 if(savedInstanceState == null) {
//			 itemArrey = new ArrayList<String>();
//		 }
//		 else {
//        	Log.d("PIEP","ONCreate restores it!!!");
//        	itemArrey = savedInstanceState.getStringArrayList("key");
//        }
//        setContentView(R.layout.activity_main);
//        if(itemArrey.size() != 0){
//        	Log.d("PIEP","GROESSER 0! IN onCreate");
//        }
//        setUpView();
//		 spinner = (Spinner)findViewById(R.id.spinner1);
//		 ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this,
//				 android.R.layout.simple_spinner_item,paths);
//
//		 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		 spinner.setAdapter(adapter);
//		 spinner.setOnItemSelectedListener(this);

		 
		 if(savedInstanceState == null) {
			 itemArrey = new ArrayList<String>();
			 allData = new ArrayList<DataHolder>();
		 }
		 else {
        	Log.d("PIEP","ONCreate restores it!!!");
        	itemArrey = savedInstanceState.getStringArrayList("key");
        	allData = savedInstanceState.getParcelableArrayList("key2");
        }
		 
		 super.onCreate(savedInstanceState);
		 
//		 if(allData == null){
//			 allData = new ArrayList<DataHolder>();
//		 }
		 
	        setContentView(R.layout.activity_main);

	        ListView listView = (ListView) findViewById(R.id.listView_items);

	        DataHolder data = new DataHolder(this);
	        allData.add(data);

	        setUpView();
	        dataAdapter = new DataAdapter(this, R.layout.initialrow, allData.toArray(new DataHolder[allData.size()]));
	        listView.setAdapter(dataAdapter);

	 }


	 @Override
	 public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			 long arg3) {
		 // TODO Auto-generated method stub

	 }

	 @Override
	 public void onNothingSelected(AdapterView<?> arg0) {
		 // TODO Auto-generated method stub

	 }

    private void setUpView(){
    	
    	textIn = (EditText)this.findViewById(R.id.textin);
        buttonAdd = (Button)this.findViewById(R.id.add);
        lvItem = (ListView)this.findViewById(R.id.listView_items);

        //itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,itemArrey);
        itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,itemArrey);
        lvItem.setAdapter(itemAdapter);


        buttonAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                addItemList();
            }
        });

//        textIn.setOnKeyListener(new View.OnKeyListener() {
//
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                // TODO Auto-generated method stub
//
//                if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                    addItemList();
//                }
//                return true;
//            }
//        });
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	//note super.onSaveInstanceState(outState) must be called at the end to recognize the changes!!!
    	Log.d("PIEP","GETS SAVED, size is " + itemArrey.size());
        outState.putStringArrayList("key", itemArrey);
        outState.putParcelableArray("key2", allData.toArray(new DataHolder[allData.size()]));
        super.onSaveInstanceState(outState);
    }
    
    
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//    	Log.d("PIEP","ONRestore!!!");
//        super.onRestoreInstanceState(savedInstanceState);
//        if(savedInstanceState == null || !savedInstanceState.containsKey("key")) {
//        	itemArrey = new ArrayList<String>();
//        }
//        else {
//        	Log.d("PIEP","ONRestore GEHT!!!");
////        	itemArrey = savedInstanceState.getStringArrayList("key");
//        	itemArrey = savedInstanceState.getStringArrayList("key");
//        }
//    }
    
    protected void addItemList() {
//        itemArrey.add(0,textIn.getText().toString());
//        //textIn.setText("");
//        
//        itemAdapter.notifyDataSetChanged();
        
//    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        TableLayout tl = new TableLayout(this);
//            TableRow tr = new TableRow(this);
//            TextView t = new TextView(this);
//            t.setText(textIn.getText().toString());
//            TableRow.LayoutParams params1 = new TableRow.LayoutParams(
//                    TableRow.LayoutParams.WRAP_CONTENT,
//                    TableRow.LayoutParams.WRAP_CONTENT);
//            tr.addView(t, params1);
//            Spinner spinner = (Spinner)findViewById(R.id.spinner1);
//            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                    android.R.layout.simple_spinner_item, paths);
//            dataAdapter
//                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner.setAdapter(dataAdapter);
//            TableRow.LayoutParams params2 = new TableRow.LayoutParams(
//                    TableRow.LayoutParams.FILL_PARENT,
//                    TableRow.LayoutParams.WRAP_CONTENT);
//            tr.addView(spinner, params2);
//            TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(
//                    TableLayout.LayoutParams.FILL_PARENT,
//                    TableLayout.LayoutParams.WRAP_CONTENT);
//            tl.addView(tr, trParams);
//        alert.setView(tl);
    	Log.d("PIEP","ADD ITEM!");
    	DataHolder data4 = new DataHolder(this);
    	allData.add(data4);
//    	dataAdapter.notifyDataSetChanged();
    	ListView listView = (ListView) findViewById(R.id.listView_items);
    	dataAdapter = new DataAdapter(this, R.layout.initialrow, allData.toArray(new DataHolder[allData.size()]));
        listView.setAdapter(dataAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_main) {
        	Intent startNewActivityOpen = new Intent(MainActivity.this, MainActivity.class);
        	startActivityForResult(startNewActivityOpen, 0);
        }
        else if (id == R.id.action_showlists) {
        	Intent startNewActivityOpen = new Intent(MainActivity.this, ShowLists.class);
        	startActivityForResult(startNewActivityOpen, 0);
        }
        return super.onOptionsItemSelected(item);
    }


//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            return rootView;
//        }
//    }

}
