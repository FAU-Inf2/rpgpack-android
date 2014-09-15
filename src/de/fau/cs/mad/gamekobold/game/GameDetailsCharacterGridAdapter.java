package de.fau.cs.mad.gamekobold.game;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.ThumbnailLoader;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class GameDetailsCharacterGridAdapter extends
		ArrayAdapter<GameCharacter> {
	Context context;

	// the list of objects we want to display
	private List<GameCharacter> characters;
	private int layoutID;

	public GameDetailsCharacterGridAdapter(Context context, int layoutID,
			Template template) {
		// super(context, R.layout.itemlayout_expandablelist_charakter, template
		// .getCharacters());
		super(context, layoutID, template.getCharacters());
		this.context = context;
		this.characters = template.getCharacters();
		this.layoutID = layoutID;
	}

	public GameDetailsCharacterGridAdapter(Context context, int layoutID,
			Game game) {
		super(context, layoutID, game.getCharacterList());
		this.context = context;
		this.characters = game.getCharacterList();
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

			ImageView characterIconView = (ImageView) convertView
					.findViewById(R.id.character_icon_circle);

			characterName.setText(curCharacter.getCharacterName());

			bitmap = ThumbnailLoader.loadThumbnail(curCharacter.getIconPath(), context);
			if(bitmap == null) {
				// set some default game icon
				characterIconView.setImageResource(R.drawable.person_without_plus);
			}
			else {
				characterIconView.setImageBitmap(bitmap);
			}
		}
		// or reuse
		else {
			Log.e("Reusing", "true");
			TextView characterName = (TextView) convertView
					.findViewById(R.id.textItemTitle);
			GameCharacter curCharacter = characters.get(position);
			characterName.setText(curCharacter.getCharacterName());
			Log.d("Character is null?", "" + (curCharacter == null));
			ImageView characterIconView = (ImageView) convertView
					.findViewById(R.id.character_icon_circle);

			bitmap = ThumbnailLoader.loadThumbnail(curCharacter.getIconPath(), context);
			if(bitmap == null) {
				// set some default game icon
				characterIconView.setImageResource(R.drawable.person_without_plus);
			}
			else {
				characterIconView.setImageBitmap(bitmap);
			}
		}

		return convertView;
	}

	@Override
	public int getCount() {
		return characters.size();
	}
	
	public List<GameCharacter> getItems(){
		return characters;
	}

}