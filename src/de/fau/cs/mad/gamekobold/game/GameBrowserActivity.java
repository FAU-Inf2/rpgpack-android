package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;

public class GameBrowserActivity extends ListActivity {
	private List<Game> gameList = null;
	private static Activity myActivity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_browser);
		Log.e("d", "On create!!!");

		myActivity = this;

		gameList = getDataForListView();

		if (gameList == null) {
			gameList = new ArrayList<Game>();
		}

		// have to make it final because of adapter.getCount()-method for
		// newTemplate-intent
		final GameBrowserArrayAdapter adapter = new GameBrowserArrayAdapter(
				this, gameList);
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				// is it last row?
				if (position == adapter.getCount() - 1) {

					Log.e("er", "Position, getCount: " + adapter.getCount());
					// TODO call fragment

					Toast.makeText(getApplicationContext(), "Last Row!",
							Toast.LENGTH_SHORT).show();

					// Intent i = new Intent(GameBrowserActivity.this,
					// CreateNewGameActivity.class);
					// startActivity(i);

				} else {
					// TODO call fragment
					Toast.makeText(getApplicationContext(), "Notlast Row!",
							Toast.LENGTH_SHORT).show();

					// Intent i = new Intent(GameBrowserActivity.this,
					// GameDetailsActivity.class);
					//
					// Log.e("er", "position: " + position);
					//
					// i.putExtra("position", position);
					// i.putExtra("template", gameList.get(position));
					// startActivity(i);
				}
			}
		});

		// set onItemClickListener. if the user long clicks on an item we show a
		// dialog the user then gets the option to delete the game
		// getListView().setOnItemLongClickListener(
		// new AdapterView.OnItemLongClickListener() {
		// @Override
		// public boolean onItemLongClick(AdapterView<?> arg0,
		// View arg1, int pos, long id) {
		// final Game longClickedGame = gameList.get(pos);
		//
		// Log.d("TemplateBrowser", "longClickOn:"
		// + longClickedGame.absoluteFilePath);
		//
		// if (longClickedTemplate.absoluteFilePath != null) {
		// AlertDialog.Builder builder = new AlertDialog.Builder(
		// myActivity);
		// builder.setTitle(getResources().getString(
		// R.string.msg_want_to_delete));
		// builder.setMessage(getResources().getString(
		// R.string.msg_yes_to_delete));
		// builder.setNegativeButton(
		// getResources().getString(R.string.no),
		//
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(
		// DialogInterface dialog,
		// int which) {
		// }
		// });
		// builder.setPositiveButton(
		// getResources().getString(R.string.yes),
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(
		// DialogInterface dialog,
		// int which) {
		// File file = new File(
		// longClickedTemplate.absoluteFilePath);
		// if (file != null) {
		// Log.d("TempalteBrowser",
		// "delete template:"
		// + longClickedTemplate);
		// // removeItem(longClickedTemplate);
		// if (file.delete()) {
		// // check if we removed the
		// // last edited template
		// SharedPreferences pref = getSharedPreferences(
		// TemplateGeneratorActivity.SHARED_PREFERENCES_FILE_NAME,
		// MODE_PRIVATE);
		// String lastEditedTemplate = pref
		// .getString(
		// TemplateGeneratorActivity.LAST_EDITED_TEMPLATE_NAME,
		// "");
		// if (lastEditedTemplate
		// .equals(file
		// .getName())) {
		// // if so we remove it
		// // from the saved
		// // preference
		// SharedPreferences.Editor editor = pref
		// .edit();
		// editor.remove(TemplateGeneratorActivity.LAST_EDITED_TEMPLATE_NAME);
		// editor.commit();
		// }
		// removeItem(longClickedTemplate);
		// }
		// }
		// }
		// });
		// builder.create().show();
		// }
		// return true;
		// }
		// });

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	private List<Game> getDataForListView() {
		List<Game> gameList = new ArrayList<Game>();
		Game game1 = new Game("My First Game", "Dungeons and Dragons",
				"20.05.2014");
		Game game2 = new Game("The Best Game", "Vampire the Masquerade",
				"20.05.2014");
		Game game3 = new Game("Schwarze Auge Game", "Das Schwarze Auge",
				"21.05.2014");

		gameList.add(game1);
		gameList.add(game2);
		gameList.add(game3);
		gameList.add(game3);
		return gameList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_new_game,
					container, false);
			return rootView;
		}
	}

}
