package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class ExpandableListArrayAdapter extends BaseExpandableListAdapter {

	public ArrayList<Template> templates;
	public LayoutInflater inflater;
	private Context context;
	private Game newGame;

	public ExpandableListArrayAdapter(Context context,
			ArrayList<Template> templates, Game game) {
		this.context = context;
		this.templates = templates;
		this.newGame = game;
	}

	public void setInflater(LayoutInflater inflater, Context context) {
		this.inflater = inflater;
		this.context = context;
	}

	@Override
	public View getChildView(int templatePosition, int characterPosition,
			boolean isLastCharacter, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					R.layout.rowlayout_expandablelist_character, null);
		}

		GridView gridView = (GridView) convertView
				.findViewById(R.id.gridViewCharacterItem);

		CharacterGridAdapter adapter = new CharacterGridAdapter(context,
				R.layout.itemlayout_expandablelist_charakter,
				templates.get(templatePosition));

		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// add to picked character
				if (position != adapterView.getChildCount() - 1) {
					GameCharacter curGameCharacter = (GameCharacter) adapterView
							.getItemAtPosition(position);

					Toast.makeText(
							context,
							curGameCharacter.getCharacterName()
									+ " wird zum Spiel hinzugefuegt!",
							Toast.LENGTH_SHORT).show();

					// TODO pruefen ob es nur ein Template moeglich ist!!!!
					Log.d("newGame is null?", "" + (newGame == null));

					newGame.setTemplate(curGameCharacter.getTemplate());

					Log.d("Character is null?", "" + (curGameCharacter == null));

					newGame.addCharacter(curGameCharacter);
					notifyDataSetChanged();
				}
				// create new character from template
				else {
					Toast.makeText(context, "Neues Charakter wird erstellt!",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView,
					View view, final int position, long id) {
				Log.d("LONG CLICK", "pos:" + position);

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(context.getResources().getString(
						R.string.text_remove_character_from_game));
				builder.setMessage(context.getResources().getString(
						R.string.text_click_to_remove_character_from_game));
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
				.size()-1));

		return convertView;

	}

}
