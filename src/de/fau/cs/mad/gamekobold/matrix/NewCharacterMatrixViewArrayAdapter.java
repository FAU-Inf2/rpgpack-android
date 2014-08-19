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
import android.widget.ImageView;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.game.GameCharacter;

public class NewCharacterMatrixViewArrayAdapter extends
		ArrayAdapter<MatrixItem> {

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
	public ArrayList<MatrixItem> selectedItems = new ArrayList<MatrixItem>();

	// the list of objects we want to display
	private List<MatrixItem> items;

	public NewCharacterMatrixViewArrayAdapter(Context context,
			List<MatrixItem> items) {
		super(context, R.layout.itemlayout_newcharacter_matrix_view, items);
		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// View itemView;

		// if it's not recycled, initialize some attributes
		if (convertView == null) {

			convertView = inflater
					.inflate(R.layout.itemlayout_newcharacter_matrix_view,
							parent, false);
			MatrixItem curItem = items.get(position);
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

			// ///////////
			highlightingImageView = (ImageView) convertView
					.findViewById(R.id.item_highlighting_circle);

			Log.e("selectedItems.contains(position)?",
					"" + (selectedItems.contains(position)));

			GradientDrawable highlightingShape = (GradientDrawable) highlightingImageView
					.getDrawable();

			Log.e("highlightingShape is null?", ""
					+ (highlightingShape == null));

			if (selectedItems.contains(curItem)) {
				itemName.setTextColor(context.getResources().getColor(
						R.color.white));
				itemValue.setTextColor(context.getResources().getColor(
						R.color.white));
				itemRange.setTextColor(context.getResources().getColor(
						R.color.white));
				itemModificator.setTextColor(context.getResources().getColor(
						R.color.white));

			} else {
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
					.setColor(selectedItems.contains(curItem) ? context
							.getResources().getColor(R.color.background_green)
							: context.getResources().getColor(
									android.R.color.transparent));

		}
		// or reuse
		else {

			TextView itemName = (TextView) convertView
					.findViewById(R.id.textItemTitle);
			TextView itemValue = (TextView) convertView
					.findViewById(R.id.textValue);
			// combine min and max
			TextView itemRange = (TextView) convertView
					.findViewById(R.id.textRangeFromTo);

			TextView itemModificator = (TextView) convertView
					.findViewById(R.id.textModificator);

			MatrixItem curItem = items.get(position);

			itemName.setText(curItem.getItemName());
			itemValue.setText(curItem.getValue());
			/*
			 * Log.d("ADAPTER", "pos:"+position); Log.d("ADAPTER",
			 * "iRange:"+iRange); Log.d("ADAPTER", "curItem:"+curItem);
			 * Log.d("ADAPTER", "min:"+curItem.getRangeMin()); Log.d("ADAPTER",
			 * "max:"+curItem.getRangeMax());
			 */
			itemRange.setText(curItem.getRangeMin() + " - "
					+ curItem.getRangeMax());

			itemModificator.setText(curItem.getModificator());

			// // set modificator text color: blue for positive red for
			// // negative
			// // TODO make this check smarter
			// if (!curItem.getModificator().isEmpty()) {
			//
			// if (curItem.getModificator().charAt(0) == '+')
			// iModificator.setTextColor(getContext().getResources()
			// .getColor(R.color.a_blue));
			// else if (curItem.getModificator().charAt(0) == '-')
			// iModificator.setTextColor(getContext().getResources()
			// .getColor(R.color.a_red));
			// else
			// iModificator.setTextColor(getContext().getResources()
			// .getColor(R.color.a_grey));
			//
			// }

			highlightingImageView = (ImageView) convertView
					.findViewById(R.id.item_highlighting_circle);

			Log.e("selectedItems.contains(position)?",
					"" + (selectedItems.contains(position)));

			GradientDrawable highlightingShape = (GradientDrawable) highlightingImageView
					.getDrawable();

			Log.e("highlightingShape is null?", ""
					+ (highlightingShape == null));

			if (selectedItems.contains(curItem)) {
				itemName.setTextColor(context.getResources().getColor(
						R.color.white));
				itemValue.setTextColor(context.getResources().getColor(
						R.color.white));
				itemRange.setTextColor(context.getResources().getColor(
						R.color.white));
				itemModificator.setTextColor(context.getResources().getColor(
						R.color.white));

			} else {
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
					.setColor(selectedItems.contains(curItem) ? context
							.getResources().getColor(R.color.background_green)
							: context.getResources().getColor(
									android.R.color.transparent));

		}

		return convertView;
	}

}
