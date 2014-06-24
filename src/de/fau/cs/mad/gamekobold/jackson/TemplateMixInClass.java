package de.fau.cs.mad.gamekobold.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TemplateMixInClass {
	/* Character */
	@JsonIgnore
	public CharacterSheet characterSheet = null;
	
	@JsonCreator
	public TemplateMixInClass() {
		characterSheet = null;
	}
	
	@JsonIgnore
	@JsonCreator
	public TemplateMixInClass(@JsonProperty("characterSheet") CharacterSheet sheet) {
		characterSheet = sheet;
	}
}
