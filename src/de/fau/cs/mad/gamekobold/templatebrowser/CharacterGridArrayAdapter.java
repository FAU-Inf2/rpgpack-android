package de.fau.cs.mad.gamekobold.templatebrowser;

import java.util.List;

import de.fau.cs.mad.gamekobold.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CharacterGridArrayAdapter extends ArrayAdapter<String>{
	Context context;

	// the list of objects we want to display
	private List<String> items;
	
	public CharacterGridArrayAdapter(Context context, List<String> items) {
		super(context, R.layout.itemlayout_character_icon, items);
		this.context = context;
		this.items = items;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemView = inflater.inflate(R.layout.itemlayout_character_icon, parent, false);
		} else {
			itemView = convertView;
		}
		TextView name = (TextView)itemView.findViewById(R.id.textView1);
		name.setText(items.get(position));
		return itemView;
	}
}
