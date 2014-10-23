package de.fau.cs.mad.rpgpack.jackson;

import java.io.File;

import de.fau.cs.mad.rpgpack.templatebrowser.Template;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 *
 *	AsyncTask to save changes made to the meta data of a Template.
 *	Pass the changed BrowserTemplate to the execute method.
 *	This class will take care of taking over the changes and saves it to the file system.
 */
public class TemplateChangesSaverTask extends AsyncTask<Template, Void, Boolean> {
	// TODO not realy needed if we write a method for saving directly to a file.
	private final Context appContext;

	public TemplateChangesSaverTask(Context context) {
		appContext = context;
	}


	@Override
	protected Boolean doInBackground(Template... params) {
		Template template = params[0];
		// check for filepath existence
		if(template.fileAbsolutePath == null) {
			return Boolean.FALSE;
		}
		// check for valid filepath
		if(template.fileAbsolutePath.isEmpty()) {
			return Boolean.FALSE;
		}
		// create File Object for template
		File templateFile = new File(template.fileAbsolutePath);
		// check if the file exists. if not -> fail.
		if(!templateFile.exists()) {
			return Boolean.FALSE;
		}
		// load template from file
		de.fau.cs.mad.rpgpack.jackson.Template jacksonTemplate = null;
		try {
			jacksonTemplate = JacksonInterface.loadTemplate(templateFile, false);
			if(jacksonTemplate != null) {
				// take over all changes
				jacksonTemplate.takeOverValues(template);
				// save template again.
				JacksonInterface.saveTemplate(jacksonTemplate, templateFile);				
			}
		}
		catch(Throwable e) {
			// if anything went wrong -> fail
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	@Override
	protected void onPostExecute(Boolean status) {
		if(status.booleanValue()) {
			Toast.makeText(appContext, "Took over changes.", Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(appContext, "FAILED to take over changes!", Toast.LENGTH_LONG).show();
		}
	}
}
