package de.fau.cs.mad.gamekobold.templatestore;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class TemplateStoreClient extends HttpClient {

	//private final String apiUrl = "http://192.168.2.110:8080/templates";
	private final String apiUrl = "http://kobold-devel.cloudapp.net:8080/templates";
	private int currentPage=1;
	private String currentMethod = null;
	private ArrayList<NameValuePair> currentNameValuePairs;
	
	/** default constructor **/
	public TemplateStoreClient() {
		
	}
	
	/**
	 * 
	 * @param nameValuePairs - HTTP URL Parameters
	 * @return request information in form of an ApiResponse object
	 */
	public ApiResponse postTemplate(ArrayList<NameValuePair> nameValuePairs){
		try {
			this.post(this.apiUrl, nameValuePairs, true);
		} catch(Exception e) {
			Log.e("store client", e.getMessage());
			return new ApiResponse(1000, "Exception in postTemplate ArrayList<NameValuePair> nameValuePairs" , e.getMessage());
		}
		
		return new ApiResponse(this.statusCode, this.reasonPhrase, this.responseBody);
	}
	
	/**
	 * method overload
	 * 
	 * @return - Templates in form of an ApiResponse object
	 */
	public ApiResponse getTemplates() {
		this.currentPage = 1;
		return this.getTemplates(false);
	}
	
	/**
	 * 
	 * @param more - indicates that pagination is used 
	 * @return - Templates in form of an ApiResponse object
	 */
	public ApiResponse getTemplates(boolean more) {
		this.currentMethod = "getTemplates";
		
		try {
			this.get(this.apiUrl+"?page="+currentPage);
		} catch (Exception e) {
			Log.e("store clients", e.getMessage());
		}
		
		return new ApiResponse(this.statusCode, this.reasonPhrase, this.responseBody);
	}
	
	/**
	 * method overload
	 * @param nameValuePairs
	 * @return templates in form of an ApiResponse object
	 */
	public ApiResponse searchTemplates(ArrayList<NameValuePair> nameValuePairs) {
		this.currentPage = 1;
		this.currentNameValuePairs = nameValuePairs;
		return this.searchTemplates(nameValuePairs, false);
	}
	
	/**
	 * Searches store by custom search criteria
	 * 
	 * @see API Docs of template store
	 * @param nameValuePairs - http get parameter used for searching
	 * @param more - indicates pagination
	 * @return found templates in form of an ApiResponse object
	 */
	public ApiResponse searchTemplates(ArrayList<NameValuePair> nameValuePairs, boolean more) {
		this.currentMethod = "searchTemplates";
		if(more) {
			nameValuePairs.add(new BasicNameValuePair("page", Integer.valueOf(this.currentPage).toString()));
		}
		
		try {
			this.get(this.apiUrl+"/search/custom", nameValuePairs);
		} catch (Exception e) {
			Log.e("store clients", e.getMessage());
		}
		if(more) {
			nameValuePairs.remove(nameValuePairs.size()-1);
		}
		return new ApiResponse(this.statusCode, this.reasonPhrase, this.responseBody);
	}
	
	/** 
	 * method overload
	 * @param nameValuePairs
	 * @return
	 */
	public ApiResponse searchByTag(ArrayList<NameValuePair> nameValuePairs) {
		this.currentNameValuePairs = nameValuePairs;
		this.currentPage = 1;
		return this.searchByTag(nameValuePairs, false);
	}
	
	/**
	 * searches templates by tag
	 * @param nameValuePairs - parameters that are appended to URL as get parameter
	 * @param more - indicates that more content should be loaded in form of pagination
	 * @return
	 */
	public ApiResponse searchByTag(ArrayList<NameValuePair> nameValuePairs, boolean more) {
		this.currentMethod = "searchByTag";
		if(more) {
			nameValuePairs.add(new BasicNameValuePair("page", Integer.valueOf(this.currentPage).toString()));
		}
		
		try {
			this.get(this.apiUrl+"/search/tag", nameValuePairs);
		} catch (Exception e) {
			Log.e("store clients", e.getMessage());
		}
		if(more) {
			nameValuePairs.remove(nameValuePairs.size()-1);
		}
		return new ApiResponse(this.statusCode, this.reasonPhrase, this.responseBody);
	}
	
	/**
	 * get best rated templates from store
	 * @return best rated templates in form of an ApiResponseObject
	 */
	public ApiResponse bestRated() {
		// TODO Auto-generated method stub
		this.currentMethod = "bestRated";
		try {
			this.get(this.apiUrl+"/search/bestRated?page="+currentPage);
		} catch (Exception e) {
			Log.e("store clients", e.getMessage());
		}
		return new ApiResponse(this.statusCode, this.reasonPhrase, this.responseBody);
	}

	/**
	 * gets templates ordered by date descending
	 * @return templates in form of ApiResponse Object
	 */
	public ApiResponse latest() {
		// TODO Auto-generated method stub
		this.currentMethod = "latest";
		try {
			this.get(this.apiUrl+"/search/latest?page="+currentPage);
		} catch (Exception e) {
			Log.e("store clients", e.getMessage());
		}
		return new ApiResponse(this.statusCode, this.reasonPhrase, this.responseBody);		
	}
	
	/**
	 * 
	 * @return the base URL of the API end point
	 */
	public String getApiUrl() {
		return apiUrl;
	}
	
	/**
	 * cancels current request
	 * @return void
	 */
	public void cancel() {
		super.cancel();
	}
	
	/**
	 * load more templates when pagination is needed
	 * @return Templates in form an ApiResponse
	 */
	public ApiResponse loadMore() {
		this.currentPage++;
		switch (this.currentMethod) {
		case "getTemplates":
			return getTemplates(true);
		case "searchByTag" :
			return this.searchByTag(currentNameValuePairs, true);
		case "searchTemplates" :
			return this.searchTemplates(currentNameValuePairs, true);
		case "bestRated":
			return this.bestRated();
		case "latest":
			return this.latest();
		}
		return getTemplates();
		
	}
	
	/**
	 * set current page for pagination
	 * @param page current page
	 */
	public void setCurrentPage(int page) {
		this.currentPage = page;
	}

	public ApiResponse getCharsheet(int id) {
		// TODO Auto-generated method stub
		this.currentMethod = "latest";
		try {
			this.get(this.apiUrl+"/charsheet/"+id);
		} catch (Exception e) {
			Log.e("store clients", e.getMessage());
		}
		return new ApiResponse(this.statusCode, this.reasonPhrase, this.responseBody);
	}

}
