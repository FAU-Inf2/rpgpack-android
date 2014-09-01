package de.fau.cs.mad.gamekobold.jackson;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ColumnHeader implements IEditableContent{
	public String name,type;
	
	@JsonCreator
	public ColumnHeader(@JsonProperty("name") String name,
						@JsonProperty("type") String type)
	{
		this.name = name;
		this.type = type;
	}
	
	@JsonIgnore
	public boolean isPopup() {
		return PopupClass.TYPE_STRING.equals(type);
	}

	@JsonIgnore
	public boolean isString() {
		return StringClass.TYPE_STRING.equals(type);
	}

	@JsonIgnore
	public boolean isCheckBox() {
		return CheckBoxClass.TYPE_STRING.equals(type);
	}

	@Override
	public void setContent(String newContent) {
//		Log.d("ColumnHeader", "Changed from:"+name+" to:"+newContent);
		name = newContent;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public String getContent() {
		return name;
	}
}
