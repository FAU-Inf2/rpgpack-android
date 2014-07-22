package de.fau.cs.mad.gamekobold.matrix;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.jackson.MatrixTable;
import de.fau.cs.mad.gamekobold.template_generator.GeneralFragment;

public class MatrixFragment extends GeneralFragment {
	GridView gridView;
	public List<MatrixItem> itemsList = null;
	MatrixViewArrayAdapter adapter;

	/*
	 * JACKSON START
	 */
	public MatrixTable jacksonTable;
	public boolean jacksonInflateWithData;

	/*
	 * JACKSON END
	 */

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.d("MatrixFragment", "ON_CREATE_VIEW_MATRIX");
		View rootView = inflater.inflate(R.layout.fragment_matrix_view,
				container, false);

		gridView = (GridView) rootView.findViewById(R.id.gridView);
		// check needed for jackson data loading
		if (itemsList == null) {
			itemsList = getDataForGridView();
			
			
			jacksonTable.entries = itemsList;
		}
		if (adapter == null) {
			adapter = new MatrixViewArrayAdapter(getActivity(), itemsList);
			// adapter.jacksonTable = jacksonTable;
		}

		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				Toast.makeText(
						getActivity(),
						((TextView) view.findViewById(R.id.textItemTitle))
								.getText(), Toast.LENGTH_SHORT).show();

