package de.fau.cs.mad.gamekobold.characterbrowser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.game.GameCharacter;
import de.fau.cs.mad.gamekobold.game.TemplateLab;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.templatebrowser.CharacterDetailsActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;
import de.fau.cs.mad.gamekobold.templatebrowser.TemplateBrowserActivity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

// TODO get always the newest characters. like template or game lab
public class CharacterBrowserActivity extends ListActivity {
	private CharacterBrowserArrayAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_character_browser);
		List<GameCharacter> list = new ArrayList<GameCharacter>();
		adapter = new CharacterBrowserArrayAdapter(this, list);
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {

				if(position < adapter.getCount()-1) {
					// edit character 
					Intent i = new Intent(CharacterBrowserActivity.this,
							CharacterDetailsActivity.class);
					final GameCharacter clickedChar = adapter.getItem(position);
					try {
						final CharacterSheet sheet = JacksonInterface.loadCharacterSheet(new File(clickedChar.getFileAbsPath()), true);
						i.putExtra("CharacterSheet", sheet);
						startActivity(i);
					}
					catch(Throwable e) {
						e.printStackTrace();
					}	
				}
				else {
					// create new character
					Intent i = new Intent(CharacterBrowserActivity.this,
							TemplateBrowserActivity.class);
					Toast.makeText(CharacterBrowserActivity.this, "Please pick a template", Toast.LENGTH_LONG).show();
					startActivity(i);
				}
			}
		});
		
		getListView().setOnItemLongClickListener(
			new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {
					if(position < adapter.getCount()-1) {
						final GameCharacter clickedCharacter = adapter.getItem(position);
						AlertDialog.Builder builder = new AlertDialog.Builder(
								CharacterBrowserActivity.this);
						builder.setTitle(getResources().getString(
								R.string.msg_ask_character_deletion));
						builder.setMessage(getResources().getString(
								R.string.msg_yes_to_delete_character));
						builder.setNegativeButton(
								getResources().getString(R.string.no),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick( DialogInterface dialog, int which) {
									}
								});
						builder.setPositiveButton(
								getResources().getString(R.string.yes),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialog,
											int which) {
										File file = new File(clickedCharacter.getFileAbsPath());
										if (file != null) {
											// delete character
											if (file.isFile()) {
												file.delete();
												adapter.remove(clickedCharacter);
												adapter.notifyDataSetChanged();
											}
										}
									}
								});
						builder.create().show();
						return true;
					}
					return false;
				}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		List<Template> templateList = TemplateLab.get(this).getTemplates();
		adapter.clear();
		for(final Template template : templateList) {
			List<GameCharacter> characterList = template.getCharacters();
			if(characterList.size() > 1) {
				// remove fake character
				adapter.addAll(characterList.subList(0, characterList.size()-1));	
			}
		}
		Collections.sort(adapter.getList());
		// TODO string raus ziehen
		adapter.add(new GameCharacter("Create new character"));
		adapter.notifyDataSetChanged();
	}
}
