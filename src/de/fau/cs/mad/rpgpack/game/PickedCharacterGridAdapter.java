package de.fau.cs.mad.rpgpack.game;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.fau.cs.mad.rpgpack.R;
import de.fau.cs.mad.rpgpack.jackson.CharacterSheet;
import de.fau.cs.mad.rpgpack.templatebrowser.Template;

public class PickedCharacterGridAdapter extends ArrayAdapter<CharacterSheet> {
	Context context;

	// the list of objects we want to display
	private List<CharacterSheet> characters;
	private int layoutID;
	private Game curGame;

	public PickedCharacterGridAdapter(Context context, int layoutID,
			Template template) {
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
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		if (convertView == null) {
			rowView = inflater.inflate(layoutID, parent, false);
		} else {
			rowView = convertView;
		}

		characters = curGame.getCharacterSheetList();
		Bitmap bitmap = null;
		CharacterSheet curCharacter = characters.get(position);

		TextView characterName = (TextView) rowView
				.findViewById(R.id.textItemTitle);
		characterName.setText(curCharacter.getName());
		ImageView characterIconView = (ImageView) rowView
				.findViewById(R.id.character_icon_circle);

		bitmap = curCharacter.getIcon(context);
		if (bitmap == null) {
			// set some default game icon
			characterIconView.setImageResource(R.drawable.character_white);
		} else {
			// set game icon
			characterIconView.setImageBitmap(bitmap);
		}

		return rowView;
	}

}