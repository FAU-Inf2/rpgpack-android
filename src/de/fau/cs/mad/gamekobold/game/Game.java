package de.fau.cs.mad.gamekobold.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import android.util.Log;

/**
 * This class represents one game group. All played characters are in
 * <code>characterList</code>.
 * 
 */
public class Game implements Serializable {
	private String gameName;
	private String author;
	private String date = null;
	private List<String> tagList;
	private String description;
	private List<GameCharacter> characterList;
	private String iconPath;
	private String fileAbsolutePath;
	private long fileTimeStamp;

	public Game(String gameName, String author, String date,
			List<String> tagList, String description,
			List<GameCharacter> characterList, String iconPath) {
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
		this.iconPath = "";
	}

	/**
	 * needed for json.
	 */
	public Game() {
		/**
		 * need to set all fields to default values for json.
		 */
		this.gameName = "";
		this.author = "";
		this.date = "";
		this.tagList = new LinkedList<String>();
		this.description = "";
		this.characterList = new ArrayList<GameCharacter>();
		this.iconPath = "";
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

	@JsonProperty("author")
	public void setAuthor(String author) {
		this.author = author;
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

	@JsonProperty("tags")
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	public String getDescription() {
		if (description == null) {
			description = "";
		}
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	public List<GameCharacter> getCharacterList() {
		return characterList;
	}

	@JsonProperty("characters")
	public void setCharakterList(List<GameCharacter> characterList) {
		Log.e("CharacterList",
				"Setting CharacterList to " + characterList.size());
		this.characterList.clear();
		this.characterList.addAll(characterList);
		return;
	}

	public String getIconPath() {
		return iconPath;
	}

	@JsonProperty("icon")
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getFileAbsolutePath() {
		return fileAbsolutePath;
	}

	@JsonIgnore
	public void setFileAbsolutePath(String fileAbsolutePath) {
		this.fileAbsolutePath = fileAbsolutePath;
	}

	public long getFileTimeStamp() {
		return fileTimeStamp;
	}

	@JsonIgnore
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
