package de.fau.cs.mad.gamekobold.game;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;

public class ToolboxRandomListActivity extends Activity{
	
	private TextView tv_test;
	
	public String [] char_array = {"Albert","Bertram","Claudio","Dennis","Emanuela","Franzi","Gretchen","Hanna","Ida"};
	
	@Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 
		 setContentView(R.layout.activity_game_toolbox_randomlist);
		 
		 tv_test = (TextView) findViewById(R.id.tv_sum);
		 		 
	}
	
    public void randomCharList(View v){
    	tv_test.setText("");
    	shuffleArray(char_array);
    	String s = "";
    	setContentView(R.layout.activity_game_toolbox_dice);
    	 
    	for (int i = 0; i<char_array.length; i++){
    		s = s + char_array[i] + "\n";	 
    	}
    	tv_test = (TextView) findViewById(R.id.tv_test);
    	tv_test.setText(s);
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
