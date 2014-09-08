package de.fau.cs.mad.gamekobold.templatestore;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class TemplateStoreClient extends HttpClient {

	//private final String apiUrl = "http://192.168.2.110:8080/templates";
	private final String apiUrl = "http://kobold-devel.cloudapp.net:8080/templates";
	private int currentPage=1;
	private String currentMethod = null;
	private ArrayList<NameValuePair> currentNameValuePairs;
	private boolean pageAdded = false;
	public TemplateStoreClient() {
		
	}
	

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
	 * @return
	 */
	public ApiResponse getTemplates() {
		this.currentPage = 1;
		return this.getTemplates(false);
	}
	/**
	 * gets all Templates from Store
	 * @return
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
	 * @return
	 */
	public ApiResponse searchTemplates(ArrayList<NameValuePair> nameValuePairs) {
		this.currentPage = 1;
		this.currentNameValuePairs = nameValuePairs;
		return this.searchTemplates(nameValuePairs, false);
	}
	
	
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
		pageAdded = false;
		return this.searchByTag(nameValuePairs, false);
	}
	
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

	
	public String getApiUrl() {
		return apiUrl;
	}
	
	public void cancel() {
		super.cancel();
	}
	
	public ApiResponse loadMore() {
		this.currentPage++;
		switch (this.currentMethod) {
		case "getTemplates":
			return getTemplates(true);
		case "searchByTag" :
			return this.searchByTag(currentNameValuePairs, true);
		case "searchTemplates" :
			return this.searchTemplates(currentNameValuePairs, true);
		}
		return getTemplates();
		
	}
	
	public void setCurrentPage(int page) {
		this.currentPage = page;
	}

}
