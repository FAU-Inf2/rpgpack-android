package de.fau.cs.mad.rpgpack.jackson;

import de.fau.cs.mad.rpgpack.R;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * This class handles the process of saving a template to the file system in an async way.
 */
public class TemplateSaverTask extends AsyncTask<Template, Void, Boolean>{
	private final Context appContext;
	private final boolean setLastEditedFlag;

	/**
	 * 
	 * @param appContext The Application context. Use for determining the template folder.
	 * @param setLastEditedFlag If set to true, the "last edited template" preference will be updated.
	 */
	public TemplateSaverTask(Context appContext, boolean setLastEditedFlag) {
		this.appContext = appContext;
		this.setLastEditedFlag = setLastEditedFlag;
	}
	
	@Override
	protected Boolean doInBackground(Template... params) {
		Template template = params[0];
		if(template == null) {
			return Boolean.FALSE;
		}
		try {
			JacksonInterface.saveTemplate(template, appContext, setLastEditedFlag);
		} catch (Throwable e) {
			e.printStackTrace();
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(!result) {
			Toast.makeText(appContext, appContext.getResources().getString(R.string.template_save_error), Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(appContext, appContext.getResources().getString(R.string.template_saved), Toast.LENGTH_LONG).show();
			Log.d("MainTemplateGenerator", "saved template async");
		}
	}
}
