package de.fau.cs.mad.gamekobold.filebrowser;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import de.fau.cs.mad.gamekobold.AsyncTaskWithProgressDialog;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.jackson.Template;

public class TemplateExportTask extends AsyncTaskWithProgressDialog<File, Void, Boolean>{
	private File targetFile = null;
	private File templateFile = null;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(File... params) {
		// get files
		this.templateFile = params[0];
		this.targetFile = params[1];
		// 
		if(targetFile == null || templateFile == null) {
			return Boolean.FALSE;
		}
		// first load template
		try {
			Template template = JacksonInterface.loadTemplate(templateFile, false);
			if(!template.getIconPath().isEmpty()) {
				// load icon
				Bitmap icon = BitmapFactory.decodeFile(template.getIconPath());
				if(icon != null) {
					// compress as png to byte buffer
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					icon.compress(Bitmap.CompressFormat.PNG, 100, baos);
					// encode it as base64
					String iconBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
					// set as icon path
					template.setIconPath(iconBase64);
				}
			}
			// save template to file
			JacksonInterface.saveTemplate(template, targetFile);
		}
		catch(Exception e) {
			e.printStackTrace();
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	protected void onPostExecute(Boolean param) {
		if(param.booleanValue()) {
			
		}
		else {
			
		}
		super.onPostExecute(param);
	}
}
