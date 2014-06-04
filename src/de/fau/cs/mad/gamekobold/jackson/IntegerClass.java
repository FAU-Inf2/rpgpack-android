package de.fau.cs.mad.gamekobold.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class IntegerClass extends AbstractColumnEntry{
	@JsonIgnore
	public static final String TYPE_STRING = "int";
	
	public int value;
	public int rangeFrom, rangeTo;
	public int modificator;
	public String subValue;
	
	@JsonCreator
	public IntegerClass(@JsonProperty("value") int value) {
		this.value = value;
	//	this.type = TYPE_STRING;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}

	public String getType() {
		return "integer";
	}
	
	public String getValueAsString() {
		return String.valueOf(value);
	}
}
