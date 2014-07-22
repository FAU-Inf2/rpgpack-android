package de.fau.cs.mad.gamekobold.game;

import java.util.List;

import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class Game {
	private String appGameName;
	private String author;
	private String date;
	private List<String> tagList;
	private String description;
	private Template template;
	private List<Charakter> charakterList;

	public Game(String appGameName, String author, String date,
			List<String> tagList, String description, Template template,
			List<Charakter> charakterList) {
		this.appGameName = appGameName;
		this.author = author;
		this.date = date;
		this.tagList = tagList;
		this.description = description;
		this.template = template;
		this.charakterList = charakterList;
	}

	public Game(String appGameName, String templateName, String date) {
		this.appGameName = appGameName;
		// TODO change it to template later
		this.description = templateName;
		this.date = date;
	}

	public String getAppGameName() {
		return appGameName;
	}

	public void setAppGameName(String appGameName) {
		this.appGameName = appGameName;
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

	public List<Charakter> getCharakterList() {
		return charakterList;
	}

	public void setCharakterList(List<Charakter> charakterList) {
		this.charakterList = charakterList;
	}

}
