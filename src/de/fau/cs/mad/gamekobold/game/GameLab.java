package de.fau.cs.mad.gamekobold.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;
import android.content.Context;
import android.util.Log;

public class GameLab {
	// tag for logging
	private static String LOG_TAG = "GameLab";

	private static GameLab sGameLab;
	private ArrayList<Game> games;
	private Context appContext;
	private ArrayList<Template> templates;
	// time stamp for the games folder
	private long folderTimeStamp;

	GameLab(Context appContext) {
		this.appContext = appContext;
		this.games = new ArrayList<Game>();
		this.templates = new TemplateLab(appContext).getTemplates();
		folderTimeStamp = 0;
	}

	public static GameLab get(Context c) {
		if (sGameLab == null) {
			sGameLab = new GameLab(c.getApplicationContext());
		}
		return sGameLab;
	}

	/**
	 * Returns the list of available games on this device. Assures that the list
	 * is up to date, and reloads it if necessary. The games in the list DO NOT
	 * have a fake character element for adding a new character!
	 * 
	 * @return The list of available games on this device.
	 */
	public ArrayList<Game> getGames() {
		assureListIsUpToDate();
		return games;
	}

	/**
	 * The game DOES NOT have a fake character element for adding a new
	 * character!
	 * 
	 * @param gameName
	 *            The name of the game.
	 * @return The game with the given name or null if there is no such game.
	 */
	public Game getGame(String gameName) {
		assureListIsUpToDate();
		for (Game g : games) {
			if (g.getGameName().equals(gameName))
				return g;
		}
		return null;
	}

	/**
	 * Assures that the game list is up to date. If any thing changed the list
	 * will be updated.
	 */
	private void assureListIsUpToDate() {
		// checks if a game has been created or deleted
		if (!checkForGameDirectoryChange()) {
			// only check every game for a change when not reloading the whole
			// list
			checkEveryGameForChanges();
		}
	}

	/**
	 * Checks the game directory for a change. Its time stamp is updated when a
	 * file is created or deleted.
	 * 
	 * @return true if the game list has been reloaded, false otherwise.
	 */
	private boolean checkForGameDirectoryChange() {
		Log.d(LOG_TAG, "Checking for directory changes.");
		// get game root directory
		final File gameDir = JacksonInterface.getGameRootDirectory(appContext);
		if (gameDir != null) {
			final long newTimeStamp = gameDir.lastModified();
			// check time stamps
			if (folderTimeStamp < newTimeStamp) {
				// remove all old games
				games.clear();
				// add default test data

				// FIXME remove as not using!
				// addDefaultData();

				// update time stamp
				folderTimeStamp = newTimeStamp;
				// reload template list
				Log.d(LOG_TAG, "Reloading game list");
				Log.d(LOG_TAG, "game directory:" + gameDir.getAbsolutePath());
				// check if file is a valid directory
				if (gameDir.isDirectory()) {
					// get file list
					final File[] fileList = gameDir.listFiles();
					Game loadedGame = null;
					// for every file
					for (final File file : fileList) {
						try {
							// load game
							loadedGame = JacksonInterface.loadGame(file);
							// check if successful
							if (loadedGame != null) {
								// TODO LOAD CHARACTERS
								// add game to list
								games.add(loadedGame);
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks every game if it has been changed. If so it will be reloaded.
	 */
	private void checkEveryGameForChanges() {
		Log.d(LOG_TAG, "Checking every game for changes");
		// for every game
		for (Game game : games) {
			// get file for game
			final File gameFile = new File(game.getFileAbsolutePath());
			// check nullpointer
			if (gameFile != null) {
				// check time stamp
				if (gameFile.lastModified() > game.getFileTimeStamp()) {
					// update time stamp
					game.setFileTimeStamp(gameFile.lastModified());
					// game file has been changed
					try {
						// load game
						Game loadedGame = JacksonInterface.loadGame(gameFile);
						if (loadedGame != null) {
							// take over changes
							game.takeOverValues(loadedGame);
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
