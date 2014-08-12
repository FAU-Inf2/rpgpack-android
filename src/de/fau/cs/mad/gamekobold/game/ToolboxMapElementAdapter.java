package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;

public class ToolboxMapElementAdapter extends BaseAdapter{
	private Context context;
	private final int rows;
	private final int columns;
	LayoutInflater inflater;
	ArrayList<String> dots;

	public ToolboxMapElementAdapter (Context context, int rows, int columns, ArrayList<String> dots) {
		this.context = context;
		this.rows = rows;
		this.columns = columns;
		this.dots = dots;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View gridView;

		if (convertView == null)
			convertView = inflater.inflate(
					R.layout.activity_game_toolbox_map_cell, null);

		TextView textView = (TextView) convertView.findViewById(R.id.grid_map_item);
		textView.setText(dots.get(position));
		textView.setTextSize(40);
		return convertView;
	}

	@Override
	public int getCount() {
		return rows*columns;
	}

	@Override
	public Object getItem(int position) {
		return dots.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}