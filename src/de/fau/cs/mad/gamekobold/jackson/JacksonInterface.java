package de.fau.cs.mad.gamekobold.jackson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.fau.cs.mad.gamekobold.R.string;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.filebrowser.CharacterExportTask;
import de.fau.cs.mad.gamekobold.filebrowser.FileTargetIsSourceException;
import de.fau.cs.mad.gamekobold.filebrowser.FileWouldOverwriteException;
import de.fau.cs.mad.gamekobold.filebrowser.TemplateExportTask;
import de.fau.cs.mad.gamekobold.game.Game;

public abstract class JacksonInterface {
	private static final String LOG_TAG = "JacksonInterface";

	public static final String CHARACTER_ROOT_FOLDER_NAME = "Characters";
	public static final String TEMPLATE_ROOT_FOLDER_NAME = "Templates";
	public static final String GAME_ROOT_FOLDER_NAME = "Games";

	/**
	 * If set to false, jackson will not intent or align anything. Uses less space if set to false.
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
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		if(onlyMetaData) {
			mapper.addMixInAnnotations(CharacterSheet.class, CharacterSheetMixInClass.class);
		}
		FileInputStream inStream = new FileInputStream(jsonFile);
		CharacterSheet sheet = mapper.readValue(inStream, CharacterSheet.class);
		sheet.setFileAbsolutePath(jsonFile.getAbsolutePath());
		sheet.setFileTimeStamp(jsonFile.lastModified());
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
	@SuppressLint("SimpleDateFormat")
	public static void saveCharacterSheet(final CharacterSheet sheet, final File jsonFile) throws JsonGenerationException, JsonMappingException, IOException {
		if(jsonFile == null || sheet == null) {
			return;
		}
		Log.d(LOG_TAG, " saving character. path:"+jsonFile.getAbsolutePath());
		FileOutputStream outStream = new FileOutputStream(jsonFile);
		ObjectMapper mapper = new ObjectMapper();
		if(use_pretty_writer) {
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
		}
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		sheet.setFileLastUpdated(dateFormat.format(date));
		mapper.writer().writeValue(outStream, sheet);
	}
	//
	// TEMPLATE FUNCTIONS START
	//
	// MISC FUNCTIONS
	/**
	 * Returns the file pointing to the subDir in the apps directory. The apps directory
	 * may be on the internal or external storage. If an external storage is mounted it
	 * will be preferred. Also creates the subDir if it does not exist.
	 * @param subDir The sub directory to get.
	 * @param context
	 * @return The file pointing to the subDir in the apps directory.
	 */
	private static File getRootDirectoryFor(final String subDir, final Context context) {
		// file for app root directory
		File appRootDir = getAppRootDirectory(context);
		// nullpointer check
		if(appRootDir == null) {
			return null;
		}
		// File for the requested sub directory
		final File ret = new File(appRootDir, subDir);
		// check if sub dir exists
		if(!ret.exists()) {
			// if not make it
			ret.mkdir();
		}
		return ret;
	}

