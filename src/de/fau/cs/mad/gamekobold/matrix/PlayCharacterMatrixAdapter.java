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

public class PlayCharacterMatrixAdapter extends ArrayAdapter<MatrixItem> implements Serializable{
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

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// View itemView;
		// TODO ate refactor with view holder
		// if it's not recycled, initialize some attributes
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.itemlayout_play_character_matrix_view, parent,
					false);
			final MatrixItem curItem = items.get(position);

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

				CheckBox favoriteItem = (CheckBox) convertView
						.findViewById(R.id.favorite_checkbox);
				favoriteItem.setChecked(curItem.isFavorite());

				favoriteItem
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {

								curItem.setFavorite(isChecked);
								// TODO Benni save isFavorite for current
								// Matrix
								// Item
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

			} else {
				Log.d("curItem.isSelected()???", "" + curItem.isSelected());

			}
			// or reuse
		} else {

			final MatrixItem curItem = items.get(position);
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

				CheckBox favoriteItem = (CheckBox) convertView
						.findViewById(R.id.favorite_checkbox);
				favoriteItem.setChecked(curItem.isFavorite());

				favoriteItem.setEnabled(true);
				favoriteItem
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {

								curItem.setFavorite(isChecked);
								// TODO Benni save isFavorite for current
								// Matrix
								// Item
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

			} else {
				Log.d("curItem.isSelected()???", "" + curItem.isSelected());

			}
		}
		return convertView;
	}
}
