package de.fau.cs.mad.gamekobold.game;

import java.io.Serializable;
import java.util.List;

import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class GameCharacter implements Serializable{
	// TODO CharakterInfo
	private String characterName;
	private String date;
	private List<String> tagList;
	private String description;
	private Template template;

	public GameCharacter(String characterName, String date,
			List<String> tagList, String description, Template template) {
		this.characterName = characterName;
		this.date = date;
		this.tagList = tagList;
		this.description = description;
		this.template = template;
	}

	public GameCharacter(String characterName, String date, Template template) {
		this.characterName = characterName;
		this.date = date;
		this.template = template;
	}

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<String> getTagList() {
		return tagList;
	}

	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

}
