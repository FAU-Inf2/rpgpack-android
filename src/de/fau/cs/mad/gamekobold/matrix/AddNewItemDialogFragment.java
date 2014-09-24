package de.fau.cs.mad.gamekobold.matrix;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;

public class AddNewItemDialogFragment extends DialogFragment {
	private static final String KEY_SAVE_ITEM_NAME = "KEY_SAVE_ITEM_NAME";
	private static final String KEY_SAVE_RANGE_FROM = "KEY_SAVE_RANGE_FROM";
	private static final String KEY_SAVE_RANGE_TO = "KEY_SAVE_RANGE_TO";
	private static final String KEY_SAVE_DEFAULT_VALUE = "KEY_SAVE_DEFAULT_VALUE";
	private static final String KEY_SAVE_MODIFICATOR = "KEY_SAVE_MODIFICATOR";
	private static final String KEY_SAVE_DESCRIPTION = "KEY_SAVE_DESCRIPTION";

	public static final int FLAG_FROM = 1; // Binary 00001
	public static final int FLAG_TO = 2; // Binary 00010
	public static final int FLAG_VALUE = 4; // Binary 00100
	public static final int FLAG_MOD = 8; // Binary 01000

	private EditText itemName, rangeMin, rangeMax, defaultVal, modificator,
			description;
	public MatrixFragment matrixFragment;
	public MatrixItem editItem = null;

	public static AddNewItemDialogFragment newInstance(MatrixFragment receiver) {
		AddNewItemDialogFragment fragment = new AddNewItemDialogFragment();
		fragment.matrixFragment = receiver;
		return fragment;
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

	// // restore instance state on onActivityCreated
	// public void onActivityCreated(Bundle savedInstanceState) {
	// super.onActivityCreated(savedInstanceState);
	// Log.d("onActivityCreated savedInstanceState is NULL?", "" +
	// (savedInstanceState == null));
	// if (savedInstanceState != null) {
	// // Restore the fragment's state here
	// if (savedInstanceState.containsKey(KEY_SAVE_ITEM_NAME)) {
	// itemName.setText((savedInstanceState
	// .getString(KEY_SAVE_ITEM_NAME)));
	// }
	// }
	// }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

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
		description = (EditText) view.findViewById(R.id.description);

		final Switch switchFrom = (Switch) view
				.findViewById(R.id.switchRangeFrom);
		final Switch switchTo = (Switch) view.findViewById(R.id.switchRangeTo);
		final Switch switchValue = (Switch) view
				.findViewById(R.id.switchDefaultValue);
		final Switch switchMod = (Switch) view
				.findViewById(R.id.switchModificator);

		// check for editItem
		if (editItem != null) {
			// insert values from editItem into views
			itemName.setText(editItem.getItemName());
			rangeMin.setText(String.valueOf(editItem.getRangeMin()));
			rangeMax.setText(String.valueOf(editItem.getRangeMax()));
			defaultVal.setText(editItem.getValue());
			modificator.setText(editItem.getModificator());
			description.setText(editItem.getDescription());

			if ((editItem.getVisibility() & FLAG_FROM) == FLAG_FROM)
				switchFrom.setChecked(true);
			else
				switchFrom.setChecked(false);
			if ((editItem.getVisibility() & FLAG_TO) == FLAG_TO)
				switchTo.setChecked(true);
			else
				switchTo.setChecked(false);
			if ((editItem.getVisibility() & FLAG_VALUE) == FLAG_VALUE)
				switchValue.setChecked(true);
			else
				switchValue.setChecked(false);
			if ((editItem.getVisibility() & FLAG_MOD) == FLAG_MOD)
				switchMod.setChecked(true);
			else
				switchMod.setChecked(false);
		}

		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(KEY_SAVE_ITEM_NAME)) {
				itemName.setText((savedInstanceState
						.getString(KEY_SAVE_ITEM_NAME)));
			}
			if (savedInstanceState.containsKey(KEY_SAVE_RANGE_FROM)) {
				rangeMin.setText((savedInstanceState
						.getString(KEY_SAVE_RANGE_FROM)));
			}
			if (savedInstanceState.containsKey(KEY_SAVE_RANGE_TO)) {
				rangeMax.setText((savedInstanceState
						.getString(KEY_SAVE_RANGE_TO)));
			}
			if (savedInstanceState.containsKey(KEY_SAVE_DEFAULT_VALUE)) {
				defaultVal.setText((savedInstanceState
						.getString(KEY_SAVE_DEFAULT_VALUE)));
			}
			if (savedInstanceState.containsKey(KEY_SAVE_MODIFICATOR)) {
				modificator.setText((savedInstanceState
						.getString(KEY_SAVE_MODIFICATOR)));
			}
			if (savedInstanceState.containsKey(KEY_SAVE_DESCRIPTION)) {
				description.setText((savedInstanceState
						.getString(KEY_SAVE_DESCRIPTION)));
			}
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
						if (name.equals("")) {
							Toast.makeText(
									getActivity(),
									getResources()
											.getString(
													R.string.warning_set_matrixitem_name),
									Toast.LENGTH_SHORT).show();
							return;
						}
						final String defValue = defaultVal.getEditableText()
								.toString();
						int min = 0;
						if (!rangeMin.getEditableText().toString().isEmpty()) {
							min = Integer.parseInt(rangeMin.getEditableText()
									.toString());
						}
						int max = 0;
						if (!rangeMax.getEditableText().toString().isEmpty()) {
							max = Integer.parseInt(rangeMax.getEditableText()
									.toString());
						}

