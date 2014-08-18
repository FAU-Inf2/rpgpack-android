package de.fau.cs.mad.gamekobold.game;

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
		super(context, layoutID, game.getCharakterList());
		this.context = context;
		this.characters = game.getCharakterList();
		this.layoutID = layoutID;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Bitmap bitmap = null;
		String path = "";

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// if it's not recycled, initialize some attributes
		if (convertView == null) {
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
						R.drawable.figure1head);
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
			Log.e("Reusing", "true");
			TextView characterName = (TextView) convertView
					.findViewById(R.id.textItemTitle);
			GameCharacter curCharacter = characters.get(position);
			characterName.setText(curCharacter.getCharacterName());
			Log.d("Character is null?", "" + (curCharacter == null));

			ImageView characterIconView = (ImageView) convertView
					.findViewById(R.id.character_icon_circle);
		}

		return convertView;
	}

}