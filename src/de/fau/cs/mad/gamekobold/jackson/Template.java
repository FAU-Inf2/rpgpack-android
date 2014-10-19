package de.fau.cs.mad.gamekobold.jackson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.fau.cs.mad.gamekobold.ThumbnailLoader;

public class Template implements Parcelable{
	@JsonIgnore
	public static final String PARCELABLE_STRING = "JacksonTemplate";
	/* META DATA */

	private String fileAbsPath = "";
	private String fileName = "";
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
		if(fileName.isEmpty()) {
			final String sanitizedName = JacksonInterface.getSanitizedFileName(templateName); 
			//extracted following 3 lines from else part
			//in if case there is an error if we don't do it that way
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss");
			Date date = new Date();
			fileName = format.format(date);
			if(!sanitizedName.isEmpty()) {
				fileName = sanitizedName + "-" + date;
			}
//			else {
//				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss");
//				Date date = new Date();
//				fileName = format.format(date);
//				Log.d("Template.java", "formating date; filename: " + fileName);
//			}
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

	public String getFileAbsPath() {
		return fileAbsPath;
	}

	@JsonIgnore
	public void setFileAbsPath(String fileAbsPath) {
		this.fileAbsPath = fileAbsPath;
	}

	@JsonIgnore
	public boolean hasIcon() {
		if(iconPath == null) {
			return false;
		}
		if(iconPath.isEmpty()) {
			return false;
		}
		return true;
	}
	
	@JsonIgnore
	public boolean isIconBase64() {
		if(!hasIcon()) {
			return false;
		}
		try {
			final File testFile = new File(iconPath);
			if(testFile.isFile()) {
				return false;
			}
			else {
				return true;		
			}
		}
		catch(Exception e) {
			return true;
		}
	}
	
	@JsonIgnore
	public Bitmap getIcon(final Context context) {
		if(!hasIcon()) {
			return null;
		}
		if(isIconBase64()) {
			try {
				final byte[] decodedBase64 = Base64.decode(iconPath, Base64.DEFAULT);
				return BitmapFactory.decodeByteArray(decodedBase64, 0, decodedBase64.length);
			}
			catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return ThumbnailLoader.loadThumbnail(iconPath, context);
		}
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
		dest.writeString(fileName);
		dest.writeString(fileAbsPath);
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
			ret.fileName = source.readString();
			ret.fileAbsPath = source.readString();
			return ret;
		}
	};
	//
	// PARCELABLE END
	//
}
