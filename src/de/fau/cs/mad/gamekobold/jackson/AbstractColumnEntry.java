package de.fau.cs.mad.gamekobold.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

//for polymorphism
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = StringClass.class, name = "string"),
				@Type(value = CheckBoxClass.class, name ="chkbox"),
				@Type(value = PopupClass.class, name = "popup")})

//for getType()
@JsonIgnoreProperties({"type"})

public abstract class AbstractColumnEntry implements IEditableContent {
	
//	public String name;
//	public String shortCut;
//	public String description;
//	public boolean importantValue;
	
	@JsonIgnore
	public abstract String getType();
	
	@JsonIgnore
	@Override
	public abstract String toString();
}
