package de.fau.cs.mad.gamekobold.game;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import de.fau.cs.mad.gamekobold.R;
import android.os.CountDownTimer; 

public class TimerActivity extends Activity{
	
	Button btn_start, btn_pause;
	TextView textViewTimeHour, textViewTimeMin, textViewTimeSec ;
	Boolean timer_status = false;
	
	@Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_game_timer);
		 btn_start = (Button)findViewById(R.id.btn_start);
		 btn_pause = (Button)findViewById(R.id.btn_stop);
		 textViewTimeHour = (TextView)findViewById(R.id.textViewTimeHour);
		 textViewTimeHour.setText("00");
		 textViewTimeMin = (TextView)findViewById(R.id.textViewTimeMin);
		 textViewTimeMin.setText("03");
		 textViewTimeSec = (TextView)findViewById(R.id.textViewTimeSec);
		 textViewTimeSec.setText("00");
	}
	
	 public class CounterClass extends CountDownTimer {  
         public CounterClass(long millisInFuture, long countDownInterval) {  
              super(millisInFuture, countDownInterval);  
         }  
         @Override  
        public void onFinish() {  
        //add some sound
          textViewTimeHour.setText("00");
          textViewTimeMin.setText("00");
          textViewTimeSec.setText("00");
        }  
         @Override  
         public void onTick(long millisUntilFinished) {  
               long millis = millisUntilFinished;  
                String h = String.format("%02d", TimeUnit.MILLISECONDS.toHours(millis));
                String m = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
                String s = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));      
                      
                textViewTimeHour.setText(h);
                textViewTimeMin.setText(m);
                textViewTimeSec.setText(s);
         }  
    }
	 @Override
	 public void onSaveInstanceState(Bundle icicle) {
		 super.onSaveInstanceState(icicle);
		 icicle.putString("hour", ((TextView)findViewById(R.id.textViewTimeHour)).getText().toString());
		 icicle.putString("minute", ((TextView)findViewById(R.id.textViewTimeMin)).getText().toString());
		 icicle.putString("second", ((TextView)findViewById(R.id.textViewTimeSec)).getText().toString());
		 icicle.putBoolean("status", timer_status);
	 }
	 
	 @Override
	 public void onRestoreInstanceState(Bundle icicle) {
		 textViewTimeHour.setText(icicle.getString("hour"));
		 textViewTimeMin.setText(icicle.getString("minute"));
		 textViewTimeSec.setText(icicle.getString("second"));
		 if(icicle.getBoolean("status"))
			 startTimer(findViewById(android.R.id.content));
	 }

	 public void addSec(View v) {
		 textViewTimeSec = (TextView)findViewById(R.id.textViewTimeSec);
		 String tmp = textViewTimeSec.getText().toString();
		 int sec = Integer.parseInt(tmp) + 15;
		 if (sec > 59){
			 sec = sec - 60;
			 addMin(findViewById(android.R.id.content));
		 }
		 tmp = String.format("%02d", sec);
		 textViewTimeSec.setText(tmp);
	 }
	 
	 public void addMin(View v){
		 textViewTimeMin = (TextView)findViewById(R.id.textViewTimeMin);
		 String tmp = textViewTimeMin.getText().toString();
		 int min = Integer.parseInt(tmp) + 1;
		 if (min > 59){
			 min = min - 60;
			 addHour(findViewById(android.R.id.content));
		 }
		 tmp = String.format("%02d", min);
		 textViewTimeMin.setText(tmp);
	 }
	 
	 public void addHour(View v){
		 textViewTimeHour = (TextView)findViewById(R.id.textViewTimeHour);
		 String tmp = textViewTimeHour.getText().toString();
		 int hour = Integer.parseInt(tmp) + 1;
		 tmp = String.format("%02d", hour);
		 textViewTimeHour.setText(tmp);
	 }
	 
	 public void removeSec(View v) {
		 textViewTimeSec = (TextView)findViewById(R.id.textViewTimeSec);
		 textViewTimeMin = (TextView)findViewById(R.id.textViewTimeMin);
		 textViewTimeHour = (TextView)findViewById(R.id.textViewTimeHour);
		 int hour = Integer.parseInt(textViewTimeHour.getText().toString());
		 int min = Integer.parseInt(textViewTimeMin.getText().toString());
		 String tmp = textViewTimeSec.getText().toString();
		 int sec = Integer.parseInt(tmp);
		 		 
		 if (hour > 0 || min > 0){
			sec = sec - 15;
			if (sec < 0){
				 sec = sec + 60;
				 removeMin(findViewById(android.R.id.content));
			}
		 }
		 else if(sec > 0){
			 sec = sec - 15;
		 }
		tmp = String.format("%02d", sec);
		textViewTimeSec.setText(tmp);
	 }
	 
	 public void removeMin(View v){
		 textViewTimeMin = (TextView)findViewById(R.id.textViewTimeMin);
		 textViewTimeHour = (TextView)findViewById(R.id.textViewTimeHour);
		 int hour = Integer.parseInt(textViewTimeHour.getText().toString());
		 String tmp = textViewTimeMin.getText().toString();
		 int min = Integer.parseInt(tmp);
		 
		 if (hour > 0){
			 min = min - 1;
			 if (min < 0 ){
				 min = min + 60;
				 removeHour(findViewById(android.R.id.content)); 
			 }
		 }
		 else if(min > 0){
				 min = min -1;
			 }
			 tmp = String.format("%02d", min);
			 textViewTimeMin.setText(tmp);
	 }
	 
	 public void removeHour(View v){
		 textViewTimeHour = (TextView)findViewById(R.id.textViewTimeHour);
		 String tmp = textViewTimeHour.getText().toString();
		 int hour = Integer.parseInt(tmp);
		 if (hour > 0){
			 hour = hour - 1;
		 }
		 tmp = String.format("%02d", hour);
		 textViewTimeHour.setText(tmp);
	 }
	 
	 public void startTimer(View v){
		 textViewTimeSec = (TextView)findViewById(R.id.textViewTimeSec);
		 textViewTimeMin = (TextView)findViewById(R.id.textViewTimeMin);
		 textViewTimeHour = (TextView)findViewById(R.id.textViewTimeHour);
		 int sec = Integer.parseInt(textViewTimeSec.getText().toString());
		 int min = Integer.parseInt(textViewTimeMin.getText().toString());
		 int hour = Integer.parseInt(textViewTimeHour.getText().toString());
		 long time = ((((60 * hour) + min) * 60) + sec)*1000;
		 final CounterClass timer = new CounterClass(time,1000);  
		 timer.start();
		 timer_status = true;
		 btn_pause.setOnClickListener(new OnClickListener() {  
			 @Override  
			 public void onClick(View v) {  
				 timer.cancel();  
				 timer_status = false;
			 }  
		 });
	 }	 
}