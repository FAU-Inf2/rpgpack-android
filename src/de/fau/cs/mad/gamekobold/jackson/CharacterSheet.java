package de.fau.cs.mad.gamekobold.jackson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;

public class CharacterSheet implements Parcelable{
	@JsonIgnore
	public static final String FOLDER_NAME = "Characters";
	
	/* METADATA */
	public String name;
	public int level;
	public String description;
	public int color = Color.parseColor("#2980b9");
	@JsonIgnore
	public String fileAbsolutePath = null;
	
	/* ROOT_TABLE */
	private ContainerTable rootTable = null;
	
	public CharacterSheet() {
		name = "";
		level = 3;
		description = "";
		fileAbsolutePath = "";
	}
	
	public CharacterSheet(String name) {
		this.name = name;		
		level = 3;
		description = "";
		fileAbsolutePath = "";
	}
	
	@JsonCreator
	public CharacterSheet(@JsonProperty("rootTable") ContainerTable table) {
		name = "";
		rootTable = table;
		level = 3;
		description = "";
		fileAbsolutePath = "";
	}
	
	public ContainerTable getRootTable() {
		if(rootTable == null) {
			rootTable = new ContainerTable();
			rootTable.tableName = "rootTable";
		}
		return rootTable;
	}
	
//	public void print() {
//		Log.d("CHARACTER_SHEET","CharacterSheet:"+name);
//		rootTable.print();
//	}

	/**
	 * 
	 * @param jsonFile File object for the character.
	 * @param onlyMetaData If set to true only meta data will be loaded.
	 * @return The Character sheet
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static CharacterSheet loadCharacterSheet(final File jsonFile, boolean onlyMetaData) throws JsonParseException, JsonMappingException, IOException {
		if(jsonFile == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		if(onlyMetaData) {
			mapper.addMixInAnnotations(CharacterSheet.class, CharacterSheetMixInClass.class);
		}
		FileInputStream inStream = new FileInputStream(jsonFile);
		CharacterSheet sheet = mapper.readValue(inStream, CharacterSheet.class);
		sheet.fileAbsolutePath = jsonFile.getAbsolutePath();
		return sheet;		
	}
	
	public void saveToJSONFile(final File jsonFile) throws JsonGenerationException, JsonMappingException, IOException {
		if(jsonFile == null) {
			return;
		}
		FileOutputStream outStream = new FileOutputStream(jsonFile);
		ObjectMapper mapper = new ObjectMapper();
		if(Template.USE_PRETTY_WRITER) {
			mapper.writerWithDefaultPrettyPrinter().writeValue(outStream, this);
		}
		else {
			mapper.writer().writeValue(outStream, this);
		}
	}

	public String toJSON() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writer().writeValueAsString(this);
	}
	
	public void takeOverChanges(final CharacterSheet otherSheet) {
		name = otherSheet.name;
		color = otherSheet.color;
		description = otherSheet.description;
		level = otherSheet.level;
	}

	// PARCELABLE START
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(color);
		dest.writeString(description);
		dest.writeInt(level);
		dest.writeString(fileAbsolutePath);
	}
	
	public static final Parcelable.Creator<CharacterSheet> CREATOR = new Creator<CharacterSheet>() {
		@Override
		public CharacterSheet[] newArray(int size) {
			return new CharacterSheet[size];
		}
		
		@Override
		public CharacterSheet createFromParcel(Parcel source) {
			// IMPORTANT read in same order as written (FIFO)
			CharacterSheet sheet = new CharacterSheet();
			sheet.name = source.readString();
			sheet.color = source.readInt();
			sheet.description = source.readString();
			sheet.level = source.readInt();
			sheet.fileAbsolutePath = source.readString();
			return sheet;
		}
	};
	// PARCELABLE END
}
