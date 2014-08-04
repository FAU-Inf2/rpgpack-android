package de.fau.cs.mad.gamekobold.game;

import java.io.IOException;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import de.fau.cs.mad.gamekobold.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ToolboxRandomGenerator extends Activity{
   
	private TextView contentView;
	public String [] char_array = {"Albert","Bertram","Claudio","Dennis","Emanuela","Franzi","Gretchen","Hanna","Ida"};
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_toolbox_random);
        contentView = (TextView) findViewById(R.id.textView);
        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	int i = diceRoller(6);
	        	contentView.setText(String.valueOf(i));
	        }
	    });      
    }
	  
    public void randomCharList(View v){
    	 shuffleArray(char_array);
    	 String s = "";
    	 for (int i = 0; i<char_array.length; i++){
    		 s = s + char_array[i] + "\n"; 
    	 }
    	 contentView.setText(s);
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