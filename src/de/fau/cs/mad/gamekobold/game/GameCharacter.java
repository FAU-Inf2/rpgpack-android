package de.fau.cs.mad.gamekobold.game;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class GameCharacter implements Serializable {
	// TODO CharakterInfo
	private String characterName;
	private String date;
	private List<String> tagList;
	private String description;
	private Template template;
	private String iconPath;
	private String fileAbsPath;

	public GameCharacter(String characterName,
							String date,
							List<String> tagList,
							String description,
							String fileAbsPath) {
		this.template = null;
		this.characterName = characterName;
		this.date = date;
		this.tagList = tagList;
		this.description = description;
		this.fileAbsPath = fileAbsPath;
	}

	public GameCharacter(String characterName, String date,
			List<String> tagList, String description, String iconPath,
			Template template) {
		this(characterName, date, template, iconPath);
		this.tagList = tagList;
		this.description = description;
	}

	public GameCharacter(String characterName, String date, Template template,
			String iconPath) {
		this(characterName, date, template);
		this.iconPath = iconPath;
	}

	public GameCharacter(String characterName, String date, Template template) {
		this(characterName);
		this.date = date;
		this.template = template;
	}

	// fake item -> createNewCharacter
	public GameCharacter(String characterName) {
		this.characterName = characterName;
		this.fileAbsPath = "";
	}
	
	/**
	 * For json deserialization.
	 */
	public GameCharacter() {
		this.characterName = "";
		this.date = "";
		this.tagList = new LinkedList<String>();
		this.description = "";
		this.template = null;
		this.iconPath = "";
		this.fileAbsPath = "";
	}

	public String getCharacterName() {
		return characterName;
	}

	@JsonProperty("characterName") 
	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public String getDate() {
		return date;
	}
	
	@JsonProperty("date") 
	public void setDate(String date) {
		this.date = date;
	}

	public List<String> getTagList() {
		return tagList;
	}

	@JsonProperty("tagList")
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	public String getDescription() {
		return description;
	}
	
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public String getIconPath() {
		return iconPath;
	}

	@JsonProperty("icon")
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getFileAbsPath() {
		return fileAbsPath;
	}

	@JsonProperty("fileAbsPath")
	public void setFileAbsPath(String fileAbsPath) {
		this.fileAbsPath = fileAbsPath;
	}

	@JsonIgnore
	public boolean isFilePathValid() {
		return !fileAbsPath.isEmpty();
	}
}
