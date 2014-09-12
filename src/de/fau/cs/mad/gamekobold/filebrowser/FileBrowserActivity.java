package de.fau.cs.mad.gamekobold.filebrowser;

import java.io.File;

import de.fau.cs.mad.gamekobold.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class FileBrowserActivity extends Activity implements IFileBrowserReceiver {
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_file_browser);

	        // Check that the activity is using the layout version with
	        // the fragment_container FrameLayout
	        if (findViewById(R.id.file_browser_frame_layout) != null) {

	            // However, if we're being restored from a previous state,
	            // then we don't need to do anything and should return or else
	            // we could end up with overlapping fragments.
	            if (savedInstanceState != null) {
	                return;
	            }

	            // Create a new Fragment to be placed in the activity layout
	            FileBrowser db = new FileBrowser();
	            db.setReceiver(this);
	            
	            // In case this activity was started with special instructions from an
	            // Intent, pass the Intent's extras to the fragment as arguments
	            Bundle extras = getIntent().getExtras();
//	            if(extras == null) {
//	            	extras = new Bundle();
//	            }
//	            extras.putString(FileBrowser.ARGUMENT_CURRENT_DIR_ABS_PATH, "/");
	            db.setArguments(extras);
	            
	            // Add the fragment to the 'fragment_container' FrameLayout
	            getFragmentManager().beginTransaction()
	                    .add(R.id.file_browser_frame_layout, db).commit();
	        }
	    }

	@Override
	public void onDirectoryPicked(File directory) {
		Log.d("CHOOOSEN DIR", directory.getAbsolutePath());
	}
}
