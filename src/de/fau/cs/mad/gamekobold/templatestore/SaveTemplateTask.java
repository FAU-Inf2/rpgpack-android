package de.fau.cs.mad.gamekobold.templatestore;

import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.jackson.Template;
import android.content.Context;
import android.os.AsyncTask;

public class SaveTemplateTask extends AsyncTask<Void, Integer, ApiResponse> {
	
	private TemplateStoreMainActivity activity;
	private StoreTemplate tmpl;
	
	public SaveTemplateTask(Context context, StoreTemplate tmpl) {
		this.activity = (TemplateStoreMainActivity) context;
		this.tmpl = tmpl;
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
		return client.getCharsheet(tmpl.getId());
	}
	
	@Override
	protected void onPostExecute(ApiResponse response) {
		activity.progress.dismiss();
		activity.alertMessage(response.responseBody);
	}

}
