package de.fau.cs.mad.gamekobold.game;

import java.io.File;
import java.util.ArrayList;

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

	public ArrayList<Game> getGames() {
		assureListIsUpToDate();
		return games;
	}

	public Game getGame(String gameName) {
		assureListIsUpToDate();
		for (Game g : games) {
			if (g.getGameName().equals(gameName))
				return g;
		}
		return null;
	}

	private void assureListIsUpToDate() {
		// checks if a game has been created or deleted
		if(!checkForGameDirectoryChange()) {
			// only check every game for a change when not reloading the whole list
			checkEveryGameForChanges();
		}
	}

	/**
	 * Checks the game directory for a change. Its time stamp is updated
	 * when a file is created or deleted.
	 * @return true if the game list has been reloaded, false otherwise.
	 */
	private boolean checkForGameDirectoryChange() {
		final File gameDir = JacksonInterface.getGameRootDirectory(appContext);
		if(gameDir != null) {
			final long newTimeStamp = gameDir.lastModified();
			if(folderTimeStamp < newTimeStamp) {
				games.clear();
				// add default test data
				addDefaultData();
				// update time stamp
				folderTimeStamp = newTimeStamp;
				// reload template list
				Log.d(LOG_TAG, "Reloading game list");
				Log.d(LOG_TAG, "game directory:" + gameDir.getAbsolutePath());
				if (gameDir.isDirectory()) {
					final File[] fileList = gameDir.listFiles();
					Game loadedGame = null;
					for (final File file : fileList) {
						try {
							loadedGame = JacksonInterface.loadGame(file);
							if (loadedGame != null) {
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
		for(Game game : games) {
			final File gameFile = new File(game.getFileAbsolutePath());
			if(gameFile != null) {
				if(gameFile.lastModified() > game.getFileTimeStamp()) {
					game.setFileTimeStamp(gameFile.lastModified());
					// game file has been changed
					try {
						Game loadedGame = JacksonInterface.loadGame(gameFile);
						if (loadedGame != null) {
							// take over changes
							game.takeOverValues(loadedGame);
						}					
					}
					catch(Throwable e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void addDefaultData() {
		Game game1 = new Game("My First Game", templates.get(0), "20.05.2014");
		Game game2 = new Game("The Best Game", templates.get(1), "20.05.2014");
		Game game3 = new Game("Schwarze Auge Game", templates.get(2),
				"21.05.2014");

		game1.setCharakterList(templates.get(0).getCharacters());
		game2.setCharakterList(templates.get(1).getCharacters());
		game3.setCharakterList(templates.get(2).getCharacters());

		games.add(game1);
		games.add(game2);
		games.add(game3);
	}
}
