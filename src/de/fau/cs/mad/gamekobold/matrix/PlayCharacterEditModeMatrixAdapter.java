package de.fau.cs.mad.gamekobold.matrix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;

/**
 * This class handles matrix elements we want to display on character playing in
 * edit mode.
 * 
 */
public class PlayCharacterEditModeMatrixAdapter extends
		ArrayAdapter<MatrixItem> implements Serializable {
	// this flags is used to store visibility of UI elements
	public static final int FLAG_FROM = 1; // Binary 00001
	public static final int FLAG_TO = 2; // Binary 00010
	public static final int FLAG_VALUE = 4; // Binary 00100
	public static final int FLAG_MOD = 8; // Binary 01000

	private Context context;
	private ImageView highlightingImageView;

	// list of highlighted matrix items assigned to character
	public ArrayList<MatrixItem> selectedMatrixItems;

	// the list of objects we want to display
	List<MatrixItem> items;

	public PlayCharacterEditModeMatrixAdapter(Context context,
			List<MatrixItem> items) {
		super(context, R.layout.itemlayout_newcharacter_matrix_view, items);
		this.context = context;
		this.items = items;
		this.selectedMatrixItems = new ArrayList<MatrixItem>();
	}

	// needed for viewConvertion so that the system knows that there are
	// different layouts in the adapter 1 for the edit last template item and 0
	// for templates
	@Override
	public int getItemViewType(int position) {
		// if create new item
		if (position == getCount() - 1) {
			// return 1
			return 1;
		}
		// return 0 for every other item
		return 0;
	}

	// 2 otherwise
	@Override
	public int getViewTypeCount() {
		return 2; // Count of different layouts
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;

		if (convertView == null) {
			// if it is the last row -> edit last template
			if (position == getCount() - 1) {
				rowView = inflater
						.inflate(R.layout.itemlayout2_newitem_matrix_view,
								parent, false);
			} else {
				rowView = inflater.inflate(
						R.layout.itemlayout_newcharacter_matrix_view, parent,
						false);
			}
		} else {
			rowView = convertView;
		}

		MatrixItem curItem = items.get(position);
		// if it is the last row -> create new item
		if (position == getCount() - 1) {
			TextView itemName = (TextView) rowView
					.findViewById(R.id.textItemTitle);
			TextView itemValue = (TextView) rowView
					.findViewById(R.id.textItemAdd);

			itemName.setText(curItem.getItemName());
			itemValue.setText(curItem.getValue());
		}
		// it is normal matrix item
		else {
			TextView itemName = (TextView) rowView
					.findViewById(R.id.matrix_textItemTitle);
			TextView itemValue = (TextView) rowView
					.findViewById(R.id.matrix_textValue);
			// combine min and max
			TextView itemRange = (TextView) rowView
					.findViewById(R.id.matrix_textRangeFromTo);
			TextView itemModificator = (TextView) rowView
					.findViewById(R.id.matrix_textModificator);

			itemName.setText(curItem.getItemName());
			itemValue.setText(curItem.getValue());
			itemRange.setText(curItem.getRangeMin() + " - "
					+ curItem.getRangeMax());
			itemModificator.setText(curItem.getModificator());

			highlightingImageView = (ImageView) rowView
					.findViewById(R.id.matrix_item_highlighting_circle);

			GradientDrawable highlightingShape = (GradientDrawable) highlightingImageView
					.getDrawable();

			// we want to see it from UI design directly if an item is selected
			if (selectedMatrixItems.contains(curItem)) {
				itemName.setTextColor(context.getResources().getColor(
						R.color.white));
				itemValue.setTextColor(context.getResources().getColor(
						R.color.white));
				itemRange.setTextColor(context.getResources().getColor(
						R.color.white));

				if ((!(curItem.getModificator() == null))
						&& !curItem.getModificator().equals("")) {
					if (Integer.valueOf(curItem.getModificator()) > 0) {
						itemModificator.setTextColor(context.getResources()
								.getColor(R.color.blue));
					} else if (Integer.valueOf(curItem.getModificator()) < 0) {
						itemModificator.setTextColor(context.getResources()
								.getColor(R.color.red));
					} else
						itemModificator.setTextColor(context.getResources()
								.getColor(R.color.white));
				}
			}
			// not selected items are all grey
			else {
				itemName.setTextColor(context.getResources().getColor(
						R.color.grey));
				itemValue.setTextColor(context.getResources().getColor(
						R.color.grey));
				itemRange.setTextColor(context.getResources().getColor(
						R.color.grey));
				itemModificator.setTextColor(context.getResources().getColor(
						R.color.grey));
			}
			highlightingShape
					.setColor(selectedMatrixItems.contains(curItem) ? context
							.getResources().getColor(R.color.background_green)
							: context.getResources().getColor(
									android.R.color.transparent));
		}
		return rowView;
	}
}
