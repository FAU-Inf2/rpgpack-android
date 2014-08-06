package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;

public class ToolboxRandomListActivity extends ListActivity{
	
	private TextView tv_randomlist;
	
	public String [] char_array = {"Albert","Bertram","Claudio","Dennis","Emanuela","Franzi","Gretchen","Hanna","Ida"};
	ArrayList<String> listItems=new ArrayList<String>();
	ArrayAdapter<String> adapter;
	
	@Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 
		 setContentView(R.layout.activity_game_toolbox_randomlist);
		 adapter=new ArrayAdapter<String>(this,
		            android.R.layout.simple_list_item_1,
		            listItems);
		        setListAdapter(adapter);
		 tv_randomlist = (TextView) findViewById(R.id.tv_randomlist);
		 addItems();
		 		 
	}
	

    public void addItems() {
        for(String item : char_array){
        	listItems.add(item);	
        }
        adapter.notifyDataSetChanged();
    }
	
    public void randomCharList(View v){
    	tv_randomlist.setText("");
    	shuffleArray(char_array);
    	String s = "";
    	 
    	for (int i = 0; i<char_array.length; i++){
    		s = s + char_array[i] + "\n";	 
    	}
    	tv_randomlist.setText(s);
    }
    
    static void shuffleArray(String[] ar){
    	Random rnd = new Random();
    	for (int i = ar.length - 1; i > 0; i--){
    		int index = rnd.nextInt(i + 1);
    		String a = ar[index];
    		ar[index] = ar[i];
    		ar[i] = a;
    	}
    }
}
