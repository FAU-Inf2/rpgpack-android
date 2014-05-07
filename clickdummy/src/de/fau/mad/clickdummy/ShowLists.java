package de.fau.mad.clickdummy;

import com.example.kobold.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ShowLists extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {         

		super.onCreate(savedInstanceState);    
		setContentView(R.layout.showlists);
		//rest of the code
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
			Intent startNewActivityOpen = new Intent(ShowLists.this, MainActivity.class);
			startActivityForResult(startNewActivityOpen, 0);
		}
		else if (id == R.id.action_showlists) {
			Intent startNewActivityOpen = new Intent(ShowLists.this, ShowLists.class);
			startActivityForResult(startNewActivityOpen, 0);
		}
		return super.onOptionsItemSelected(item);
	}

}
