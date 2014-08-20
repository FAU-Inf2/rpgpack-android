package de.fau.cs.mad.gamekobold.game;

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
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.character.CharacterEditActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.CreateNewCharacterActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;
import de.fau.cs.mad.gamekobold.templatebrowser.TemplateDetailsActivity;

public class ExpandableListArrayAdapter extends BaseExpandableListAdapter {

	public ArrayList<Template> templates;
	public LayoutInflater inflater;
	private Context context;
	private Game newGame;
	private GameCharacter curGameCharacter;
	private GridView gridView;
	// to delete highlighting on logcklicked items
	public CharacterGridAdapter adapter;

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

		gridView = (GridView) convertView
				.findViewById(R.id.gridViewCharacterItem);

		// final CharacterGridAdapter adapter;
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
						// newGame.setTemplate(curGameCharacter.getTemplate());
						newGame.removeCharacter(curGameCharacter);
						notifyDataSetChanged();
					} else {
						selectedCharacters.add(curGameCharacter);
						Toast.makeText(
								context,
								curGameCharacter.getCharacterName()
										+ " "
										+ context.getResources().getString(
												R.string.msg_added_to_game),
								Toast.LENGTH_SHORT).show();
						// FIXME set Template not here!
						// newGame.setTemplate(curGameCharacter.getTemplate());
						newGame.addCharacter(curGameCharacter);
						notifyDataSetChanged();
					}

					// if
					// (!newGame.getCharakterList().contains(curGameCharacter))
					// {
					// Toast.makeText(
					// context,
					// curGameCharacter.getCharacterName()
					// + " wird zum Spiel hinzugefuegt!",
					// Toast.LENGTH_SHORT).show();
					// // TODO pruefen ob es nur ein Template moeglich ist!!!!
					// Log.d("newGame is null?", "" + (newGame == null));
					// newGame.setTemplate(curGameCharacter.getTemplate());
					// Log.d("Character is null?", ""
					// + (curGameCharacter == null));
					// newGame.addCharacter(curGameCharacter);
					// // notifyDataSetChanged();
					// }

					adapter.notifyDataSetChanged();

				}
				// create new character from template
				else {

					Template curClickedTemplate = templates
							.get(templatePosition);

					Intent intent = new Intent(context,
							CreateNewCharacterActivity.class);
					// TODO bei den anderen auch curTemplate.getFileName()
					// don't add the activity to the history
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

					intent.putExtra("templateFileName",
							curClickedTemplate.getFileName());
					context.startActivity(intent);

					// Intent intent = new Intent(context,
					// CharacterEditActivity.class);
					// intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					// // flag to distinguish between editing and creating
					// intent.putExtra(
					// SlideoutNavigationActivity.MODE_CREATE_NEW_TEMPLATE,
					// false);
					// intent.putExtra(
					// SlideoutNavigationActivity.EDIT_TEMPLATE_FILE_NAME,
					// templates.get(templatePosition).getTemplateName());
					// context.startActivity(intent);
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
