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
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.jackson.Template;

public class CharacterExportTask extends AsyncTaskWithProgressDialog<File, Void, Boolean>{
	private File targetFile = null;
	private File characterFile = null;
	private Context context = null;

	public static CharacterExportTask getInstance(Context context) {
		CharacterExportTask task = new CharacterExportTask();
		task.context = context;
		return task;
	}

	@Override
	protected void onPreExecute() {
		// TODO STRING
		super.onPreExecute(context, context.getString(R.string.msg_please_wait), context.getString(R.string.msg_exporting_template));
	}

	@Override
	protected Boolean doInBackground(File... params) {
		// get files
		this.characterFile = params[0];
		this.targetFile = params[1];
		// 
		if(targetFile == null || characterFile == null) {
			return Boolean.FALSE;
		}
		try {
			// first load character
			CharacterSheet character = JacksonInterface.loadCharacterSheet(characterFile, false);
			if(!character.isIconBase64()) {
				// load icon
				Bitmap icon = BitmapFactory.decodeFile(character.getIconPath());
				if(icon != null) {
					// compress as png to byte buffer
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					icon.compress(Bitmap.CompressFormat.PNG, 100, baos);
					// encode it as base64
					String iconBase64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
					// set as icon path
					character.setIconPath(iconBase64);
					Log.d("TemplateExportTask", "encoded icon");
				}
			}
			// save character to file
			JacksonInterface.saveCharacterSheet(character, targetFile);
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
			Toast.makeText(context, context.getString(R.string.toast_exported_character), Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(context, R.string.toast_exported_character_failed, Toast.LENGTH_LONG).show();
		}
		super.onPostExecute(param);
	}
}