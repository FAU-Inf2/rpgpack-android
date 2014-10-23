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

public class GameDetailsCharacterGridAdapter extends
		ArrayAdapter<CharacterSheet> {
	Context context;

	// the list of objects we want to display
	private List<CharacterSheet> characters;
	private int layoutID;

	public GameDetailsCharacterGridAdapter(Context context, int layoutID,
			Template template) {
		super(context, layoutID, template.getCharacters());
		this.context = context;
		this.characters = template.getCharacters();
		this.layoutID = layoutID;
	}

	public GameDetailsCharacterGridAdapter(Context context, int layoutID,
			Game game) {
		super(context, layoutID, game.getCharacterSheetList());
		this.context = context;
		this.characters = game.getCharacterSheetList();
		this.layoutID = layoutID;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Bitmap bitmap = null;
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		if (convertView == null) {
			rowView = inflater.inflate(layoutID, parent, false);
		} else {
			rowView = convertView;
		}

		CharacterSheet curCharacter = characters.get(position);

		TextView characterName = (TextView) rowView
				.findViewById(R.id.textItemTitle);
		ImageView characterIconView = (ImageView) rowView
				.findViewById(R.id.character_icon_circle);
		
		characterName.setText(curCharacter.getName());
		
		bitmap = curCharacter.getIcon(context);
		if (bitmap == null) {
			// set some default game icon
			characterIconView.setImageResource(R.drawable.character_white);
		} else {
			characterIconView.setImageBitmap(bitmap);
		}
		return rowView;
	}

	@Override
	public int getCount() {
		return characters.size();
	}

	public List<CharacterSheet> getItems() {
		return characters;
	}

}