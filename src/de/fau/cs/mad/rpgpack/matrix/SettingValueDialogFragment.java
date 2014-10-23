package de.fau.cs.mad.rpgpack.matrix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import de.fau.cs.mad.rpgpack.R;

/**
 * This class handles pop up to set new value to the matrix element.
 * 
 */
public class SettingValueDialogFragment extends DialogFragment {
	private static final String KEY_SAVE_MATRIX_VALUE = "KEY_SAVE_MATRIX_VALUE";
	private static final String KEY_SAVE_MATRIX_ITEM = "KEY_SAVE_MATRIX_ITEM";
	private static final String KEY_SAVE_SEL_ITEMS = "KEY_SAVE_SEL_ITEMS";
	private static final String KEY_SAVE_ADAPTER = "KEY_SAVE_ADAPTER";
	private static final String KEY_SAVE_ADAPTER_ALL_ITEMS = "KEY_SAVE_ADAPTER_ALL_ITEMS";

	private EditText editTextMatrixValue;
	protected ArrayAdapter<MatrixItem> editModeAdapter;
	protected ArrayAdapter<MatrixItem> playAdapter;
	protected ArrayList<MatrixItem> playMatrixItems;

	public MatrixItem matrixItem = null;
	public MatrixFragment matrixFragment;

	public static SettingValueDialogFragment newInstance(MatrixFragment receiver) {
		SettingValueDialogFragment frag = new SettingValueDialogFragment();
		frag.matrixFragment = receiver;
		return frag;
	}

	public void passAdapterEdit(ArrayAdapter<MatrixItem> editModeAdapter) {
		this.editModeAdapter = editModeAdapter;
	}

	public void passAdapterNormal(ArrayAdapter<MatrixItem> playAdapter) {
		this.playAdapter = playAdapter;
	}

	public void passSelItems(List<MatrixItem> playMatrixItems) {
		this.playMatrixItems = (ArrayList<MatrixItem>) playMatrixItems;
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
		// retain values
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
				playMatrixItems = (ArrayList<MatrixItem>) savedInstanceState
						.getSerializable(KEY_SAVE_SEL_ITEMS);
			}
			if (savedInstanceState.containsKey(KEY_SAVE_SEL_ITEMS)) {
				editModeAdapter = (ArrayAdapter<MatrixItem>) savedInstanceState
						.getSerializable(KEY_SAVE_ADAPTER);
			}
			if (savedInstanceState.containsKey(KEY_SAVE_SEL_ITEMS)) {
				playAdapter = (ArrayAdapter<MatrixItem>) savedInstanceState
						.getSerializable(KEY_SAVE_ADAPTER_ALL_ITEMS);
			}

		}

		// set default popup value to the current matrix value
		editTextMatrixValue.setText(matrixItem.getValue());
		alertDialogBuilder.setView(view);
		alertDialogBuilder.setTitle(getResources().getString(
				R.string.string_set_matrix_value));

		// create add and subtract buttons for the dialog
		ImageButton addButton = (ImageButton) view
				.findViewById(R.id.button_add_column);
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int oldValue;
				if (editTextMatrixValue == null
						|| editTextMatrixValue.getText() == null
						|| editTextMatrixValue.getText().toString().equals("")) {
					// if no value is set, then use a default one (here is 0)
					oldValue = 0;
				} else {
					oldValue = (Integer.parseInt(editTextMatrixValue.getText()
							.toString()));
				}

				int newValue = oldValue + 1;
				editTextMatrixValue.setText(Integer.toString(newValue));
			}
		});
		ImageButton subtractButton = (ImageButton) view
				.findViewById(R.id.button_remove_column);
		subtractButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int oldValue;
				if (editTextMatrixValue == null
						|| editTextMatrixValue.getText() == null
						|| editTextMatrixValue.getText().toString().equals("")) {
					// if no value is set, then use a default one (here is 0)
					oldValue = 0;
				} else {
					oldValue = (Integer.parseInt(editTextMatrixValue.getText()
							.toString()));
				}
				int newValue = oldValue - 1;
				editTextMatrixValue.setText(Integer.toString(newValue));
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
						playMatrixItems.add(matrixItem);
						if (!(editModeAdapter == null))
							editModeAdapter.notifyDataSetChanged();
						if (!(playAdapter == null))
							playAdapter.notifyDataSetChanged();

					}
				});
		alertDialogBuilder.setNegativeButton(
				getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
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
				(Serializable) playMatrixItems);
		if (!(editModeAdapter == null))
			outState.putSerializable(KEY_SAVE_ADAPTER,
					(Serializable) editModeAdapter);
		if (!(playAdapter == null))
			outState.putSerializable(KEY_SAVE_ADAPTER_ALL_ITEMS,
					(Serializable) playAdapter);

		// Save the fragment's instance
		getFragmentManager().putFragment(outState, "matrixFragment",
				matrixFragment);
	}
}
