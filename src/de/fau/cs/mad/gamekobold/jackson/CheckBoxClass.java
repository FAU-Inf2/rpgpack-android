package de.fau.cs.mad.gamekobold.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckBoxClass extends AbstractColumnEntry{
	@JsonIgnore
	public static final String TYPE_STRING = "chkbox";
	
	public boolean checked;
	
	public CheckBoxClass() {
		checked = false;
	}
	
	@JsonCreator
	public CheckBoxClass(@JsonProperty("checked") boolean isChecked) {
		checked = isChecked;
	}
	
	public CheckBoxClass(String value) {
		// true if content == "true", false otherwise. no exception thrown!
		checked = Boolean.parseBoolean(value);		
	}

	@Override
	public String getType() {
		return TYPE_STRING;
	}
	
	@Override
	public String toString() {
		return String.valueOf(checked);
	}

	@Override
	public void setContent(String content) {
		// true if content == "true", false otherwise. no exception thrown!
		checked = Boolean.parseBoolean(content);
	}

	@Override
	public String getContent() {
		return String.valueOf(checked);
	}
}
