package de.fau.cs.mad.gamekobold.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY, property = "@class")
@JsonSubTypes({ @Type(value = Table.class, name = "table"),
				@Type(value = ContainerTable.class, name = "containertable"),
				@Type(value = MatrixTable.class, name ="matrixtable")})
public abstract class AbstractTable {
	
	public String tableName;
	
	public abstract void print();
	
	public AbstractTable() {
		tableName = "";
	}
}
