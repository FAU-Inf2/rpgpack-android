package de.fau.cs.mad.gamekobold.matrix;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;

public class SettingValueDialogFragment extends DialogFragment {
	private static final String KEY_SAVE_MATRIX_VALUE = "KEY_SAVE_MATRIX_VALUE";
	private EditText editTextMatrixValue;
	protected ArrayAdapter<MatrixItem> selectedItemsAdapter;
	protected ArrayList<MatrixItem> selectedItems;
	public MatrixItem matrixItem = null;

	public static SettingValueDialogFragment newInstance() {
		SettingValueDialogFragment frag = new SettingValueDialogFragment();
		return frag;
	}

	public void passAdapter(ArrayAdapter<MatrixItem> selectedItemsAdapter) {
		this.selectedItemsAdapter = selectedItemsAdapter;
	}

	public void passSelItems(ArrayList<MatrixItem> selectedItems) {
		this.selectedItems = selectedItems;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());

		View view = getActivity().getLayoutInflater().inflate(
				R.layout.dialog_fragment_setting_matrix_value, null);

		editTextMatrixValue = (EditText) view
				.findViewById(R.id.editTextMatrixValue);
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(KEY_SAVE_MATRIX_VALUE)) {
				editTextMatrixValue.setText((savedInstanceState
						.getInt(KEY_SAVE_MATRIX_VALUE)));
			}
		}

		alertDialogBuilder.setView(view);
		alertDialogBuilder.setTitle(getResources().getString(
				R.string.string_set_matrix_value));

		// // create add and subtract buttons for the dialog
		// ImageButton addButton = (ImageButton) view
		// .findViewById(R.id.button_add_column);
		// addButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// int oldValue = (Integer.parseInt(editTextMatrixValue.getText()
		// .toString()));
		// int newValue = oldValue + 1;
		// editTextMatrixValue.setText(Integer.toString(newValue));
		// // adaptDialogTable(dialogTable,
		// // (Integer.parseInt(editTextMatrixValue.getText().toString())));
		// }
		// });
		// ImageButton subtractButton = (ImageButton) view
		// .findViewById(R.id.button_remove_column);
		// subtractButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// int oldValue = (Integer.parseInt(editTextMatrixValue.getText()
		// .toString()));
		// int newValue = oldValue - 1;
		// editTextMatrixValue.setText(Integer.toString(newValue));
		// // adaptDialogTable(dialogTable,
		// // (Integer.parseInt(editTextMatrixValue.getText().toString())));
		// }
		// });

		alertDialogBuilder.setPositiveButton(
				getResources().getString(R.string.save),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(
								getActivity(),
								matrixItem.getItemName()
										+ "-Attribut wird zu dem Charakter hinzugefuegt",
								Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						matrixItem.setValue(editTextMatrixValue.getText()
								.toString());
						selectedItems.add(matrixItem);

						selectedItemsAdapter.notifyDataSetChanged();

					}
				});
		alertDialogBuilder.setNegativeButton(
				getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(getActivity(), "nothing",
								Toast.LENGTH_SHORT).show();
						dialog.cancel();

						// TODO set selected or deselected here

					}
				});

		return alertDialogBuilder.create();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(KEY_SAVE_MATRIX_VALUE,
				Integer.getInteger(editTextMatrixValue.getText().toString()));
		super.onSaveInstanceState(outState);
	}
}