	/**
	 * Returns the root directory for the app. The apps directory
	 * may be on the internal or external storage. If an external storage is mounted it
	 * will be preferred. Also creates the directory if it does not exist.
	 * @param context
	 * @return The root directory for this app.
	 */
	public static File getAppRootDirectory(final Context context) {
		if(context == null) {
			return null;
		}
		// file for app root directory
		File appRootDir = null;
		// check if we can write on external storage
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			// get external storage file
			final File externalStorage = Environment.getExternalStorageDirectory();
			// get file for a directory called $app_name on the external storage
			appRootDir = new File(externalStorage, context.getString(string.app_name));
		}
		else {
			// use internal storage
			appRootDir = context.getDir("data", Context.MODE_PRIVATE);
		}
		// nullpointer check
		if(appRootDir == null) {
			return null;
		}
		// check if directory exists
		if(!appRootDir.exists()) {
			// if not make it
			appRootDir.mkdir();
		}
		return appRootDir;
	}

	/**
	 * @param context
	 * @return The root template directory.
	 * Returns the root directory templates are saved for this device.
	 */
	public static File getTemplateRootDirectory(final Context context) {
		return getRootDirectoryFor(TEMPLATE_ROOT_FOLDER_NAME, context);
	}

	/**
	 * @param context
	 * @return The root game directory.
	 * Returns the root directory games are saved for this device.
	 */
	public static File getGameRootDirectory(final Context context) {
		return getRootDirectoryFor(GAME_ROOT_FOLDER_NAME, context);
	}

	/**
	 * 
	 * @param context
	 * @return The root character directory.
	 * Returns the root directory characters are saved for this device.
	 */
	public static File getCharacterRootDirectory(final Context context) {
		return getRootDirectoryFor(CHARACTER_ROOT_FOLDER_NAME, context);
	}
	
	public static File getCharacterImportDirectory(final Context context) {
		File ret = new File(getCharacterRootDirectory(context), "imported");
		if(!ret.exists()) {
			ret.mkdir();
		}
		return ret;
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
		//3 lines added
		if(!dir.exists()){
			dir.mkdirs();
		}
		//comments added -> tried something when storing failed
		//but problem was that date format wasn't given right in template.getFileName()
//		String pathWithTime = dir.getAbsolutePath() + File.separator + template.getFileName();
//		String path = pathWithTime;
//		if (null != pathWithTime && pathWithTime.length() > 0 )
//		{
//		    int endIndex = pathWithTime.lastIndexOf(" ");
//		    if (endIndex != -1)  
//		    {
//		        path = pathWithTime.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
//		    }
//		}  
//		Log.d("JacksonInterface", "path to store template: " + path);
//		Log.d("JacksonInterface", "just template name: " + path);
//		File theFileToWrite = new File(path);
//		if(!theFileToWrite.exists()){
//			theFileToWrite.createNewFile();
//		}
		FileOutputStream outStream = new FileOutputStream(dir.getAbsolutePath() + File.separator + template.getFileName());
//		FileOutputStream outStream = new FileOutputStream(theFileToWrite);
		saveTemplate(template, outStream);
		if(setLastEditedFlag) {
			// save in shared preferences the last edited template file name
			SharedPreferences pref = context.getSharedPreferences(SlideoutNavigationActivity.SHARED_PREFERENCES_FILE_NAME,  Activity.MODE_PRIVATE);
			SharedPreferences.Editor edit = pref.edit();
			edit.putString(SlideoutNavigationActivity.PREFERENCE_LAST_EDITED_TEMPLATE_NAME, template.getFileName());
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
		Log.d(LOG_TAG, "saving template. name:"+template.getTemplateName());
		ObjectMapper mapper = new ObjectMapper();
		if(use_pretty_writer) {
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
		}
		mapper.writer().writeValue(outStream, template);
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
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
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
		ret.setFileAbsPath(file.getAbsolutePath());
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
		File templateFile = new File(dir, fileName);
		FileInputStream inStream = new FileInputStream(templateFile);
		Template ret = loadTemplate(inStream, onlyMetaData);
		ret.setFileName(fileName);
		ret.setFileAbsPath(templateFile.getAbsolutePath());
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
		File rootDir = getRootDirectoryFor(CHARACTER_ROOT_FOLDER_NAME, context);
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
	
	/**
	 * Sanitizes the given string. The String returned does not contain any characters
	 * not allowed for file names. Returns an empty string on failure or if the given
	 * string contains only forbidden characters.
	 * @param stringToSanitize The string to sanitize.
	 * @return The sanitized string.
	 */
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
	
	public static void saveGame(Game game, Context context) throws JsonGenerationException, JsonMappingException, IOException {
		if(game == null || context == null) {
			return;
		}
		if(game.getFileAbsolutePath().isEmpty()) {
			final File gameFolder = JacksonInterface.getGameRootDirectory(context);
			String fileName = JacksonInterface.getSanitizedFileName(game.getGameName());
			if(gameFolder != null) {
				game.setFileAbsolutePath(gameFolder.getAbsolutePath() + File.separatorChar + fileName);
			}
		}
		File gameFile = new File(game.getFileAbsolutePath());
		saveGame(gameFile, game);
	}
	
	public static void saveGame(File gameFile, Game game) throws JsonGenerationException, JsonMappingException, IOException {
		FileOutputStream outStream = new FileOutputStream(gameFile);
		saveGame(outStream, game);
		game.setFileTimeStamp(gameFile.lastModified());
	}
	
	private static void saveGame(FileOutputStream outStream, Game game) throws JsonGenerationException, JsonMappingException, IOException {
		if(game == null || outStream == null) {
			return;
		}
//		Log.d("JACKSONINTERFACE", "saving game");
//		game.print();
		ObjectMapper mapper = new ObjectMapper();
//		if(use_pretty_writer) {
//			mapper.writerWithDefaultPrettyPrinter().writeValue(outStream, game);
//		}
//		else {
//			mapper.writer().writeValue(outStream, game);
//		}
		if(use_pretty_writer) {
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
		}
		mapper.writer().writeValue(outStream, game);
	}

	public static Game loadGame(File gameFile) throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {
		Game game = loadGame(new FileInputStream(gameFile));
		game.setFileAbsolutePath(gameFile.getAbsolutePath());
		game.setFileTimeStamp(gameFile.lastModified());
		return game;
	}
	
	private static Game loadGame(FileInputStream inStream) throws JsonParseException, JsonMappingException, IOException {
		if(inStream == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//		if(onlyMetaData) {
//			// in order to load only meta data we use mix in annotations
//			// the Template.Character sheet won't be loaded.
//			mapper.addMixInAnnotations(Template.class, TemplateMixInClass.class);
//		}
		Game game = mapper.readValue(inStream, Game.class);
//		Log.d("JACKSONINTERFACE", "loading game");
//		game.print();
		return game;	
	}
	
	public static void exportTemplate(final Context context, final File templateFile, final File targetFile, boolean overrideIfExists) throws FileWouldOverwriteException, FileTargetIsSourceException {
		if(templateFile == null || targetFile == null) {
			throw new NullPointerException();
		}
		if(templateFile.getAbsolutePath().equals(targetFile.getAbsolutePath())) {
			throw new FileTargetIsSourceException(targetFile);
		}
		if(targetFile.exists() && !overrideIfExists) {
			throw new FileWouldOverwriteException(targetFile);
		}
		TemplateExportTask exportTask = TemplateExportTask.getInstance(context);
		exportTask.execute(new File[] {templateFile, targetFile});
	}
	
	public static void exportCharacter(final Context context, final File characterFile, final File targetFile, boolean overrideIfExists) throws FileWouldOverwriteException, FileTargetIsSourceException {
		if(characterFile == null || targetFile == null) {
			throw new NullPointerException();
		}
		if(characterFile.getAbsolutePath().equals(targetFile.getAbsolutePath())) {
			throw new FileTargetIsSourceException(targetFile);
		}
		if(targetFile.exists() && !overrideIfExists) {
			throw new FileWouldOverwriteException(targetFile);
		}
		CharacterExportTask exportTask = CharacterExportTask.getInstance(context);
		exportTask.execute(new File[] {characterFile, targetFile});
	}
}
