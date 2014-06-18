package de.fau.cs.mad.gamekobold.matrix;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;

public class MatrixViewActivity extends Activity {
	GridView gridView;
	List<MatrixItem> itemsList = getDataForGridView();
	MatrixViewArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_matrix_view);

		// set create new item to the end
		MatrixItem addNewMatrixItem = new MatrixItem("Neues Element", "+", null);
		itemsList.add(addNewMatrixItem);

		gridView = (GridView) findViewById(R.id.gridView1);
		adapter = new MatrixViewArrayAdapter(this, itemsList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				Toast.makeText(
						getApplicationContext(),
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
					Intent i = new Intent(MatrixViewActivity.this,
							AddNewItemActivity.class);

					Log.e("er", "position: " + position);

					i.putExtra("position", position);
					startActivity(i);
				}

			}
		});

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.matrix_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_matrix_view,
					container, false);
			return rootView;
		}
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

		return itemsList;
	}

	private void showPopup() {
		AddNewItemDialogFragment popupAddNewItemFragment = AddNewItemDialogFragment
				.newInstance();
		popupAddNewItemFragment.show(getFragmentManager(),
				"popupAddNewItemFragment");

	}

	public static class AddNewItemDialogFragment extends DialogFragment {
		// TODO prüfen
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

			// TODO x-Button on the top, make it clickable
			// Button cancel =
			// (Button)getActivity().findViewById(R.id.imageView_close);
			// cancel.setOnClickListener(l);
			// setOnClickListener(new OnClickListener() {
			// public void onClick(View v) {
			// AddNewItemDialogFragment.this.getDialog().cancel();
			// }
			// });

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

}
