package de.fau.cs.mad.rpgpack.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IEditableContent {
	public void setContent(String newContent);
	@JsonIgnore
	public String getContent();
}
