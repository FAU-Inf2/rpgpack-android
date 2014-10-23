package de.fau.cs.mad.rpgpack.jackson;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import de.fau.cs.mad.rpgpack.R;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * Handler for periodically auto saving a {@link Template} or {@link CharacterSheet}.
 * @author Benjamin
 *
 */
public class AutosaveHandler extends Handler {
	private final int autosaveInterval;
	private final Runnable runnable;
	private final AtomicBoolean running;
	private final Object theObject;
	private final Context myContext;
	private final String toastMessage;

	/**
	 * 
	 * @param context A valid context for accessing the resources.
	 * @param objectToSave The {@link Template} or {@link CharacterSheet}.
	 * The saving process is started when creating the {@link AutosaveHandler}.
	 */
	public AutosaveHandler(Context context, Object objectToSave) {
		autosaveInterval = context.getResources().getInteger(R.integer.autosave_interval_in_ms);
		running = new AtomicBoolean(true);
		theObject = objectToSave;
		myContext = context;
		toastMessage = context.getResources().getString(R.string.autosave_toast_text);
		
		runnable = new Runnable() {
			@Override
			public void run() {
				Log.d("AUTOSAVE_HANDLER", "autosave!");
				if(theObject instanceof CharacterSheet) {
					final File characterFile = new File(((CharacterSheet)theObject).getFileAbsolutePath()); 
					try {
						JacksonInterface.saveCharacterSheet((CharacterSheet)theObject, characterFile);
						Toast.makeText(myContext, toastMessage, Toast.LENGTH_SHORT).show();
					}
					catch(Throwable e) {
						e.printStackTrace();
					}
				}
				else if(theObject instanceof Template) {
					try {
						JacksonInterface.saveTemplate((Template)theObject, myContext, true);
						Toast.makeText(myContext, toastMessage, Toast.LENGTH_SHORT).show();
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
	
	/**
	 * Starts the auto saving.
	 */
	public void start() {
		if(!running.get()) {
			running.set(true);
			postDelayed(runnable, autosaveInterval);
		}
	}
	
	/**
	 * Stops the auto saving.
	 */
	public void stop() {
		running.set(false);
		removeCallbacks(runnable);
	}
}
