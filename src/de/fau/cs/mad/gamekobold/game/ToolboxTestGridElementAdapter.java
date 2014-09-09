package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;

public class ToolboxTestGridElementAdapter extends BaseAdapter {
	 private Activity activity;
	 //   public ExampleAdapter(Activity activity)
	private Context context;
	private final ArrayList<String> textViewItems;
	LayoutInflater inflater;

	public ToolboxTestGridElementAdapter(Context context, ArrayList<String> items) {
		this.context = context;
		this.textViewItems = items;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View gridView;

		if (convertView == null)
			convertView = inflater.inflate(
					R.layout.activity_game_toolbox_test_cell, null);

		TextView imageView = (TextView) convertView
				.findViewById(R.id.grid_test_item);
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
