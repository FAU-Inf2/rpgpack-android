package de.fau.cs.mad.gamekobold.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import android.util.Log;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class Game implements Serializable {
	private String gameName;
	private String author;
	private String date = null;
	private List<String> tagList;
	private String description;

	//TODO dte check this! because of notifydatasetchanged()
	private List<GameCharacter> characterList;
	private String iconPath;

	private String fileAbsolutePath;

	private long fileTimeStamp;
	
	@JsonCreator
	public Game(@JsonProperty("name") String gameName,
				@JsonProperty("author") String author,
				@JsonProperty("date") String date,
				@JsonProperty("tags") List<String> tagList,
				@JsonProperty("description") String description,
				@JsonProperty("characters") List<GameCharacter> characterList,
				@JsonProperty("iconPath") String iconPath) {
		this(gameName, date, iconPath);
		this.tagList = tagList;
		this.description = description;
		this.setCharakterList(characterList);
		this.fileAbsolutePath = "";
		this.fileTimeStamp = 0;
	}

	public Game(String gameName, String date, String iconPath) {
		this(gameName, date);
		this.iconPath = iconPath;
	}

	public Game(String gameName, String date) {
		this(gameName);
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

	@JsonProperty("name")
	public String getGameName() {
		return gameName;
	}

	@JsonProperty("name")
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

	@JsonProperty("tags")
	public List<String> getTagList() {
		return tagList;
	}

	@JsonProperty("tags")
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	public String getDescription() {
		if(description == null) {
			description = "";
		}
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("characters")
	public List<GameCharacter> getCharacterList() {
		return characterList;
	}

	//TODO dte check this!!!!
	@JsonProperty("characters")
	public void setCharakterList(List<GameCharacter> characterList) {
		Log.e("CharacterList", "Setting CharacterList to " + characterList.size());
		this.characterList.clear();
		this.characterList.addAll(characterList);
		return; 
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	@JsonIgnore
	public String getFileAbsolutePath() {
		return fileAbsolutePath;
	}

	public void setFileAbsolutePath(String fileAbsolutePath) {
		this.fileAbsolutePath = fileAbsolutePath;
	}

	@JsonIgnore
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
		this.characterList = otherGame.characterList;
		this.fileAbsolutePath = otherGame.fileAbsolutePath;
		this.fileTimeStamp = otherGame.fileTimeStamp;
		this.iconPath = otherGame.iconPath;
	}
}
