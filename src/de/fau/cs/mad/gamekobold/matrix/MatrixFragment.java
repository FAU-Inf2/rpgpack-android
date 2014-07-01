package de.fau.cs.mad.gamekobold.matrix;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.jackson.MatrixTable;
import de.fau.cs.mad.gamekobold.jackson.Table;
import de.fau.cs.mad.gamekobold.template_generator.GeneralFragment;

public class MatrixFragment extends GeneralFragment {
	GridView gridView;
	List<MatrixItem> itemsList = null;
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
		Log.d("MatrixFragment", "ON_CREATE_VIEW_MATRIX");
		View rootView = inflater.inflate(R.layout.fragment_matrix_view,
				container, false);

		gridView = (GridView) rootView.findViewById(R.id.gridView1);
		// check needed for jackson data loading
		if(itemsList == null) {
			itemsList = getDataForGridView();
			jacksonTable.entries = itemsList;
		}
		if(adapter == null) {
			adapter = new MatrixViewArrayAdapter(getActivity(), itemsList);
			adapter.jacksonTable = jacksonTable;
		}
		
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				Toast.makeText(
						getActivity(),
						((TextView) view.findViewById(R.id.textView4))
								.getText(), Toast.LENGTH_SHORT).show();

				// is it last item?
				if (position == adapter.getCount() - 1) {

					Log.e("am", "position: " + position);
					Log.e("am", "Position, getCount: " + adapter.getCount());

					showPopup();

				} else {
					// click on item
					// TODO what's next?
					Log.e("er", "position: " + position);
				}

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

	// TODO replace with real data, now it is just stub for real data from DB or
	// json file?
	public static List<MatrixItem> getDataForGridView() {
		List<MatrixItem> itemsList = new ArrayList<MatrixItem>();

		MatrixItem item1 = new MatrixItem("Mut", "34", 0, 100, "+1");
		MatrixItem item2 = new MatrixItem("Ausdauer", "45", 20, 500, "-2");
		MatrixItem item3 = new MatrixItem("Kraft", "70", 0, 100, "+3");

		itemsList.add(item1);
		itemsList.add(item2);
		itemsList.add(item3);

		// set create new item to the end
		MatrixItem addNewMatrixItem = new MatrixItem("Neues Element", "+", null);
		itemsList.add(addNewMatrixItem);
		
		return itemsList;
	}

	private void showPopup() {
		AddNewItemDialogFragment popupAddNewItemFragment = AddNewItemDialogFragment
				.newInstance();
		popupAddNewItemFragment.show(getFragmentManager(),
				"popupAddNewItemFragment");

	}

	public static class AddNewItemDialogFragment extends DialogFragment {
		// TODO pr�fen
		public static AddNewItemDialogFragment newInstance() {
			AddNewItemDialogFragment fragment = new AddNewItemDialogFragment();
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
			builder.setView(inflater
					.inflate(R.layout.popup2_add_new_item, null));

			// set Dialog characteristics
			builder.setMessage(getString(R.string.popup_titel));
			builder.setPositiveButton(getString(R.string.add_item),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							// TODO ADD new Item
							// TODO @Benni JACKSON Add new Item
						}
					});

			builder.setNegativeButton("Cancel",
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
							.getResources().getColor(R.color.a_green));
					positiveButton.invalidate();
				}
			});
			return dialog;
		}
	}
	
	/*
	 * JACKSON START
	 */
	public void jacksonInflate(MatrixTable myTable, Activity activity) {
		// set table
		setJacksonTable(myTable);
		// set flag, so that we are inflating the views with data from jackson model
		//jacksonInflateWithData = true;
		itemsList = jacksonTable.entries;
		// add the "new item" entry
		itemsList.add(new MatrixItem("Neues Element", "+", null));
	}
	
	public void setJacksonTable(MatrixTable myTable) {
		jacksonTable = myTable;
		if(adapter != null) {
			adapter.jacksonTable = myTable;			
		}
	}
	/*
	 * JACKSON END
	 */
}
