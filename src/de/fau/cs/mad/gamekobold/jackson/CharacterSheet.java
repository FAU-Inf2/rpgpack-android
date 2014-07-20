package de.fau.cs.mad.gamekobold.jackson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
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

public class CharacterSheet implements Parcelable{
	@JsonIgnore
	public static final String FOLDER_NAME = "Characters";
	
	/* METADATA */
	public String name;
	@JsonIgnore
	public int level;
	@JsonIgnore
	public String description;
	
	@JsonIgnore
	public int color = Color.parseColor("#2980b9");
	
	/* ROOT_TABLE */
	private ContainerTable rootTable = null;
	
	public CharacterSheet() {
		name = "";
		level = 3;
		description = "";
	}
	
	public CharacterSheet(String name) {
		this.name = name;		
		level = 3;
		description = "";
	}
	
	@JsonCreator
	public CharacterSheet(@JsonProperty("rootTable") ContainerTable table) {
		name = "";
		rootTable = table;
		level = 3;
		description = "";
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
	
//	public void saveToJSON(Context context, String fileName) throws JsonGenerationException, JsonMappingException, IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		File dir = context.getDir(FOLDER_NAME, Context.MODE_PRIVATE);
//		FileOutputStream outStream = new FileOutputStream(dir.getAbsolutePath() + File.separator + fileName);
//		mapper.writerWithDefaultPrettyPrinter().writeValue(outStream, this);
//	}
//	
//	public static CharacterSheet loadFromJSONFile(Context context, String fileName) throws JsonParseException, JsonMappingException, IOException {
//		ObjectMapper mapper = new ObjectMapper();
//		File dir = context.getDir(FOLDER_NAME, Context.MODE_PRIVATE);
//		FileInputStream inStream = new FileInputStream(dir.getAbsolutePath() + File.separator + fileName);
//		CharacterSheet sheet = mapper.readValue(inStream, CharacterSheet.class);
//		return sheet;
//	}
	
	/**
	 * Loads the CharacterSheet from the provided file.
	 * Only loads meta data! So no sheet data will be loaded!
	 * @param jsonFile File object for the character
	 * @return The CharacterSheet
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static CharacterSheet loadForCharacterList(File jsonFile) throws JsonParseException, JsonMappingException, IOException {
		if(jsonFile == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.addMixInAnnotations(CharacterSheet.class, CharacterSheetMixInClass.class);
		FileInputStream inStream = new FileInputStream(jsonFile);
		CharacterSheet sheet = mapper.readValue(inStream, CharacterSheet.class);
		return sheet;
	}
	
	public String toJSON() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writer().writeValueAsString(this);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(color);
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
			return sheet;
		}
	};
}
