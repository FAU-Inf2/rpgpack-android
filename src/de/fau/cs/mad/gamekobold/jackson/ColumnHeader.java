package de.fau.cs.mad.gamekobold.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ColumnHeader {
	//TODO evtl final?
	public String name,type;
	public boolean hidden;
	
	
	public ColumnHeader(String name, String type) {
		this.name = name;
		this.type = type;
		hidden = false;
	}
	
	@JsonCreator
	public ColumnHeader(@JsonProperty("name") String name,
						@JsonProperty("type") String type,
						@JsonProperty("hidden") boolean hidden)
	{
		this.name = name;
		this.type = type;
		this.hidden = hidden;
	}
	
	
	public boolean isInt() {
		return IntegerClass.TYPE_STRING.equals(type);
	}
	
	public boolean isString() {
		return StringClass.TYPE_STRING.equals(type);
	}
}
