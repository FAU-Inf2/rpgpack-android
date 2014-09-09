package de.fau.cs.mad.gamekobold.templatestore;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class HttpClient {

	private InputStream content;
    protected  DefaultHttpClient client;
	
	protected int statusCode;
	protected String reasonPhrase;
	protected String responseBody;
	
	private HttpGet httpGet;
	private HttpPost httpPost;
	private HttpPut httpPut;
	
	public HttpClient() {
		this.client = new DefaultHttpClient();
	}
	
	/**
	 * Executes GET-Qury and saves Information
	 * 
	 * @param url
	 * @return
	 * @throws Exception 
	 */
	public void get(String url) throws Exception {
		this.get(url, null);
	}
	
	/**
	 * Executes GET-QUery with Params
	 * @param apiUrl2
	 * @param nameValuePairs
	 * @throws Exception 
	 */
	protected void get(String url, ArrayList<NameValuePair> nameValuePairs) throws Exception {
		// TODO Auto-generated method stub
		if(nameValuePairs != null) {
			String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
			url += "?"+paramString;
		}
		Log.e("store", url);
		this.httpGet = new HttpGet(url);
		
		try {
			HttpResponse execute = client.execute(httpGet);
			content = execute.getEntity().getContent();
			
			this.statusCode = execute.getStatusLine().getStatusCode();
			this.reasonPhrase = execute.getStatusLine().getReasonPhrase();			
			
		} catch (Exception e) {
			Log.e("http client", e.getMessage());
			throw e;
		}
		
		this.responseBody = buildResponse(content);
	}
	
	/**
	 * Executes a Post Query and saves Response Information
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @param json 
	 * @throws Exception
	 */
	public void post(String url, ArrayList<NameValuePair> nameValuePairs , Boolean json) throws Exception {
		this.httpPost = new HttpPost(url);
		
		try {
			
			if(!json) {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			} else {
				  // set the Content-type
				  httpPost.setHeader("Content-type", "application/json");

				  // add the JSON as a StringEntity
				  httpPost.setEntity(new StringEntity(nameValuePairs.get(0).getValue(), "UTF8"));			
			}
			
			HttpResponse execute = client.execute(httpPost);
			
			this.statusCode = execute.getStatusLine().getStatusCode();
			this.reasonPhrase = execute.getStatusLine().getReasonPhrase();
			content = execute.getEntity().getContent();
			
		} catch (Exception e) {
			throw e;
		}
		
		this.responseBody = buildResponse(content);
	}
	
	/**
	 * Method overload to simulate default json = false
	 * @param url
	 * @param nameValuePairs
	 * @throws Exception
	 */
	public void post(String url, ArrayList<NameValuePair> nameValuePairs) throws Exception {
		this.post(url, nameValuePairs, false);
	}
	
	/**
	 * Executes a Put query and saves Response Information
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	public String put(String url, ArrayList<NameValuePair> nameValuePairs ) {
		this.httpPut = new HttpPut(url);
		
		try {
			httpPut.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse execute = client.execute(httpPut);
			
			// execute and get content
			content = execute.getEntity().getContent();
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}
		return buildResponse(content);		
	}
	
	/**
	 * Builds Response from InputStream 
	 * 
	 * @param content
	 * @return
	 */
	private String buildResponse(InputStream content) {
		String response = "";
		BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
		
		String s = "";
		try {
			while((s = buffer.readLine()) != null) {
				response += s;
			}
		} catch (Exception e) {
			Log.e("http client" , e.getMessage());
			return "-1";
		}
		
		return response;
	}
	
	protected void cancel() {
		if(this.httpGet != null)
			this.httpGet.abort();
	}
}

