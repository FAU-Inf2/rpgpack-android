package de.fau.cs.mad.gamekobold.jackson;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CharacterSheet implements Parcelable{	
	/* METADATA */
	private String name;
	private int level;
	private String description;
	private int color = Color.RED;
	private String fileAbsolutePath = null;
	private long fileTimeStamp;

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
		this.level = 0;
		this.description = "";
		this.fileAbsolutePath = "";
		this.fileTimeStamp = 0;
	}

	public CharacterSheet(String name,
							int level,
							int color,
							String description,
							ContainerTable table) {
		this.name = name;
		this.level = level;
		this.color = color;
		this.description = description;
		this.rootTable = table;
		fileAbsolutePath = "";
		fileTimeStamp = 0;
	}
	
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	@JsonProperty("level")
	public void setLevel(int level) {
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	public int getColor() {
		return color;
	}
	
	@JsonProperty("color")
	public void setColor(int color) {
		this.color = color;
	}

	public String getFileAbsolutePath() {
		return fileAbsolutePath;
	}

	@JsonIgnore
	public void setFileAbsolutePath(String fileAbsolutePath) {
		this.fileAbsolutePath = fileAbsolutePath;
	}

	public long getFileTimeStamp() {
		return fileTimeStamp;
	}

	@JsonIgnore
	public void setFileTimeStamp(long fileTimeStamp) {
		this.fileTimeStamp = fileTimeStamp;
	}

	public ContainerTable getRootTable() {
		if(rootTable == null) {
			rootTable = new ContainerTable();
			rootTable.tableName = "rootTable";
		}
		return rootTable;
	}

	@JsonProperty("rootTable")
	public void setRootTable(ContainerTable rootTable) {
		this.rootTable = rootTable;
	}

	public String toJSON(boolean withPrettyWriter) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		if(withPrettyWriter) {
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
		}
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
