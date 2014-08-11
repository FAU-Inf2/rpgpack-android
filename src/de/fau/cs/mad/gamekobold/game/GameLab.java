package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;
import java.util.UUID;

import de.fau.cs.mad.gamekobold.templatebrowser.Template;
import android.content.Context;

public class GameLab {

	private static GameLab sGameLab;
	private ArrayList<Game> games;
	private Context appContext;
	private ArrayList<Template> templates;

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
}
