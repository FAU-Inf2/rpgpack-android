package de.fau.cs.mad.rpgpack.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ColumnHeader implements IEditableContent{
	private String name,type;

	public ColumnHeader() {
		this.name = "";
		this.type = StringClass.TYPE_STRING;
	}

	public ColumnHeader(String name, String type)
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
	@JsonIgnore
	public String toString() {
		return name;
	}

	/**
	 * Same as setName().
	 */
	@Override
	public void setContent(String newContent) {
		name = newContent;
	}

	/**
	 * Same as getName().
	 */
	@Override
	public String getContent() {
		return name;
	}
	
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
