package de.fau.cs.mad.gamekobold.jackson;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Template implements Parcelable{
	@JsonIgnore
	public static final String PARCELABLE_STRING = "JacksonTemplate";
	/* META DATA */
	// TODO set fileName when loading + abspath
	@JsonIgnore
	private String fileName = null;
	public String templateName = "";
	public String gameName = "";
	public String author = "";
	public String date = "";
	public int iconID;
	public String description = "";
	private String tagString = "";


	/* Character */
	public CharacterSheet characterSheet = null;
	
	public Template() {
		Log.d("Template", "default constructor");
		characterSheet = new CharacterSheet();
	}
	
	@JsonCreator
	public Template(@JsonProperty("characterSheet") CharacterSheet sheet) {
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
		iconID = otherTemplate.getIconID();
		description = otherTemplate.getDescription();
		tagString = otherTemplate.getTagString();
	}

	/**
	 * @return Json representation of this template
	 * @throws JsonProcessingException
	 */
	public String toJSON(boolean withPrettyWriter) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		if(withPrettyWriter) {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		}
		else {
			return mapper.writer().writeValueAsString(this);
		}
	}
	
	@JsonProperty("tags")
	public String getTagString() {
		return tagString;
	}

	@JsonProperty("tags")
	public void setTagString(String tagString) {
		this.tagString = tagString;
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
		dest.writeInt(iconID);
		dest.writeString(description);
		dest.writeString(tagString);
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
			ret.iconID = source.readInt();
			ret.description = source.readString();
			ret.tagString = source.readString();
			return ret;
		}
	};
	//
	// PARCELABLE END
	//
}
