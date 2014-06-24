package de.fau.cs.mad.gamekobold.templatebrowser;

import java.io.Serializable;

public class Template implements Serializable{
	private String templateName;
	private String gameName;
	private String author;
	private String date;
	private int iconID;
	private String description;
	public String filePath = null;

	public Template(String templateName, String gameName, String author,
			String date, int iconID, String description) {
		this.templateName = templateName;
		this.gameName = gameName;
		this.author = author;
		this.date = date;
		this.iconID = iconID;
		this.description = description;
	}

	public Template(String templateName, String gameName, String author,
			String date, int iconID) {
		this.templateName = templateName;
		this.gameName = gameName;
		this.author = author;
		this.date = date;
		this.iconID = iconID;
		this.description = "No description found!";
	}
	
	public Template(String templateName, String gameName, String author,
			String date) {
		this.templateName = templateName;
		this.gameName = gameName;
		this.author = author;
		this.date = date;
		//some value for default icon
		this.iconID = -1; 
	}
	
	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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
	
}
