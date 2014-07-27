package de.fau.cs.mad.gamekobold.templatebrowser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.gamekobold.game.GameCharacter;

public class Template implements Serializable {
	private String templateName;
	private String worldName;
	private String author;
	private String date;
	private int iconID;
	private String description;
	public String absoluteFilePath = null;
	
	//TODO pruefen in die andere richtung!!!
	private List<GameCharacter> characters = new ArrayList<GameCharacter>();

	public Template(String templateName, String worldName, String author,
			String date, int iconID, String description) {
		this.templateName = templateName;
		this.worldName = worldName;
		this.author = author;
		this.date = date;
		this.iconID = iconID;
		this.description = description;
	}

	public Template(String templateName, String worldName, String author,
			String date, int iconID) {
		this.templateName = templateName;
		this.worldName = worldName;
		this.author = author;
		this.date = date;
		this.iconID = iconID;
		this.description = "No description found!";
	}

	public Template(String templateName, String worldName, String author,
			String date) {
		this.templateName = templateName;
		this.worldName = worldName;
		this.author = author;
		this.date = date;
		// some value for default icon
		this.iconID = -1;
	}

	//TODO pruefen
	public boolean addCharacter(GameCharacter character) {
		characters.add(character);
		return true;
	}
	
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
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

	public int getIconID() {
		return iconID;
	}

	public void setIconID(int iconID) {
		this.iconID = iconID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileName() {
		if (absoluteFilePath == null) {
			return "";
		}
		if (absoluteFilePath.isEmpty()) {
			return "";
		}
		int lastSlashPos = absoluteFilePath.lastIndexOf("/");
		String fileName = null;
		if (lastSlashPos == -1) {
			fileName = absoluteFilePath;
		} else {
			fileName = absoluteFilePath.substring(lastSlashPos + 1);
		}
		return fileName;
	}
	
	public List<GameCharacter> getCharacters() {
		return characters;
	}

	public void setCharacters(List<GameCharacter> characters) {
		this.characters = characters;
	}
}
