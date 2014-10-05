package de.fau.cs.mad.gamekobold.game;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.ThumbnailLoader;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class PickedCharacterGridAdapter extends ArrayAdapter<CharacterSheet> {
	Context context;

	// the list of objects we want to display
	private List<CharacterSheet> characters;
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
		super(context, layoutID, game.getCharacterSheetList());
		this.context = context;
		this.characters = game.getCharacterSheetList();
		this.layoutID = layoutID;
		this.curGame = game;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.e("Position in getView picked", "" + position);

		characters = curGame.getCharacterSheetList();

		Log.e("Size of character list", "" + characters.size());
		Bitmap bitmap = null;
		String path = "";

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// if it's not recycled, initialize some attributes
		if (convertView == null) {

			int i = 0;
			for (CharacterSheet g : characters) {
				i++;
				Log.e("PickedCharcterAdapter-first use",
						i + " - " + g.getName());
			}

			convertView = inflater.inflate(layoutID, parent, false);

			CharacterSheet curCharacter = characters.get(position);

			TextView characterName = (TextView) convertView
					.findViewById(R.id.textItemTitle);
			characterName.setText(curCharacter.getName());
			ImageButton characterIconView = (ImageButton) convertView
					.findViewById(R.id.character_icon_circle);

			bitmap = ThumbnailLoader.loadThumbnail(curCharacter.getIconPath(),
					context);
			if (bitmap == null) {
				// set some default game icon
				characterIconView
						.setImageResource(R.drawable.person_without_plus);
			} else {
				// set game icon
				characterIconView.setImageBitmap(bitmap);
			}
		}
		// or reuse
		// TODO check!!!!
		else {
			characters = curGame.getCharacterSheetList();
			int i = 0;
			for (CharacterSheet g : curGame.getCharacterSheetList()) {
				i++;
				Log.e("PickedCharcterAdapter-reuse", i + " - " + g.getName());
			}
			CharacterSheet curCharacter = characters.get(position);

			TextView characterName = (TextView) convertView
					.findViewById(R.id.textItemTitle);
			characterName.setText(curCharacter.getName());
			ImageButton characterIconView = (ImageButton) convertView
					.findViewById(R.id.character_icon_circle);

			bitmap = ThumbnailLoader.loadThumbnail(curCharacter.getIconPath(),
					context);
			if (bitmap == null) {
				// set some default game icon
				characterIconView.setImageResource(R.drawable.character_white);
			} else {
				// set game icon
				characterIconView.setImageBitmap(bitmap);
			}
		}

		return convertView;
	}

}