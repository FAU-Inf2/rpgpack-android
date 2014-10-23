package de.fau.cs.mad.rpgpack.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CharacterSheetMixInClass {
	
	@JsonIgnore
	private ContainerTable rootTable;
		
	public ContainerTable getRootTable() {
		return rootTable;
	}

	@JsonIgnore
	@JsonProperty("rootTable")
	public void setRootTable(ContainerTable rootTable) {
		this.rootTable = rootTable;
	}
}
