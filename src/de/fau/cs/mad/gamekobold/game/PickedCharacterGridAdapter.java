package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class PickedCharacterGridAdapter extends ArrayAdapter<GameCharacter> {
	Context context;

	// the list of objects we want to display
	private List<GameCharacter> characters;
	private int layoutID;
	private Game curGame;

	public PickedCharacterGridAdapter(Context context, int layoutID,
			Template template) {
		// super(context, R.layout.itemlayout_expandablelist_charakter, template
		// .getCharacters());
		super(context, layoutID, template.getCharacters());
		this.context = context;
		this.characters = template.getCharacters();
		this.layoutID = layoutID;
	}

	public PickedCharacterGridAdapter(Context context, int layoutID, Game game) {
		super(context, layoutID, game.getCharacterList());
		this.context = context;
		this.characters = game.getCharacterList();
		this.layoutID = layoutID;
		this.curGame = game;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.e("Position in getView picked", "" + position);

		characters = curGame.getCharacterList();

		Log.e("Size of character list", "" + characters.size());
		Bitmap bitmap = null;
		String path = "";

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// if it's not recycled, initialize some attributes
		if (convertView == null) {

			int i = 0;
			for (GameCharacter g : characters) {
				i++;
				Log.e("PickedCharcterAdapter-first use",
						i + " - " + g.getCharacterName());
			}

			convertView = inflater.inflate(layoutID, parent, false);

			GameCharacter curCharacter = characters.get(position);

			TextView characterName = (TextView) convertView
					.findViewById(R.id.textItemTitle);
			characterName.setText(curCharacter.getCharacterName());
			ImageView characterIconView = (ImageView) convertView
					.findViewById(R.id.character_icon_circle);

			if (curCharacter.getIconPath() == null) {
				// set some default game icon
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.person_without_plus);
			} else {
				bitmap = BitmapFactory.decodeFile(path);
			}
			if (bitmap != null) {
				// set game icon
				characterIconView.setImageBitmap(bitmap);
			}
		}

		// or reuse
		// TODO check!!!!
		else {
			characters = curGame.getCharacterList();
			int i = 0;
			for (GameCharacter g : curGame.getCharacterList()) {
				i++;
				Log.e("PickedCharcterAdapter-reuse",
						i + " - " + g.getCharacterName());
			}
			GameCharacter curCharacter = characters.get(position);

			TextView characterName = (TextView) convertView
					.findViewById(R.id.textItemTitle);
			characterName.setText(curCharacter.getCharacterName());
			ImageView characterIconView = (ImageView) convertView
					.findViewById(R.id.character_icon_circle);

			if (curCharacter.getIconPath() == null) {
				// set some default game icon
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.person_without_plus);
			} else {
				bitmap = BitmapFactory.decodeFile(path);
			}
			if (bitmap != null) {
				// set game icon
				characterIconView.setImageBitmap(bitmap);
			}
		}

		return convertView;
	}

}