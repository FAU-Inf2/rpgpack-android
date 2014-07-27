package de.fau.cs.mad.gamekobold.game;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.game_browser_title);
		games = GameLab.get(getActivity()).getGames();
		GameBrowserArrayAdapter adapter = new GameBrowserArrayAdapter(
				getActivity(), games);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Game g = ((GameBrowserArrayAdapter) getListAdapter()).getItem(position);
		Log.d("sd", g.getGameName() + " was clicked");

		if (position == games.size() - 1) {
			Log.d("sd", games.size() + " was clicked");
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
}
