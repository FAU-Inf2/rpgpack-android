package de.fau.cs.mad.gamekobold.characterbrowser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.game.Game;
import de.fau.cs.mad.gamekobold.game.GameCharacter;
import de.fau.cs.mad.gamekobold.game.GameLab;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.templatebrowser.CharacterDetailsActivity;

public class CharacterBrowserFragment extends ListFragment {
	private CharacterBrowserArrayAdapter adapter = null;
	private List<CharacterSheet> characters;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		characters = new ArrayList<CharacterSheet>();
		adapter = new CharacterBrowserArrayAdapter(getActivity(), characters);
		setListAdapter(adapter);
	}

//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		// Inflate the layout for this fragment
//		return inflater.inflate(R.layout.fragment_character_browser, container, false);
//	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				// edit character
				Intent i = new Intent(getActivity(),
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
									getActivity());
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
											// remove character from all games
											ArrayList<Game> games = new ArrayList<Game>();
											games.addAll(GameLab.get(
													getActivity()).getGames());

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
	public void onResume() {
		super.onResume();
		adapter.clear();
		loadCharacterList();
		Collections.sort(adapter.getList());
		adapter.notifyDataSetChanged();
	}

	private void loadCharacterList() {
		final File rootCharacterDir = JacksonInterface
				.getCharacterRootDirectory(getActivity());
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