				// is it last item?
				if (position == adapter.getCount() - 1) {

					Log.e("am", "position: " + position);
					Log.e("am", "Position, getCount: " + adapter.getCount());

					showPopup();

				} else {
					// click on item
					Log.e("er", "position: " + position);
					showPopupForEditing(adapter.getItem(position));
				}

			}
		});

		gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView,
					View view, final int position, long id) {
				Log.d("LONG CLICK", "pos:" + position);
				if (position == adapter.getCount() - 1) {
					return true;
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle(getResources().getString(
						R.string.msg_delete_item));
				builder.setMessage(getResources().getString(
						R.string.msg_yes_to_item_delete));
				builder.setNegativeButton(
						getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				builder.setPositiveButton(getResources()
						.getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								removeMatrixItem(position);
							}
						});
				builder.create().show();
				return true;
			}
		});

		return rootView;

	}

	@Override
	protected void addItemList() {
		// TODO Auto-generated method stub
	}

	@Override
	public void showDialog() {
		// TODO Auto-generated method stub
		showPopup();
	}

	public void addMatrixItem(MatrixItem newItem) {
		// adapter.getCount >= 1
		adapter.insert(newItem, adapter.getCount() - 1);
		adapter.notifyDataSetChanged();
	}

	public void removeMatrixItem(int position) {
		if (position < 0 || position == adapter.getCount() - 1) {
			return;
		}
		adapter.remove(adapter.getItem(position));
		adapter.notifyDataSetChanged();
	}

	// TODO replace with real data, now it is just stub for real data from DB or
	// json file?
	public static List<MatrixItem> getDataForGridView() {
		List<MatrixItem> itemsList = new ArrayList<MatrixItem>();

		MatrixItem item1 = new MatrixItem("Mut", "34", 0, 100, "+1");
		MatrixItem item2 = new MatrixItem("Ausdauer", "45", 20, 500, "-2");
		MatrixItem item3 = new MatrixItem("Kraft", "70", 0, 50, "+3");

		itemsList.add(item1);
		itemsList.add(item2);
		itemsList.add(item3);

		 // set create new item to the end
		 MatrixItem addNewMatrixItem = new MatrixItem("Neues Element", "+",
		 null);
		 itemsList.add(addNewMatrixItem);

		return itemsList;
	}

	private void showPopup() {
		AddNewItemDialogFragment popupAddNewItemFragment = AddNewItemDialogFragment
				.newInstance(this);
		popupAddNewItemFragment.show(getFragmentManager(),
				"popupAddNewItemFragment");

	}

	private void showPopupForEditing(MatrixItem item) {
		AddNewItemDialogFragment popupAddNewItemFragment = AddNewItemDialogFragment
				.newInstance(this);
		popupAddNewItemFragment.editItem = item;
		popupAddNewItemFragment.show(getFragmentManager(),
				"popupAddNewItemFragment");
	}

	public static class AddNewItemDialogFragment extends DialogFragment {
		private EditText itemName, rangeMin, rangeMax, defaultVal, modificator;
		public MatrixFragment matrixFragment;
		public MatrixItem editItem = null;

		// TODO pr�fen
		public static AddNewItemDialogFragment newInstance(
				MatrixFragment receiver) {
			AddNewItemDialogFragment fragment = new AddNewItemDialogFragment();
			fragment.matrixFragment = receiver;
			return fragment;
		}

		@Override
		public Dialog onCreateDialog(Bundle SaveInstanceState) {
			// Use the Builder class for convenient Dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			// Get the layout inflater
			LayoutInflater inflater = getActivity().getLayoutInflater();

			// Inflate and set the layout for the dialog
			// Pass null as the parent view because its going in the dialog
			// layout
			View view = inflater.inflate(R.layout.popup2_add_new_item, null);
			// get all EditTexts
			itemName = (EditText) view.findViewById(R.id.itemName);
			rangeMin = (EditText) view.findViewById(R.id.rangeFrom);
			rangeMax = (EditText) view.findViewById(R.id.rangeTo);
			defaultVal = (EditText) view.findViewById(R.id.defaultValue);
			modificator = (EditText) view.findViewById(R.id.modificator);
			// check for editItem
			if (editItem != null) {
				// insert values from editItem into views
				itemName.setText(editItem.getItemName());
				rangeMin.setText(String.valueOf(editItem.getRangeMin()));
				rangeMax.setText(String.valueOf(editItem.getRangeMax()));
				defaultVal.setText(editItem.getValue());
				modificator.setText(editItem.getModificator());
			}
			builder.setView(view);

			// set Dialog characteristics
			// get right button text
			String positiveButtonText;
			if (editItem == null) {
				// add new
				positiveButtonText = getString(R.string.add_item);
			} else {
				// editing
				positiveButtonText = getString(R.string.save_changes);
			}
			builder.setMessage(getString(R.string.popup_titel));
			builder.setPositiveButton(positiveButtonText,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// TODO Check for null values
							final String name = itemName.getEditableText()
									.toString();
							final String defValue = defaultVal
									.getEditableText().toString();
							final int min = Integer.parseInt(rangeMin
									.getEditableText().toString());
							final int max = Integer.parseInt(rangeMax
									.getEditableText().toString());
							final String mod = modificator.getEditableText()
									.toString();
							/*
							 * Log.d("NEW ITEM", "Name:"+name);
							 * Log.d("NEW ITEM", "min:"+min); Log.d("NEW ITEM",
							 * "max:"+max);
							 */
							if (editItem == null) {
								final MatrixItem newItem = new MatrixItem(name,
										defValue, min, max, mod);
								matrixFragment.addMatrixItem(newItem);
							} else {
								editItem.setItemName(name);
								editItem.setValue(defValue);
								editItem.setRangeMin(min);
								editItem.setRangeMax(max);
								editItem.setModificator(mod);
								matrixFragment.adapter.notifyDataSetChanged();
							}
						}
					});

			builder.setNegativeButton(getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog
							AddNewItemDialogFragment.this.getDialog().cancel();
						}
					});

			// Create the AlertDialog object and return it
			final AlertDialog dialog = builder.create();

			dialog.setOnShowListener(new DialogInterface.OnShowListener() {
				@Override
				public void onShow(final DialogInterface dialog) {

					Button positiveButton = ((AlertDialog) dialog)
							.getButton(DialogInterface.BUTTON_POSITIVE);

					// set OK button color here
					positiveButton.setBackgroundColor(getActivity()
							.getResources().getColor(R.color.bright_green));
					positiveButton.invalidate();
				}
			});
	        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			return dialog;
		}
	}

	/*
	 * JACKSON START
	 */
	public void jacksonInflate(MatrixTable myTable, Context appContext) {
		// set table
		setJacksonTable(myTable);
		// set flag, so that we are inflating the views with data from jackson
		// model
		// jacksonInflateWithData = true;
		itemsList = jacksonTable.entries;
		// add the "new item" entry
		itemsList.add(new MatrixItem(appContext.getResources().getString(
				R.string.text_new_element), "+", null));
	}

	public void setJacksonTable(MatrixTable myTable) {
		jacksonTable = myTable;
	}
	/*
	 * JACKSON END
	 */
}
