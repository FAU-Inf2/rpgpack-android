package de.fau.cs.mad.gamekobold.template_generator;

import java.util.ArrayList;
import java.util.Arrays;

import de.fau.cs.mad.gamekobold.*;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.Toast;

public class MainTemplateGenerator extends Activity implements OnItemSelectedListener{

	
	 EditText textIn;
	 Button buttonAdd;
	 //LinearLayout container;
	 //LinkedList<View> added_views = new LinkedList<View>();
	 private ListView lvItem;
//	 private ArrayList<String> itemArrey;
//	 private ArrayAdapter<String> itemAdapter;
	 DataAdapter dataAdapter;
//	 private Spinner spinner;
//	 private static final String[]paths = {"Typ", "Ordner", "Zahl", "Text"};
	 protected ArrayList<DataHolder> allData;
//	 ArrayAdapter<CharSequence> adapters[];
//	 private Context m_context;
	 TemplateGeneratorFragment fragment;


	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
//		 setContentView(R.layout.activity_main);
		 setContentView(R.layout.activity_empty);
		 
		 //method: use fragment to store everything
		 FragmentManager fragmentManager = getFragmentManager();
		 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		 fragment = new TemplateGeneratorFragment();
		 fragmentTransaction.add(R.id.main_view_empty, fragment);
//		 fragmentTransaction.commit();
		 

		 //method: store data here
//		 if(savedInstanceState == null) {
//			 allData = new ArrayList<DataHolder>();
//		 }
//		 else {
//        	Log.d("PIEP","ONCreate restores it!!!");
//        	allData = savedInstanceState.getParcelableArrayList("key2");
//        }
//		 
//		 
//		 if(allData == null){
//			 allData = new ArrayList<DataHolder>();
//			 Log.d("PIEP","ONCreate: ALLDATA == NULL!!!");
//			 DataHolder data = new DataHolder(this);
//			 allData.add(data);
//		 }
//		 
//	        
//
//	        ListView listView = (ListView) findViewById(R.id.listView_items);
//
//	        
//	        if(allData == null){
//	        	System.exit(0);
//	        }
//	        
//
//	        setUpView();
//	        
//	        
//	        if(dataAdapter == null){
//	        	dataAdapter = new DataAdapter(this, R.layout.initialrow, allData.toArray(new DataHolder[allData.size()]), allData);
//	        }
//	        else{
//	            Log.d("PIEP", "DATAADAPTER != NULL");
//	        }
//	        
//	        listView.setAdapter(dataAdapter);

	 }
	 
//	 public ArrayAdapter getAdapter(int i){
//		 return adapters[i];
//	 }


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

//        itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,itemArrey);
        if(lvItem == null){
        	System.exit(0);
        }
//        lvItem.setAdapter(itemAdapter);


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
//    	Log.d("PIEP","GETS SAVED, size is " + itemArrey.size());
//        outState.putStringArrayList("key", itemArrey);
    	outState.putParcelableArrayList("key2", allData);
//        outState.putParcelableArray("key2", allData.toArray(new DataHolder[allData.size()]));
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
    
    public DataHolder addItemList() {
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
    	DataHolder data4 = new DataHolder(this);
    	allData.add(data4);
//    	dataAdapter.notifyDataSetChanged();
    	ListView listView = (ListView) findViewById(R.id.listView_items);
    	dataAdapter = new DataAdapter(this, R.layout.initialrow, allData.toArray(new DataHolder[allData.size()]), allData);
        listView.setAdapter(dataAdapter);
    	Log.d("PIEP","ADD ITEM. now amount == " + allData.size());
    	return data4;
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
        	Intent startNewActivityOpen = new Intent(MainTemplateGenerator.this, MainTemplateGenerator.class);
//        	startNewActivityOpen.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        	startActivityForResult(startNewActivityOpen, 0);
        }
        else if (id == R.id.action_showlists) {
        	Intent startNewActivityOpen = new Intent(MainTemplateGenerator.this, ShowLists.class);
        	startActivityForResult(startNewActivityOpen, 0);
        }
        else if (id == R.id.action_add_folder) {
//        	Toast.makeText(this, "selected: " + getResources().getIdentifier("choices", "values", getPackageName()) ,Toast.LENGTH_LONG).show();
        	String[] items = getResources().getStringArray(R.array.choices);
        	int index = Arrays.asList(items).indexOf("Ordner");
        	fragment.addItemList(index);
        	
//        	fragment.addItemList(getResources().getStringArray(R.array.choices));

//        	DataHolder data = addItemList();
//        	data.setSelected("Ordner");
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
