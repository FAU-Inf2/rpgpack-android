package de.fau.cs.mad.gamekobold.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import de.fau.cs.mad.gamekobold.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ToolboxRandomGenerator extends Activity{
   
	ArrayList<String> list = new ArrayList<String>();
	GridView grid;
	private TextView contentView, testText;
	public String [] char_array = {"Albert","Bertram","Claudio","Dennis","Emanuela","Franzi","Gretchen","Hanna","Ida"};
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_toolbox_random);
        contentView = (TextView) findViewById(R.id.textView);    
    }
	  
    public void randomCharList(View v){
    	 shuffleArray(char_array);
    	 String s = "";
    	 for (int i = 0; i<char_array.length; i++){
    		 s = s + char_array[i] + "\n"; 
    	 }
    	 contentView.setText(s);
    }
    
    public void addDice(View v){
    	    	
    	RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.random_layout);
    	grid = new GridView(ToolboxRandomGenerator.this);
    	if (list.size()<10){
    		list.add(String.valueOf(list.size()));
    	}
    	
    	ToolboxRandomElementAdapter adp=new ToolboxRandomElementAdapter (ToolboxRandomGenerator.this, list);
    	grid.setNumColumns(3);
        grid.setBackgroundColor(getResources().getColor(R.color.background_dark));        
        grid.setAdapter(adp);
        relativeLayout.addView(grid);
    	setContentView(relativeLayout);
    }
    
    public void rollDice(View v){
    	final int size = grid.getChildCount();
    	for(int i = 0; i < size; i++) {
    		  ViewGroup gridChild = (ViewGroup) grid.getChildAt(i);
    		  int childSize = gridChild.getChildCount();
    		  for(int k = 0; k < childSize; k++) {
    		    if( gridChild.getChildAt(k) instanceof TextView ) {
    		    	int dice = diceRoller(6);
    		    	TextView tmp = (TextView)gridChild.getChildAt(k);
    		    	tmp.setText(String.valueOf(dice));
    		    }
    		  }
    		}
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
    
    static int diceRoller(int maxValue){
    	Random rnd = new Random();
    	int rndint = (rnd.nextInt(maxValue))+1;
    	return rndint;
    }

}