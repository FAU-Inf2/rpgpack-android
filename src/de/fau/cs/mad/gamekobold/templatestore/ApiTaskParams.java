package de.fau.cs.mad.gamekobold.templatestore;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.NameValuePair;


/**
 * This class supports use of asynchronous tasks to query API 
 * @author Gregor Völkl
 *
 */
public class ApiTaskParams {
	private String method;
	private String[] allowedMethods = {"getTemplates", "postTemplate", "searchTemplates", "searchByTag", "loadMore"};
	
	private ArrayList<NameValuePair> params;
	
	ApiTaskParams() {
		
	}
	
	ApiTaskParams(String method) {
		this.setMethod(method);
	}
	
	public String getMethod() {
		if( method == null) 
			throw new IllegalStateException("No method specified");
		
		return method;
	}

	public void setMethod(String method) {
		if( Arrays.asList(this.allowedMethods).contains(method))
			this.method = method;
		else {
			throw new IllegalArgumentException("Method is not supported");
		}
	}

	public ArrayList<NameValuePair> getParams() {
		return params;
	}

	public void setParams(ArrayList<NameValuePair> params) {
		this.params = params;
	}
	
	
}
