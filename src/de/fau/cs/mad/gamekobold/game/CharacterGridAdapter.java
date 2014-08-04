package de.fau.cs.mad.gamekobold.game;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.matrix.MatrixItem;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class CharacterGridAdapter extends ArrayAdapter<GameCharacter> {
	Context context;

	// the list of objects we want to display
	private List<GameCharacter> characters;
	private int layoutID;

	public CharacterGridAdapter(Context context, int layoutID, Template template) {
		// super(context, R.layout.itemlayout_expandablelist_charakter, template
		// .getCharacters());
		super(context, layoutID, template.getCharacters());
		this.context = context;
		this.characters = template.getCharacters();
		this.layoutID = layoutID;
	}

	public CharacterGridAdapter(Context context, int layoutID, Game game) {
		super(context, layoutID, game.getCharakterList());
		this.context = context;
		this.characters = game.getCharakterList();
		this.layoutID = layoutID;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// last item -> create new Character
		if (position == characters.size() - 1) {
			Log.d("position", "" + String.valueOf(position));
			Log.d("characters.size", "" + String.valueOf(characters.size() - 1));
			
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// if it's not recycled, initialize some attributes
			if (convertView == null) {
				convertView = inflater
						.inflate(
								R.layout.itemlayout_expandablelist_create_new_character,
								parent, false);

				GameCharacter curCharacter = characters.get(position);

				TextView iName = (TextView) convertView
						.findViewById(R.id.textItemTitle);

				iName.setText(curCharacter.getCharacterName());
			}
			// or reuse
			else {
				Log.e("Reusing", "true");
				TextView iName = (TextView) convertView
						.findViewById(R.id.textItemTitle);
				GameCharacter curCharacter = characters.get(position);
				iName.setText(curCharacter.getCharacterName());
				Log.d("Character is null?", "" + (curCharacter == null));
			}
			// normal item
		} else {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// if it's not recycled, initialize some attributes
			if (convertView == null) {
				convertView = inflater.inflate(layoutID, parent, false);

				GameCharacter curCharacter = characters.get(position);

				TextView iName = (TextView) convertView
						.findViewById(R.id.textItemTitle);

				iName.setText(curCharacter.getCharacterName());
			}
			// or reuse
			else {
				Log.e("Reusing", "true");
				TextView iName = (TextView) convertView
						.findViewById(R.id.textItemTitle);
				GameCharacter curCharacter = characters.get(position);
				iName.setText(curCharacter.getCharacterName());
				Log.d("Character is null?", "" + (curCharacter == null));
			}

		}
		return convertView;
	}

}