package de.fau.cs.mad.gamekobold.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CharacterSheetMixInClass {
	
	@JsonIgnore
	public ContainerTable rootTable;
		
	@JsonIgnore
	@JsonCreator
	public CharacterSheetMixInClass(@JsonProperty("rootTable") ContainerTable table) {
		rootTable = table;
	}
}
