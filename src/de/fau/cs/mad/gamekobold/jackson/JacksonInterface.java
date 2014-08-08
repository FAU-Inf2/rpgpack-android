package de.fau.cs.mad.gamekobold.jackson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;

public abstract class JacksonInterface {

	public static final String CHARACTER_ROOT_FOLDER_NAME = "Characters";
	public static final String TEMPLATE_ROOT_FOLDER_NAME = "Templates";

	/**
	 * If set to false, jackson will not intend or align anything. Uses less space if set to false.
	 */
	public static boolean use_pretty_writer = true;

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
		sheet.fileTimeStamp = jsonFile.lastModified();
		return sheet;		
	}

	/**
	 * Saves the CharacterSheet to the File.
	 * @param sheet The CharacterSheet to save.
	 * @param jsonFile The File to which the sheet will be saved.
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void saveCharacterSheet(final CharacterSheet sheet, final File jsonFile) throws JsonGenerationException, JsonMappingException, IOException {
		if(jsonFile == null || sheet == null) {
			return;
		}
		FileOutputStream outStream = new FileOutputStream(jsonFile);
		ObjectMapper mapper = new ObjectMapper();
		if(use_pretty_writer) {
			mapper.writerWithDefaultPrettyPrinter().writeValue(outStream, sheet);
		}
		else {
			mapper.writer().writeValue(outStream, sheet);
		}
	}
	//
	// TEMPLATE FUNCTIONS START
	//
	// MISC FUNCTIONS
	/**
	 * @param context
	 * @return The root template directory.
	 * Returns the root directory templates are saved for this device.
	 */
	public static File getTemplateRootDirectory(final Context context) {
		if(context == null) {
			return null;
		}
		File templateDir;
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File externalStorage = Environment.getExternalStorageDirectory();
			templateDir = new File(externalStorage.getAbsolutePath() + File.separatorChar + TEMPLATE_ROOT_FOLDER_NAME);
			if(!templateDir.exists()) {
				templateDir.mkdir();
			}
		}
		else {
			templateDir = context.getDir(TEMPLATE_ROOT_FOLDER_NAME, Context.MODE_PRIVATE);
		}
		return templateDir;
	}

	/**
	 * Checks whether the file for the template exists on the file system.
	 * @param context
	 * @return true if the file exists, false otherwise
	 */
	public static boolean doesTemplateFileExist(final Template template, final Context context) {
		if(template == null || context == null) {
			// TODO thats not right
			return false;
		}
		File dir = getTemplateRootDirectory(context);
		File templateFile = new File(dir, template.getFileName());
		return templateFile.exists();
	}

	// SAVING FUNCTIONS
	/**
	 * Saves the template to a file. The filename and directory will automatically be determined.
	 * @param template The template to save.
	 * @param context
	 * @param setLastEditedFlag If set to true, the "last edited template" preference will be updated.
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void saveTemplate(final Template template, final Context context, boolean setLastEditedFlag) throws JsonGenerationException, JsonMappingException, IOException {
		if(template == null || context == null) {
			return;
		}
		File dir = getTemplateRootDirectory(context);
		FileOutputStream outStream = new FileOutputStream(dir.getAbsolutePath() + File.separator + template.getFileName());
		saveTemplate(template, outStream);
		if(setLastEditedFlag) {
			// save in shared preferences the last edited template file name
			SharedPreferences pref = context.getSharedPreferences(SlideoutNavigationActivity.SHARED_PREFERENCES_FILE_NAME,  Activity.MODE_PRIVATE);
			SharedPreferences.Editor edit = pref.edit();
			edit.putString(SlideoutNavigationActivity.LAST_EDITED_TEMPLATE_NAME, template.getFileName());
			edit.commit();
		}
	}

	/**
	 * Saves the template to the given file
	 * @param template The template to save.
	 * @param file The file to which the template will be saved to.
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static void saveTemplate(final Template template, final File file) throws JsonGenerationException, JsonMappingException, IOException {
		if(template == null || file == null) {
			return;
		}
		FileOutputStream outStream = new FileOutputStream(file);
		saveTemplate(template, outStream);
	}

	/**
	 * Writes the template to the given FileOutputStream.
	 * @param template The template to write.
	 * @param outStream FileOutputStream to be used.
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private static void saveTemplate(final Template template, final FileOutputStream outStream) throws JsonGenerationException, JsonMappingException, IOException {
		if(template == null || outStream == null) {
			return;
		}
		ObjectMapper mapper = new ObjectMapper();
		if(use_pretty_writer) {
			mapper.writerWithDefaultPrettyPrinter().writeValue(outStream, template);
		}
		else {
			mapper.writer().writeValue(outStream, template);
		}
	}

	// LOADING FUNCTIONS

	/**
	 *
	 * @param inStream FileInputStream to use for loading.
	 * @param onlyMetaData If set to true only meta data will be loaded.
	 * @return The loaded template
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * Loads a template using the FileInputStream.
	 */
	private static Template loadTemplate(FileInputStream inStream, boolean onlyMetaData) throws JsonParseException, JsonMappingException, IOException {
		if(inStream == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		if(onlyMetaData) {
			// in order to load only meta data we use mix in annotations
			// the Template.Character sheet won't be loaded.
			mapper.addMixInAnnotations(Template.class, TemplateMixInClass.class);
		}
		Template template = mapper.readValue(inStream, Template.class);
		return template;				
	}

	/**
	 * Loads a template using the given file.
	 * @param file The template file.
	 * @param onlyMetaData Set to true if you want to load only the metadata of the template.
	 * @return The loaded Template
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static Template loadTemplate(File file, boolean onlyMetaData) throws JsonParseException, JsonMappingException, IOException {
		if(file == null) {
			return null;
		}
		FileInputStream inStream = new FileInputStream(file);
		Template ret = loadTemplate(inStream, onlyMetaData);
		ret.setFileName(file.getName());
		return ret;
	}

	/**
	 * 
	 * @param context
	 * @param fileName Template file name
	 * @param onlyMetaData Set to true if you want to load only the metadata of the template.
	 * @return The loaded template
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * Loads the template with the given file name. The file is supposed to be in the template directory.
	 */
	public static Template loadTemplate(Context context, String fileName, boolean onlyMetaData) throws JsonParseException, JsonMappingException, IOException {
		if(context == null || fileName == null) {
			return null;
		}
		File dir = getTemplateRootDirectory(context);
		FileInputStream inStream = new FileInputStream(dir.getAbsolutePath() + File.separator + fileName);
		Template ret = loadTemplate(inStream, onlyMetaData);
		ret.setFileName(fileName);
		return ret;
	}
	// CHARACTER FOLDER FUNCTIONS
	/**
	 * 
	 * @param template Template for which to get the directory
	 * @param context
	 * @param createIfNotExists If set to true the directory will be created if it does not already exist.
	 * @return Directory for the given template in which its characters are saved.
	 */
	public static File getDirectoryForCharacters(final Template template, final Context context, boolean createIfNotExists) {
		if(template == null || context == null) {
			return null;
		}
		return getDirectoryForCharacters(template.getFileName(), context, createIfNotExists);
	}
	/**
	 * 
	 * @param template Template for which to get the directory
	 * @param context
	 * @param createIfNotExists If set to true the directory will be created if it does not already exist.
	 * @return Directory for the given template in which its characters are saved.
	 */
	public static File getDirectoryForCharacters(final de.fau.cs.mad.gamekobold.templatebrowser.Template template,
												final Context context, boolean createIfNotExists) {
		if(template == null || context == null) {
			return null;
		}
		return getDirectoryForCharacters(template.getFileName(), context, createIfNotExists);
	}

	/**
	 * 
	 * @param templateFileName The template's filename for which to get the directory.
	 * @param context
	 * @param createIfNotExists If set to true the directory will be created if it does not already exist.
	 * @return Directory for the given template in which its characters are saved.
	 */
	public static File getDirectoryForCharacters(final String templateFileName, final Context context, boolean createIfNotExists) {
		if(templateFileName.isEmpty()) {
			return null;
		}
		File rootDir;
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File externalStorage = Environment.getExternalStorageDirectory();
			rootDir = new File(externalStorage.getAbsolutePath() + File.separatorChar + CHARACTER_ROOT_FOLDER_NAME);
			if(!rootDir.exists()) {
				rootDir.mkdir();
			}
		}
		else {
			rootDir = context.getDir(CHARACTER_ROOT_FOLDER_NAME, Context.MODE_PRIVATE);
		}
		// find folder for this template
		File[] subDirs = rootDir.listFiles();
		for (final File dir : subDirs) {
			if(dir.isDirectory()) {
				if(dir.getName().equals(templateFileName)) {
					return dir;					
				}
			}
		}
		if(!createIfNotExists) {
			return null;
		}
		// folder not found, so we create a new one
		File characterFolder = new File(rootDir.getAbsolutePath() + File.separatorChar + templateFileName);
		characterFolder.mkdir();
		return characterFolder;
	}
	
	//
	public static String getSanitizedFileName(String stringToSanitize) {
		if(stringToSanitize == null) {
			return "";
		}
		if(stringToSanitize.isEmpty()) {
			return "";
		}
		final String forbiddenCharacters = "/\\?%*:|\"<>";
		StringBuilder builder = new StringBuilder();
		for(final char character : stringToSanitize.toCharArray()) {
			if(forbiddenCharacters.indexOf(character) != -1) {
				continue;
			}
			builder.append(character);
		}
		return builder.toString();
	}
}
