package de.fau.cs.mad.gamekobold.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.templatebrowser.CreateNewCharacterActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class ExpandableListArrayAdapter extends BaseExpandableListAdapter {

	public ArrayList<Template> templates;
	public LayoutInflater inflater;
	private Context context;
	private Game newGame;
	private GameCharacter curGameCharacter;
	private GridView gridView;
	// to delete highlighting on logcklicked items
	public CharacterGridAdapter adapter;
	protected ArrayAdapter pickedAdapter;

	// TODO check if it is necessary to set new Adapter every time
	// now i cache adapter to keep selected characters highlighted
	private Map<Integer, CharacterGridAdapter> adapterCache;

	@SuppressLint("UseSparseArrays")
	public ExpandableListArrayAdapter(Context context,
			ArrayList<Template> templates, Game game) {
		this.context = context;
		this.templates = templates;
		this.newGame = game;
		this.adapterCache = new HashMap<Integer, CharacterGridAdapter>();
	}

	public void passAdapterForPickedGrid(ArrayAdapter pickedAdapter) {
		this.pickedAdapter = pickedAdapter;
	}

	public void setInflater(LayoutInflater inflater, Context context) {
		this.inflater = inflater;
		this.context = context;
	}

	@Override
	public View getChildView(final int templatePosition, int characterPosition,
			boolean isLastCharacter, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					R.layout.rowlayout_expandablelist_character, null);
		}

		// initialize values assigned to the items in the grid.
		final int spacingDp = 10;
		final int colWidthDp = 100;
		final int rowHeightDp = 120;

		// convert the dp values to pixels
		final float COL_WIDTH = context.getResources().getDisplayMetrics().density
				* colWidthDp;
		final float ROW_HEIGHT = context.getResources().getDisplayMetrics().density
				* rowHeightDp;
		final float SPACING = context.getResources().getDisplayMetrics().density
				* spacingDp;

		// calculate the column and row counts based on display
		final int colCount = (int) Math
				.floor((parent.getWidth() - (2 * SPACING))
						/ (COL_WIDTH + SPACING));

		final int rowCount = (int) Math.ceil((templates.get(templatePosition)
				.getCharacters().size() + 0d)
				/ colCount);

		// calculate the height for the current grid
		final int GRID_HEIGHT = Math.round(rowCount * (ROW_HEIGHT + SPACING));

		gridView = (GridView) convertView
				.findViewById(R.id.gridViewCharacterItem);
		// set the height of the current grid
		gridView.getLayoutParams().height = GRID_HEIGHT;

		if (this.adapterCache.containsKey(templatePosition)) {
			adapter = this.adapterCache.get(templatePosition);
		} else {
			adapter = new CharacterGridAdapter(context,
					templates.get(templatePosition));
			this.adapterCache.put(templatePosition, adapter);
		}

		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("ResourceAsColor")
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				// add to picked character
				if (position != adapterView.getChildCount() - 1) {
					curGameCharacter = (GameCharacter) adapterView
							.getItemAtPosition(position);

					// selected characters will be highlighted and added into
					// pickedCharactersGrid
					ArrayList<GameCharacter> selectedCharacters = ((CharacterGridAdapter) adapter).selectedCharacters;
					if (selectedCharacters.contains(curGameCharacter)) {
						selectedCharacters.remove(curGameCharacter);
						newGame.removeCharacter(curGameCharacter);

					} else {
						selectedCharacters.add(curGameCharacter);
						Toast.makeText(
								context,
								curGameCharacter.getCharacterName()
										+ " "
										+ context.getResources().getString(
												R.string.msg_added_to_game),
								Toast.LENGTH_SHORT).show();
						newGame.addCharacter(curGameCharacter);
					}
					pickedAdapter.notifyDataSetChanged();
					adapter.notifyDataSetChanged();

				}
				// create new character from template
				else {
					Template curClickedTemplate = templates
							.get(templatePosition);
					Intent intent = new Intent(context,
							CreateNewCharacterActivity.class);
					intent.putExtra("templateFileName",
							curClickedTemplate.getFileName());
					intent.putExtra("template",
							curClickedTemplate);
					
					context.startActivity(intent);
					// TODO notify adapter!
					// to be sure, that new character will appear in expandable
					// list
					//adapter.notifyDataSetChanged();
				}
			}
		});

		gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView,
					View view, final int position, long id) {
				Log.d("LONG CLICK", "pos:" + position);

				// if longclicked on last item >create new character<
				if (position == adapterView.getChildCount() - 1) {
					// do nothing
					return false;
				}

				// if longclicked on character -> redirect to
				// edit-character-mode
				else {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);

					builder.setTitle(context.getResources().getString(
							R.string.text_redirect_to_edit_character));
					builder.setMessage(context.getResources().getString(
							R.string.click_to_redirect_to_edit_character));
					builder.setNegativeButton(
							context.getResources().getString(R.string.no),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					builder.setPositiveButton(
							context.getResources().getString(R.string.yes),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO remove character from game
									// removeCharacter(position);
								}
							});
					builder.create().show();
					return true;
				}
			}
		});

		return convertView;

	}

	@Override
	public int getGroupCount() {
		return templates.size();
	}

	// Have to return 1! otherwise builds get ChildView duplicate rows with
	// character grid!
	@Override
	public int getChildrenCount(int groupPosition) {
		return 1; // this.templates.get(groupPosition).getCharacters().size();

	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.templates.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return this.templates.get(groupPosition).getCharacters()
				.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					R.layout.rowlayout_expandaplelist_template, null);
		}

		Template curTemplate = (Template) getGroup(groupPosition);

		TextView templateName = (TextView) convertView
				.findViewById(R.id.templateName);
		templateName.setText(curTemplate.getTemplateName());

		TextView worldName = (TextView) convertView
				.findViewById(R.id.worldName);
		worldName.setText(curTemplate.getWorldName());

		TextView characterCounter = (TextView) convertView
				.findViewById(R.id.charactersCounter);

		characterCounter.setText(String.valueOf(curTemplate.getCharacters()
				.size() - 1));

		return convertView;

	}

}
