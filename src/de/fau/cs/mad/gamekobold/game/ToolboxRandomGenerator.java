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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ToolboxRandomGenerator extends Activity{
   
	ArrayList<String> list = new ArrayList<String>();
	GridView grid;
	private TextView TextView, testText;
	Button btn_add;
	public String [] char_array = {"Albert","Bertram","Claudio","Dennis","Emanuela","Franzi","Gretchen","Hanna","Ida"};
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_game_toolbox_random);
        
        TextView = (TextView) findViewById(R.id.textView);
        btn_add = (Button)findViewById(R.id.btn_add);
        
    }
	  
    public void randomCharList(View v){
    	list.removeAll(list);
    	 shuffleArray(char_array);
    	 String s = "";
    	 setContentView(R.layout.activity_game_toolbox_random);
         
         TextView = (TextView) findViewById(R.id.textView);
         btn_add = (Button)findViewById(R.id.btn_add);
    	 
    	 for (int i = 0; i<char_array.length; i++){
    		 s = s + char_array[i] + "\n"; 
    	 }
    	 TextView.setText(s);
    }
    
    public void addDice(View v){
    	
    	PopupMenu popup = new PopupMenu(getBaseContext(), v);
    	 
        popup.getMenuInflater().inflate(R.menu.game_toolbox_random, popup.getMenu());

        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
            	TextView.setText("");
            	String dice = "";
            	if (list.size()<9){
            		
            		switch (item.getItemId()) {
                    case R.id.item_d4:
                    	dice = "4";
                    	break;
                    case R.id.item_d6:
                    	dice = "6";
                    	break;
                    case R.id.item_d8:
                    	dice = "8";
                    	break;
                    case R.id.item_d10:
                    	dice = "10";
                    	break;
                    case R.id.item_d12:
                    	dice = "12";
                    	break;
                    case R.id.item_d20:
                    	dice = "20";
                    	break;
                    default:
                    	break;	
            		}
            		
            		list.add(dice);
            	}
            	
            	setGridView();
            	return true;
            }
        });
        popup.show();
    	
    	
    	TextView.setText("");
    }
    
    public void rollDice(View v){
    	final int size = grid.getChildCount();
    	int sum = 0;
    	for(int i = 0; i < size; i++) {
    		  ViewGroup gridChild = (ViewGroup) grid.getChildAt(i);
    		  int childSize = gridChild.getChildCount();
    		  for(int k = 0; k < childSize; k++) {
    		    if( gridChild.getChildAt(k) instanceof TextView ) {
    		    	TextView tmp = (TextView)gridChild.getChildAt(k);
    		    	int maxValue = Integer.parseInt((String) tmp.getHint());
    		    	int dice = diceRoller(maxValue);
    		    	sum = sum + dice;
    		    	tmp.setText(String.valueOf(dice));
    		  }
    		}
    	}
    	TextView tv_sum = (TextView)findViewById(R.id.tv_sum);
    	tv_sum.setText("Sum: " + String.valueOf(sum));
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
    
    public String getDiceNum(String s){
    	s = s.replaceAll("\\D+","");
    	return s;
    }
    
    public void clearView (View v) {
    	setContentView(R.layout.activity_game_toolbox_random);
    	TextView = (TextView) findViewById(R.id.textView);
    	btn_add = (Button)findViewById(R.id.btn_add);
    	TextView.setText("");
    	list.removeAll(list);
    }

    public void setGridView(){

    	RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.random_layout);
    	grid = new GridView(ToolboxRandomGenerator.this);
    	ToolboxRandomElementAdapter adp=new ToolboxRandomElementAdapter (ToolboxRandomGenerator.this, list);
    	grid.setNumColumns(3);
        grid.setBackgroundColor(getResources().getColor(R.color.background_dark));        
        grid.setAdapter(adp);
        relativeLayout.addView(grid);
    	setContentView(relativeLayout);
    	
    }
    
}