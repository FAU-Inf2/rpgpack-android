package de.fau.cs.mad.gamekobold.templatebrowser;

import java.io.File;
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
	public String fileAbsolutePath = null;
	private long fileTimeStamp = 0;
	
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
		if (fileAbsolutePath == null) {
			// return null?
			return "";
		}
		if (fileAbsolutePath.isEmpty()) {
			return "";
		}
		int lastSlashPos = fileAbsolutePath.lastIndexOf("/");
		String fileName = null;
		if (lastSlashPos == -1) {
			fileName = fileAbsolutePath;
		} else {
			fileName = fileAbsolutePath.substring(lastSlashPos + 1);
		}
		return fileName;
	}
	
	public List<GameCharacter> getCharacters() {
		return characters;
	}

	public void setCharacters(List<GameCharacter> characters) {
		this.characters = characters;
	}

	public void takeOverValues(final Template otherTemplate) {
		this.description = otherTemplate.description;
	}
	
	/**
	 * Checks whether the file for this template has changed by checking the time stamp of it.
	 * @return true if the file has been changed, false otherwise.
	 */
	public boolean hasFileTimeStampChanged() {
		if(fileAbsolutePath == null) {
			return false;
		}
		final File templateFile = new File(fileAbsolutePath);
		final long newTimeStamp = templateFile.lastModified();
		if(newTimeStamp > fileTimeStamp) {
			fileTimeStamp = newTimeStamp;
			return true;
		}
		return false;
	}
	
	public void setFileTimeStamp(final long timeStamp) {
		fileTimeStamp = timeStamp;
	}
	
	public long getFileTimeStamp() {
		return fileTimeStamp;
	}
	
	public File getTemplateFile() {
		if(fileAbsolutePath == null) {
			return null;
		}
		return new File(fileAbsolutePath);
	}
}
