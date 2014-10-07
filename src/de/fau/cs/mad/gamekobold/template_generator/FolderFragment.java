package de.fau.cs.mad.gamekobold.template_generator;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.drawable.Drawable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.jackson.AbstractTable;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.MatrixTable;
import de.fau.cs.mad.gamekobold.jackson.Table;
import de.fau.cs.mad.gamekobold.matrix.MatrixFragment;
import de.fau.cs.mad.gamekobold.matrix.MatrixItem;
import de.fau.cs.mad.gamekobold.template_generator.FolderElementData.element_type;

public class FolderFragment extends GeneralFragment {
	/*
	 * JACKSON START
	 */
	public ContainerTable jacksonTable;
	/*
	 * JACKSON END
	 */

	View view;
	public ListView lView;
	public ArrayList<FolderElementData> allData;

	Button buttonAdd;
	View mainView;
	public FolderElementAdapter dataAdapter;
	// AlertDialog dialogCreateElement;
	// View dialogViewCreateElement;
	// public boolean editable = true;
	public static final int CREATE_DIALOG = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		// AlertDialog.Builder alertDialogBuilder = new
		// AlertDialog.Builder(SlideoutNavigationActivity.theActiveActivity);
		// LayoutInflater inflater =
		// SlideoutNavigationActivity.theActiveActivity.getLayoutInflater();
		// dialogViewCreateElement =
		// inflater.inflate(R.layout.alertdialog_template_generator_add_new_element,
		// null);
		// final EditText nameInput = (EditText)
		// dialogViewCreateElement.findViewById(R.id.enter_name_of_element);
		// nameInput.setSingleLine();
		// alertDialogBuilder.setView(dialogViewCreateElement);
		// alertDialogBuilder.setCancelable(true);
		// ImageButton createTable = (ImageButton)
		// dialogViewCreateElement.findViewById(R.id.create_table);
		// ImageButton createCollection= (ImageButton)
		// dialogViewCreateElement.findViewById(R.id.create_collection);
		// ImageButton createFolder = (ImageButton)
		// dialogViewCreateElement.findViewById(R.id.create_folder);
		// createTable.setOnClickListener(new View.OnClickListener() {
		// // private final EditText elementName =
		// (EditText)view.findViewById(R.id.enter_name_of_element);
		// @Override
		// public void onClick(View v) {
		// // check if a name has been set
		// if(nameInput.getText().toString().isEmpty()) {
		// Toast.makeText(getActivity(),
		// getResources().getString(R.string.alert_set_element_name),
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		// addItemList(editable, element_type.table,
		// nameInput.getText().toString());
		// dialogCreateElement.cancel();
		// nameInput.setText("");
		// }
		// });
		// createCollection.setOnClickListener(new View.OnClickListener() {
		// //private final EditText elementName =
		// (EditText)view.findViewById(R.id.enter_name_of_element);
		// @Override
		// public void onClick(View v) {
		// // check if a name has been set
		// if(nameInput.getText().toString().isEmpty()) {
		// Toast.makeText(getActivity(),
		// getResources().getString(R.string.alert_set_element_name),
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		// addItemList(editable, element_type.matrix,
		// nameInput.getText().toString());
		// dialogCreateElement.cancel();
		// nameInput.setText("");
		// }
		// });
		// createFolder.setOnClickListener(new View.OnClickListener() {
		// //private final EditText elementName =
		// (EditText)view.findViewById(R.id.enter_name_of_element);
		// @Override
		// public void onClick(View v) {
		// // check if a name has been set
		// if(nameInput.getText().toString().isEmpty()) {
		// Toast.makeText(getActivity(),
		// getResources().getString(R.string.alert_set_element_name),
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		// addItemList(editable, element_type.folder,
		// nameInput.getText().toString());
		// dialogCreateElement.cancel();
		// nameInput.setText("");
		// }
		// });
		// dialogCreateElement = alertDialogBuilder.create();
	}

	// public void setAllCheckboxesVisibility(boolean visible){
	// for(FolderElementData datum : allData){
	// datum.checkBoxVisible = visible;
	// }
	// }

	/**
	 * sets visibilty of all folder-element checkboxes below the one it is
	 * called on
	 * 
	 * @param visible
	 */
	public void setCheckboxVisibilityBelow(boolean visible) {
		// check if allData exists (== items in folder)
		if (allData != null) {
			for (FolderElementData datum : allData) {
				if (datum.childFragment instanceof FolderFragment) {
					((FolderFragment) datum.childFragment)
							.setCheckboxVisibility(visible);
					((FolderFragment) datum.childFragment)
							.setCheckboxVisibilityBelow(visible);
				}
			}
		}
	}

	/**
	 * 
	 * @return all matrix elements contained in this folder
	 */
	public ArrayList<MatrixItem> getAllMatrixReferences() {
		ArrayList<MatrixItem> results = new ArrayList<MatrixItem>();
		// Log.d("popupReferences", "subdirs: " + dataAdapter.getAll().length);
		if (dataAdapter != null) {
			for (FolderElementData currentDatum : dataAdapter.getAll()) {
				GeneralFragment currentFragment = currentDatum.childFragment;
				if (currentFragment instanceof FolderFragment) {
					// Log.d("popupReferences",
					// "folderfragment found, descending now");
					ArrayList<MatrixItem> toAdd = ((FolderFragment) currentFragment)
							.getAllMatrixReferences();
					results.addAll(toAdd);
				} else if (currentFragment instanceof TableFragment) {
					// Log.d("popupReferences",
					// "tableview found; atm ignoring");
				} else if (currentFragment instanceof MatrixFragment) {
					// Log.d("popupReferences", "matrix found. Elements:" +
					// (((MatrixFragment) currentFragment).itemsList).size());
					for (MatrixItem oneItem : ((MatrixFragment) currentFragment).itemsList) {
						results.add(oneItem);
					}
				} else {
					Log.d("popupReferences", "unhandled element found!!!");
				}
			}
		}
		return results;
	}

	public int getElementCount() {
		return allData.size();
	}

	/**
	 * sets the visibilty of checkboxes for this
	 * 
	 * @param visible
	 */
	public void setCheckboxVisibility(boolean visible) {
		// dataAdapter might not be set -> no subitems
		if (dataAdapter != null) {
			// but if: set checkboxes of items visible
			dataAdapter.setCheckboxVisibility(visible);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.d("FolderFragment", "onCreateView; editMode == "
				+ SlideoutNavigationActivity.theActiveActivity.inEditMode());
		if (allData == null) {
			allData = new ArrayList<FolderElementData>();
		}
		// TODO: check if a problem when nullcheck is commented out; it is
		// because of needed by reinflating
		// in edit/selection-mode
		// nullcheck needed for jackson inflation. creates dataAdapter before
		// onCreate is called
		if (dataAdapter == null) {
			dataAdapter = new FolderElementAdapter(
					(SlideoutNavigationActivity) getActivity(),
					SlideoutNavigationActivity.theActiveActivity.inEditMode(),
					R.layout.template_listview_row, allData);
			/*
			 * JACKSON START
			 */
			dataAdapter.jacksonTable = jacksonTable;
			/*
			 * JACKSON END
			 */
		}
		if (SlideoutNavigationActivity.getAc().inEditMode()) {
			dataAdapter.setEditable(true);
		} else {
			dataAdapter.setEditable(false);
		}
		// dataAdapter.setCheckboxVisibility(SlideoutNavigationActivity.getAc().onlySelected)

		// nullcheck needed for jackson inflation. creates allData before
		// onCreate is called
		if (SlideoutNavigationActivity.theActiveActivity.inEditMode()) {
			view = (LinearLayout) inflater.inflate(
					R.layout.activity_template_generator_add_button, null);
			TextView addRowBelow = (TextView) view.findViewById(R.id.add_below);
			final FolderFragment ff = this;
			addRowBelow.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					DialogFragment df = CreateElementDialog
							.newInstance(SlideoutNavigationActivity.theActiveActivity
									.inEditMode());
					df.setTargetFragment(ff, CREATE_DIALOG);
					FragmentManager fm = getFragmentManager();
					df.show(fm, "");
					// dialogCreateElement.show();
				}
			});
			setAddButtonStyle(addRowBelow);
		} else {
			view = (LinearLayout) inflater.inflate(
					R.layout.activity_template_generator, null);
		}
		mainView = view;
		lView = (ListView) view.findViewById(R.id.listView_items);
		lView.setAdapter(dataAdapter);
		return view;
	}

	@Override
	public void showDialog() {
		DialogFragment df = CreateElementDialog
				.newInstance(SlideoutNavigationActivity.theActiveActivity
						.inEditMode());
		df.setTargetFragment(this, CREATE_DIALOG);
		FragmentManager fm = getFragmentManager();
		df.show(fm, "");
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	protected void setAddButtonStyle(TextView text) {
		String uri = "@drawable/cell_shape_add_folder";
		int imageResource = getResources().getIdentifier(uri, null,
				getActivity().getPackageName());
		Drawable res = getResources().getDrawable(imageResource);
		if (android.os.Build.VERSION.SDK_INT >= 16) {
			text.setBackground(res);
		} else {
			text.setBackgroundDrawable(res);
		}
		text.setTextColor(getResources().getColor(R.color.own_grey));
		text.setSingleLine();
	}

	public void addItemList() {
		addItemList(true, element_type.folder, "");
	}

	public void addItemList(boolean editable) {
		addItemList(editable, element_type.folder, "");
	}

	public void addItemList(boolean editable, element_type selected, String name) {
		FolderElementData newDataItem = new FolderElementData(
				(SlideoutNavigationActivity) getActivity(), editable, selected);
		newDataItem.text.setText(name);
		Toast.makeText(getActivity(), "selected: " + selected,
				Toast.LENGTH_LONG).show();
		/*
		 * JACKSON START
		 */
		// not needed because we save right here
		// we do it here because if the new created item is not visible it would
		// not
		// be saved
		// newDataItem.jacksonDoSaveOnNextChance = true;
		if (newDataItem.getType() == element_type.folder) {
			newDataItem.jacksonTable = dataAdapter.jacksonTable
					.createAndAddNewContainerTable();
		} else if (newDataItem.getType() == element_type.table) {
			newDataItem.jacksonTable = dataAdapter.jacksonTable
					.createAndAddNewTable();
		} else if (newDataItem.getType() == element_type.matrix) {
			newDataItem.jacksonTable = dataAdapter.jacksonTable
					.createAndAddNewMatrixTable();
		}
		newDataItem.jacksonTable.tableName = name;
		// TemplateGeneratorActivity.saveTemplateAsync();
		/*
		 * JACKSON END
		 */

		allData.add(newDataItem);
		dataAdapter.notifyDataSetChanged();
		final ListView myListView = (ListView) view
				.findViewById(R.id.listView_items);
		myListView.post(new Runnable() {
			@Override
			public void run() {
				// Select the last row so it will scroll into view...
				myListView.setSelection(dataAdapter.getCount() - 1);
			}
		});
		Log.d("addItemList", "ADD ITEM. now amount == " + allData.size());
	}

	/*
	 * JACKSON START
	 */
	public void setJacksonTable(ContainerTable table) {
		if (dataAdapter != null) {
			dataAdapter.jacksonTable = table;
		}
		jacksonTable = table;
	}

	public void inflateWithJacksonData(ContainerTable myTable, Activity activity) {
		Log.d("FolderFragment", "inflating jackson data; editable: "
				+ SlideoutNavigationActivity.theActiveActivity.inEditMode());
		// new Throwable().printStackTrace();
		// set jackson table for this fragment
		jacksonTable = myTable;
		// check if there are any sub tables in this folder
		if (myTable.subTables == null) {
			return;
		}
		// initialize fields for this FolderFragment, so we can add data
		if (allData == null) {
			allData = new ArrayList<FolderElementData>();
		}
		if (dataAdapter == null) {
			dataAdapter = new FolderElementAdapter(activity,
					SlideoutNavigationActivity.theActiveActivity.inEditMode(),
					R.layout.template_listview_row, allData);
			dataAdapter.jacksonTable = jacksonTable;
		}
		// add data
		for (final AbstractTable subTable : jacksonTable.subTables) {
			Log.d("INFLATING", "subtables");
			FolderElementData newDataItem;
			// switch type if sub table
			if (subTable instanceof ContainerTable) {
				// create new folder element
				newDataItem = new FolderElementData(activity,
						SlideoutNavigationActivity.theActiveActivity
								.inEditMode(), element_type.folder);
				// set its name
				if (subTable.tableName == null) {
					newDataItem.text.setText("");
				} else {
					newDataItem.text.setText(subTable.tableName);
				}
				// add data holder to adapter
				Log.d("INFLATING", "subTable == " + subTable.tableName
						+ "; isSelected == " + subTable.isSelected());
				if ((SlideoutNavigationActivity.getAc().onlySelected && subTable
						.isSelected())
						|| !SlideoutNavigationActivity.getAc().onlySelected
						|| SlideoutNavigationActivity.getAc().showInvisible) {
					allData.add(newDataItem);
					// set data holder jackson Table
					newDataItem.jacksonTable = subTable;
					Log.d("INFLATING",
							"setting checked to " + subTable.isSelected());
					newDataItem.checked = subTable.isSelected();
					newDataItem.favorite = subTable.isFavorite();
					// create fragment
					newDataItem.childFragment = new FolderFragment();
					newDataItem.childFragment.elementName = subTable.tableName;
					newDataItem.childFragment.fragment_parent = this;
					// ((FolderFragment) newDataItem.childFragment).jacksonTable
					// = (ContainerTable)subTable;
					// call recursive
					// note: editable must be given because the onCreate was not
					// yet called
					// and therefore variable "editable" not yet initialized
					((FolderFragment) newDataItem.childFragment)
							.inflateWithJacksonData((ContainerTable) subTable,
									activity);
				}
			} else if (subTable instanceof Table) {
				// create new folder element
				newDataItem = new FolderElementData(activity,
						SlideoutNavigationActivity.theActiveActivity
								.inEditMode(), element_type.table);
				// set its name
				if (subTable.tableName == null) {
					newDataItem.text.setText("");
				} else {
					newDataItem.text.setText(subTable.tableName);
				}
				// add data holder to adapter
				Log.d("INFLATING", "subTable == " + subTable.tableName
						+ "; isSelected == " + subTable.isSelected());
				if ((SlideoutNavigationActivity.getAc().onlySelected && subTable
						.isSelected())
						|| !SlideoutNavigationActivity.getAc().onlySelected
						|| SlideoutNavigationActivity.getAc().showInvisible) {
					allData.add(newDataItem);
					// set data holder jackson Table
					newDataItem.jacksonTable = subTable;
					Log.d("INFLATING",
							"setting checked to " + subTable.isSelected());
					newDataItem.checked = subTable.isSelected();
					newDataItem.favorite = subTable.isFavorite();
					// create fragment
					newDataItem.childFragment = new TableFragment();
					newDataItem.childFragment.elementName = subTable.tableName;
					newDataItem.childFragment.fragment_parent = this;
					((TableFragment) newDataItem.childFragment).jacksonInflate(
							(Table) subTable, activity);

				}
			} else if (subTable instanceof MatrixTable) {
				// create new folder element
				newDataItem = new FolderElementData(activity,
						SlideoutNavigationActivity.theActiveActivity
								.inEditMode(), element_type.matrix);
				// set its name
				if (subTable.tableName == null) {
					newDataItem.text.setText("");
				} else {
					newDataItem.text.setText(subTable.tableName);
				}
				// add data holder to adapter
				Log.d("INFLATING", "subTable == " + subTable.tableName
						+ "; isSelected == " + subTable.isSelected());
				if ((SlideoutNavigationActivity.getAc().onlySelected && subTable
						.isSelected())
						|| !SlideoutNavigationActivity.getAc().onlySelected
						|| SlideoutNavigationActivity.getAc().showInvisible) {
					allData.add(newDataItem);
					// set data holder jackson Table
					newDataItem.jacksonTable = subTable;
					Log.d("INFLATING",
							"setting checked to " + subTable.isSelected());
					newDataItem.checked = subTable.isSelected();
					newDataItem.favorite = subTable.isFavorite();
					// create fragment
					// TODO add matrix inflation
					newDataItem.childFragment = new MatrixFragment();
					newDataItem.childFragment.elementName = subTable.tableName;
					newDataItem.childFragment.fragment_parent = this;
					((MatrixFragment) newDataItem.childFragment)
							.jacksonInflate((MatrixTable) subTable, activity);

				}
			}
			// } else if (subTable instanceof FavoriteTable) {
			// Log.d("INFLATING FOR FavoriteTable, name", ""
			// + subTable.tableName);
			// //TODO ate
			// }
			dataAdapter.notifyDataSetChanged();
		}
	}
	/*
	 * JACKSON END
	 */
}
