package de.fau.cs.mad.rpgpack.jackson;

import java.io.File;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.fau.cs.mad.rpgpack.ThumbnailLoader;
import de.fau.cs.mad.rpgpack.templatebrowser.Template;

public class CharacterSheet implements Parcelable, Comparable<CharacterSheet> {
	/* METADATA */
	private String name;
	private int level;
	private String description;
	private int color = Color.RED;
	private String fileAbsolutePath = null;
	private long fileTimeStamp;
	private String fileLastUpdated;
	private String iconPath;
	private Template template;

	/* ROOT_TABLE */
	private ContainerTable rootTable = null;

	public CharacterSheet() {
		name = "";
		level = 0;
		description = "";
		fileAbsolutePath = "";
		fileTimeStamp = 0;
		fileLastUpdated = "";
		iconPath = "";
	}

	public CharacterSheet(String name) {
		this.name = name;
		this.level = 0;
		this.description = "";
		this.fileAbsolutePath = "";
		this.fileTimeStamp = 0;
		this.fileLastUpdated = "";
		this.iconPath = "";
	}

	public CharacterSheet(String name, int level, int color,
			String description, ContainerTable table) {
		this.name = name;
		this.level = level;
		this.color = color;
		this.description = description;
		this.rootTable = table;
		fileAbsolutePath = "";
		fileTimeStamp = 0;
		fileLastUpdated = "";
		iconPath = "";
	}

	public String getIconPath() {
		return iconPath;
	}

	@JsonProperty("icon")
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
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

	@JsonProperty("lvl")
	public void setLevel(int level) {
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	@JsonProperty("des")
	public void setDescription(String description) {
		this.description = description;
	}

	public int getColor() {
		return color;
	}

	@JsonProperty("clr")
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
		if (rootTable == null) {
			rootTable = new ContainerTable();
			rootTable.tableName = "rootTable";
		}
		return rootTable;
	}

	@JsonProperty("root")
	public void setRootTable(ContainerTable rootTable) {
		this.rootTable = rootTable;
	}

	/**
	 * @param withPrettyWriter
	 *            If true the writer will indent the output.
	 * @return Json representation of this CharacterSheet.
	 * @throws JsonProcessingException
	 */
	public String toJSON(boolean withPrettyWriter)
			throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		if (withPrettyWriter) {
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

	public String getFileLastUpdated() {
		return fileLastUpdated;
	}

	@JsonProperty("lastUpd")
	public void setFileLastUpdated(String fileLastUpdated) {
		this.fileLastUpdated = fileLastUpdated;
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
		dest.writeString(iconPath);
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
			sheet.iconPath = source.readString();
			return sheet;
		}
	};

	// PARCELABLE END

	/**
	 * Used for sorting with {@link Collections#sort(List)}. Sorts by
	 * {@link #getFileLastUpdated()}. Newest first oldest last.
	 */
	@Override
	public int compareTo(CharacterSheet another) {
		final int res = this.fileLastUpdated
				.compareToIgnoreCase(another.fileLastUpdated);
		if (res == 0) {
			return 0;
		}
		if (res < 0) {
			return 1;
		}
		return -1;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	@JsonIgnore
	public Template getTemplate() {
		return template;
	}

	// used in game edit to check if character is selected or not
	@Override
	public boolean equals(Object object) {
		boolean isEqual = false;

		if (object != null && object instanceof CharacterSheet) {
			isEqual = (this.fileAbsolutePath
					.equals(((CharacterSheet) object).fileAbsolutePath));
		}

		return isEqual;
	}

	@Override
	public int hashCode() {
		return this.fileAbsolutePath.hashCode();
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
}
