package de.fau.cs.mad.gamekobold.templatestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.jackson.Template;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class SaveTemplateTask extends AsyncTask<Void, Integer, ApiResponse> {
	
	private TemplateStoreMainActivity activity;
	private StoreTemplate storeTmpl;
	private Context context; 
	
	public SaveTemplateTask(Context context, StoreTemplate tmpl) {
		this.activity = (TemplateStoreMainActivity) context;
		this.context = context;
		this.storeTmpl = tmpl;
	}
	
	protected void onPreExecute() {
         activity.progress.setTitle("Loading");
         activity.progress.setMessage("Wait while loading...");
         activity.progress.setCanceledOnTouchOutside(false);
         activity.progress.show();
	}
	
	@Override
	protected ApiResponse doInBackground(Void... params) {
		// TODO Auto-generated method stub
		TemplateStoreClient client = new TemplateStoreClient();
		return client.getCharsheet(storeTmpl.getId());
	}
	
	@Override
	protected void onPostExecute(ApiResponse response) {
		Template tmpl = new Template();
		tmpl.setAuthor(storeTmpl.getAuthor());
		tmpl.setGameName(storeTmpl.getWorldname());
		tmpl.setTemplateName(storeTmpl.getName());
		tmpl.setDate(storeTmpl.getDate());
		tmpl.setDescription(storeTmpl.getDescription());
		
		ObjectMapper mapper = new ObjectMapper();
		CharacterSheet charsheet = new CharacterSheet();
		activity.alertMessage(response.responseBody);
		try {
			charsheet = mapper.readValue(response.responseBody, CharacterSheet.class);
		} catch (IOException e) {
			Log.e("store", "invalid charsheet");
			e.printStackTrace();
		}
		
		try {
			System.out.println(charsheet.toJSON(true));
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		tmpl.setCharacterSheet(charsheet);
		
		
		// TODO fallback to internal storage
		if(Helper.isExternalStorageWritable() && storeTmpl.hasImage()) {
			// files in getExternalStorageDirectory() are deleted, when uninstalling the app
		    String root = Environment.getExternalStorageDirectory().toString();
		    String location = root + "/store_images";
		    File myDir = new File(location);    
		    myDir.mkdirs();
		    Random generator = new Random();
		    int n = 100000;
		    n = generator.nextInt(n);
		    String fname = "Image-"+ n +storeTmpl.getDate()+".jpg";
		    File file = new File (myDir, fname);
		    if (file.exists ()) file.delete (); 
		    try {
		           FileOutputStream out = new FileOutputStream(file);
		           storeTmpl.getBm().compress(Bitmap.CompressFormat.JPEG, 90, out);
		           out.flush();
		           out.close();
	
		    } catch (Exception e) {
		           e.printStackTrace();
		    }
			tmpl.setIconPath(location+"/"+fname);
		}
		
		try {
			JacksonInterface.saveTemplate(tmpl, context, false);
			//activity.alertMessage(tmpl.getFileAbsPath()+tmpl.getFileName());
		} catch (JsonGenerationException e) {
			Log.e("store", "JsonGenerationException");
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			Log.e("store", "JsonMappingException");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("store", "IOException");
			e.printStackTrace();
		}
		activity.progress.dismiss();
		//activity.alertMessage(response.responseBody);
	}

}
