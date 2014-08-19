package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class CharacterGridAdapter extends ArrayAdapter<GameCharacter> {
	private Context context;

	// list of highlighted characters
	public ArrayList<GameCharacter> selectedCharacters = new ArrayList<GameCharacter>();

	// the list of objects we want to display
	private List<GameCharacter> characters;
	private ImageView characterIconImageView;
	private ImageView highlightingImageView;
	private TextView characterNameTextView;

	public CharacterGridAdapter(Context context, Template template) {
		super(context, R.layout.itemlayout_expandablelist_charakter, template
				.getCharacters());
		this.context = context;
		this.characters = template.getCharacters();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Bitmap bitmap = null;
		String path = "";

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

				characterNameTextView = (TextView) convertView
						.findViewById(R.id.textItemTitle);
				characterNameTextView.setText(curCharacter.getCharacterName());

			}
			// or reuse
			else {
				Log.e("Reusing", "true");
				characterNameTextView = (TextView) convertView
						.findViewById(R.id.textItemTitle);
				GameCharacter curCharacter = characters.get(position);
				characterNameTextView.setText(curCharacter.getCharacterName());
				Log.d("Character is null?", "" + (curCharacter == null));
			}
			// normal item
		} else {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// if it's not recycled, initialize some attributes
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.itemlayout_expandablelist_charakter, parent,
						false);

				GameCharacter curCharacter = characters.get(position);

				characterNameTextView = (TextView) convertView
						.findViewById(R.id.textItemTitle);
				characterNameTextView.setText(curCharacter.getCharacterName());

				characterIconImageView = (ImageView) convertView
						.findViewById(R.id.character_icon_circle);

				if (curCharacter.getIconPath() == null) {
					// set some default game icon
					bitmap = BitmapFactory.decodeResource(
							context.getResources(), R.drawable.person_without_plus);
				} else {
					bitmap = BitmapFactory.decodeFile(path);
				}

				if (bitmap != null) {
					// set game icon
					characterIconImageView.setImageBitmap(bitmap);
				}

				highlightingImageView = (ImageView) convertView
						.findViewById(R.id.character_highlighting_circle);

				Log.d("characterIconImageView is null?", ""
						+ (characterIconImageView == null));

				Log.d("selectedCharacters.contains(position)?", ""
						+ (selectedCharacters.contains(position)));

				GradientDrawable highlightingShape = (GradientDrawable) highlightingImageView
						.getDrawable();

				Log.d("highlightingShape is null?", ""
						+ (highlightingShape == null));

				highlightingShape.setColor(selectedCharacters
						.contains(curCharacter) ? context.getResources()
						.getColor(R.color.background_green) : context
						.getResources().getColor(android.R.color.transparent));

				// characterIconImageView.setBackgroundColor(selectedCharacters
				// .contains(curCharacter) ? context.getResources()
				// .getColor(R.color.blue) : context.getResources()
				// .getColor(android.R.color.transparent));
			}
			// or reuse
			else {
				Log.e("Reusing", "true");
				TextView iName = (TextView) convertView
						.findViewById(R.id.textItemTitle);
				GameCharacter curCharacter = characters.get(position);
				iName.setText(curCharacter.getCharacterName());
				Log.d("Character is null?", "" + (curCharacter == null));

				characterIconImageView = (ImageView) convertView
						.findViewById(R.id.character_icon_circle);

				if (curCharacter.getIconPath() == null) {
					// set some default game icon
					bitmap = BitmapFactory.decodeResource(
							context.getResources(),
							R.drawable.person_without_plus);
				} else {
					bitmap = BitmapFactory.decodeFile(path);
				}

				if (bitmap != null) {
					// set game icon
					characterIconImageView.setImageBitmap(bitmap);
				}

				highlightingImageView = (ImageView) convertView
						.findViewById(R.id.character_highlighting_circle);

				// characterIconImageView.setBackgroundResource(selectedCharacters
				// .contains(curCharacter) ? (R.drawable.background)
				// : (R.drawable.clickdummy_background));

				// characterIconImageView.setBackgroundColor(selectedCharacters
				// .contains(curCharacter) ? context.getResources()
				// .getColor(R.color.blue) : context.getResources()
				// .getColor(android.R.color.transparent));
				//
				GradientDrawable highlightingShape = (GradientDrawable) highlightingImageView
						.getDrawable();

				Log.d("highlightingShape is null?", ""
						+ (highlightingShape == null));

				highlightingShape.setColor(selectedCharacters
						.contains(curCharacter) ? context.getResources()
						.getColor(R.color.background_green) : context
						.getResources().getColor(android.R.color.transparent));
			}

		}
		return convertView;
	}
}