package de.fau.cs.mad.gamekobold.toolbox;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;

public class DiceElementAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> textViewValues;
	private ArrayList<String> textViewItems;
	private LayoutInflater mInflater;

	public DiceElementAdapter(Context context, ArrayList<String> items,
			ArrayList<String> values) {
		this.mContext = context;
		this.textViewItems = items;
		this.textViewValues = values;
		mInflater = (LayoutInflater) this.mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = mInflater.inflate(
					R.layout.activity_toolbox_dice_cell, null);

		TextView textView = (TextView) convertView
				.findViewById(R.id.grid_dice_item);
		
		//set text for actual value of die
		textView.setText(textViewValues.get(position));
	
		//set hint to identify maxValue of die
		textView.setHint(textViewItems.get(position));
		
		//set background based on maxValue of die
		if (textView.getHint() != null) {
			switch (Integer.parseInt((String) textView.getHint())) {
			case 4:
				textView.setBackgroundResource(R.drawable.dice_4);
				break;
			case 6:
				textView.setBackgroundResource(R.drawable.dice_6);
				break;
			case 8:
				textView.setBackgroundResource(R.drawable.dice_8);
				break;
			case 10:
				textView.setBackgroundResource(R.drawable.dice_10);
				break;
			case 12:
				textView.setBackgroundResource(R.drawable.dice_12);
				break;
			case 20:
				textView.setBackgroundResource(R.drawable.dice_20);
				break;

			}
		}

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
