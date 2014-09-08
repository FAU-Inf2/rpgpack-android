package de.fau.cs.mad.gamekobold.jackson;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import de.fau.cs.mad.gamekobold.R;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class AutosaveHandler extends Handler {
	private final int autosaveInterval;
	private final Runnable runnable;
	private final AtomicBoolean running;
	private final Object theObject;
	private final Context myContext;

	public AutosaveHandler(Context context, Object objectToSave) {
		autosaveInterval = context.getResources().getInteger(R.integer.autosave_interval_in_ms);
		running = new AtomicBoolean(true);
		theObject = objectToSave;
		myContext = context;
		
		runnable = new Runnable() {
			@Override
			public void run() {
				Log.d("AUTOSAVE_HANDLER", "autosave!");
				if(theObject instanceof CharacterSheet) {
					final File characterFile = new File(((CharacterSheet)theObject).getFileAbsolutePath()); 
					try {
						JacksonInterface.saveCharacterSheet((CharacterSheet)theObject, characterFile);
						Toast.makeText(myContext, "Autosave...", Toast.LENGTH_SHORT).show();
					}
					catch(Throwable e) {
						e.printStackTrace();
					}
				}
				else if(theObject instanceof Template) {
					try {
						JacksonInterface.saveTemplate((Template)theObject, myContext, true);
						Toast.makeText(myContext, "Autosave...", Toast.LENGTH_SHORT).show();
					}
					catch(Throwable e) {
						e.printStackTrace();
					}					
				}
				// reschedule the autosave.
				if(running.get()) {
					postDelayed(runnable, autosaveInterval);
				}
			}
		};
	}
	
	public void start() {
		postDelayed(runnable, autosaveInterval);
	}
	
	public void stop() {
		running.set(false);
		removeCallbacks(runnable);
	}
}
