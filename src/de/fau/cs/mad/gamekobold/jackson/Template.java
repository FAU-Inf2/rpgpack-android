package de.fau.cs.mad.gamekobold.jackson;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Template implements Parcelable{
	@JsonIgnore
	public static final String PARCELABLE_STRING = "JacksonTemplate";
	/* META DATA */

	private String fileName = null;
	private String templateName = "";
	private String gameName = "";
	private String author = "";
	private String date = "";
	private String description = "";
	private String tagString = "";
	private String iconPath = "";

	/* Character */
	private CharacterSheet characterSheet = null;

	public Template() {
		Log.d("Template", "default constructor");
		characterSheet = new CharacterSheet();
	}

	public Template(CharacterSheet sheet) {
		characterSheet = sheet;
	}

	@SuppressLint("SimpleDateFormat")
	@JsonIgnore
	public String getFileName() {
		if(fileName == null) {
			final String sanitizedName = JacksonInterface.getSanitizedFileName(templateName); 
			if(!sanitizedName.isEmpty()) {
				fileName = sanitizedName + "-" + date;
			}
			else {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss");
				Date date = new Date();
				fileName = format.format(date);
			}
		}
		return fileName;
	}

	@JsonIgnore
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Takes over all values form the given Template
	 * @param otherTemplate TemplateBrowser Template
	 */
	public void takeOverValues(de.fau.cs.mad.gamekobold.templatebrowser.Template otherTemplate) {
		templateName = otherTemplate.getTemplateName();
		gameName = otherTemplate.getWorldName();
		author = otherTemplate.getAuthor();
		date = otherTemplate.getDate();
		iconPath = otherTemplate.getIconPath();
		description = otherTemplate.getDescription();
		tagString = otherTemplate.getTagString();
	}

	/**
	 * @param withPrettyWriter If true the writer will indent the output.
	 * @return Json representation of this template
	 * @throws JsonProcessingException
	 */
	public String toJSON(boolean withPrettyWriter) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		if(withPrettyWriter) {
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
		}
		return mapper.writer().writeValueAsString(this);
	}
	
	public String getTagString() {
		return tagString;
	}
	
	@JsonProperty("tags")
	public void setTagString(String tagString) {
		this.tagString = tagString;
	}

	public String getTemplateName() {
		return templateName;
	}

	@JsonProperty("name")
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getGameName() {
		return gameName;
	}

	@JsonProperty("world")
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

	public String getDescription() {
		return description;
	}

	@JsonProperty("des")
	public void setDescription(String description) {
		this.description = description;
	}

	public CharacterSheet getCharacterSheet() {
		return characterSheet;
	}
	
	@JsonProperty("charSheet")
	public void setCharacterSheet(CharacterSheet characterSheet) {
		this.characterSheet = characterSheet;
	}

	public String getIconPath() {
		return iconPath;
	}

	@JsonProperty("icon")
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	//
	// PARCELABLE BELOW
	//
	@Override
	public int describeContents() {
		return 0;
	}

	/*
	 * Ignores CharacterSheet!
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(templateName);
		dest.writeString(gameName);
		dest.writeString(author);
		dest.writeString(date);
		dest.writeString(description);
		dest.writeString(tagString);
		dest.writeString(iconPath);
	}

	public static final Parcelable.Creator<Template> CREATOR = new Creator<Template>() {
		@Override
		public Template[] newArray(int size) {
			return new Template[size];
		}

		@Override
		public Template createFromParcel(Parcel source) {
			// IMPORTANT read in same order as written (FIFO)
			Template ret = new Template();
			ret.templateName = source.readString();
			ret.gameName = source.readString();
			ret.author = source.readString();
			ret.date = source.readString();
			ret.description = source.readString();
			ret.tagString = source.readString();
			ret.iconPath = source.readString();
			return ret;
		}
	};
	//
	// PARCELABLE END
	//
}
