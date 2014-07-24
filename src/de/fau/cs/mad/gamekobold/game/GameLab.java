package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class GameLab {

	private static GameLab sGameLab;
	private ArrayList<Game> games;
	private Context appContext;

	private GameLab(Context appContext) {
		this.appContext = appContext;
		games = new ArrayList<Game>();

		Game game1 = new Game("My First Game", "Dungeons and Dragons",
				"20.05.2014");
		Game game2 = new Game("The Best Game", "Vampire the Masquerade",
				"20.05.2014");
		Game game3 = new Game("Schwarze Auge Game", "Das Schwarze Auge",
				"21.05.2014");

		games.add(game1);
		games.add(game2);
		games.add(game3);
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
