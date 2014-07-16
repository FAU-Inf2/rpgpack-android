package de.fau.cs.mad.gamekobold.jackson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
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

import de.fau.cs.mad.gamekobold.template_generator.TemplateGeneratorActivity;
public class Template implements Parcelable{
	@JsonIgnore
	public static boolean USE_PRETTY_WRITER = true;
	@JsonIgnore
	public static final String FOLDER_NAME = "Templates";
	@JsonIgnore
	public static final String PARCELABLE_STRING = "JacksonTemplate";
	/* META DATA */
	@JsonIgnore
	private String fileName = null;
	public String templateName;
	public String gameName;
	public String author;
	public String date;
	public int iconID;
	public String description;
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
	
//	public void print() {
//		Log.d("TEMPLATE", "name:" + templateName);
//		Log.d("TEMPLATE", "game:" + gameName);
//		Log.d("TEMPLATE", "author:" + author);
//		Log.d("TEMPLATE", "date:" + date);
//		Log.d("TEMPLATE", "icon:" + iconID);
//		Log.d("TEMPLATE", "description:" + description);
//		if(characterSheet != null) {
//			characterSheet.print();
//		}
//		else {
//			Log.d("TEMPLATE", "characterSheet:null");
//		}
//	}
	
	
	public static String getSanitizeFileNameForTemplate(Template template) {
		final String forbiddenCharacters = "/\\?%*:|\"<>1";
		StringBuilder builder = new StringBuilder();
		for(final char character : template.templateName.toCharArray()) {
			if(forbiddenCharacters.indexOf(character) != -1) {
				continue;
			}
			builder.append(character);
		}
		if(!builder.toString().isEmpty()) {
			builder.append("-"+template.date);
		}
		else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss");
			Date date = new Date();
			builder.append(format.format(date));
		}
		
		//Log.d("sanitizeFileName:", "orig:"+template.fileName+" sani:"+builder.toString());
		return builder.toString();
	}
	
	@JsonIgnore
	public String getFileName() {
		if(fileName == null) {
			fileName = getSanitizeFileNameForTemplate(this);
		}
		return fileName;
	}
	
	//TODO vllt unter files/Templates/ speichern
	/**
	 * @param activity 
	 * @param fileName file name to be used
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * Saves the template as a json file in the template directory with the given name
	 */
	public void saveToJSON(Activity activity) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File dir = getTemplateDirectory(activity);
		FileOutputStream outStream = new FileOutputStream(dir.getAbsolutePath() + File.separator + getFileName());
		if(USE_PRETTY_WRITER) {
			mapper.writerWithDefaultPrettyPrinter().writeValue(outStream, this);
		}
		else {
			mapper.writer().writeValue(outStream, this);
		}
		// save in shared preferences the last edited template file name
		SharedPreferences pref = activity.getSharedPreferences(TemplateGeneratorActivity.SHARED_PREFERENCES_FILE_NAME,  Activity.MODE_PRIVATE);
		SharedPreferences.Editor edit = pref.edit();
		edit.putString(TemplateGeneratorActivity.LAST_EDITED_TEMPLATE_NAME, getFileName());
		edit.commit();
	}

	/**
	 * @param context
	 * @return the template directory
	 * Returns the directory templates are saved for this device
	 */
	public static File getTemplateDirectory(Context context) {
		File templateDir;
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File externalStorage = Environment.getExternalStorageDirectory();
			templateDir = new File(externalStorage.getAbsolutePath() + File.separatorChar + FOLDER_NAME);
			if(!templateDir.exists()) {
				templateDir.mkdir();
			}
		}
		else {
			templateDir = context.getDir(FOLDER_NAME, Context.MODE_PRIVATE);
		}
		return templateDir;
	}
	
	/**
	 * Checks whether the file for this template exists on the file system.
	 * @param context
	 * @return true if the file exists, false otherwise
	 */
	public boolean doesTemplateFileExist(Context context) {
		File dir = getTemplateDirectory(context);
		File templateFile = new File(dir, getFileName());
		return templateFile.exists();
	}
	
	/**
	 * 
	 * @param context
	 * @param fileName Template file name
	 * @return Loaded template
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * Loads the template with the given file name. The file is supposed to be in the template directory.
	 */
	public static Template loadFromJSONFile(Context context, String fileName) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File dir = getTemplateDirectory(context);
		FileInputStream inStream = new FileInputStream(dir.getAbsolutePath() + File.separator + fileName);
		Template template = mapper.readValue(inStream, Template.class);
		return template;
	}
	
	/**
	 * 
	 * @param templateFile template to be loaded
	 * @return the loaded template
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 
	 * Loads the template meta data saved in @templateFile. No template structure is loaded.
	 * Only items like name, author, game name are loaded!
	 */
	public static Template loadFromJSONFileForTemplateBrowser(File templateFile) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		// in order to load only meta data we use mix in annotations
		// the Template.Character sheet won't be loaded.
		mapper.addMixInAnnotations(Template.class, TemplateMixInClass.class);
		FileInputStream inStream = new FileInputStream(templateFile);
		Template template = mapper.readValue(inStream, Template.class);
		return template;
	}

	/**
	 * @return Json representation of this template
	 * @throws JsonProcessingException
	 */
	public String toJSON() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writer().writeValueAsString(this);
	}
	
	/**
	 * @param context
	 * @return the character root folder directory
	 * Returns the directory in which all characters for this template are stored
	 * The structure should be like "app/Characters/$TemplateName/Character1.json"
	 */
	public File getDirectoryForCharacters(Context context) {
		File rootDir;
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File externalStorage = Environment.getExternalStorageDirectory();
			rootDir = new File(externalStorage.getAbsolutePath() + File.separatorChar + CharacterSheet.FOLDER_NAME);
			if(!rootDir.exists()) {
				rootDir.mkdir();
			}
		}
		else {
			rootDir = context.getDir(CharacterSheet.FOLDER_NAME, Context.MODE_PRIVATE);
		}
		// find folder for this template
		File[] subDirs = rootDir.listFiles();
		for (final File dir : subDirs) {
			if(dir.isDirectory()) {
				if(dir.getName().equals(getFileName())) {
					return dir;
				}
			}
		}
		// folder not found, so we create a new one
		File characterFolder = new File(rootDir.getAbsolutePath() + File.separatorChar + getFileName());
		characterFolder.mkdir();
		return characterFolder;
	}
	
	// TODO move to some other class?
	/**
	 * 
	 * @param context context
	 * @param template template for which to get the directory
	 * @return directory for the given template in which the characters are saved
	 */
	public static File getDirectoryForCharacters(Context context, de.fau.cs.mad.gamekobold.templatebrowser.Template template) {
		File rootDir;
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File externalStorage = Environment.getExternalStorageDirectory();
			rootDir = new File(externalStorage.getAbsolutePath() + File.separatorChar + CharacterSheet.FOLDER_NAME);
			if(!rootDir.exists()) {
				rootDir.mkdir();
			}
		}
		else {
			rootDir = context.getDir(CharacterSheet.FOLDER_NAME, Context.MODE_PRIVATE);
		}
		// find folder for this template
		File[] subDirs = rootDir.listFiles();

		for (final File dir : subDirs) {
			if(dir.isDirectory()) {
				if(dir.getName().equals(template.getFileName())) {
					return dir;
				}
			}
		}
		// folder not found, so we create a new one
		File characterFolder = new File(rootDir.getAbsolutePath() + File.separatorChar + template.getFileName());
		characterFolder.mkdir();
		return characterFolder;
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
