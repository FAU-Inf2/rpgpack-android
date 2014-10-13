package de.fau.cs.mad.gamekobold.toolbox;

import java.util.ArrayList;

import android.R.color;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RandomListElementAdapter extends BaseAdapter {
	private Context context;
	private final ArrayList<String> textViewItems;
	private int selected;
	LayoutInflater inflater;

	public RandomListElementAdapter(Context context,
			ArrayList<String> names, int position) {
		this.context = context;
		this.textViewItems = names;
		this.selected = position;

		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null)
			convertView = inflater.inflate(
					R.layout.activity_game_toolbox_random_cell, null);

		TextView textView = (TextView) convertView
				.findViewById(R.id.listview_item);

		textView.setText(textViewItems.get(position));
		textView.setClickable(true);
		textView.setHint("false");
		if ((selected == position) && (textView.getHint().equals("false"))){
			textView.setTextColor(color.darker_gray);
		}
		else if ((selected == position) && (textView.getHint().equals("true"))){
			textView.setTextColor(color.white);
		}
		textView.setTextSize(30);

		/*
		 * switch (Integer.parseInt((String) textView.getHint())){ case 4:
		 * textView.setBackgroundResource(R.drawable.d4_128x128); break; case 6:
		 * textView.setBackgroundResource(R.drawable.d6_128x128); break; case 8:
		 * textView.setBackgroundResource(R.drawable.d8_128x128); break; case
		 * 10: textView.setBackgroundResource(R.drawable.d10_128x128); break;
		 * case 12: textView.setBackgroundResource(R.drawable.d12_128x128);
		 * break; case 20:
		 * textView.setBackgroundResource(R.drawable.d20_128x128); break;
		 * default: textView.setBackgroundResource(R.color.background_green); }
		 */
		return convertView;
	}

	@Override
	public int getCount() {
		return textViewItems.size();
	}

	@Override
	public Object getItem(int position) {
		return textViewItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
