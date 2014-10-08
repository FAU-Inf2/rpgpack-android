package de.fau.cs.mad.gamekobold.filebrowser;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.AsyncTaskWithProgressDialog;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.jackson.Template;

public class TemplateExportTask extends AsyncTaskWithProgressDialog<File, Void, Boolean>{
	private File targetFile = null;
	private File templateFile = null;
	private Context context = null;

	public static TemplateExportTask getInstance(Context context) {
		TemplateExportTask task = new TemplateExportTask();
		task.context = context;
		return task;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute(context, context.getString(R.string.msg_please_wait), context.getString(R.string.msg_exporting_template));
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
			if(!template.isIconBase64()) {
				Log.d("TemplateExportTask", "icon is not base64");
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
					Log.d("TemplateExportTask", "encoded icon");
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
			final String msg = String.format(context.getString(R.string.toast_exported_template), targetFile.getName());
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(context, R.string.toast_exported_template_failed, Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(param);
	}
}
