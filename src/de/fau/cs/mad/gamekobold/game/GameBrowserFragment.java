package de.fau.cs.mad.gamekobold.game;

import java.io.File;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import de.fau.cs.mad.gamekobold.R;

public class GameBrowserFragment extends ListFragment {
	private ArrayList<Game> games;
	private GameBrowserArrayAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.game_browser_title);
		games = new ArrayList<Game>();
		adapter = new GameBrowserArrayAdapter(
				getActivity(), games);
		setListAdapter(adapter);
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				Log.d("GameBrowserFragment", "position >>"+position);
				final Game longClickedGame = games.get(position);
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(getResources().getString(
						R.string.msg_want_to_delete_game));
				builder.setMessage(getResources().getString(
						R.string.msg_yes_to_delete_game));
				builder.setNegativeButton(
						getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {
							}
						});
				builder.setPositiveButton(
						getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {
								File file = new File(longClickedGame.getFileAbsolutePath());
								if (file != null) {
									if (file.delete()) {
										adapter.remove(longClickedGame);
										adapter.notifyDataSetChanged();
									}
								}
							}
						});
				builder.create().show();
				return true;
			}
		});
	}

	@Override
	public void onResume() {
		// clear list
		adapter.clear();
		// get updated games list
		adapter.addAll(GameLab.get(getActivity()).getGames());
		// notify adapter
		adapter.notifyDataSetChanged();
		super.onResume();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Game g = ((GameBrowserArrayAdapter) getListAdapter()).getItem(position);
		Log.d("GAME", g.getGameName() + " was clicked");
		// Start GameDetailsActivity
		Intent i = new Intent(getActivity(), GameDetailsActivity.class);
		i.putExtra(GameDetailsFragment.EXTRA_GAME_NAME, g.getGameName());
		startActivity(i);
	}
}
