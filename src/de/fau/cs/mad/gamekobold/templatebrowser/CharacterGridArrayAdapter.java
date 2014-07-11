package de.fau.cs.mad.gamekobold.templatebrowser;

import java.util.List;

import de.fau.cs.mad.gamekobold.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CharacterGridArrayAdapter extends ArrayAdapter<String>{
	Context context;

	// the list of objects we want to display
	private List<String> items;
	
	public CharacterGridArrayAdapter(Context context, List<String> items) {
		super(context, R.layout.itemlayout_character_icon, items);
		if (items != null) {
			items.add(context.getString(R.string.create_new_character));
		}
		this.context = context;
		this.items = items;
	}
	
	/**
	 * Changes the color of the circle shape for a character matrix item.
	 * @param color The color to use
	 * @param view The View for which the to change the color
	 */
	private void setColorForView(int color, View view) {
		// The view ids have to be the same for both layouts!
		ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
		if(imageView != null) {
			Drawable drawable =imageView.getDrawable(); 
			drawable.setColorFilter(color, Mode.SRC_ATOP);
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(position == getCount()-1) {
				itemView = inflater.inflate(R.layout.itemlayout_new_character_icon, parent, false);
				// the following code sets the color for the circle shape
				setColorForView(Color.WHITE, itemView);
			}
			else {
				itemView = inflater.inflate(R.layout.itemlayout_character_icon, parent, false);
				// the following code sets the color for the circle shape
				setColorForView(Color.YELLOW, itemView);
			}
		} else {
			itemView = convertView;
		}
		TextView name = (TextView)itemView.findViewById(R.id.textView1);
		name.setText(items.get(position));
		return itemView;
	}
	
	// needed for viewConvertion so that the system knows that there are different layouts in the adapter
	// 0 for normal item. 1 for "new item" item
	@Override
	public int getItemViewType(int position) {
		// if it is the last element
		if(position == getCount()-1) {
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
