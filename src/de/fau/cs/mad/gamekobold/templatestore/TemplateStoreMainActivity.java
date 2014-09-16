package de.fau.cs.mad.gamekobold.templatestore;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhaarman.supertooltips.*;
import com.nhaarman.supertooltips.ToolTipView.OnToolTipViewClickedListener;

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
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
    private boolean isLoading = false;
	private boolean moreDataAvailable = true;
	private boolean initialLoad = true;
    ListView listView;
    View footer;
	private ListView sidebar;
	private TemplateStoreSidebarArrayAdapter sidebarAdapter;
    
      private class ScrollListener implements OnScrollListener {
			private int currentScrollState;
			private int lastItem;
			private int totalItemCount;


			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	    	    this.lastItem = firstVisibleItem + visibleItemCount;
	    	    this.totalItemCount = totalItemCount;
	    	}

	    	public void onScrollStateChanged(AbsListView view, int scrollState) {
	    	    this.currentScrollState = scrollState;
	    	    this.isScrollCompleted();
	    	 }

	    	private void isScrollCompleted() {
	    	    if (this.lastItem == this.totalItemCount && this.currentScrollState == SCROLL_STATE_IDLE) {
	    	    	
	    	        if(!isLoading && moreDataAvailable ){
	    	        	// alertMessage("loading...");
	    	        	 getListView().addFooterView(footer);
	    	        	 // scroll down to see loading animation
	    	        	 getListView().setSelection(lastItem);
	    	             isLoading = true;
	    	           
	    	           /* TODO just introduced delay to see the loading animation working... */
	  	    		   Handler handler = new Handler(); 
		    		    handler.postDelayed(new Runnable() { 
		    		         public void run() { 
			    	             ApiTaskParams apiParams = new ApiTaskParams();
			    	             apiParams.setMethod("loadMore");
			    	             ApiTask task = new ApiTask();
		    		        	 task.execute(apiParams);
		    		         } 
		    		    }, 1000);
	    	             //task.execute(apiParams);
	    	        }
	    	    }
	    	}
	    	
	    }
      
	  private class ApiTask extends AsyncTask<ApiTaskParams, Integer, ApiResponse> {
		 
		  private String method; 
		  
		  protected void onPreExecute() {
			  super.onPreExecute();
			  if(initialLoad) {
			  	 progress = new ProgressDialog(TemplateStoreMainActivity.this);
		         progress.setTitle(getResources().getString(R.string.loading));
		         progress.setMessage(getResources().getString(R.string.loading_wait));
		         progress.setCanceledOnTouchOutside(false);
		         progress.show();
		         initialLoad = false;
			  }
		  }
		 
		 @Override
	     protected ApiResponse doInBackground(ApiTaskParams... params) {
	         ApiTaskParams apiParam = params[0];
	         ApiResponse response = null;
	         
	         method = apiParam.getMethod();
	         

	         ArrayList<NameValuePair> nameValuePairs;
	         
	         if(method != "loadMore") {
	        	 client.setCurrentPage(1);
	         }
	         
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
	         case "searchByTag":
	        	 nameValuePairs = apiParam.getParams();
	        	 response = client.searchByTag(nameValuePairs);
	        	 break;
	         case "bestRated":
	        	 response = client.bestRated();
	        	 break;
	         case "loadMore":
	        	 response = client.loadMore();
	        	 break;
	         case "latest":
	        	 response = client.latest();
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
	    	 if(progress != null && progress.isShowing()) {
	    		 progress.dismiss();
	    	 }
	         isLoading = false;
	    	 // make sure response is ok
	    	 if(response.resultCode == 200) {
	    		 ObjectMapper mapper = new ObjectMapper();
	    		 StoreTemplate[] templates = null;
	    		 try {
					templates = mapper.readValue(response.responseBody, StoreTemplate[].class);
				} catch(Exception e){
					Log.e("store", e.getMessage());
					alertMessage(getResources().getString(R.string.error_occured));
					return;					
				}

	    		 getListView().removeFooterView(footer);

	    		 
	    		 if(templates.length == 0) {
	    			 if(method != "loadMore") {
	    				 alertMessage(getResources().getString(R.string.no_templates_found));
	    			 } else {
	    				 moreDataAvailable = false;
	    			 }
	    			 return;
	    		 }
	    		 
	    		 // when loading more we want to append results to list
	    		 if(method != "loadMore") {
	    			 adapter.clear();
	    		 }
	  	   		 for(StoreTemplate tmpl : templates) {
	    			// alertMessage(tmpl.toString());
	    			adapter.add(tmpl);
	    		 }
	  	   		 // show first row
	  	   		 if(method != "loadMore") {
	  	   			 getListView().setSelection(0);
	  	   		 }
	  	   		 
	  	   		 // add color to clicked item again
	  	   		  //sidebar.getChildAt(clickedSidebarPos).setBackgroundResource(R.drawable.store_list_selector_pressed);
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
		
		if(Helper.getSizeName(this) == "xlarge") {
			this.initSidebar();
		}
		
		this.templates = new ArrayList<StoreTemplate>();
		this.adapter = new TemplateStoreArrayAdapter(this, templates);
  
		// set footer
		this.footer = getLayoutInflater().inflate(R.layout.template_store_loading_footer, null);
		listView = getListView();
		listView.addFooterView(footer);
	    setListAdapter(adapter);
	    listView.removeFooterView(footer);
	    getListView().setOnScrollListener(new ScrollListener());
  
		this.task = new ApiTask();
		ApiTaskParams apiParams = new ApiTaskParams();
		
		apiParams.setMethod("bestRated");
		this.task.execute(apiParams);
		
	}
	

	private void initSidebar() {
		Resources res = getResources();
		final String[] texts = { res.getString(R.string.tag_best_rated), res.getString(R.string.tag_newest), 
				res.getString(R.string.tag_recommended), "Dungeon World",
				"D & D", "Fantasy", "Horror", getResources().getString(R.string.tag_future) };
		Integer[] images = { R.drawable.best, R.drawable.newest,
				R.drawable.best, R.drawable.dragon1,
				R.drawable.dragon2, R.drawable.fantasy, R.drawable.horror, R.drawable.future };
		
		sidebarAdapter = new TemplateStoreSidebarArrayAdapter(this, texts, images);
		
		sidebar = (ListView) findViewById(R.id.listView1);
		sidebar.setAdapter(sidebarAdapter);
		sidebar.setChoiceMode(1);
		sidebar.setItemChecked(1,true);
		sidebar.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				restoreListStatus();
				searchView.clearFocus();
				//sidebar.getChildAt(clickedSidebarPos).setBackgroundColor(Color.TRANSPARENT);
				sidebarAdapter.setActive(position);
				//view.setBackgroundResource(R.drawable.store_list_selector_pressed);
				sidebarAdapter.notifyDataSetChanged();
				switch(texts[position]) {
				case "Fantasy":
				case "Horror":
				case "Future":
				case "Zukunft":
				case "Dungeon World":
				case "D & D":
				case "Recommended":
				case "Empfohlen":
					loadTag(texts[position]);
					break;
				case "Best Rated" :
				case "Bestbewertet":
					loadBestRated();
					break;
				case "Newest":
				case "Neu":
					loadLatest();
					break;
				default:
					alertMessage("not Implemented yet");
				}
			}
			
		});
		
	}

	private void loadLatest() {
		  task = new ApiTask();
		  ApiTaskParams apiParams = new ApiTaskParams();
		  apiParams.setMethod("latest");
		  task.execute(apiParams);	
	}
	
	private void loadBestRated() {
		 task = new ApiTask();
		 ApiTaskParams apiParams = new ApiTaskParams();
		 apiParams.setMethod("bestRated");
		 task.execute(apiParams);
	}
	
	private void loadTag(String tag) {
		if(tag.equals("Empfohlen") || tag.equals("Recommended")) tag = "feature";
		
		task = new ApiTask();
		ApiTaskParams apiParams = new ApiTaskParams();
		ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		httpParams.add(new BasicNameValuePair("tagname", tag));
		apiParams.setParams(httpParams);
		apiParams.setMethod("searchByTag");
		task.execute(apiParams);	
	}
	
	public void onResume() {
		super.onResume();
		//sidebar.getChildAt(clickedSidebarPos).setBackgroundResource(R.drawable.store_list_selector_pressed);
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
	
			// for testing just get templates again
			apiParams.setMethod("searchTemplates");
			ArrayList<NameValuePair> httpParams = new ArrayList<NameValuePair>();
			httpParams.add(new BasicNameValuePair("worldname", query));
			apiParams.setParams(httpParams);
			this.task.execute(apiParams);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 final MenuItem searchMenuItem = menu.findItem(R.id.search_template);
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
	    
	    searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
	        @Override
	        public void onFocusChange(View view, boolean queryTextFocused) {
	            if(!queryTextFocused) {
	                searchView.setQuery("", false);
	            }
	        }
	    });
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		searchView.clearFocus();
		ApiTaskParams apiParams = new ApiTaskParams();
		boolean is_task = false;
		switch(id) {
			case R.id.action_restore :
				this.restoreListStatus();
	    		apiParams.setMethod("getTemplates");
	    		is_task=true;
				break;			
			case R.id.tag_fantasy:
			case R.id.tag_horror:
			case R.id.tag_future:
			case R.id.tag_d_and_d:
			case R.id.tag_dungeon_world:
				this.restoreListStatus();
				this.loadTag((String)item.getTitle());
				break;
			case R.id.tag_best_rated:
				this.restoreListStatus();
				this.loadBestRated();
				break;
			case R.id.tag_recommended:
				this.restoreListStatus();
				loadTag("feature");
				break;
			case R.id.tag_newest:
				this.restoreListStatus();
				loadLatest();
				break;
			default:
				is_task = false;
				break;
		}
		
		if(is_task) {
			task = new ApiTask();
			task.execute(apiParams);
		}
		
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	  protected void onListItemClick(ListView l, View v, int position, long id) {
		  layout_main.getForeground().setAlpha( 180); // dim
		  
		  searchView.clearFocus();
		  
		  final StoreTemplate tmpl = (StoreTemplate) getListAdapter().getItem(position);
	
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
		    Button storeButton = (Button) popupView.findViewById(R.id.buttonStore);
		    
		    storeButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SaveTemplateTask sTask = new SaveTemplateTask(TemplateStoreMainActivity.this, tmpl);
					// load charsheet and save template to device
					sTask.execute();
				}
		    	
		    });
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
	  
	  private void restoreListStatus() {
		  this.initialLoad = true;
		  this.isLoading = false;
		  this.moreDataAvailable = true;
	  }
}
