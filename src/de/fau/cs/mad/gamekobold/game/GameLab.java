package de.fau.cs.mad.gamekobold.game;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;
import android.content.Context;
import android.util.Log;

public class GameLab {

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

	public static GameLab get(Context c) {
		if (sGameLab == null) {
			sGameLab = new GameLab(c.getApplicationContext());
		}
		return sGameLab;
	}

	public ArrayList<Game> getGames() {
		return games;
	}

	public Game getGame(String gameName) {
		for (Game g : games) {
			if (g.getGameName().equals(gameName))
				return g;
		}
		return null;
	}

//	private void assureListIsUpToDate() {
//		// checks if a template has been created or deleted
//		if(!checkForGameDirectoryChange()) {
//			// only check every template for a change when not reloading the whole list
//			checkEveryTemplateForChanges();
//		}
//	}
//
//	/**
//	 * Checks the game directory for a change. Its time stamp is updated
//	 * when a file is created or deleted.
//	 * @return true if the game list has been reloaded, false otherwise.
//	 */
//	private boolean checkForGameDirectoryChange() {
//		final File gameDir = JacksonInterface.getGameRootDirectory(appContext);
//		if(gameDir != null) {
//			final long newTimeStamp = gameDir.lastModified();
//			if(folderTimeStamp < newTimeStamp) {
//				games.clear();
//				// add default test data
//				addDefaultData();
//				// update time stamp
//				folderTimeStamp = newTimeStamp;
//				// reload template list
//				Log.d(LOG_TAG, "Reloading template list");
//				Log.d(LOG_TAG, "Tempolate directory:" + templateDir.getAbsolutePath());
//				if (templateDir.isDirectory()) {
//					final File[] fileList = templateDir.listFiles();
//					de.fau.cs.mad.gamekobold.jackson.Template loadedTemplate = null;
//					for (final File file : fileList) {
//						try {
//							loadedTemplate = JacksonInterface.loadTemplate(file, true); 
//							if (loadedTemplate != null) {
//								Template temp = new Template(
//										loadedTemplate.templateName,
//										loadedTemplate.gameName,
//										loadedTemplate.author, loadedTemplate.date,
//										loadedTemplate.iconID,
//										loadedTemplate.description);
//								if (temp.getTemplateName().equals("")) {
//									temp.setTemplateName(file.getName());
//								}
//								temp.fileAbsolutePath = file.getAbsolutePath();
//								// set time stamp
//								temp.setFileTimeStamp(file.lastModified());
//								//load the characters for the template
//								loadCharacters(temp);
//								// add the template to the list
//								templates.add(temp);
//							}
//						} catch (Throwable e) {
//							e.printStackTrace();
//						}
//					}
//				}
//				// fake item to create New Character from template
//				GameCharacter createNewCharacter = new GameCharacter("+");
//				for (Template t : templates) {
//					t.addCharacter(createNewCharacter);
//				}
//				return true;
//			}
//		}
//		return false;
//	}
//
//	private void addDefaultData() {
//		Game game1 = new Game("My First Game", templates.get(0), "20.05.2014");
//		Game game2 = new Game("The Best Game", templates.get(1), "20.05.2014");
//		Game game3 = new Game("Schwarze Auge Game", templates.get(2),
//				"21.05.2014");
//
//		game1.setCharakterList(templates.get(0).getCharacters());
//		game2.setCharakterList(templates.get(1).getCharacters());
//		game3.setCharakterList(templates.get(2).getCharacters());
//
//		games.add(game1);
//		games.add(game2);
//		games.add(game3);
//	}
}
