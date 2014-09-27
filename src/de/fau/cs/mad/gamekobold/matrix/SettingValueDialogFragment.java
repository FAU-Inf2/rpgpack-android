package de.fau.cs.mad.gamekobold.matrix;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;

public class SettingValueDialogFragment extends DialogFragment {
	private static final String KEY_SAVE_MATRIX_VALUE = "KEY_SAVE_MATRIX_VALUE";
	private static final String KEY_SAVE_MATRIX_ITEM = "KEY_SAVE_MATRIX_ITEM";
	private static final String KEY_SAVE_SEL_ITEMS = "KEY_SAVE_SEL_ITEMS";
	private static final String KEY_SAVE_ADAPTER = "KEY_SAVE_ADAPTER";

	private EditText editTextMatrixValue;
	protected ArrayAdapter<MatrixItem> selectedItemsAdapter;
	protected ArrayList<MatrixItem> selectedItems;
	public MatrixItem matrixItem = null;
	public MatrixFragment matrixFragment;

	public static SettingValueDialogFragment newInstance(MatrixFragment receiver) {
		SettingValueDialogFragment frag = new SettingValueDialogFragment();
		frag.matrixFragment = receiver;
		return frag;
	}

	public void passAdapter(ArrayAdapter<MatrixItem> selectedItemsAdapter) {
		this.selectedItemsAdapter = selectedItemsAdapter;
	}

	public void passSelItems(ArrayList<MatrixItem> selectedItems) {
		this.selectedItems = selectedItems;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Restore the fragment's state here
		if (savedInstanceState != null) {
			matrixFragment = (MatrixFragment) getFragmentManager().getFragment(
					savedInstanceState, "matrixFragment");
		}
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
						.getString(KEY_SAVE_MATRIX_VALUE)));
			}
			if (savedInstanceState.containsKey(KEY_SAVE_MATRIX_ITEM)) {
				matrixItem = (MatrixItem) savedInstanceState
						.getSerializable(KEY_SAVE_MATRIX_ITEM);
			}
			if (savedInstanceState.containsKey(KEY_SAVE_SEL_ITEMS)) {
				selectedItems = (ArrayList<MatrixItem>) savedInstanceState
						.getSerializable(KEY_SAVE_SEL_ITEMS);
			}
			if (savedInstanceState.containsKey(KEY_SAVE_SEL_ITEMS)) {
				selectedItemsAdapter = (ArrayAdapter<MatrixItem>) savedInstanceState
						.getSerializable(KEY_SAVE_ADAPTER);
			}

		}

		alertDialogBuilder.setView(view);
		alertDialogBuilder.setTitle(getResources().getString(
				R.string.string_set_matrix_value));

		// create add and subtract buttons for the dialog
		ImageButton addButton = (ImageButton) view
				.findViewById(R.id.button_add_column);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int oldValue = (Integer.parseInt(editTextMatrixValue.getText()
						.toString()));
				int newValue = oldValue + 1;
				editTextMatrixValue.setText(Integer.toString(newValue));
				// adaptDialogTable(dialogTable,
				// (Integer.parseInt(editTextMatrixValue.getText().toString())));
			}
		});
		ImageButton subtractButton = (ImageButton) view
				.findViewById(R.id.button_remove_column);
		subtractButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int oldValue = (Integer.parseInt(editTextMatrixValue.getText()
						.toString()));
				int newValue = oldValue - 1;
				editTextMatrixValue.setText(Integer.toString(newValue));
				// adaptDialogTable(dialogTable,
				// (Integer.parseInt(editTextMatrixValue.getText().toString())));
			}
		});

		alertDialogBuilder.setPositiveButton(
				getResources().getString(R.string.save),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(
								getActivity(),
								matrixItem.getItemName()
										+ getResources()
												.getString(
														R.string.msg_added_matrix_to_character),
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

					}
				});

		// Create the AlertDialog object and return it
		final AlertDialog dialog = alertDialogBuilder.create();

		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(final DialogInterface dialog) {

				Button positiveButton = ((AlertDialog) dialog)
						.getButton(DialogInterface.BUTTON_POSITIVE);

				// set OK button color here
				positiveButton.setBackgroundColor(getActivity().getResources()
						.getColor(R.color.bright_green));
				positiveButton.invalidate();
			}
		});
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		return dialog;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_SAVE_MATRIX_VALUE, editTextMatrixValue.getText()
				.toString());

		outState.putSerializable(KEY_SAVE_MATRIX_ITEM,
				(Serializable) matrixItem);
		outState.putSerializable(KEY_SAVE_SEL_ITEMS,
				(Serializable) selectedItems);
		outState.putSerializable(KEY_SAVE_ADAPTER,
				(Serializable) selectedItemsAdapter);

		// Save the fragment's instance
		getFragmentManager().putFragment(outState, "matrixFragment",
				matrixFragment);
	}
}
