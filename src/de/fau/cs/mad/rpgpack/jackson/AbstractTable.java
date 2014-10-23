package de.fau.cs.mad.rpgpack.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use=Id.NAME, include=As.WRAPPER_OBJECT, property="type")
@JsonSubTypes({
	@JsonSubTypes.Type(value=Table.class, name="table"),
	@JsonSubTypes.Type(value=MatrixTable.class, name="matrix"),
	@JsonSubTypes.Type(value=ContainerTable.class, name="folder")
})
public abstract class AbstractTable extends SelectAndFavorableItem {	
	@JsonProperty(value="name")
	public String tableName;
		
	public AbstractTable() {
		tableName = "";
	}
}
