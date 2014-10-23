package de.fau.cs.mad.rpgpack.matrix;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.rpgpack.R;

/**
 * This class handles matrix elements we want to display on character playing in
 * normal (not edit) mode.
 * 
 */
public class PlayCharacterMatrixAdapter extends ArrayAdapter<MatrixItem>
		implements Serializable {
	// this flags is used to store visibility of UI elements
	public static final int FLAG_FROM = 1; // Binary 00001
	public static final int FLAG_TO = 2; // Binary 00010
	public static final int FLAG_VALUE = 4; // Binary 00100
	public static final int FLAG_MOD = 8; // Binary 01000
	Context context;

	// the list of objects we want to display
	public List<MatrixItem> items;

	public PlayCharacterMatrixAdapter(Context context, List<MatrixItem> items) {
		super(context, R.layout.itemlayout_play_character_matrix_view, items);
		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		if (convertView == null) {
			rowView = inflater.inflate(
					R.layout.itemlayout_play_character_matrix_view, parent,
					false);
		} else {
			rowView = convertView;
		}

		final MatrixItem curItem = items.get(position);

		// inflate only selected matrix items, because at normal (not edit)
		// mode, we want to see not all of them
		if (curItem.isSelected()) {
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

			// favorites - not implemented yet, it is set as invisible in xml
			// layout file
			CheckBox favoriteItem = (CheckBox) rowView
					.findViewById(R.id.favorite_checkbox);
			favoriteItem.setChecked(curItem.isFavorite());
			favoriteItem
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						// TODO add this matrix item to the favorites -not
						// implemented now
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							curItem.setFavorite(isChecked);
							if (isChecked) {
								Toast.makeText(
										context,
										curItem.getItemName()
												+ context
														.getResources()
														.getString(
																R.string.msg_added_to_matrix_favorite),
										Toast.LENGTH_SHORT).show();
							}
						}
					});

		}
		return rowView;
	}

}
