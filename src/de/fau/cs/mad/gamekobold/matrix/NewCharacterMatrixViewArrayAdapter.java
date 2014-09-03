package de.fau.cs.mad.gamekobold.matrix;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.game.GameCharacter;
import de.fau.cs.mad.gamekobold.jackson.Row;

public class NewCharacterMatrixViewArrayAdapter extends
		ArrayAdapter<MatrixItem> {
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
	private Context context;
	private ImageView highlightingImageView;

	// list of highlighted matrix items assigned to character
	public ArrayList<MatrixItem> selectedMatrixItems;

	// the list of objects we want to display
	private List<MatrixItem> items;

	public NewCharacterMatrixViewArrayAdapter(Context context,
			List<MatrixItem> items) {
		super(context, R.layout.itemlayout_newcharacter_matrix_view, items);
		this.context = context;
		this.items = items;
		this.selectedMatrixItems = new ArrayList<MatrixItem>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// TODO ate refactor with view holder
		// if it's not recycled, initialize some attributes
		if (convertView == null) {
			if (position == getCount() - 1) {
				convertView = inflater.inflate(
						R.layout.itemlayout_new_character_new_item_matrix_view,
						parent, false);
				MatrixItem curItem = items.get(position);

				TextView itemName = (TextView) convertView
						.findViewById(R.id.matr_textItemTitle);
				TextView itemValue = (TextView) convertView
						.findViewById(R.id.matr_textValue);
				itemName.setText(curItem.getItemName());
				itemValue.setText(curItem.getValue());

			} else {
				convertView = inflater.inflate(
						R.layout.itemlayout_newcharacter_matrix_view, parent,
						false);
				TextView itemName = (TextView) convertView
						.findViewById(R.id.matrix_textItemTitle);
				TextView itemValue = (TextView) convertView
						.findViewById(R.id.matrix_textValue);
				// combine min and max
				TextView itemRange = (TextView) convertView
						.findViewById(R.id.matrix_textRangeFromTo);

				TextView itemModificator = (TextView) convertView
						.findViewById(R.id.matrix_textModificator);

				final MatrixItem curItem = items.get(position);
				CheckBox favoriteItem = (CheckBox) convertView
						.findViewById(R.id.favorite_checkbox);
				favoriteItem.setChecked(curItem.isFavorite());

				if (!selectedMatrixItems.contains(curItem)) {
					favoriteItem.setChecked(false);
					favoriteItem.setEnabled(false);
				} else {
					favoriteItem.setEnabled(true);
					favoriteItem
							.setOnCheckedChangeListener(new OnCheckedChangeListener() {
								@Override
								public void onCheckedChanged(
										CompoundButton buttonView,
										boolean isChecked) {

									curItem.setFavorite(isChecked);
									// TODO Benni save isFavorite for current
									// Matrix
									// Item
									if (isChecked) {
										Toast.makeText(
												context,
												curItem.getItemName()
														+ "-Attribut wird zu Deiner Character-Schnellansicht hizugefuegt!",
												Toast.LENGTH_SHORT).show();
									}

								}
							});

				}

				itemName.setText(curItem.getItemName());
				itemValue.setText(curItem.getValue());
				/*
				 * Log.d("ADAPTER", "pos:"+position); Log.d("ADAPTER",
				 * "iRange:"+iRange); Log.d("ADAPTER", "curItem:"+curItem);
				 * Log.d("ADAPTER", "min:"+curItem.getRangeMin());
				 * Log.d("ADAPTER", "max:"+curItem.getRangeMax());
				 */
				itemRange.setText(curItem.getRangeMin() + " - "
						+ curItem.getRangeMax());

				itemModificator.setText(curItem.getModificator());

				highlightingImageView = (ImageView) convertView
						.findViewById(R.id.matrix_item_highlighting_circle);

				Log.i("selectedMatrixItems.contains(curItem) - "
						+ curItem.getItemName(),
						"" + (selectedMatrixItems.contains(curItem)));

				GradientDrawable highlightingShape = (GradientDrawable) highlightingImageView
						.getDrawable();

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

				} else {
					itemName.setTextColor(context.getResources().getColor(
							R.color.grey));
					itemValue.setTextColor(context.getResources().getColor(
							R.color.grey));
					itemRange.setTextColor(context.getResources().getColor(
							R.color.grey));
					itemModificator.setTextColor(context.getResources()
							.getColor(R.color.grey));
				}

				highlightingShape.setColor(selectedMatrixItems
						.contains(curItem) ? context.getResources().getColor(
						R.color.background_green) : context.getResources()
						.getColor(android.R.color.transparent));

				if (!selectedMatrixItems.contains(curItem)) {
					favoriteItem.setChecked(false);
				}
			}
		} else {
			// or reuse
			if (position == getCount() - 1) {
				MatrixItem curItem = items.get(position);

				TextView itemName = (TextView) convertView
						.findViewById(R.id.matr_textItemTitle);
				TextView itemValue = (TextView) convertView
						.findViewById(R.id.matr_textValue);
				itemName.setText(curItem.getItemName());
				itemValue.setText(curItem.getValue());
			} else {
				TextView itemName = (TextView) convertView
						.findViewById(R.id.matrix_textItemTitle);
				TextView itemValue = (TextView) convertView
						.findViewById(R.id.matrix_textValue);
				// combine min and max
				TextView itemRange = (TextView) convertView
						.findViewById(R.id.matrix_textRangeFromTo);

				TextView itemModificator = (TextView) convertView
						.findViewById(R.id.matrix_textModificator);

				final MatrixItem curItem = items.get(position);
				CheckBox favoriteItem = (CheckBox) convertView
						.findViewById(R.id.favorite_checkbox);
				favoriteItem.setChecked(curItem.isFavorite());

				if (!selectedMatrixItems.contains(curItem)) {
					favoriteItem.setChecked(false);
					favoriteItem.setEnabled(false);
				} else {
					favoriteItem.setEnabled(true);
					favoriteItem
							.setOnCheckedChangeListener(new OnCheckedChangeListener() {
								@Override
								public void onCheckedChanged(
										CompoundButton buttonView,
										boolean isChecked) {

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
				}

				itemName.setText(curItem.getItemName());
				itemValue.setText(curItem.getValue());
				/*
				 * Log.d("ADAPTER", "pos:"+position); Log.d("ADAPTER",
				 * "iRange:"+iRange); Log.d("ADAPTER", "curItem:"+curItem);
				 * Log.d("ADAPTER", "min:"+curItem.getRangeMin());
				 * Log.d("ADAPTER", "max:"+curItem.getRangeMax());
				 */
				itemRange.setText(curItem.getRangeMin() + " - "
						+ curItem.getRangeMax());

				itemModificator.setText(curItem.getModificator());

				highlightingImageView = (ImageView) convertView
						.findViewById(R.id.matrix_item_highlighting_circle);

				Log.i("selectedMatrixItems.contains(curItem) - "
						+ curItem.getItemName(),
						"" + (selectedMatrixItems.contains(curItem)));

				GradientDrawable highlightingShape = (GradientDrawable) highlightingImageView
						.getDrawable();

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

				} else {
					itemName.setTextColor(context.getResources().getColor(
							R.color.grey));
					itemValue.setTextColor(context.getResources().getColor(
							R.color.grey));
					itemRange.setTextColor(context.getResources().getColor(
							R.color.grey));
					itemModificator.setTextColor(context.getResources()
							.getColor(R.color.grey));
				}

				highlightingShape.setColor(selectedMatrixItems
						.contains(curItem) ? context.getResources().getColor(
						R.color.background_green) : context.getResources()
						.getColor(android.R.color.transparent));

			}
		}
		return convertView;
	}
}
