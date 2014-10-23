package de.fau.cs.mad.rpgpack.toolbox;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TimePicker;

import de.fau.cs.mad.rpgpack.R;
import de.fau.cs.mad.rpgpack.toolbox.Timer.OnTimeChangedListener;

public class TimerDialog extends AlertDialog implements OnClickListener, OnTimeChangedListener{

    public interface OnTimeSetListener {
        void onTimeSet(Timer view, int hour, int minute, int seconds);
    }

    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String SECONDS = "seconds";
    
    private final Timer mTimer;
    private final OnTimeSetListener mCallback;
    private final Calendar mCalendar;
    private final java.text.DateFormat mDateFormat;
        
    int mInitialHour;
    int mInitialMinute;
    int mInitialSeconds;    

    public TimerDialog(Context context,
            OnTimeSetListener callBack,
            int hour, int minute, int seconds) {
    	
        this(context, 0,
                callBack, hour, minute, seconds);
    }

    public TimerDialog(Context context,
            int theme,
            OnTimeSetListener callBack,
            int hour, int minute, int seconds) {
        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCallback = callBack;
        mInitialHour = hour;
        mInitialMinute = minute;
        mInitialSeconds = seconds;

        mDateFormat = DateFormat.getTimeFormat(context);
        mCalendar = Calendar.getInstance();
        setTitle(R.string.set_time);
        
        setButton(BUTTON_POSITIVE, context.getText(R.string.set_time), this);
        setButton(BUTTON_NEGATIVE, context.getText(R.string.cancel), (OnClickListener) null);
        //setIcon(android.R.drawable.ic_dialog_time);
        
        LayoutInflater inflater = 
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_game_toolbox_timer, null);
        setView(view);
        mTimer = (Timer) view.findViewById(R.id.timer);

        // initialize state
        mTimer.setCurrentHour(mInitialHour);
        mTimer.setCurrentMinute(mInitialMinute);
        mTimer.setCurrentSecond(mInitialSeconds);
        mTimer.setOnTimeChangedListener(this);      
    }
    
    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            mTimer.clearFocus();
            mCallback.onTimeSet(mTimer, mTimer.getCurrentHour(), 
                    mTimer.getCurrentMinute(), mTimer.getCurrentSeconds());
        }
    }

    public void onTimeChanged(TimePicker view, int hour, int minute, int seconds) {
        updateTitle(hour, minute, seconds);
    }
    
    public void updateTime(int hour, int minutOfHour, int seconds) {
        mTimer.setCurrentHour(hour);
        mTimer.setCurrentMinute(minutOfHour);
        mTimer.setCurrentSecond(seconds);
    }
    
    private void updateTitle(int hour, int minute, int seconds) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, seconds);
        setTitle(mDateFormat.format(mCalendar.getTime()) + ":" + String.format("%02d" , seconds));
    }
    
    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(HOUR, mTimer.getCurrentHour());
        state.putInt(MINUTE, mTimer.getCurrentMinute());
        state.putInt(SECONDS, mTimer.getCurrentSeconds());
        return state;
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int hour = savedInstanceState.getInt(HOUR);
        int minute = savedInstanceState.getInt(MINUTE);
        int seconds = savedInstanceState.getInt(SECONDS);
        mTimer.setCurrentHour(hour);
        mTimer.setCurrentMinute(minute);
        mTimer.setCurrentSecond(seconds);
        mTimer.setOnTimeChangedListener(this);
        updateTitle(hour, minute, seconds);
    }

	@Override
	public void onTimeChanged(Timer view, int hour, int minute, int seconds) {
		// TODO Auto-generated method stub
		
	}
    
   
}
