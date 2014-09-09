package de.fau.cs.mad.gamekobold.matrix;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.character.CharacterEditActivity;
import de.fau.cs.mad.gamekobold.game.CharacterPlayActivity;
import de.fau.cs.mad.gamekobold.jackson.MatrixTable;
import de.fau.cs.mad.gamekobold.template_generator.GeneralFragment;
import de.fau.cs.mad.gamekobold.template_generator.TemplateGeneratorActivity;

public class MatrixFragment extends GeneralFragment {
	public static final int FLAG_FROM = 1; // Binary 00001
	public static final int FLAG_TO = 2; // Binary 00010
	public static final int FLAG_VALUE = 4; // Binary 00100
	public static final int FLAG_MOD = 8; // Binary 01000
	GridView gridView;
	public List<MatrixItem> itemsList = null;
	public List<MatrixItem> playMatrixItems = null;

	// dte check adapter type!
	MatrixViewArrayAdapter adapterCreateTemplate;
	NewCharacterMatrixViewArrayAdapter adapterCreateCharacter;
	PlayCharacterMatrixAdapter adapterPlay;

	View rootView;

	/*
	 * JACKSON START
	 */
	public MatrixTable jacksonTable;
	public boolean jacksonInflateWithData;

	/*
	 * JACKSON END
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		LayoutInflater inflater = (LayoutInflater) SlideoutNavigationActivity.theActiveActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (SlideoutNavigationActivity.theActiveActivity instanceof TemplateGeneratorActivity) {
			Log.d("MatrixFragment", "inflated for TemplateGenerator");
			rootView = inflater.inflate(R.layout.fragment_matrix_view,
					new LinearLayout(getActivity()), false);
			Toast.makeText(getActivity(), "TEMPLATE GENERATOR",
					Toast.LENGTH_LONG).show();
		}

		else if (SlideoutNavigationActivity.theActiveActivity instanceof CharacterEditActivity) {
			Log.d("MatrixFragment", "inflated for CharacterEditActivity");
			rootView = (FrameLayout) inflater.inflate(
					R.layout.character_edit_matrix_view, new LinearLayout(
							getActivity()), false);
			Toast.makeText(getActivity(), "CHARACTER GENERATOR",
					Toast.LENGTH_LONG).show();

		} else if (SlideoutNavigationActivity.theActiveActivity instanceof CharacterPlayActivity) {
			Log.d("MatrixFragment", "inflated for CharacterPLAYActivity");
			rootView = (FrameLayout) inflater.inflate(
					R.layout.fragment_matrix_view, new LinearLayout(
							getActivity()), false);
			Toast.makeText(getActivity(), "PLAY CHARACTER!!!!!!!!!!!",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		// template creation mode
		if (SlideoutNavigationActivity.theActiveActivity instanceof TemplateGeneratorActivity) {
			Log.d("TableFragment", "inflated for TemplateGenerator");

			Log.d("MatrixFragment", "ON_CREATE_VIEW_MATRIX");
			// rootView = inflater.inflate(R.layout.fragment_matrix_view,
			// container, false);

			gridView = (GridView) rootView
					.findViewById(R.id.gridViewMatrixItem);
			// check needed for jackson data loading
			if (itemsList == null) {
				itemsList = new ArrayList<MatrixItem>();
				jacksonTable.entries = itemsList;

				// set create new item to the end, it will not appear in
				// jacksonTable.entries
				MatrixItem addNewMatrixItem = new MatrixItem(getResources()
						.getString(R.string.new_matrix_item), "+", null);
				itemsList.add(addNewMatrixItem);

			}

			// TODO check it !!!! now i have to remove last item because
			// getEntries()
			// returns n elements! (not (n-1) as it was before)

			// Log.e("REMOVED", " !!! "
			// + itemsList.get(itemsList.size() - 1).getItemName());
			// itemsList.remove(itemsList.size() - 1);

			if (adapterCreateTemplate == null) {
				adapterCreateTemplate = new MatrixViewArrayAdapter(
						getActivity(), itemsList);
				// adapter.jacksonTable = jacksonTable;
			}
			Log.d("gridView is null?", "" + (gridView == null));
			Log.d("adapter is null?", "" + (adapterCreateTemplate == null));
			gridView.setAdapter(adapterCreateTemplate);

			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view,
						int position, long id) {

					Toast.makeText(
							getActivity(),
							((TextView) view.findViewById(R.id.textItemTitle))
									.getText(), Toast.LENGTH_SHORT).show();

					// is it last item?
					if (position == adapterCreateTemplate.getCount() - 1) {
						showPopup();

					} else {
						// click on item
						showPopupForEditing(adapterCreateTemplate
								.getItem(position));
					}

				}
			});

			gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> adapterView,
						View view, final int position, long id) {
					Log.d("LONG CLICK", "pos:" + position);
					if (position == adapterCreateTemplate.getCount() - 1) {
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
					builder.setPositiveButton(
							getResources().getString(R.string.yes),
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

			// Character generator mode
		} else if (SlideoutNavigationActivity.theActiveActivity instanceof CharacterEditActivity) {
			Toast.makeText(getActivity(), "CHARACTER GENERATOR!",
					Toast.LENGTH_SHORT).show();

			gridView = (GridView) rootView.findViewById(R.id.gridView);
			// check needed for jackson data loading
			if (itemsList == null) {
				itemsList = new ArrayList<MatrixItem>();
				jacksonTable.entries = itemsList;

			}

			if (adapterCreateCharacter == null) {
				adapterCreateCharacter = new NewCharacterMatrixViewArrayAdapter(
						getActivity(), itemsList);
				// adapter.jacksonTable = jacksonTable;
			}

			// TODO ate don't work on selectedMatrixItems list directly
			final ArrayList<MatrixItem> selectedItems = ((NewCharacterMatrixViewArrayAdapter) adapterCreateCharacter).selectedMatrixItems;

			for (final MatrixItem item : itemsList) {
				if (item.isSelected()) {
					selectedItems.add(item);
				}
			}

			gridView.setAdapter(adapterCreateCharacter);

			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view,
						int position, long id) {

					if (position == itemsList.size() - 1) {

						Toast.makeText(
								getActivity(),
								"Neues Element wird in Deinem Character erstellt!",
								Toast.LENGTH_SHORT).show();
						// TODO Benni save new matrix item
						showPopup();
					} else {

						MatrixItem curMatrixItem = itemsList.get(position);

						if (selectedItems.contains(curMatrixItem)) {
							curMatrixItem.setSelected(false);
							curMatrixItem.setFavorite(false);

							selectedItems.remove(curMatrixItem);

							// newCharacter.removeMatrixItem(curMatrixItem);
							Log.d("setOnItemClickListener", "pos:" + position);
							Log.d("remove", "remove");
							adapterCreateCharacter.notifyDataSetChanged();

						} else {
							curMatrixItem.setFavorite(false);
							curMatrixItem.setSelected(true);

							// show popup to set current value
							// TODO new popup

							showSetValuePopup(curMatrixItem,
									adapterCreateCharacter, selectedItems);
//							selectedItems.add(curMatrixItem);
//
//							Toast.makeText(
//									getActivity(),
//									((TextView) view
//											.findViewById(R.id.matrix_textItemTitle))
//											.getText()
//											+ "-Attribut wird zu dem Charakter hinzugefuegt",
//									Toast.LENGTH_SHORT).show();
//
//							Log.d("add", "add");
//							adapterCreateCharacter.notifyDataSetChanged();

							// newCharacter.addMatrixItem(curMatrixItem);
						}
					}
				}
			});

			gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> adapterView,
						View view, final int position, long id) {
					Log.d("LONG CLICK", "pos:" + position);
					if (position == adapterCreateTemplate.getCount() - 1) {
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
					builder.setPositiveButton(
							getResources().getString(R.string.yes),
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

			// Character Playing mode
		} else if (SlideoutNavigationActivity.theActiveActivity instanceof CharacterPlayActivity) {
			Toast.makeText(getActivity(), "CHARACTER PLAY!", Toast.LENGTH_SHORT)
					.show();

			gridView = (GridView) rootView
					.findViewById(R.id.gridViewMatrixItem);
			// check needed for jackson data loading
			if (itemsList == null) {
				itemsList = new ArrayList<MatrixItem>();
				jacksonTable.entries = itemsList;
			}

			playMatrixItems = new ArrayList<MatrixItem>();

			for (MatrixItem ma : itemsList) {
				if (ma.isSelected())
					playMatrixItems.add(ma);
			}

			if (adapterPlay == null) {
				adapterPlay = new PlayCharacterMatrixAdapter(getActivity(),
						playMatrixItems);
				// adapter.jacksonTable = jacksonTable;
			}

			gridView.setAdapter(adapterPlay);

			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view,
						int position, long id) {
					// TODO Benni save new values
					showPopupForEditing(adapterPlay.getItem(position));
				}
			});

			gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> adapterView,
						View view, final int position, long id) {
					Log.d("LONG CLICK", "pos:" + position);

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
					builder.setPositiveButton(
							getResources().getString(R.string.yes),
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
		}
		return rootView;
	}

	// just one simple check
	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		Log.d("onSaveInstanceState", "onSaveInstanceState!!!!!!!!!");
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
		adapterCreateTemplate.insert(newItem,
				adapterCreateTemplate.getCount() - 1);
		adapterCreateTemplate.notifyDataSetChanged();
	}

	public void removeMatrixItem(int position) {
		if (position < 0 || position == adapterCreateTemplate.getCount() - 1) {
			return;
		}
		adapterCreateTemplate.remove(adapterCreateTemplate.getItem(position));
		adapterCreateTemplate.notifyDataSetChanged();
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

	private void showSetValuePopup(MatrixItem item,
			ArrayAdapter<MatrixItem> adapter, ArrayList<MatrixItem> selectedItems) {
		SettingValueDialogFragment settingValueDialogFragment = SettingValueDialogFragment
				.newInstance();
		settingValueDialogFragment.show(getFragmentManager(), "dialog");
		settingValueDialogFragment.matrixItem = item;
		settingValueDialogFragment.passAdapter(adapter);
		settingValueDialogFragment.passSelItems(selectedItems);
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

		// FIXME getEntries() returns n-1 elements!!! we need all n!!!!
		int i = 0;
		for (MatrixItem ma : itemsList) {
			i++;
			Log.e("MATRIX ITEM", i + " j " + ma.getItemName());
		}

		// add the "new item" entry
		final MatrixItem newElement = new MatrixItem(appContext.getResources()
				.getString(R.string.text_new_element), "+", null);
		newElement.setSelected(false);
		itemsList.add(newElement);
	}

	public void setJacksonTable(MatrixTable myTable) {
		jacksonTable = myTable;
	}
	/*
	 * JACKSON END
	 */
}
