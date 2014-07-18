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
	
	public IntegerClass() {
		value = 0;
		rangeFrom = 0;
		rangeTo = 0;
		modificator = 0;
		subValue = "";
	}
	
	@JsonCreator
	public IntegerClass(@JsonProperty("value") int value) {
		this.value = value;
		rangeFrom = 0;
		rangeTo = 0;
		modificator = 0;
		subValue = "";
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public String getType() {
		return TYPE_STRING;
	}
}
