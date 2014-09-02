package de.fau.cs.mad.gamekobold.characterbrowser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.game.GameCharacter;
import de.fau.cs.mad.gamekobold.game.TemplateLab;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.templatebrowser.CharacterDetailsActivity;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

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
				adapter.addAll(characterList.subList(0, characterList.size()-1));	
			}
		}
		adapter.notifyDataSetChanged();
	}
}
