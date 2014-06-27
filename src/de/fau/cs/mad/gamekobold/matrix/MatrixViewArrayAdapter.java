package de.fau.cs.mad.gamekobold.matrix;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.fau.cs.mad.gamekobold.R;

public class MatrixViewArrayAdapter extends ArrayAdapter<MatrixItem> {

	Context context;

	// the list of objects we want to display
	private List<MatrixItem> items;

	public MatrixViewArrayAdapter(Context context, List<MatrixItem> items) {
		super(context, R.layout.itemlayout2_matrix_view, items);
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
			if (position == getCount() - 1) {
				// if it is the last row -> create new item
				convertView = inflater
						.inflate(R.layout.itemlayout2_newitem_matrix_view,
								parent, false);

				MatrixItem curItem = items.get(position);

				TextView iName = (TextView) convertView
						.findViewById(R.id.textView4);
				TextView iValue = (TextView) convertView
						.findViewById(R.id.textView1);

				iName.setText(curItem.getItemName());
				iValue.setText(curItem.getValue());

				Log.e("1", "1");

			} else {
				convertView = inflater.inflate(
						R.layout.itemlayout2_matrix_view, parent, false);
				MatrixItem curItem = items.get(position);
				TextView iName = (TextView) convertView
						.findViewById(R.id.textView4);
				TextView iValue = (TextView) convertView
						.findViewById(R.id.textView1);

				// combine min and max
				TextView iRange = (TextView) convertView
						.findViewById(R.id.textView2);
				TextView iModificator = (TextView) convertView
						.findViewById(R.id.textView3);

				iName.setText(curItem.getItemName());
				iValue.setText(curItem.getValue());
				iRange.setText(curItem.getRangeMin() + " - "
						+ curItem.getRangeMax());
				iModificator.setText(curItem.getModificator());
				Log.e("2", "2");
			}
			// or reuse
		} else {
			// if it is the last row -> create new template
			if (position == getCount() - 1) {

				TextView iName = (TextView) convertView
						.findViewById(R.id.textView4);

				TextView iValue = (TextView) convertView
						.findViewById(R.id.textView1);
				MatrixItem curItem = items.get(position);

				iName.setText(curItem.getItemName());
				iValue.setText(curItem.getValue());
				Log.e("3", "3");

			} else {
				// itemView = inflater.inflate(R.layout.itemlayout2_matrix_view,
				// parent, false);
				TextView iName = (TextView) convertView
						.findViewById(R.id.textView4);
				TextView iValue = (TextView) convertView
						.findViewById(R.id.textView1);
				// combine min and max
				TextView iRange = (TextView) convertView
						.findViewById(R.id.textView2);

				TextView iModificator = (TextView) convertView
						.findViewById(R.id.textView3);

				MatrixItem curItem = items.get(position);

				iName.setText(curItem.getItemName());
				iValue.setText(curItem.getValue());
				iRange.setText(curItem.getRangeMin() + " - "
						+ curItem.getRangeMax());

				iModificator.setText(curItem.getModificator());
				Log.e("4", "4");
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
			}
		}
		return convertView;
	}

}
