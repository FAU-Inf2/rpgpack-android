package de.fau.cs.mad.gamekobold.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TemplateMixInClass {
	@JsonIgnore
	private CharacterSheet characterSheet = null;

	public CharacterSheet getCharacterSheet() {
		return characterSheet;
	}

	@JsonIgnore
	@JsonProperty("characterSheet")
	public void setCharacterSheet(CharacterSheet characterSheet) {
		this.characterSheet = characterSheet;
	}
}
