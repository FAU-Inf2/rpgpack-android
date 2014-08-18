package de.fau.cs.mad.gamekobold.game;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class GameCharacter implements Serializable{
	// TODO CharakterInfo
	private String characterName;
	private String date;
	private List<String> tagList;
	private String description;
	private Template template;
	private String fileAbsPath;
	
	/**
	 * Constructor for jackson.
	 * @param characterName
	 * @param date
	 * @param tagList
	 * @param description
	 * @param fileAbsPath
	 */
	@JsonCreator
	public GameCharacter( @JsonProperty("characterName") String characterName,
						@JsonProperty("date")String date,
						@JsonProperty("tagList") List<String> tagList,
						@JsonProperty("description") String description,
						@JsonProperty("fileAbsPath") String fileAbsPath) {
		this.template = null;
		this.characterName = characterName;
		this.date = date;
		this.tagList = tagList;
		this.description = description;
		this.fileAbsPath = fileAbsPath;
	}

	public GameCharacter(String characterName, String date,
			List<String> tagList, String description, Template template) {
		this.characterName = characterName;
		this.date = date;
		this.tagList = tagList;
		this.description = description;
		this.template = template;
		this.fileAbsPath = "";
	}

	public GameCharacter(String characterName, String date, Template template) {
		this.characterName = characterName;
		this.date = date;
		this.template = template;
		this.fileAbsPath = "";
	}
	//fake item -> createNewCharacter
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

	@JsonIgnore
	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public String getFileAbsPath() {
		return fileAbsPath;
	}

	public void setFileAbsPath(String fileAbsPath) {
		this.fileAbsPath = fileAbsPath;
	}

	@JsonIgnore
	public boolean isFilePathValid() {
		return !fileAbsPath.isEmpty();
	}
}
