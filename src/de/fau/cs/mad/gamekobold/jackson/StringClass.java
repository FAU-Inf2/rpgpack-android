package de.fau.cs.mad.gamekobold.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StringClass extends AbstractColumnEntry {
	@JsonIgnore
	public static final String TYPE_STRING = "string";
	
	public String mainText;
	
	public StringClass() {
		mainText = "";
	}
	
	@JsonCreator
	public StringClass(@JsonProperty("mainText") String text) {
		this.mainText = text;
	}
	
	@Override
	public String toString() {
		return mainText;
	}
	
	@Override
	public String getType() {
		return TYPE_STRING;
	}

	@Override
	public void setContent(String content) {
		mainText = content;
	}

	@Override
	public String getContent() {
		return mainText;
	}
}
