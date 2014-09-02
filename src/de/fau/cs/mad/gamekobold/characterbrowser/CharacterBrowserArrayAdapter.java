package de.fau.cs.mad.gamekobold.characterbrowser;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.game.GameCharacter;

public class CharacterBrowserArrayAdapter extends ArrayAdapter<GameCharacter>{
	private List<GameCharacter> characterList;
	private Context context;
	
	public CharacterBrowserArrayAdapter(Context context, List<GameCharacter> objects) {
		super(context,R.layout.rowlayout_template_browser , objects);
		this.characterList = objects;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		if (convertView == null) {
			rowView = inflater.inflate(
					R.layout.rowlayout_character_browser,
					parent, false);
		}
		else {
			rowView = convertView;
		}
		final TextView textView1 = (TextView) rowView.findViewById(R.id.textView1);
		final TextView textView2 = (TextView) rowView.findViewById(R.id.textView2);
		final TextView textView3 = (TextView) rowView.findViewById(R.id.textView3);
		
		final GameCharacter character = characterList.get(position);
		textView1.setText(character.getCharacterName());
		textView2.setText(character.getDate());
		textView3.setText(character.getDescription());
		return rowView;
	}
}
