package de.fau.cs.mad.gamekobold.templatestore;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ClientConnectionManager;

import android.util.Log;

public class TemplateStoreClient extends HttpClient {

	//private final String apiUrl = "http://192.168.2.110:8080/templates";
	private final String apiUrl = "http://kobold-devel.cloudapp.net:8080/templates";
	
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
	 * gets all Templates from Store
	 * @return
	 */
	public ApiResponse getTemplates() {
		try {
			this.get(this.apiUrl);
		} catch (Exception e) {
			Log.e("store clients", e.getMessage());
		}
		
		return new ApiResponse(this.statusCode, this.reasonPhrase, this.responseBody);
	}
	
	public ApiResponse searchTemplates(ArrayList<NameValuePair> nameValuePairs) {
		try {
			this.get(this.apiUrl+"/search/custom", nameValuePairs);
		} catch (Exception e) {
			Log.e("store clients", e.getMessage());
		}
		
		return new ApiResponse(this.statusCode, this.reasonPhrase, this.responseBody);
	}
	


	public String getApiUrl() {
		return apiUrl;
	}
	
	public void cancel() {
		super.cancel();
	}
}
