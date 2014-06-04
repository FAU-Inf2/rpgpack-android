package de.fau.cs.mad.gamekobold.jackson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CharacterSheet {
	@JsonIgnore
	private static final String FOLDER_NAME = "Characters";
	
	/* METADATA */
	public String name;
	/* ROOT_TABLE */
	public ContainerTable rootTable;
	
	public CharacterSheet() {
		rootTable = new ContainerTable();
		rootTable.tableName = "rootTable";
	}
	
	@JsonCreator
	public CharacterSheet(@JsonProperty("rootTable") ContainerTable table) {
		rootTable = table;
	}
	
	public void print() {
		Log.d("CHARACTER_SHEET","CharacterSheet:"+name);
		rootTable.print();
	}
	
	//TODO vllt unter files/Templates/ speichern
	public void saveToJSON(Context context, String fileName) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File dir = context.getDir(FOLDER_NAME, Context.MODE_PRIVATE);
		FileOutputStream outStream = new FileOutputStream(dir.getAbsolutePath() + File.separator + fileName);
		mapper.writerWithDefaultPrettyPrinter().writeValue(outStream, this);
	}
	
	public static CharacterSheet loadFromJSONFile(Context context, String fileName) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File dir = context.getDir(FOLDER_NAME, Context.MODE_PRIVATE);
		FileInputStream inStream = new FileInputStream(dir.getAbsolutePath() + File.separator + fileName);
		CharacterSheet sheet = mapper.readValue(inStream, CharacterSheet.class);
		return sheet;
	}
	
	public String toJSON() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writer().writeValueAsString(this);
	}
}
