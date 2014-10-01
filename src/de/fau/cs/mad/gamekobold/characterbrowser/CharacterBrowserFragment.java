package de.fau.cs.mad.gamekobold.characterbrowser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.game.Game;
import de.fau.cs.mad.gamekobold.game.GameCharacter;
import de.fau.cs.mad.gamekobold.game.GameLab;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import de.fau.cs.mad.gamekobold.templatebrowser.CharacterDetailsActivity;

public class CharacterBrowserFragment extends ListFragment {
	public static final String EXTRA_MODE_GAME_CREATION = "de.fau.cs.mad.gamekobold.gamecreation";
	private CharacterBrowserArrayAdapter adapter = null;
	private List<CharacterSheet> characters;
	private boolean mode_pickCharacterForGameCreation = false;

	private CallbacksCharBrowser mCallbacks;

	/**
	 * Required interface for hosting activities.
	 */
	public interface CallbacksCharBrowser {
		void onCharacterSelected(CharacterSheet clickedChar);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (CallbacksCharBrowser) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		characters = new ArrayList<CharacterSheet>();
		adapter = new CharacterBrowserArrayAdapter(getActivity(), characters);
		setListAdapter(adapter);

		if ((getActivity().getIntent().hasExtra(EXTRA_MODE_GAME_CREATION))) {
			mode_pickCharacterForGameCreation = getActivity().getIntent()
					.getBooleanExtra(EXTRA_MODE_GAME_CREATION, false);
		} else
			mode_pickCharacterForGameCreation = false;
	}

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// // Inflate the layout for this fragment
	// return inflater.inflate(R.layout.fragment_character_browser, container,
	// false);
	// }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {

				final CharacterSheet clickedChar = adapter.getItem(position);

				if (mode_pickCharacterForGameCreation) {
					// TODO change color and add to the pickedCharacterGrid
					RelativeLayout selRow = (RelativeLayout) view
							.findViewById(R.id.relativeLayout);
					int color = Color.TRANSPARENT;
					Drawable backgroundColor = selRow.getBackground();
					if (backgroundColor instanceof ColorDrawable)
						color = ((ColorDrawable) backgroundColor).getColor();
					if ((color == getResources().getColor(
							R.color.background_green))) {
						selRow.setBackgroundColor(getResources().getColor(
								R.color.background_dark));
					} else {
						selRow.setBackgroundColor(getResources().getColor(
								R.color.background_green));
					}
					mCallbacks.onCharacterSelected(clickedChar);
				} else {
					// edit character
					Intent i = new Intent(getActivity(),
							CharacterDetailsActivity.class);
					i.putExtra("CharacterSheet", clickedChar);
					startActivity(i);
				}
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

											ArrayList<CharacterSheet> characters = new ArrayList<CharacterSheet>();
											// check if character to delete
											// appears in games
											for (Game g : games) {
												characters.addAll(g
														.getCharacterSheetList());
												int location = 0;
												for (CharacterSheet c : characters) {
													if (c.getFileAbsolutePath()
															.equals(clickedCharacter
																	.getFileAbsolutePath())) {
														g.getCharacterSheetList()
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

	// /**
	// * Required interface for hosting activities.
	// */
	// public interface onCharacterSelectedListener {
	// public void onCharacterSelected(CharacterSheet clickedChar);
	// }
	//
	// @Override
	// public void onAttach(Activity activity) {
	// super.onAttach(activity);
	// if (activity instanceof onCharacterSelectedListener) {
	// listener = (onCharacterSelectedListener) activity;
	// } else {
	// throw new ClassCastException(
	// activity.toString()
	// +
	// " must implemenet CharacterBrowserFragment.onCharacterSelectedListener");
	// }
	// }

}
