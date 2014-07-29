package de.fau.cs.mad.gamekobold.game;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import de.fau.cs.mad.gamekobold.R;
import android.os.CountDownTimer; 

public class TimerActivity extends Activity{
	
	Button btn_start, btn_pause;
	TextView textViewTime;
	
	@Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_game_timer);
		 btn_start = (Button)findViewById(R.id.btn_start);
		 btn_pause = (Button)findViewById(R.id.btn_pause);
		 textViewTime = (TextView)findViewById(R.id.textViewTime);
		 textViewTime.setText("00:03:00");
		 
		 final CounterClass timer = new CounterClass(180000,1000);
		 btn_start.setOnClickListener(new OnClickListener() {  
	            @Override  
	            public void onClick(View v) {  
	              timer.start();  
	            }  
	          });  
	          btn_pause.setOnClickListener(new OnClickListener() {  
	            @Override  
	            public void onClick(View v) {  
	              timer.cancel();  
	            }  
	          });

	}
	
	 public class CounterClass extends CountDownTimer {  
         public CounterClass(long millisInFuture, long countDownInterval) {  
              super(millisInFuture, countDownInterval);  
         }  
         @Override  
        public void onFinish() {  
          textViewTime.setText("@string/complete");  
        }  
         @Override  
         public void onTick(long millisUntilFinished) {  
               long millis = millisUntilFinished;  
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),  
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),  
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));  
                textViewTime.setText(hms);  
         }  
    }  

}