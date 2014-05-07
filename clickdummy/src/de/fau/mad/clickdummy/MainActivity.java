package de.fau.mad.clickdummy;

import java.util.LinkedList;
import java.util.ListIterator;

import com.example.kobold.R;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity {
	
	 EditText textIn;
	 Button buttonAdd;
	 LinearLayout container;
	 LinkedList<View> added_views = new LinkedList<View>();
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d("PIEP","START MAIN");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textIn = (EditText)findViewById(R.id.textin);
        buttonAdd = (Button)findViewById(R.id.add);
        container = (LinearLayout)findViewById(R.id.container);
        ListIterator<View> iter = added_views.listIterator();
        if(added_views.size() != 0){
        	Log.d("PIEP","NICHT NULL");
        }
        while(iter.hasNext()){
        	Log.d("Hallooo", "Elements: " + added_views.size());
        	container.addView(iter.next());
        }
//        View aView[] = (View[]) added_views.toArray();
//        for(int i=0; i<aView.length; i++){
//        	container.addView(aView[i]);
//        }
//        

        buttonAdd.setOnClickListener(new OnClickListener(){

        	@Override
        	public void onClick(View arg0) {
        		LayoutInflater layoutInflater = 
        				(LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		final View addView = layoutInflater.inflate(R.layout.row, null);
        		TextView textOut = (TextView)addView.findViewById(R.id.textout);
        		textOut.setText(textIn.getText().toString());
        		Button buttonRemove = (Button)addView.findViewById(R.id.remove);
        		buttonRemove.setOnClickListener(new OnClickListener(){

        			@Override
        			public void onClick(View v) {
        				((LinearLayout)addView.getParent()).removeView(addView);
        			}});

        		added_views.add(addView);
        		container.addView(addView);
        	}});

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_main) {
        	Intent startNewActivityOpen = new Intent(MainActivity.this, MainActivity.class);
        	startActivityForResult(startNewActivityOpen, 0);
        }
        else if (id == R.id.action_showlists) {
        	Intent startNewActivityOpen = new Intent(MainActivity.this, ShowLists.class);
        	startActivityForResult(startNewActivityOpen, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
