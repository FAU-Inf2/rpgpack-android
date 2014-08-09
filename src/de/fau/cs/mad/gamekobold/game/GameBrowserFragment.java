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

public class GameBrowserFragment extends ListFragment {
	private ArrayList<Game> games;
	
	private long gameFolderTimeStamp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.game_browser_title);
		games = GameLab.get(getActivity()).getGames();
		GameBrowserArrayAdapter adapter = new GameBrowserArrayAdapter(
				getActivity(), games);
		setListAdapter(adapter);
		setHasOptionsMenu(true);
		gameFolderTimeStamp = 0;
	}
	
	@Override
	public void onResume() {
		if(!checkForFolderChanges()) {
			
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
		File gameFolder = new File("");
		if(gameFolder != null) {
			final long newTimeStamp = gameFolder.lastModified();
			if(newTimeStamp > gameFolderTimeStamp) {
				// TODO reload
				gameFolderTimeStamp = newTimeStamp;
				return true;
			}
		}
		return false;
	}
}
