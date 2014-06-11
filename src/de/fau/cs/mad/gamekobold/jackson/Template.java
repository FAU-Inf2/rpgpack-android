package de.fau.cs.mad.gamekobold.jackson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//TODO Log.d anpassen bzw loeschen
//TODO print() auskommentieren?
public class Template implements Parcelable{
	@JsonIgnore
	public static boolean USE_PRETTY_WRITER = true;
	@JsonIgnore
	private static final String FOLDER_NAME = "Templates";
	@JsonIgnore
	public static final String PARCELABLE_STRING = "JacksonTemplate";
	/* META DATA */
	public String templateName;
	public String gameName;
	public String author;
	public String date;
	public int iconID;
	public String description;
	/* Character */
	public CharacterSheet characterSheet;
	
	public Template() {
		characterSheet = new CharacterSheet();
	}
	
	@JsonCreator
	public Template(@JsonProperty("characterSheet") CharacterSheet sheet) {
		characterSheet = sheet;
	}
	
	public void print() {
		Log.d("TEMPLATE", "name:" + templateName);
		Log.d("TEMPLATE", "game:" + gameName);
		Log.d("TEMPLATE", "author:" + author);
		Log.d("TEMPLATE", "date:" + date);
		Log.d("TEMPLATE", "icon:" + iconID);
		Log.d("TEMPLATE", "description:" + description);
		characterSheet.print();
	}
	
	//TODO vllt unter files/Templates/ speichern
	public void saveToJSON(Context context, String fileName) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File dir = context.getDir(FOLDER_NAME, Context.MODE_PRIVATE);
		FileOutputStream outStream = new FileOutputStream(dir.getAbsolutePath() + File.separator + fileName);
		if(USE_PRETTY_WRITER) {
			mapper.writerWithDefaultPrettyPrinter().writeValue(outStream, this);
		}
		else {
			mapper.writer().writeValue(outStream, this);
		}
	}
	
	public static Template loadFromJSONFile(Context context, String fileName) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File dir = context.getDir(FOLDER_NAME, Context.MODE_PRIVATE);
		FileInputStream inStream = new FileInputStream(dir.getAbsolutePath() + File.separator + fileName);
		Template template = mapper.readValue(inStream, Template.class);
		return template;
	}
	
	public String toJSON() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writer().writeValueAsString(this);
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
			return ret;
		}
	};
	
	//
	// PARCELABLE END
	//
}
