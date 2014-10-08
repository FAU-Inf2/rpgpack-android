package de.fau.cs.mad.gamekobold.characterbrowser;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;

public class CharacterBrowserArrayAdapter extends ArrayAdapter<CharacterSheet> {
	private List<CharacterSheet> characterList;
	private Context context;

	public CharacterBrowserArrayAdapter(Context context,
			List<CharacterSheet> objects) {
		// TODO layout aendern
		super(context, R.layout.rowlayout_template_browser, objects);
		this.characterList = objects;
		this.context = context;
	}

	public List<CharacterSheet> getList() {
		return characterList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		if (convertView == null) {
			rowView = inflater.inflate(R.layout.rowlayout_character_browser,
					parent, false);
		} else {
			rowView = convertView;
		}  
		
		final TextView textView1 = (TextView) rowView
				.findViewById(R.id.textView1);
		final TextView textView2 = (TextView) rowView
				.findViewById(R.id.textView2);
		final TextView textView3 = (TextView) rowView
				.findViewById(R.id.textView3);

		final CharacterSheet character = characterList.get(position);
		textView1.setText(character.getName());

		// get template name from character file path
		// TODO now always correct
		String curStr = character.getFileAbsolutePath().split("/")[6]
				.split("-")[0];
		textView2.setText(curStr);
		textView3.setText(character.getFileLastUpdated());
		return rowView;
	}
}
