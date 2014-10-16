package de.fau.cs.mad.gamekobold.matrix;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
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
	// this flags is used to store visibility of UI elements
	public static final int FLAG_FROM = 1; // Binary 00001
	public static final int FLAG_TO = 2; // Binary 00010
	public static final int FLAG_VALUE = 4; // Binary 00100
	public static final int FLAG_MOD = 8; // Binary 01000
	private GridView gridView;
	public List<MatrixItem> itemsList = null;
	private List<MatrixItem> playMatrixItems = null;
	private MatrixItem addNewMatrixItem;

	private MatrixViewArrayAdapter adapterCreateTemplate;
	private NewCharacterMatrixViewArrayAdapter adapterCreateCharacter;
	private PlayCharacterMatrixAdapter adapterPlay;
	private PlayCharacterEditModeMatrixAdapter adapterPlayEditMode;

	private View rootView;

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
	}

	protected void createViewForTemplateCreationMode(LayoutInflater inflater) {
		Log.d("MartixFragment", "inflated for TemplateGenerator");
		rootView = inflater.inflate(R.layout.fragment_matrix_view,
				new LinearLayout(getActivity()), false);

		gridView = (GridView) rootView.findViewById(R.id.gridViewMatrixItem);
		// check needed for jackson data loading
		if (itemsList == null) {
			itemsList = new ArrayList<MatrixItem>();
			jacksonTable.entries = itemsList;

			// set create new item to the end, it will not appear in
			// jacksonTable.entries
			// FIXME comment is not correct, last fake item appears also in
			// jacksonTable.entries and make problems!!!!
			// now it is fixed but dirty!
			MatrixItem addNewMatrixItem = new MatrixItem(getResources()
					.getString(R.string.new_matrix_item), "+", "");
			itemsList.add(addNewMatrixItem);
		}

		if (adapterCreateTemplate == null) {
			adapterCreateTemplate = new MatrixViewArrayAdapter(getActivity(),
					itemsList);
			// adapter.jacksonTable = jacksonTable;
		}

		gridView.setAdapter(adapterCreateTemplate);
		addListenersForTemplateCreateMode();
	}

	protected void createViewForCharacterGeneratorMode(LayoutInflater inflater) {

		// edit mode, popup allows to correct or change matrix items fields
		// just like in create template mode
		if (SlideoutNavigationActivity.theActiveActivity.inEditMode()) {
			Log.d("Martix Fragment", "in edit mode");

			rootView = inflater.inflate(R.layout.fragment_matrix_view,
					new LinearLayout(getActivity()), false);

			TextView textView = (TextView) rootView
					.findViewById(R.id.textView1);

			textView.setText(getResources().getString(R.string.hint_edit_items));
			FrameLayout frameLayout = (FrameLayout) rootView
					.findViewById(R.id.container);
			frameLayout.setBackgroundColor(getResources().getColor(
					R.color.background));

			gridView = (GridView) rootView
					.findViewById(R.id.gridViewMatrixItem);

			// check it the last item is not a fake one, add it
			if (!(itemsList.get(itemsList.size() - 1).getValue().equals("+"))) {
				addNewMatrixItem = new MatrixItem(getResources().getString(
						R.string.new_matrix_item), "+", "");
				itemsList.add(addNewMatrixItem);
			}

			// check needed for jackson data loading
			if (itemsList == null) {
				itemsList = new ArrayList<MatrixItem>();
				jacksonTable.entries = itemsList;

				// set create new item to the end, it will not appear in
				// jacksonTable.entries
				// FIXME comment is not correct, last fake item appears also
				// in
				// jacksonTable.entries and make problems!!!!
				addNewMatrixItem = new MatrixItem(getResources().getString(
						R.string.new_matrix_item), "+", "");
				itemsList.add(addNewMatrixItem);
			}

			if (adapterCreateTemplate == null) {
				adapterCreateTemplate = new MatrixViewArrayAdapter(
						getActivity(), itemsList);
				// adapter.jacksonTable = jacksonTable;
			}
			gridView.setAdapter(adapterCreateTemplate);
			addListenersForCharacterCreateEditMode();
		}
		// not in edit mode, popup allows to set just values
		else {
			Log.d("Martix Fragment", "not editable");
			rootView = (FrameLayout) inflater.inflate(
					R.layout.character_edit_matrix_view, new LinearLayout(
							getActivity()), false);
			gridView = (GridView) rootView.findViewById(R.id.gridView);

			// check needed for jackson data loading
			if (itemsList == null) {
				itemsList = new ArrayList<MatrixItem>();
				jacksonTable.entries = itemsList;
			}

			// check it the last item is a fake one, remove it
			if (itemsList.get(itemsList.size() - 1).getValue().equals("+")) {
				itemsList.remove(itemsList.get(itemsList.size() - 1));
			}

			if (adapterCreateCharacter == null) {
				adapterCreateCharacter = new NewCharacterMatrixViewArrayAdapter(
						getActivity(), itemsList);
				// adapter.jacksonTable = jacksonTable;
			}

			final ArrayList<MatrixItem> selectedItems = ((NewCharacterMatrixViewArrayAdapter) adapterCreateCharacter).selectedMatrixItems;

			for (final MatrixItem item : itemsList) {
				if (item.isSelected()) {
					selectedItems.add(item);
				}
			}

			gridView.setAdapter(adapterCreateCharacter);
			addListenersForCharacterCreateNormalMode(selectedItems);
		}
	}

	protected void createViewForPlayMode(LayoutInflater inflater) {
		// edit mode
		if (SlideoutNavigationActivity.theActiveActivity.inEditMode()) {
			Log.d("Martix Fragment", "in edit mode");
			// inflate to edit!
			rootView = (FrameLayout) inflater.inflate(
					R.layout.character_edit_matrix_view, new LinearLayout(
							getActivity()), false);

			gridView = (GridView) rootView.findViewById(R.id.gridView);

			// check needed for jackson data loading
			if (itemsList == null) {
				itemsList = new ArrayList<MatrixItem>();
				jacksonTable.entries = itemsList;
			}

			if (adapterPlayEditMode == null) {
				adapterPlayEditMode = new PlayCharacterEditModeMatrixAdapter(
						getActivity(), itemsList);
				// adapter.jacksonTable = jacksonTable;
			}

			final ArrayList<MatrixItem> selectedItems = ((PlayCharacterEditModeMatrixAdapter) adapterPlayEditMode).selectedMatrixItems;

			for (final MatrixItem item : itemsList) {
				if (item.isSelected()) {
					selectedItems.add(item);
				}
			}

			gridView.setAdapter(adapterPlayEditMode);
			addListenersForCharacterPlayEditMode(selectedItems);

		} else {// ==!editable
			Log.d("Martix Fragment", "not editable");
			// inflate to play
			rootView = (FrameLayout) inflater.inflate(
					R.layout.character_play_matrix_view, new LinearLayout(
							getActivity()), false);

			gridView = (GridView) rootView.findViewById(R.id.gridViewM);
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
			}

			gridView.setAdapter(adapterPlay);

			addListenersForCharacterPlayNormalMode(playMatrixItems);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		// template creation mode
		if (SlideoutNavigationActivity.theActiveActivity instanceof TemplateGeneratorActivity) {
			this.createViewForTemplateCreationMode(inflater);

		}
		// character generator mode
		else if (SlideoutNavigationActivity.theActiveActivity instanceof CharacterEditActivity) {
			this.createViewForCharacterGeneratorMode(inflater);
		}
		// Character Playing mode
		else if (SlideoutNavigationActivity.theActiveActivity instanceof CharacterPlayActivity) {
			this.createViewForPlayMode(inflater);
		}
		return rootView;
	}

	protected void addListenersForTemplateCreateMode() {
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// is it last item?
				if (position == adapterCreateTemplate.getCount() - 1) {
					// create new item
					showPopup(adapterCreateTemplate);
				} else {
					// edit item
					showPopupForEditing(
							adapterCreateTemplate.getItem(position),
							adapterCreateTemplate);
				}
			}
		});

		gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView,
					View view, final int position, long id) {
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
				builder.setPositiveButton(getResources()
						.getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								removeMatrixItem(position,
										adapterCreateTemplate);
							}
						});
				builder.create().show();
				return true;
			}
		});
	}

	protected void addListenersForCharacterCreateEditMode() {
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// is it last item?
				if (position == adapterCreateTemplate.getCount() - 1) {
					// create new item
					showPopup(adapterCreateTemplate);

				} else {
					// edit item
					showPopupForEditing(
							adapterCreateTemplate.getItem(position),
							adapterCreateTemplate);
				}
			}
		});

		gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView,
					View view, final int position, long id) {
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
				builder.setPositiveButton(getResources()
						.getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								removeMatrixItem(position,
										adapterCreateTemplate);
							}
						});
				builder.create().show();
				return true;
			}
		});
	}

	protected void addListenersForCharacterCreateNormalMode(
			final List<MatrixItem> selectedItems) {
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				MatrixItem curMatrixItem = itemsList.get(position);

				// if it is yet selected then remove
				if (selectedItems.contains(curMatrixItem)) {
					curMatrixItem.setSelected(false);

					selectedItems.clear();
					for (MatrixItem ma : itemsList) {
						if (ma.isSelected())
							selectedItems.add(ma);
					}
					adapterCreateCharacter.notifyDataSetChanged();
				}
				// if is not yet selected then set as selected to show later
				else {
					curMatrixItem.setSelected(true);

					selectedItems.clear();
					for (MatrixItem ma : itemsList) {
						if (ma.isSelected())
							selectedItems.add(ma);
					}
					adapterCreateCharacter.notifyDataSetChanged();
					// show popup to set current value
					showSetValuePopup(curMatrixItem, adapterCreateCharacter,
							null, selectedItems);
				}
			}
		});

		gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView,
					View view, final int position, long id) {
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
				builder.setPositiveButton(getResources()
						.getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								removeMatrixItem(position,
										adapterCreateCharacter);
							}
						});
				builder.create().show();
				return true;
			}
		});
	}

	protected void addListenersForCharacterPlayEditMode(
			final List<MatrixItem> selectedItems) {
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				if (position == itemsList.size() - 1) {
					Toast.makeText(getActivity(),
							"Neues Element wird in Deinem Character erstellt!",
							Toast.LENGTH_SHORT).show();
					// add new matrix item
					showPopup(adapterPlayEditMode);
				} else {
					MatrixItem curMatrixItem = itemsList.get(position);
					if (selectedItems.contains(curMatrixItem)) {
						// because we do not want to show this item
						curMatrixItem.setSelected(false);

						selectedItems.clear();
						for (MatrixItem ma : itemsList) {
							if (ma.isSelected())
								selectedItems.add(ma);
						}
						adapterPlayEditMode.notifyDataSetChanged();
						adapterPlay.clear();
						adapterPlay.addAll(selectedItems);
						adapterPlay.notifyDataSetChanged();

					} else {
						// show popup to set current value
						showSetValuePopup(curMatrixItem, adapterPlayEditMode,
								adapterPlay, selectedItems);

						// because it should show up in play mode
						curMatrixItem.setSelected(true);

						selectedItems.clear();
						for (MatrixItem ma : itemsList) {
							if (ma.isSelected())
								selectedItems.add(ma);
						}
						adapterPlayEditMode.notifyDataSetChanged();
						adapterPlay.clear();
						adapterPlay.addAll(selectedItems);
						adapterPlay.notifyDataSetChanged();
					}
				}
			}
		});
	}

	protected void addListenersForCharacterPlayNormalMode(
			final List<MatrixItem> selItems) {
		// to set new value for a matrix item in play mode directly
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				showSetValuePopup(adapterPlay.getItem(position), adapterPlay,
						adapterPlayEditMode, selItems);
			}
		});
	}

	@Override
	protected void addItemList() {
		// TODO Auto-generated method stub
	}

	@Override
	public void showDialog() {
		// TODO Auto-generated method stub
	}

	/**
	 * This Method is used every time we need to add new element into adapters
	 * element list.
	 * 
	 */
	public void addMatrixItem(MatrixItem newItem, ArrayAdapter adapter) {
		adapter.insert(newItem, adapter.getCount() - 1);
		adapter.notifyDataSetChanged();

	}

	/**
	 * This Method is used every time we want remove some element from adapters
	 * element list.
	 * 
	 */
	public void removeMatrixItem(int position, ArrayAdapter adapter) {
		if (position < 0 || position == adapter.getCount() - 1) {
			return;
		}
		adapter.remove(adapter.getItem(position));
		adapter.notifyDataSetChanged();
	}

	private void showPopup(ArrayAdapter adapter) {
		AddNewItemDialogFragment popupAddNewItemFragment = AddNewItemDialogFragment
				.newInstance(this, adapter);

		popupAddNewItemFragment.show(getFragmentManager(),
				"popupAddNewItemFragment");

	}

	private void showPopupForEditing(MatrixItem item, ArrayAdapter adapter) {
		AddNewItemDialogFragment popupAddNewItemFragment = AddNewItemDialogFragment
				.newInstance(this, adapter);

		popupAddNewItemFragment.editItem = item;
		popupAddNewItemFragment.show(getFragmentManager(),
				"popupAddNewItemFragment");
	}

	private void showSetValuePopup(MatrixItem item,
			ArrayAdapter<MatrixItem> adapterEdit,
			ArrayAdapter<MatrixItem> adapterNormal,
			List<MatrixItem> selectedItems) {
		SettingValueDialogFragment settingValueDialogFragment = SettingValueDialogFragment
				.newInstance(this);
		settingValueDialogFragment.show(getFragmentManager(), "dialog");
		settingValueDialogFragment.matrixItem = item;
		settingValueDialogFragment.passAdapterEdit(adapterEdit);
		settingValueDialogFragment.passAdapterNormal(adapterNormal);
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

		// check it the last item is a fake one, remove it
		// also before check if any item exists so we dont get ArrayOutOfBoundsException
		if(itemsList.size() > 0){
			if (itemsList.get(itemsList.size() - 1).getValue().equals("+")) {
				itemsList.remove(itemsList.get(itemsList.size() - 1));
			}
		}
		// add the "new item" entry
		final MatrixItem newElement = new MatrixItem(appContext.getResources()
				.getString(R.string.text_new_element), "+", "");
		newElement.setSelected(false);
		itemsList.add(newElement);
		//
		// for (MatrixItem ma : jacksonTable.entries) {
		// Log.d("jacksonTable.entries - jacksonInflate", ma.getItemName());
		// }

	}

	public void setJacksonTable(MatrixTable myTable) {
		jacksonTable = myTable;
	}
	/*
	 * JACKSON END
	 */
}
