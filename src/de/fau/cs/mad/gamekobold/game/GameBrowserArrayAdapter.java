package de.fau.cs.mad.gamekobold.game;

import java.util.List;

import de.fau.cs.mad.gamekobold.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GameBrowserArrayAdapter extends ArrayAdapter<Game> {
	Context context;

	// the list of objects we want to display
	private List<Game> objects;

	public GameBrowserArrayAdapter(Context context, List<Game> objects) {
		super(context, R.layout.rowlayout_template_browser, objects);
		this.context = context;
		this.objects = objects;
	}

	// needed for viewConvertion so that the system knows that there are
	// different layouts in the adapter
	// 0 for template. 1 for "new template" item
	@Override
	public int getItemViewType(int position) {
		// if "create" return 1
		if (position >= getCount() - 1) {
			return 1;
		}
		// return 0 for every other item
		return 0;
	}

	// we got 2 types: normal items and the last one
	@Override
	public int getViewTypeCount() {
		return 2; // Count of different layouts
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView;
		if (convertView == null) {
			// if it is the last row -> create new game
			if (position == getCount() - 1) {
				rowView = inflater.inflate(
						R.layout.rowlayout_newgame_game_browser, parent, false);

				TextView gName = (TextView) rowView
						.findViewById(R.id.textViewNewGame);
				gName.setText("Create New Game...");

			} else {
				Log.e("er", "position: " + position);

				rowView = inflater.inflate(R.layout.rowlayout_game_browser,
						parent, false);

				TextView gName = (TextView) rowView
						.findViewById(R.id.textViewGameName);
				TextView gTemplate = (TextView) rowView
						.findViewById(R.id.textViewTemplateName);
				TextView gDate = (TextView) rowView
						.findViewById(R.id.textViewDate);
				ImageView imageView = (ImageView) rowView
						.findViewById(R.id.iconView);
				TextView gCounter = (TextView) rowView
						.findViewById(R.id.counter);

				Game curGame = objects.get(position);

				gName.setText(curGame.getGameName());
				gTemplate.setText(curGame.getTemplate().getTemplateName());
				gDate.setText(curGame.getDate());
				gCounter.setText(String.valueOf(curGame.getCharakterList()
						.size()));
				// // change the icon for different games
				// GameIcons gameIcons = GameIcons.getInstance();
				// imageView.setImageResource(Integer.valueOf(gameIcons
				// .getGameIcon(curGame.getIconID())));
			}
		} else {
			// check for last line -> create new game
			// it uses an other layout
			if (position == getCount() - 1) {
				rowView = inflater.inflate(
						R.layout.rowlayout_newgame_game_browser, parent, false);
				TextView gName = (TextView) rowView
						.findViewById(R.id.textViewNewGame);
				Game curGame = objects.get(position);
				gName.setText(curGame.getGameName());
			} else {
				rowView = inflater.inflate(R.layout.rowlayout_game_browser,
						parent, false);

				TextView gName = (TextView) rowView
						.findViewById(R.id.textViewGameName);
				TextView gTemplate = (TextView) rowView
						.findViewById(R.id.textViewTemplateName);
				TextView gDate = (TextView) rowView
						.findViewById(R.id.textViewDate);

				ImageView imageView = (ImageView) rowView
						.findViewById(R.id.iconView);

				Game curGame = objects.get(position);

				gName.setText(curGame.getGameName());
				gTemplate.setText(curGame.getTemplate().getTemplateName());
				gDate.setText(curGame.getDate());

				// // change the icon for different games
				// GameIcons gameIcons = GameIcons.getInstance();
				// imageView.setImageResource(Integer.valueOf(gameIcons
				// .getGameIcon(curGame.getIconID())));
			}
		}
		return rowView;
	}
}