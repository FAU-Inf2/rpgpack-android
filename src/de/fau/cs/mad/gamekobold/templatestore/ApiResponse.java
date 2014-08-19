package de.fau.cs.mad.gamekobold.templatestore;

/**
 * Representation of an ApiResponse
 * @author Gregor Völkl
 *
 */
public class ApiResponse {
	public int resultCode;
	public String reasonPhrase;
	public String responseBody;
	
	ApiResponse(int resultCode, String reasonPhrase, String responseBody) {
		this.resultCode = resultCode;
		this.reasonPhrase = reasonPhrase;
		this.responseBody = responseBody;
	}
	
	public String toString() {
		return "ResultCode: "+this.resultCode+" "+this.reasonPhrase+" \n\n " + this.responseBody;
	}
}
