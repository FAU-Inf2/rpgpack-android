package de.fau.cs.mad.gamekobold.characterbrowser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.game.Game;
import de.fau.cs.mad.gamekobold.game.GameCharacter;
import de.fau.cs.mad.gamekobold.game.GameLab;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.templatebrowser.CharacterDetailsActivity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class CharacterBrowserActivity extends ListActivity {
	private CharacterBrowserArrayAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_character_browser);
		List<CharacterSheet> list = new ArrayList<CharacterSheet>();
		adapter = new CharacterBrowserArrayAdapter(this, list);
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				// edit character
				Intent i = new Intent(CharacterBrowserActivity.this,
						CharacterDetailsActivity.class);
				final CharacterSheet clickedChar = adapter.getItem(position);
				i.putExtra("CharacterSheet", clickedChar);
				startActivity(i);
			}
		});

		getListView().setOnItemLongClickListener(
				new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View view, int position, long id) {
						if (position < adapter.getCount() - 1) {
							final CharacterSheet clickedCharacter = adapter
									.getItem(position);
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
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									});
							builder.setPositiveButton(
									getResources().getString(R.string.yes),
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO remove character
											// from all games too
											ArrayList<Game> games = new ArrayList<Game>();
											games.addAll(GameLab
													.get(CharacterBrowserActivity.this)
													.getGames());

											ArrayList<GameCharacter> characters = new ArrayList<GameCharacter>();
											// check if character to delete
											// appears in games
											for (Game g : games) {
												characters.addAll(g
														.getCharacterList());
												int location = 0;
												for (GameCharacter c : characters) {
													if (c.getFileAbsPath()
															.equals(clickedCharacter
																	.getFileAbsolutePath())) {
														g.getCharacterList()
																.remove(location);
														Log.d("CharacterBrowserActivity",
																c.getCharacterName()
																		+ " was deleted!!!!");
													}
													location++;
												}
												characters.clear();
											}
											File file = new File(
													clickedCharacter
															.getFileAbsolutePath());
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
		adapter.clear();
		loadCharacterList();
		Collections.sort(adapter.getList());
		adapter.notifyDataSetChanged();
	}

	private void loadCharacterList() {
		final File rootCharacterDir = JacksonInterface
				.getCharacterRootDirectory(this);
		if (rootCharacterDir == null) {
			return;
		}
		if (!rootCharacterDir.isDirectory()) {
			return;
		}
		final File[] characterFolders = rootCharacterDir.listFiles();
		CharacterSheet charSheet = null;
		for (final File charFolder : characterFolders) {
			if (!charFolder.isDirectory()) {
				continue;
			}
			final File[] characters = charFolder.listFiles();
			for (final File characterFile : characters) {
				if (!characterFile.isFile()) {
					continue;
				}
				try {
					charSheet = JacksonInterface.loadCharacterSheet(
							characterFile, true);
					adapter.add(charSheet);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}
}
