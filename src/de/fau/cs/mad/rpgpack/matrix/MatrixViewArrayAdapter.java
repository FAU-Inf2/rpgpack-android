package de.fau.cs.mad.rpgpack.matrix;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.fau.cs.mad.rpgpack.R;

/**
 * This class handles matrix elements we want to display on template creation /
 * or on template editing.
 * 
 */
@SuppressWarnings("serial")
public class MatrixViewArrayAdapter extends ArrayAdapter<MatrixItem> implements
		Serializable {
	public static final int FLAG_FROM = 1; // Binary 00001
	public static final int FLAG_TO = 2; // Binary 00010
	public static final int FLAG_VALUE = 4; // Binary 00100
	public static final int FLAG_MOD = 8; // Binary 01000
	/*
	 * JACKSON START
	 */
	// public MatrixTable jacksonTable;
	/*
	 * JACKSON END
	 */
	Context context;

	// the list of objects we want to display
	private List<MatrixItem> items;

	public MatrixViewArrayAdapter(Context context, List<MatrixItem> items) {
		super(context, R.layout.itemlayout2_matrix_view, items);
		this.context = context;
		this.items = items;
	}

	// needed for viewConvertion so that the system knows that there are
	// different layouts in the adapter
	// 0 for normal item. 1 for "new item" item
	@Override
	public int getItemViewType(int position) {
		// if it is the last element
		if (position == getCount() - 1) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;

		// if it is the last row -> create new item
		if (position == getCount() - 1) {
			if (convertView == null) {
				rowView = inflater
						.inflate(R.layout.itemlayout2_newitem_matrix_view,
								parent, false);
			} else {
				rowView = convertView;
			}
			MatrixItem curItem = items.get(position);

			TextView itemName = (TextView) rowView
					.findViewById(R.id.textItemTitle);
			TextView itemValue = (TextView) rowView
					.findViewById(R.id.textItemAdd);

			itemName.setText(curItem.getItemName());
			itemValue.setText(curItem.getValue());
		}
		// it is normal matrix item
		else {
			if (convertView == null) {
				rowView = inflater.inflate(R.layout.itemlayout2_matrix_view,
						parent, false);
			} else {
				rowView = convertView;
			}
			MatrixItem curItem = items.get(position);

			TextView itemName = (TextView) rowView
					.findViewById(R.id.textItemTitle);
			TextView itemValue = (TextView) rowView
					.findViewById(R.id.textValue);
			// combine min and max
			TextView itemRange = (TextView) rowView
					.findViewById(R.id.textRangeFromTo);
			TextView itemModificator = (TextView) rowView
					.findViewById(R.id.textModificator);

			itemName.setText(curItem.getItemName());

			if (((curItem.getVisibility() & FLAG_FROM) == FLAG_FROM)
					&& ((curItem.getVisibility() & FLAG_TO) == FLAG_TO)) {
				itemRange.setText(curItem.getRangeMin() + " - "
						+ curItem.getRangeMax());
			} else
				itemRange.setText("");

			if ((curItem.getVisibility() & FLAG_VALUE) == FLAG_VALUE) {
				itemValue.setText(curItem.getValue());
			} else
				itemValue.setText("");

			if ((curItem.getVisibility() & FLAG_MOD) == FLAG_MOD) {
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
			} else
				itemModificator.setText("");
		}
		return rowView;
	}

}
