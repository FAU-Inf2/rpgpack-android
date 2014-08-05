package de.fau.cs.mad.gamekobold.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class Game implements Serializable {
	private String gameName;
	private String author;
	private String date;
	private List<String> tagList;
	private String description;
	private Template template;
	private List<GameCharacter> characterList;

	public Game(String gameName, String author, String date,
			List<String> tagList, String description, Template template,
			List<GameCharacter> characterList) {
		this(gameName, template, date);
		this.tagList = tagList;
		this.description = description;
		this.template = template;
		this.setCharakterList(characterList);
	}

	public Game(String gameName, Template template, String date) {
		this();
		this.gameName = gameName;
		this.template = template;
		this.date = date;
	}

	public Game() {
		this.characterList = new ArrayList<GameCharacter>();
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

}
