package de.fau.cs.mad.gamekobold.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

//for polymorphism
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = IntegerClass.class, name = "integerclass"), @Type(value = StringClass.class, name = "stringclass") })

//for getType()
@JsonIgnoreProperties({ "type"})

public abstract class AbstractColumnEntry {
	//protected String type;
	
	public String name;
	public String shortCut;
	public String description;
	public boolean importantValue;
	
	public abstract String getType();
	
	@JsonIgnore
	public abstract String getValueAsString();
	
	/*
	@JsonIgnore
	public boolean isInteger()  {
		return IntegerClass.TYPE_STRING.equals(type);
	}
	@JsonIgnore
	public boolean isString()  {
		return IntegerClass.TYPE_STRING.equals(type);
	}*/
}
