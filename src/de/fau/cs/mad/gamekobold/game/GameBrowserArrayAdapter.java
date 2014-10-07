package de.fau.cs.mad.gamekobold.game;

import java.util.List;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.ThumbnailLoader;
import android.content.Context;
import android.graphics.Bitmap;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView;
		Bitmap bitmap = null;

		Log.d("er", "position: " + position);
		if (convertView == null) {
			rowView = inflater.inflate(R.layout.rowlayout_game_browser,
					parent, false);
		}
		else {
			rowView = convertView;
		}
		TextView gName = (TextView) rowView
				.findViewById(R.id.textViewGameName);
		TextView gWorld = (TextView) rowView
				.findViewById(R.id.textViewWorldName);
		TextView gDate = (TextView) rowView
				.findViewById(R.id.textViewDate);
		ImageView imageViewGameIcon = (ImageView) rowView
				.findViewById(R.id.iconView);
		TextView gCounter = (TextView) rowView
				.findViewById(R.id.characterCounter);

		Game curGame = objects.get(position);
	
		gName.setText(curGame.getGameName());
		gDate.setText(curGame.getDate());
		gCounter.setText(String.valueOf(curGame.getCharacterSheetList().size()));

		bitmap = ThumbnailLoader.loadThumbnail(curGame.getIconPath(), context);
		if(bitmap == null) {
			// set some default game icon
			imageViewGameIcon.setImageResource(R.drawable.group_white);
		}
		else {
			// set game icon
			imageViewGameIcon.setImageBitmap(bitmap);					
		}
		return rowView;
	}
}
