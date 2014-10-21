package de.fau.cs.mad.gamekobold.template_generator;

import java.util.ArrayList;

import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.character.CharacterEditActivity;
import de.fau.cs.mad.gamekobold.game.CharacterPlayActivity;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.MatrixTable;
import de.fau.cs.mad.gamekobold.jackson.Table;
import de.fau.cs.mad.gamekobold.matrix.MatrixFragment;
import de.fau.cs.mad.gamekobold.template_generator.FolderElementData.element_type;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.v4.view.GravityCompat;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FolderElementAdapter extends ArrayAdapter<FolderElementData> {
	/*
	 * JACKSON START
	 */
	// DataAdapter only used in FolderFragments. one per fragment, so use
	// DataAdapter for table info
	public ContainerTable jacksonTable;
	/*
	 * JACKSON END
	 */

	public ArrayList<FolderElementData> allData;
	/**
	 * can we edit the names of the folders/tables/matrices? if editable ->
	 * EditText-field else -> TextView
	 */
	private boolean editable = true;
	private boolean checkBoxesVisible = false;
	private boolean favoritesVisible = false;

	public FolderElementAdapter(Activity context, boolean editable,
			int textViewResourceId, ArrayList<FolderElementData> objects) {
		super(context, textViewResourceId, objects);
		allData = new ArrayList<FolderElementData>();
		allData = objects;
		// this.editable = editable;
		this.editable = SlideoutNavigationActivity.getAc().inEditMode();
		checkBoxesVisible = (SlideoutNavigationActivity.getAc() instanceof CharacterEditActivity) ? true
				: false;
		// set to false for slideout menu; doesnt work; set in oncreate of
		// activity now
		// boolean thisIsRoot =
		// SlideoutNavigationActivity.getAc().getRootFragment().dataAdapter
		// == this?true:false;
		Log.d("FolderElementAdapter", "FolderElementAdapter onCreate");
		// if(thisIsRoot){
		// checkBoxesVisible = false;
		// }
		// favoritesVisible = (SlideoutNavigationActivity.getAc() instanceof
		// CharacterPlayActivity)?true:false;
		favoritesVisible = false;
	}

	// We keep this ViewHolder object to save time. It's quicker than
	// findViewById() when repainting.
	static class ViewHolder {
		TextView elementName;
		protected View row;
		TextWatcher jacksonTableNameChangeWatcher = null;
		CheckBox box;
		CheckBox favorite;
	}

	@Override
	public int getCount() {
		return allData.size();
	}

	/**
	 * @return 0 -> nothing visible <br>
	 *         1 -> checkboxes visible <br>
	 *         2 -> favorites visible <br>
	 *         3 -> both <br>
	 *         4 -> editable-mode
	 */
	@Override
	public int getItemViewType(int position) {
		if (editable) {
			return 4;
		} else if (checkBoxesVisible && favoritesVisible) {
			return 3;
		} else if (checkBoxesVisible) {
			return 1;
		} else if (favoritesVisible) {
			return 2;
		} else {
			return 0;
		}
		// return getItem(position).checkBoxVisible?1:0;
	}

	@Override
	public int getViewTypeCount() {
		return 5;
	}

	@Override
	public FolderElementData getItem(int position) {
		return allData.get(position);
	}

	/**
	 * 
	 * @return all elements inside this folder <br>
	 *         null if no elements are inside
	 */
	public FolderElementData[] getAll() {
		FolderElementData[] toReturn = new FolderElementData[allData.size()];
		allData.toArray(toReturn);
		return toReturn;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	private void setListeners(final ViewHolder holder,
			final FolderElementData data) {
		holder.row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GeneralFragment newFragment;
				GeneralFragment oldFragment;
				FragmentTransaction fragmentTransaction = SlideoutNavigationActivity
						.getAc().getFragmentManager().beginTransaction();
				// attach fragment (old one if it already exists)
				if (data.childFragment == null) {
					/*
					 * JACKSON START
					 */
					// childFragment was null, so we need to create a new table
					// (childFragment == FolderFragment)
					// data.jacksonTable should be a Container Table, created
					// when type changed
					if (data.type == element_type.folder) {
						newFragment = new FolderFragment();
						((FolderFragment) newFragment)
								.setJacksonTable((ContainerTable) data.jacksonTable);
					} else if (data.type == element_type.table) {
						newFragment = new TableFragment();
						((TableFragment) newFragment).jacksonTable = (Table) data.jacksonTable;
					} else {// if(data.type == element_type.matrix){
						newFragment = new MatrixFragment();
						((MatrixFragment) newFragment).jacksonTable = (MatrixTable) data.jacksonTable;
					}
					/*
					 * JACKSON END
					 */
					fragmentTransaction.add(R.id.frame_layout_container,
							newFragment);
				}
				// fragment already exisits -> show it
				else {
					newFragment = data.childFragment;
					/*
					 * JACKSON START needed if template is edited, because we
					 * can create but we cannot add the fragment during
					 * inflation
					 */
					if (!newFragment.isAdded()) {
						fragmentTransaction.add(R.id.frame_layout_container,
								newFragment);
						Log.d("jackson",
								"jackson did add to fragmentTransaction!");
					}
					/*
					 * JACKSON END
					 */
					fragmentTransaction.attach(newFragment);
				}
				// detach old and set backstack-element (for back button)
				oldFragment = SlideoutNavigationActivity.getAc()
						.getCurrentFragment();
				if (oldFragment != newFragment) {
					fragmentTransaction.detach(oldFragment);
					newFragment.backStackElement = oldFragment;
					newFragment.fragment_parent = oldFragment;
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
					data.childFragment = newFragment;
					SlideoutNavigationActivity.getAc().setCurrentFragment(
							newFragment);
					// next line now done in GeneralFragment.onAttach
					// ((TemplateGeneratorActivity)
					// TemplateGeneratorActivity.theActiveActivity).currentFragment
					// = newFragment;
					// set old Fragment to be the parent IF NOT from slideout
					// menu
					if (SlideoutNavigationActivity.getAc().getDrawerLayout()
							.isDrawerOpen(GravityCompat.START)) {
						// drawer is open -> new fragment will be top fragment
						SlideoutNavigationActivity.getAc().setTopFragment(
								newFragment);
						newFragment.isATopFragment = true;
						newFragment.fragment_parent = oldFragment;
					}
					Log.d("FolderElementAdapter", "invalidateOptionsMenu!");
					SlideoutNavigationActivity.getAc().invalidateOptionsMenu();
					newFragment.elementName = holder.elementName.getText()
							.toString();
				}
				if (SlideoutNavigationActivity.getAc().getDrawerLayout()
						.isDrawerOpen(GravityCompat.START)) {
					SlideoutNavigationActivity.getAc().getDrawerLayout()
							.closeDrawers();
				}
			}
		});
		if (holder.box != null) {
			holder.box
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						// final AbstractTable jacksonTable = data.jacksonTable;
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							data.checked = isChecked;
							if (!isChecked) {
								holder.favorite.setChecked(false);
								data.jacksonTable.setFavorite(false);
							}
							// enabling(check)/disabling(uncheck) this category
							data.jacksonTable.setSelected(isChecked);
						}
					});
		}
		if (holder.favorite != null) {
			holder.favorite
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						// final AbstractTable jacksonTable = data.jacksonTable;
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							data.favorite = isChecked;
							if (isChecked) {
								holder.box.setChecked(true);
								data.jacksonTable.setSelected(true);
							}
							data.jacksonTable.setFavorite(isChecked);
						}
					});
		}
	}

	@Override
	public View getView(int viewPosition, View convertView, ViewGroup parent) {
		final FolderElementData data = this.getItem(viewPosition);
		Log.d("FolderElementAdapter", "viewPosition == " + viewPosition);
		View view = null;
		// Check to see if this row has already been painted once.
		if (convertView == null) {
			Log.d("FolderElementAdapter", "convertview == NULL or reinflating");
			// If it hasn't, set up everything:
			LayoutInflater inflator = SlideoutNavigationActivity.getAc()
					.getLayoutInflater();
			ViewHolder holder = new ViewHolder();
			// distinguish slightly between how an inflated row should looke
			// like
			// Log.d("FolderElementAdapter", "getView; editable: " + editable +
			// "; checking: " + allowCheckingItems);
			Log.d("FolderElementAdapter", "getItemViewType(viewPosition) == "
					+ getItemViewType(viewPosition));
			if (getItemViewType(viewPosition) == 4) {
				view = inflator.inflate(
						R.layout.template_listview_row_editable,
						new LinearLayout(SlideoutNavigationActivity.getAc()),
						false);
				holder.elementName = (EditText) view.findViewById(R.id.text);
			} else {
				// checking items should only be allowed if they are not
				// editable
				if (getItemViewType(viewPosition) == 1) {
					view = inflator
							.inflate(
									R.layout.template_listview_row_checking,
									new LinearLayout(SlideoutNavigationActivity
											.getAc()), false);
					holder.elementName = (TextView) view
							.findViewById(R.id.text);
					CheckBox cb = (CheckBox) view
							.findViewById(R.id.element_checkbox);
					CheckBox favorite = (CheckBox) view
							.findViewById(R.id.favorite_checkbox);
					if (SlideoutNavigationActivity.getAc() instanceof CharacterEditActivity) {
						favorite.setVisibility(View.INVISIBLE);
					} else if (SlideoutNavigationActivity.getAc() instanceof CharacterPlayActivity) {
						cb.setVisibility(View.INVISIBLE);
					}
					holder.box = cb;
					holder.favorite = favorite;
					if (data.checked) {
						holder.box.setChecked(true);
					} else {
						holder.box.setChecked(false);
					}
					if (data.favorite) {
						holder.favorite.setChecked(true);
					} else {
						holder.favorite.setChecked(false);
					}
				} else {
					view = inflator
							.inflate(
									R.layout.template_listview_row,
									new LinearLayout(SlideoutNavigationActivity
											.getAc()), false);
					holder.elementName = (TextView) view
							.findViewById(R.id.text);
				}
			}
			holder.row = view;
			view.setTag(holder);
		} else {
			view = convertView;
		}

		ViewHolder holder = (ViewHolder) view.getTag();
		setListeners(holder, data);
		/*
		 * JACKSON START
		 */
		// check if we are converting a view
		if (convertView != null) {
			// if so we need to remove the old TextWatcher
			holder.elementName
					.removeTextChangedListener(holder.jacksonTableNameChangeWatcher);
			// Log.d("TableNameChangeWatcher", "convert View");
		}
		// if checkboxes are activated, set them
		if (holder.box != null) {
			holder.box.setChecked(data.checked);
		}
		if (holder.favorite != null) {
			holder.favorite.setChecked(data.favorite);
		}
		// set view.elementName according to data
		holder.elementName.setText(data.text.getText());
		// add TextWatcher
		holder.elementName.addTextChangedListener(data.nameChangeWatcher);
		// save which TextWatcher is currently added. needed for removal
		holder.jacksonTableNameChangeWatcher = data.nameChangeWatcher;
		// long click listener for removal of items
		holder.row.setOnLongClickListener(new View.OnLongClickListener() {
			public final FolderElementData myDataItem = data;

			@Override
			public boolean onLongClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setTitle(R.string.question_delete);
				builder.setMessage(R.string.long_click_dialog_message);
				builder.setNegativeButton(R.string.no,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				builder.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								jacksonTable
										.removeTable(myDataItem.jacksonTable);
								remove(myDataItem);
								notifyDataSetChanged();
							}
						});
				builder.create().show();
				return true;
			}
		});
		/*
		 * JACKSON END
		 */
		// set image left of the element
		ImageView elementSymbol = (ImageView) view
				.findViewById(R.id.element_symbol);
		switch (this.getItem(viewPosition).getType()) {
		case table:
			elementSymbol.setImageResource(R.drawable.table_icon);
			break;
		case folder:
			elementSymbol.setImageResource(R.drawable.folder_icon);
			break;
		case matrix:
			elementSymbol.setImageResource(R.drawable.collection_icon);
			break;
		}
		return view;
	}

	/**
	 * set visibilty of the checkboxes (all data items handled by this adapter)
	 */
	public void setCheckboxVisibility(boolean visible) {
		// for(FolderElementData oneDatum: allData){
		// oneDatum.checkBoxVisible = visible;
		// }
		// notifyDataSetChanged();
		if (checkBoxesVisible != visible) {
			boolean thisIsRoot = SlideoutNavigationActivity.getAc()
					.getRootFragment().dataAdapter == this ? true : false;
			if (!thisIsRoot) {
				Log.d("FolderElementAdapter", "setCheckboxVisibility to "
						+ visible);
			}
			// Log.d("FolderElementAdapter",
			// "setCheckboxVisibility rootFragment? " + thisIsRoot);
			// new Throwable().printStackTrace();
			// Log.d("FolderElementAdapter", "checkBoxesVisible SET TO " +
			// visible);
			checkBoxesVisible = visible;
			notifyDataSetChanged();
		}
	}

	public void setEditable(boolean value) {
		if (editable != value) {
			editable = value;
			notifyDataSetChanged();
		}
	}

}
