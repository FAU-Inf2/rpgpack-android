package de.fau.cs.mad.gamekobold.templatestore;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.fau.cs.mad.gamekobold.R;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.SearchView;
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
    SearchView searchView;
    
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
	         

	         ArrayList<NameValuePair> nameValuePairs;
	         
	         switch(method) {
	         case "getTemplates":
	         		response =  client.getTemplates();
	         		break;
	         case "searchTemplates":
	        	 	nameValuePairs = apiParam.getParams();
	        	 	response = client.searchTemplates(nameValuePairs);
	        	 	break;
	         case "postTemplate":
	        	 nameValuePairs = apiParam.getParams();
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
	    		 
	    		 if(templates.length == 0) {
	    			 alertMessage("Sorry, es wurden keine Templates gefunden");
	    			 return;
	    		 }
	    		 
	    		 adapter.clear();
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
		
	    // Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    handleIntent(intent);
		
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
	
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

//	    // Checks the orientation of the screen
//	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//	        Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//	        Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//	    }
	}
	
	
	@Override
	protected void onNewIntent(Intent intent) {
	    setIntent(intent);
	    handleIntent(intent);
	}
	
	private void handleIntent(Intent intent) {
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	     // this.alertMessage("Your search query was: " + query);
			this.task = new ApiTask();
			ApiTaskParams apiParams = new ApiTaskParams();
	
			// for testing just geht templates again
			apiParams.setMethod("searchTemplates");
			ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
			httpParams.add(new BasicNameValuePair("worldname", query));
			apiParams.setParams(httpParams);
			this.task.execute(apiParams);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.template_store_main, menu);
		
	    // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    searchView = (SearchView) menu.findItem(R.id.search_template).getActionView();
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
	    searchView.setSubmitButtonEnabled(true);
	    
	    // Get the changes immediately.
	    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


	        @Override
	        public boolean onQueryTextChange(String newText) {
	        	// TODO maybe live search? 
	            return true;
	        }

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				return false;
			}
	    });
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_restore) {
    		task = new ApiTask();
    		ApiTaskParams apiParams = new ApiTaskParams();
    		apiParams.setMethod("getTemplates");
    		// show the original list, when search field is cleared
    		task.execute(apiParams);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	  @Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
		  layout_main.getForeground().setAlpha( 180); // dim
		  
//		  InputMethodManager inputManager = (InputMethodManager)
//                  getSystemService(Context.INPUT_METHOD_SERVICE); 
//
//		  inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                     InputMethodManager.HIDE_NOT_ALWAYS);
		  searchView.clearFocus();
		  
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
		    
		    /*
		     *  if(tmpl.hasImage()) {
				byte[] decodedString = Base64.decode(tmpl.getImage_data(), Base64.DEFAULT);
				Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); 
				image.setImageBitmap(decodedByte);
			}
		     */
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
