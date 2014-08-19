package de.fau.cs.mad.gamekobold.templatestore;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.fau.cs.mad.gamekobold.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TemplateStoreMainActivity extends ListActivity {
	private ArrayList<StoreTemplate> templates = new ArrayList<StoreTemplate>();
    private TemplateStoreArrayAdapter adapter;
    
	 private class ApiTask extends AsyncTask<ApiTaskParams, Integer, ApiResponse> {
		 TemplateStoreClient client = new TemplateStoreClient();
		 
		 @Override
	     protected ApiResponse doInBackground(ApiTaskParams... params) {
	         ApiTaskParams apiParam = params[0];
	         ApiResponse response = null;
	         
	         String method = apiParam.getMethod();
	         
	         switch(method) {
	         case "getTemplates":
	         		response =  client.getTemplates();
	         		break;
	         case "postTemplate":
	        	 ArrayList<NameValuePair> nameValuePairs = apiParam.getParams();
	        	 response = client.postTemplate(nameValuePairs);
	        	 break;
	         default:
	        	 break;
	         }
	         return response;
	     }

	     protected void onProgressUpdate(Integer... progress) {
//	         setProgressPercent(progress[0]);
	     }

	     protected void onPostExecute(ApiResponse response) {
//	         showDialog("Downloaded " + result + " bytes");
	    	 
	    	 // make sure response is ok
	    	 if(response.resultCode == 200) {
	    		 ObjectMapper mapper = new ObjectMapper();
	    		 StoreTemplate[] templates = null;
	    		 try {
					templates = mapper.readValue(response.responseBody, StoreTemplate[].class);
				} catch(Exception e){
					Log.e("store", e.getMessage());
					alertMessage("There was an error processing the result - See log for details");
					return;					
				}
	    		 
	  	   		 for(StoreTemplate tmpl : templates) {
	    			 alertMessage(tmpl.toString());
	    			 adapter.add(tmpl);
	    		 }
	  	   		 
	 	  	 } else {
	    		 alertMessage(response.toString());
	    	 }
	    	 
	     }
	 }

	public void alertMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Add the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		builder.setMessage(message);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_template_store_main);
		setTitle(R.string.template_store_title);
		
		this.templates = new ArrayList<StoreTemplate>();
		this.adapter = new TemplateStoreArrayAdapter(this, templates);
  
	    setListAdapter(adapter);
	       
	        
		ApiTask task = new ApiTask();
		ApiTaskParams apiParams = new ApiTaskParams();
		/*
		apiParams.setMethod("postTemplate");
		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		httpParams.add(new BasicNameValuePair("json", "{}"));
		apiParams.setParams(httpParams);
		*/
		
		apiParams.setMethod("getTemplates");
		task.execute(apiParams);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.template_store_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	  @Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
	    String item = (String) getListAdapter().getItem(position);
	    Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	  }
}
