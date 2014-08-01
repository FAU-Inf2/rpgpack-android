package de.fau.cs.mad.gamekobold.game;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.fau.cs.mad.gamekobold.game.TimerDialog;
import de.fau.cs.mad.gamekobold.game.Timer;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import android.os.CountDownTimer; 

public class TimerActivity extends Activity{
	
	TextView textViewTime;
	
	Boolean timer_status = false;
	private CountDownTimer timer;
	
	@Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_game_timer);
		 
		 textViewTime = (TextView)findViewById(R.id.textViewTime);
		 textViewTime.setText("00:00:00");
		 
		 textViewTime.setOnClickListener(new View.OnClickListener() {

		        @Override
		        public void onClick(View v) {
		        	setTimer(findViewById(android.R.id.content));
		        }
		    });
		 		 
	}
	
	 @Override
	 public void onSaveInstanceState(Bundle icicle) {
		 super.onSaveInstanceState(icicle);
		 icicle.putString("time", ((TextView)findViewById(R.id.textViewTime)).getText().toString());
		 icicle.putBoolean("status", timer_status);
	 }
	 
	 @Override
	 public void onRestoreInstanceState(Bundle icicle) {
		 textViewTime.setText(icicle.getString("time"));
		 if(icicle.getBoolean("status"))
			 startTimer(findViewById(android.R.id.content));
	 }

	public void setTimer(View v){
		TimerDialog mTimer = new TimerDialog(this, new TimerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(Timer view, int hour, int minute, int seconds) {
				((TextView)findViewById(R.id.textViewTime)).setText(String.format("%02d:%02d:%02d", hour, minute, seconds));
			}
		}, getTimeInt(textViewTime)[0], getTimeInt(textViewTime)[1], getTimeInt(textViewTime)[2]);
		mTimer.show();		
	}	
	 
	 public void addSec(View v) {
		 textViewTime = (TextView)findViewById(R.id.textViewTime);
		 int [] time = getTimeInt(textViewTime);
		 time[2] = time[2] + 15;
		 if (time[2] > 59){
			time[2] = time[2] - 60;
			int tmp = time[2];
			addMin(findViewById(android.R.id.content));
			time = getTimeInt(textViewTime);
			time[2] = tmp;
			 
		}
		setTime(time);
		if (timer_status){
			startTimer(findViewById(android.R.id.content));
		}
	 }
	 
	 public void addMin(View v){
		textViewTime = (TextView)findViewById(R.id.textViewTime);
		int [] time = getTimeInt(textViewTime);
		time[1] = time[1] + 1;
		if (time[1] > 59){
			time[1] = time[1] - 60;
			int tmp = time[1];
			addHour(findViewById(android.R.id.content));
			time = getTimeInt(textViewTime);
			time[1] = tmp;
		}
		setTime(time);
		if (timer_status){
			startTimer(findViewById(android.R.id.content));
		}
	 }
	 
	 public void addHour(View v){
		 textViewTime = (TextView)findViewById(R.id.textViewTime);
		 int [] time = getTimeInt(textViewTime);
		 
		 if (time[0] < 99){
			 time[0] = time[0] + 1;
			 setTime(time);
		 }
		 
		if (timer_status){
			startTimer(findViewById(android.R.id.content));
		}
	 }
	 
	 public void removeSec(View v) {
		 textViewTime = (TextView)findViewById(R.id.textViewTime);
		 int [] time = getTimeInt(textViewTime);
		 		 
		 if (time[0] > 0 || time[1] > 0){
			time[2] = time[2] - 15;
			if (time[2] < 0){
				time[2] = time[2] + 60;
				int tmp = time[2];
				removeMin(findViewById(android.R.id.content));
				time = getTimeInt(textViewTime);
				time[2] = tmp;
				 
			}
		 }
		 else if(time[2] > 0){
			 time[2] = time[2] - 15;
			 if (time[2] < 0){
				 time[2] = 0;
			 }
		 }
		 setTime(time);	
		
		if (timer_status){
			startTimer(findViewById(android.R.id.content));
		}
	 }
	 
	 public void removeMin(View v){		 
		textViewTime = (TextView)findViewById(R.id.textViewTime);
		int [] time = getTimeInt(textViewTime);
			
		if (time[0] > 0){
			time[1] = time[1] - 1;
			if (time[1] < 0 ){
				time[1] = time[1] + 60;
				int tmp = time[1];
				removeHour(findViewById(android.R.id.content)); 
				time = getTimeInt(textViewTime);
				time[1] = tmp;
			}
		}
		else if(time[1] > 0)
			time[1] = time[1] -1;
		
		setTime(time);	
		if (timer_status){
			startTimer(findViewById(android.R.id.content));
		}
		 
	 }
	 
	 public void removeHour(View v){
		 textViewTime = (TextView)findViewById(R.id.textViewTime);
		 int [] time = getTimeInt(textViewTime);
		 if (time[0] > 0)
			 time[0] = time[0] - 1;		 	
		 setTime(time);
		 if (timer_status)
			startTimer(findViewById(android.R.id.content));
	 }
	 
	 public void setTime(int[] time){
		 String tmp = String.format("%02d:%02d:%02d", time[0], time[1], time[2]);
		 textViewTime.setText(tmp);
	 }
	 	 
	 public void startTimer(View v){
		 long time = getTimems();
		 if (timer_status){
			 timer.cancel();
		 }
		 
		 timer = createTimer(time);  
		 timer.start();
		 timer_status = true;
	 }
	 
	 public void stopTimer(View v){
		 if (timer_status)
			 timer.cancel();
		 timer_status = false;
	 }
	 
	 public long getTimems(){
		 //returns setted time in ms
		 textViewTime = (TextView)findViewById(R.id.textViewTime);
		 int [] time = getTimeInt(textViewTime);
		 long timems = ((((60 * time[0]) + time[1]) * 60) + time[2])*1000;
		 return timems;
	 }
	 
	 public CountDownTimer createTimer(long time) {
		 timer = new CountDownTimer(time,1000){
			 public void onFinish() {  
				 //add some sound
				 Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

				 MediaPlayer mediaPlayer = new MediaPlayer();
				 
				 textViewTime.setText("00:00:00");
				 timer_status = false;
				 try {
					mediaPlayer.setDataSource(getApplicationContext(), defaultRingtoneUri);
					mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
					mediaPlayer.prepare();
			     
					mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp){
							mp.release();
						}
					});
			     
					mediaPlayer.start();
				 } catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
				 
			 }   
			 
			 public void onTick(long millisUntilFinished) {  
				 long millis = millisUntilFinished;  
				 String h = String.format("%02d", TimeUnit.MILLISECONDS.toHours(millis));
				 String m = String.format("%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
				 String s = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));      
                      
				 textViewTime.setText(h + ":" + m + ":" + s);          
			 }  
		 };
		 return timer;
	 }
	 
	 public int [] getTimeInt(TextView time){
		 int [] time_array = new int [3];
		 String time_string = textViewTime.getText().toString();
		 String [] tmp = time_string.split(":");
		 for (int i = 0; i<3; i++)
			 time_array[i] = Integer.parseInt(tmp[i]);
		 return time_array;
	 }
	 
}