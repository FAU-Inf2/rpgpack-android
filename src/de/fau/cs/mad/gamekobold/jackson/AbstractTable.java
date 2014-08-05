package de.fau.cs.mad.gamekobold.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY, property = "@class")
//@JsonSubTypes({ @Type(value = Table.class, name = "table"),
//				@Type(value = ContainerTable.class, name = "containertable"),
//				@Type(value = MatrixTable.class, name ="matrixtable")})
@JsonTypeInfo(use=Id.NAME, include=As.WRAPPER_OBJECT, property="type")
@JsonSubTypes({
	@JsonSubTypes.Type(value=Table.class, name="table"),
	@JsonSubTypes.Type(value=MatrixTable.class, name="matrix"),
	@JsonSubTypes.Type(value=ContainerTable.class, name="folder")
})
public abstract class AbstractTable {
	
	@JsonProperty(value="name")
	public String tableName;
	
	public abstract void print();
	
	public AbstractTable() {
		tableName = "";
	}
}
