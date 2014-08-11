package de.fau.cs.mad.gamekobold.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import android.util.Log;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class Game implements Serializable {
	private String gameName;
	private String author;
	private String date;
	private List<String> tagList;
	private String description;
	private Template template;
	@JsonIgnore
	private List<GameCharacter> characterList;
	private String iconPath;
	@JsonIgnore
	private String fileAbsolutePath;
	@JsonIgnore
	private long fileTimeStamp;
	private List<String> characterFileAbsPathList;

	public Game(String gameName, String author, String date,
			List<String> tagList, String description, Template template,
			List<GameCharacter> characterList, String iconPath) {
		this(gameName, template, date, iconPath);
		this.tagList = tagList;
		this.description = description;
		this.template = template;
		this.setCharakterList(characterList);
		this.fileAbsolutePath = "";
		this.fileTimeStamp = 0;
		this.characterFileAbsPathList = new ArrayList<String>();
	}

	public Game(String gameName, Template template, String date, String iconPath) {
		this(gameName, template, date);
		this.iconPath = iconPath;
	}

	public Game(String gameName, Template template, String date) {
		this(gameName);
		this.template = template;
		this.date = date;
	}

	// fake last item for create new game
	public Game(String gameName) {
		this();
		this.gameName = gameName;
	}

	public Game() {
		this.characterList = new ArrayList<GameCharacter>();
		this.fileAbsolutePath = "";
		this.fileTimeStamp = 0;
		this.characterFileAbsPathList = new ArrayList<String>();
	}

	public boolean addCharacter(GameCharacter character) {
		Log.e("Character is null?", "" + (character == null));
		Log.e("List is null?", "" + (characterList == null));
		characterList.add(character);
		return true;
	}

	public boolean removeCharacter(GameCharacter character) {
		characterList.remove(character);
		return true;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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

	public List<GameCharacter> getCharakterList() {
		return characterList;
	}

	public void setCharakterList(List<GameCharacter> charakterList) {
		Log.e("CharacterList", "Setting CharacterList to " + charakterList);
		this.characterList = charakterList;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getFileAbsolutePath() {
		return fileAbsolutePath;
	}

	public void setFileAbsolutePath(String fileAbsolutePath) {
		this.fileAbsolutePath = fileAbsolutePath;
	}

	public long getFileTimeStamp() {
		return fileTimeStamp;
	}

	public void setFileTimeStamp(long fileTimeStamp) {
		this.fileTimeStamp = fileTimeStamp;
	}

	public void takeOverValues(final Game otherGame) {
		this.gameName = otherGame.gameName;
		this.author = otherGame.author;
		this.date = otherGame.date;
		this.tagList = otherGame.tagList;
		this.description = otherGame.description;
		this.template = otherGame.template;
		this.characterList = otherGame.characterList;
		this.fileAbsolutePath = otherGame.fileAbsolutePath;
		this.fileTimeStamp = otherGame.fileTimeStamp;
		this.iconPath = otherGame.iconPath;
	}

}
