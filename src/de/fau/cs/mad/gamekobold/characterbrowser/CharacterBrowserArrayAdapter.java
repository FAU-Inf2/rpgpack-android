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

public class CharacterBrowserArrayAdapter extends ArrayAdapter<CharacterSheet>{
	private List<CharacterSheet> characterList;
	private Context context;
	
	public CharacterBrowserArrayAdapter(Context context, List<CharacterSheet> objects) {
		super(context,R.layout.rowlayout_template_browser , objects);
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
		// check for layout type
		if(position < getCount()-1) {
			if(convertView == null) {
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
			
			final CharacterSheet character = characterList.get(position);
			textView1.setText(character.getName());
			textView2.setText(character.getFileLastUpdated());
			textView3.setText(character.getDescription());	
		}
		else {
			if(convertView == null) {
				rowView = inflater.inflate(
						R.layout.rowlayout_character_browser_new_character,
						parent, false);
				final TextView textView1 = (TextView) rowView.findViewById(R.id.textView1);
				textView1.setText(context.getResources().getString(R.string.create_new_character));
			}
			else {
				rowView = convertView;
			}
		}
		return rowView;
	}
	
	// needed for viewConvertion so that the system knows that there are different layouts in the adapter
	// 0 for character. 1 for "new character" item.
	@Override
	public int getItemViewType(int position) {
		// if "create"
		if(position >= getCount() - 1) {
			// return 1
			return 1;
		}
		// return 0 for every other item
		return 0;
	}
	
	// we got 2 types: normal items and the last one
	@Override
	public int getViewTypeCount() {
	    return 2; // Count of different layouts
	}
}
