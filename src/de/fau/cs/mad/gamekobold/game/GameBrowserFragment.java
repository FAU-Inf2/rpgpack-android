package de.fau.cs.mad.gamekobold.game;

import java.io.File;
import java.util.ArrayList;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;

public class GameBrowserFragment extends ListFragment {
	private ArrayList<Game> games;
	
	private long gameFolderTimeStamp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.game_browser_title);
		games = new ArrayList<Game>();
		GameBrowserArrayAdapter adapter = new GameBrowserArrayAdapter(
				getActivity(), games);
		setListAdapter(adapter);
		setHasOptionsMenu(true);
		gameFolderTimeStamp = 0;
	}
	
	@Override
	public void onResume() {
		if(!checkForFolderChanges()) {
			checkForGameChanges();
		}
		super.onResume();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Game g = ((GameBrowserArrayAdapter) getListAdapter()).getItem(position);
		Log.d("GAME", g.getGameName() + " was clicked");

		if (position == games.size() - 1) {
			Log.d("GAME", games.size() + " was clicked");
			// Start NewGameActivity
			Intent i = new Intent(getActivity(), CreateNewGameActivity.class);
			startActivity(i);
		} else {
			// Start GameDetailsActivity
			Intent i = new Intent(getActivity(), GameDetailsActivity.class);
			i.putExtra(GameDetailsFragment.EXTRA_GAME_NAME, g.getGameName());
			startActivity(i);
		}
	}
	
	/**
	 * Checks the time stamp for the folder. If the folder has changed, we load
	 * the entire list again.
	 * @return
	 */
	private boolean checkForFolderChanges() {
		File gameFolder = JacksonInterface.getGameRootDirectory(getActivity());
		if(gameFolder != null) {
			final long newTimeStamp = gameFolder.lastModified();
			if(newTimeStamp > gameFolderTimeStamp) {
				// get adapter
				final GameBrowserArrayAdapter adapter = (GameBrowserArrayAdapter) getListAdapter();
				// clear content
				adapter.clear();
				// update time stamp
				gameFolderTimeStamp = newTimeStamp;
				// list game files
				final File[] gameFileList = gameFolder.listFiles();
				// iterate over all files
				for(final File gameFile : gameFileList) {
					try {
						// load and add game
						final Game game = JacksonInterface.loadGame(gameFile);
						adapter.add(game);
					}
					catch(Throwable e) {
						e.printStackTrace();
					}
				}
				adapter.addAll(GameLab.get(getActivity()).getGames());
				// add the create new game item, because we clear the list
				adapter.add(new Game("Create New Game..."));
				// notify adapter that data set has changed
				adapter.notifyDataSetChanged();
				return true;
			}
		}
		return false;
	}
	
	private void checkForGameChanges() {
		// flag to indicate whether we have changed the list or not
		boolean gameListChanged = false;
		// iterate over all loaded games
		for(final Game game : games) {
			// get path
			final String path = game.getFileAbsolutePath();
			// check if it is valid
			if(!path.isEmpty()) {
				// create file
				File gameFile = new File(path);
				// check if it has been modified
				if(gameFile.lastModified() > game.getFileTimeStamp()) {
					try {
						// reload the game
						Game loadedGame = JacksonInterface.loadGame(gameFile);
						// take over changes
						game.takeOverValues(loadedGame);
						// set flag so we notify the adapter later
						gameListChanged = true;
					}
					catch(Throwable e) {
						e.printStackTrace();
					}
				}
			}
		}
		if(gameListChanged) {
			// get adapter
			final GameBrowserArrayAdapter adapter = (GameBrowserArrayAdapter) getListAdapter();
			adapter.notifyDataSetChanged();
		}
	}
}
