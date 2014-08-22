package de.fau.cs.mad.gamekobold.jackson;


import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CharacterSheet implements Parcelable{	
	/* METADATA */
	public String name;
	public int level;
	public String description;
	public int color = Color.parseColor("#2980b9");
	@JsonIgnore
	public String fileAbsolutePath = null;
	@JsonIgnore
	public long fileTimeStamp;

	/* ROOT_TABLE */
	private ContainerTable rootTable = null;

	public CharacterSheet() {
		name = "";
		level = 0;
		description = "";
		fileAbsolutePath = "";
		fileTimeStamp = 0;
	}

	public CharacterSheet(String name) {
		this.name = name;		
		level = 0;
		description = "";
		fileAbsolutePath = "";
		fileTimeStamp = 0;
	}

	@JsonCreator
	public CharacterSheet(@JsonProperty("name") String name,
							@JsonProperty("level") int level,
							@JsonProperty("color") int color,
							@JsonProperty("description") String description,
							@JsonProperty("rootTable") ContainerTable table) {
		this.name = name;
		this.rootTable = table;
		this.level = level;
		this.description = description;
		fileAbsolutePath = "";
		fileTimeStamp = 0;
	}

	public ContainerTable getRootTable() {
		if(rootTable == null) {
			rootTable = new ContainerTable();
			rootTable.tableName = "rootTable";
		}
		return rootTable;
	}

	public String toJSON(boolean withPrettyWriter) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		if(withPrettyWriter) {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
		}
		else {
			return mapper.writer().writeValueAsString(this);
		}
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
		dest.writeLong(fileTimeStamp);
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
			sheet.fileTimeStamp = source.readLong();
			return sheet;
		}
	};
	// PARCELABLE END
}
