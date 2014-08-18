package de.fau.cs.mad.gamekobold.game;

import java.io.Serializable;
import java.util.List;

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

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getFileAbsPath() {
		return fileAbsPath;
	}

	public void setFileAbsPath(String fileAbsPath) {
		this.fileAbsPath = fileAbsPath;
	}

	public boolean isFilePathValid() {
		return !fileAbsPath.isEmpty();
	}
}