						String mod = modificator.getEditableText().toString();

						final String desc = description.getEditableText()
								.toString();

						int vis = 0;
						if (switchFrom.isChecked())
							vis = vis | FLAG_FROM;
						if (switchTo.isChecked())
							vis = vis | FLAG_TO;
						if (switchValue.isChecked())
							vis = vis | FLAG_VALUE;
						if (switchMod.isChecked())
							vis = vis | FLAG_MOD;

						if (editItem == null) {
							final MatrixItem newItem = new MatrixItem(name,
									defValue, min, max, mod, desc, vis);

							Log.d("newItem is NULL?", "" + (newItem == null));
							Log.d("matrixFragment is NULL?", ""
									+ (matrixFragment == null));

							matrixFragment.addMatrixItem(newItem);
							matrixFragment.adapterCreateTemplate
									.notifyDataSetChanged();
						} else {
							editItem.setItemName(name);
							editItem.setValue(defValue);
							editItem.setRangeMin(min);
							editItem.setRangeMax(max);
							editItem.setModificator(mod);
							editItem.setDescription(desc);
							editItem.setVisibility(vis);
							matrixFragment.adapterCreateTemplate
									.notifyDataSetChanged();
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
		// Save the fragment's state
		outState.putString(KEY_SAVE_ITEM_NAME, itemName.getText().toString());
		outState.putString(KEY_SAVE_RANGE_FROM, rangeMin.getText().toString());
		outState.putString(KEY_SAVE_RANGE_TO, rangeMax.getText().toString());
		outState.putString(KEY_SAVE_DEFAULT_VALUE, defaultVal.getText()
				.toString());
		outState.putString(KEY_SAVE_MODIFICATOR, modificator.getText()
				.toString());
		outState.putString(KEY_SAVE_DESCRIPTION, description.getText()
				.toString());

		// Save the fragment's instance
		getFragmentManager().putFragment(outState, "matrixFragment",
				matrixFragment);

	}
	// // There's a bug in the compatibility library that can cause dismissing
	// // after the rotation. Note that there are reports that
	// // getDialog().setOnDismissListener(null); causes a crash on some
	// devices.
	// // The workaround is to call getDialog().setDismissMessage(null);
	// instead.
	// // Issue 17423: DialogFragment dismissed on orientation change when
	// // setRetainInstance(true) is set (compatibility library)
	// @Override
	// public void onDestroyView() {
	// if (getDialog() != null && getRetainInstance())
	// getDialog().setDismissMessage(null);
	// super.onDestroyView();
	// }
}
