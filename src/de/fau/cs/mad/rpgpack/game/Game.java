package de.fau.cs.mad.rpgpack.game;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.fau.cs.mad.rpgpack.jackson.CharacterSheet;
import de.fau.cs.mad.rpgpack.jackson.JacksonInterface;

/**
 * This class represents one game group. All played characters are in
 * <code>characterList</code>.
 * 
 */
// TODO added new Element gameMaster and worldName
@SuppressWarnings("serial")
public class Game implements Parcelable, Serializable {
	private String gameName;
	private String gameMaster;
	private String date = null;
	private String description;
	private List<CharacterSheet> characterSheetList;
	private String worldName;
	private String iconPath;
	private String fileAbsolutePath;
	private long fileTimeStamp;

	public Game(String gameName, String gameMaster, String date,
			String description, List<CharacterSheet> characterSheetList,
			String iconPath) {
		this(gameName, date, iconPath);
		this.gameMaster = gameMaster;
		this.description = description;
		this.setCharacterSheetList(characterSheetList);
		this.fileAbsolutePath = "";
		this.fileTimeStamp = 0;
	}

	public Game(String gameName, String date, String iconPath) {
		this(gameName, date);
		this.iconPath = iconPath;
	}

	public Game(String gameName, String date) {
		this(gameName);
		this.date = date;
	}

	// fake last item for create new game
	public Game(String gameName) {
		this();
		this.gameName = gameName;
		this.iconPath = "";
	}

	/**
	 * needed for json.
	 */
	@JsonCreator
	public Game() {
		/**
		 * need to set all fields to default values for json.
		 */
		this.gameName = "";
		this.gameMaster = "";
		this.date = "";
		this.description = "";
		this.characterSheetList = new ArrayList<CharacterSheet>();
		this.iconPath = "";
		this.fileAbsolutePath = "";
		this.fileTimeStamp = 0;
		this.worldName = "";
	}

	@JsonIgnore
	public boolean addCharacterSheet(CharacterSheet characterSheet) {
		characterSheetList.add(characterSheet);
		return true;
	}

	@JsonIgnore
	public boolean removeCharacterSheet(CharacterSheet characterSheet) {
		characterSheetList.remove(characterSheet);
		return true;
	}

	public boolean isInCharacterList(CharacterSheet characterSheet) {
		return this.characterSheetList.contains(characterSheet);
	}

	public String getGameName() {
		return gameName;
	}

	@JsonProperty("name")
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameMaster() {
		return gameMaster;
	}

	public void setGameMaster(String gameMaster) {
		this.gameMaster = gameMaster;
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public String getDate() {
		return date;
	}

	@JsonProperty("date")
	public void setDate(String date) {
		this.date = date;
	}

	public String getDescription() {
		if (description == null) {
			description = "";
		}
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	public List<CharacterSheet> getCharacterSheetList() {
		return characterSheetList;
	}

	@JsonIgnore
	public void setCharacterSheetList(List<CharacterSheet> characterSheetList) {
		this.characterSheetList.clear();
		this.characterSheetList.addAll(characterSheetList);
		return;
	}

	/**
	 * Needed for jackson
	 * 
	 * @return
	 */
	@JsonProperty("characters")
	private List<String> getCharacterList() {
		ArrayList<String> characterPaths = new ArrayList<String>();
		for (final CharacterSheet sheet : characterSheetList) {
			characterPaths.add(sheet.getFileAbsolutePath());
		}
		return characterPaths;
	}

	/**
	 * Needed for jackson
	 * 
	 * @return
	 */
	@JsonProperty("characters")
	private void setCharacterList(List<String> characterList) {
		for (final String absPath : characterList) {
			try {
				File characterFile = new File(absPath);
				CharacterSheet character = JacksonInterface.loadCharacterSheet(
						characterFile, false);
				characterSheetList.add(character);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String getIconPath() {
		return iconPath;
	}

	@JsonProperty("icon")
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
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

	public void takeOverValues(final Game otherGame) {
		this.gameName = otherGame.gameName;
		this.gameMaster = otherGame.gameMaster;
		this.date = otherGame.date;
		this.description = otherGame.description;
		this.characterSheetList = otherGame.characterSheetList;
		this.fileAbsolutePath = otherGame.fileAbsolutePath;
		this.fileTimeStamp = otherGame.fileTimeStamp;
		this.iconPath = otherGame.iconPath;
		this.worldName = otherGame.worldName;
	}

	// PARCELABLE START
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(gameName);
		dest.writeString(gameMaster);
		dest.writeString(date);
		dest.writeString(description);
		dest.writeList(characterSheetList);
		dest.writeString(iconPath);
		dest.writeString(fileAbsolutePath);
		dest.writeLong(fileTimeStamp);
		dest.writeString(worldName);
	}

	public static final Parcelable.Creator<Game> CREATOR = new Creator<Game>() {
		@Override
		public Game[] newArray(int size) {
			return new Game[size];
		}

		@Override
		public Game createFromParcel(Parcel source) {
			// IMPORTANT read in same order as written (FIFO)
			Game game = new Game();
			game.setGameName(source.readString());
			game.setGameMaster(source.readString());
			game.setDate(source.readString());
			game.setDescription(source.readString());
			List<CharacterSheet> characterList = new ArrayList<CharacterSheet>();
			source.readList(characterList,
					CharacterSheet.class.getClassLoader());
			game.setCharacterSheetList(characterList);
			game.setIconPath(source.readString());
			game.setFileAbsolutePath(source.readString());
			game.setFileTimeStamp(source.readLong());
			game.setWorldName(source.readString());
			return game;
		}
	};

}
