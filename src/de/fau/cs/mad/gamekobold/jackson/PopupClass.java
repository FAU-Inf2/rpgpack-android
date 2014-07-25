package de.fau.cs.mad.gamekobold.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PopupClass extends AbstractColumnEntry{
	@JsonIgnore
	public static final String TYPE_STRING = "popup";
	
	String content;
	
	public PopupClass() {
		content = "";
	}
	
	@JsonCreator
	public PopupClass(@JsonProperty("content") String content) {
		this.content = content;
	}

	@Override
	public String getType() {
		return TYPE_STRING;
	}

	@Override
	public String toString() {
		return content;
	}

}
