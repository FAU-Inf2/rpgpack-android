package de.fau.cs.mad.gamekobold.matrix;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;

public class PlayCharacterMatrixAdapter extends ArrayAdapter<MatrixItem> {
	Context context;

	// the list of objects we want to display
	private List<MatrixItem> items;

	public PlayCharacterMatrixAdapter(Context context, List<MatrixItem> items) {
		super(context, R.layout.itemlayout_play_character_matrix_view, items);
		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// View itemView;
		// TODO ate refactor with view holder
		// if it's not recycled, initialize some attributes
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.itemlayout_play_character_matrix_view, parent,
					false);
			MatrixItem curItem = items.get(position);

			if (curItem.isSelected()) {

				TextView itemName = (TextView) convertView
						.findViewById(R.id.textItemTitle);
				TextView itemValue = (TextView) convertView
						.findViewById(R.id.textValue);

				// combine min and max
				TextView itemRange = (TextView) convertView
						.findViewById(R.id.textRangeFromTo);

				TextView iModificator = (TextView) convertView
						.findViewById(R.id.textModificator);

				itemName.setText(curItem.getItemName());
				itemValue.setText(curItem.getValue());
				itemRange.setText(curItem.getRangeMin() + " - "
						+ curItem.getRangeMax());
				iModificator.setText(curItem.getModificator());
			} else {
				Log.d("curItem.isSelected()???", "" + curItem.isSelected());
				
			}
			// or reuse
		} else {

			MatrixItem curItem = items.get(position);
			if (curItem.isSelected()) {
				TextView itemName = (TextView) convertView
						.findViewById(R.id.textItemTitle);
				TextView itemValue = (TextView) convertView
						.findViewById(R.id.textValue);
				// combine min and max
				TextView itemRange = (TextView) convertView
						.findViewById(R.id.textRangeFromTo);

				TextView itemModificator = (TextView) convertView
						.findViewById(R.id.textModificator);

				itemName.setText(curItem.getItemName());
				itemValue.setText(curItem.getValue());

				itemRange.setText(curItem.getRangeMin() + " - "
						+ curItem.getRangeMax());

				itemModificator.setText(curItem.getModificator());

				// // set modificator text color: blue for positive red for
				// // negative
				if (!curItem.getModificator().isEmpty()) {
					if (Integer.valueOf(curItem.getModificator()) > 0) {
						itemModificator.setTextColor(context.getResources()
								.getColor(R.color.a_blue));
					} else if (Integer.valueOf(curItem.getModificator()) < 0) {
						itemModificator.setTextColor(context.getResources()
								.getColor(R.color.a_red));
					} else
						itemModificator.setTextColor(context.getResources()
								.getColor(R.color.white));
				}
			} else {
				Log.d("curItem.isSelected()???", "" + curItem.isSelected());
				
			}
		}
		return convertView;
	}
}
