package de.fau.cs.mad.gamekobold.matrix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;

public class PlayCharacterMatrixAdapter extends ArrayAdapter<MatrixItem>
		implements Serializable {
	public static final int FLAG_FROM = 1; // Binary 00001
	public static final int FLAG_TO = 2; // Binary 00010
	public static final int FLAG_VALUE = 4; // Binary 00100
	public static final int FLAG_MOD = 8; // Binary 01000
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
			itemValue.setText(curItem.getValue());
			itemRange.setText(curItem.getRangeMin() + " - "
					+ curItem.getRangeMax());
			itemModificator.setText(curItem.getModificator());

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

			// set modificator text color: blue for positive red for
			// negative
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

		}
		return rowView;
	}
}
