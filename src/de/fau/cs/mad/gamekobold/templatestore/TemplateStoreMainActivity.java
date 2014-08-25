package de.fau.cs.mad.gamekobold.templatestore;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.fau.cs.mad.gamekobold.R;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class TemplateStoreMainActivity extends ListActivity {
	private ArrayList<StoreTemplate> templates = new ArrayList<StoreTemplate>();
    private TemplateStoreArrayAdapter adapter;
    private ApiTask task;
    private TemplateStoreClient client = new TemplateStoreClient();
    private FrameLayout layout_main;
    PopupWindow popupWindow = null;
    ProgressDialog progress = null;
    
	  private class ApiTask extends AsyncTask<ApiTaskParams, Integer, ApiResponse> {
		 
		  protected void onPreExecute() {
			  super.onPreExecute();
			  	 progress = new ProgressDialog(TemplateStoreMainActivity.this);
		         progress.setTitle("Loading");
		         progress.setMessage("Wait while loading...");
		         progress.setCanceledOnTouchOutside(false);
		         progress.show();
			 // ProgressDialog.show(TemplateStoreMainActivity.this , "Test", "test");
		  }
		 
		 @Override
	     protected ApiResponse doInBackground(ApiTaskParams... params) {
	         ApiTaskParams apiParam = params[0];
	         ApiResponse response = null;
	         
	         String method = apiParam.getMethod();
	         

	       //  ProgressDialog.show(TemplateStoreMainActivity.this , "Test", "test");

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

	     protected void onCancelled() {
	    	 //alertMessage("cancelled");
	     }
	     
	     
	     protected void onPostExecute(ApiResponse response) {
//	         showDialog("Downloaded " + result + " bytes");
	         // To dismiss the dialog
	         progress.dismiss();
	         
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
	    			// alertMessage(tmpl.toString());
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
		
        // To dismiss the dialog
        progress = new ProgressDialog(this);
		
		// frame layout used for dimming....
		layout_main = (FrameLayout) findViewById( R.id.template_main_layout);
		layout_main.getForeground().setAlpha( 0);
		
		this.templates = new ArrayList<StoreTemplate>();
		this.adapter = new TemplateStoreArrayAdapter(this, templates);
  
	    setListAdapter(adapter);
	       
	        
		this.task = new ApiTask();
		ApiTaskParams apiParams = new ApiTaskParams();
		/*
		apiParams.setMethod("postTemplate");
		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		httpParams.add(new BasicNameValuePair("json", "{}"));
		apiParams.setParams(httpParams);
		*/
		
		apiParams.setMethod("getTemplates");
		this.task.execute(apiParams);
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.template_store_main, menu);
		return true;
	}
	public void onStart() {
		super.onStart();		
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
		  layout_main.getForeground().setAlpha( 180); // dim
		  
		  StoreTemplate tmpl = (StoreTemplate) getListAdapter().getItem(position);
		 // Toast.makeText(this, "For now only description is displayed: " + tmpl.getDescription(), Toast.LENGTH_LONG).show();
	    
		    LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);  
		    
		    View popupView = layoutInflater.inflate(R.layout.popup_templatestore_details, null);  
		    popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);  
		    
		    popupWindow.setOutsideTouchable(false);
		    popupWindow.setTouchable(true);
		    
		    //this is needed to close on back button... 
		    // had to remove this because it conflicted with other settings (outside touchable e.g)
		    //popupWindow.setBackgroundDrawable(new ColorDrawable());
		    
		    /* close image on right corner */
		    ImageView imgDismiss = (ImageView)popupView.findViewById(R.id.dismissImg);
		    imgDismiss.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					layout_main.getForeground().setAlpha(0);
					popupWindow.dismiss();
					
				}
			});
		    
		    // populate popup with template values
		    TextView worldname = (TextView) popupView.findViewById(R.id.txt_popup_worldname);
		    TextView templatename = (TextView) popupView.findViewById(R.id.txt_popup_templatename);
		    TextView date_author = (TextView) popupView.findViewById(R.id.txt_popup_date_author);
		    TextView description = (TextView) popupView.findViewById(R.id.txt_popup_description);
		    ImageView image = (ImageView) popupView.findViewById(R.id.img_popup);
		    RatingBar bar = (RatingBar) popupView.findViewById(R.id.ratingbar_popup);
		    
		    worldname.setText(tmpl.getWorldname());
		    templatename.setText(tmpl.getName());;
		    description.setText(tmpl.getDescription());
		    date_author.setText( ((TextView) v.findViewById(R.id.tv_store_date_author)).getText());
		    
		    ImageView img = (ImageView) v.findViewById(R.id.templateStoreImg);
		    image.setImageDrawable(img.getDrawable());
		    
		    bar.setRating(tmpl.getRating());
		    
		    //popupWindow.showAsDropDown(l, 50, -30);
		    popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		    popupWindow.showAtLocation(l, Gravity.CENTER, 0, 0);
		    popupWindow.update();
		         
	  }
	  
	  @Override
	  public void onBackPressed() {
		  if(this.popupWindow != null) {
			  this.popupWindow.dismiss();
			  this.popupWindow = null;
		  }
		  
		  if(this.task.getStatus() == AsyncTask.Status.RUNNING) {
		      this.task.cancel(true);
		      this.client.cancel();
		      if(progress.isShowing()) {
		    	  progress.dismiss();
		      }
		  }
	      finish();
	  }
}
