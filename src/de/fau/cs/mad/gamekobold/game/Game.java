package de.fau.cs.mad.gamekobold.game;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

/**
 * This class represents one game group. All played characters are in
 * <code>characterList</code>.
 * 
 */
public class Game implements Parcelable, Serializable {
	private String gameName;
	private String author;
	private String date = null;
	private List<String> tagList;
	private String description;
//	private List<GameCharacter> characterList;
	private List<CharacterSheet> characterSheetList;

	private String iconPath;
	private String fileAbsolutePath;
	private long fileTimeStamp;

	public Game(String gameName, String author, String date,
			List<String> tagList, String description,
			List<CharacterSheet> characterSheetList, String iconPath) {
		this(gameName, date, iconPath);
		this.tagList = tagList;
		this.description = description;
//		this.setCharakterList(characterList);
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
		this.author = "";
		this.date = "";
		this.tagList = new LinkedList<String>();
		this.description = "";
//		this.characterList = new ArrayList<GameCharacter>();
		this.characterSheetList = new ArrayList<CharacterSheet>();
		this.iconPath = "";
		this.fileAbsolutePath = "";
		this.fileTimeStamp = 0;
	}
	@JsonIgnore
	public boolean addCharacterSheet(CharacterSheet characterSheet) {
		Log.e("Character is null?", "" + (characterSheet == null));
		Log.e("List is null?", "" + (characterSheetList == null));
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

//	public boolean addCharacter(GameCharacter character) {
//		Log.e("Character is null?", "" + (character == null));
//		Log.e("List is null?", "" + (characterList == null));
//		characterList.add(character);
//		return true;
//	}
//
//	public boolean removeCharacter(GameCharacter character) {
//		characterList.remove(character);
//		return true;
//	}
	
	public String getGameName() {
		return gameName;
	}

	@JsonProperty("name")
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getAuthor() {
		return author;
	}

	@JsonProperty("author")
	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	@JsonProperty("date")
	public void setDate(String date) {
		this.date = date;
	}

	public List<String> getTagList() {
		return tagList;
	}

	@JsonProperty("tags")
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
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

//	public List<GameCharacter> getCharacterList() {
//		return characterList;
//	}
//
//	@JsonProperty("characters")
//	public void setCharakterList(List<GameCharacter> characterList) {
//		Log.e("CharacterList",
//				"Setting CharacterList to " + characterList.size());
//		this.characterList.clear();
//		this.characterList.addAll(characterList);
//		return;
//	}
	
	@JsonIgnore
	public List<CharacterSheet> getCharacterSheetList() {
		return characterSheetList;
	}
	
	@JsonIgnore
	public void setCharacterSheetList(List<CharacterSheet> characterSheetList) {
		Log.e("characterSheetList",
				"Setting characterSheetList to " + characterSheetList.size());
		this.characterSheetList.clear();
		this.characterSheetList.addAll(characterSheetList);
		return;
	}

	private List<String> getCharacterList() {
		ArrayList<String> characterPaths = new ArrayList<String>();
		for(final CharacterSheet sheet : characterSheetList) {
			characterPaths.add(sheet.getFileAbsolutePath());
		}
		return  characterPaths;
	}
	
	@JsonProperty("characters")
	private void setCharacterList(List<String> characterList) {
		for(final String absPath : characterList) {
			try {
					File characterFile = new File(absPath);
					CharacterSheet character = JacksonInterface.loadCharacterSheet(characterFile, false);
					characterSheetList.add(character);
			}
			catch(Exception e) {
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
		this.author = otherGame.author;
		this.date = otherGame.date;
		this.tagList = otherGame.tagList;
		this.description = otherGame.description;
//		this.characterList = otherGame.characterList;
		this.characterSheetList = otherGame.characterSheetList;
		this.fileAbsolutePath = otherGame.fileAbsolutePath;
		this.fileTimeStamp = otherGame.fileTimeStamp;
		this.iconPath = otherGame.iconPath;
	}
	
	// PARCELABLE START
		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(gameName);
			dest.writeString(author);
			dest.writeString(date);
			dest.writeStringList(tagList);
			dest.writeString(description);
			dest.writeList(characterSheetList);
			dest.writeString(iconPath);
			dest.writeString(fileAbsolutePath);
			dest.writeLong(fileTimeStamp);

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
				game.setAuthor(source.readString());
				game.setDate(source.readString());
				List<String> tagList = new LinkedList<String>();
				source.readStringList(tagList);
				game.setTagList(tagList);
				game.setDescription(source.readString());
				List<CharacterSheet> characterList = new ArrayList<CharacterSheet>();
				source.readList(characterList, CharacterSheet.class.getClassLoader());
				game.setIconPath(source.readString());
				game.setFileAbsolutePath(source.readString());
				game.setFileTimeStamp(source.readLong());
				return game;
			}
		};

		// PARCELABLE END
}
