package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;

public class ToolboxRandomListActivity extends Activity{
	
	private TextView tv_randomlist;
	public String [] names = {"Albert","Bertram","Claudio","Dennis","Emanuela","Franzi","Gretchen","Hanna","Ida"};
	public ArrayList<String> char_array = new ArrayList<String>();
			
	ArrayList<String> listItems=new ArrayList<String>();
	ArrayAdapter<String> adapter;
	ListView lv_randomlist;
	
	@Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 
		 setContentView(R.layout.activity_game_toolbox_randomlist);
		 tv_randomlist = (TextView) findViewById(R.id.tv_randomlist);
		 
		 shuffleArray(names);
		 for(String item : names)
			 char_array.add(item);
		 
		 setListView();
		 
		 		 
	}
	
    public void randomCharList(View v){
    	tv_randomlist.setText("");
    	shuffleArray(names);
    	String s = "";
    	 
    	for (int i = 0; i<names.length; i++){
    		s = s + names[i] + "\n";	 
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
    
    public void setListView(){
    	
    	RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.randomlist_layout);
    	lv_randomlist = new ListView(ToolboxRandomListActivity.this);
    	ToolboxRandomListElementAdapter adp=new ToolboxRandomListElementAdapter (ToolboxRandomListActivity.this, char_array);
    	//lv_randomlist.setNumColumns(3);
    	lv_randomlist.setBackgroundColor(getResources().getColor(R.color.background_dark));   
    	lv_randomlist.setDivider(new ColorDrawable(this.getResources().getColor(R.color.background_green)));
    	lv_randomlist.setDividerHeight(1);
    	lv_randomlist.setAdapter(adp);
        relativeLayout.addView(lv_randomlist);
    	setContentView(relativeLayout);
    	
    }
    
}
