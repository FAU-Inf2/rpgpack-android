package de.fau.cs.mad.gamekobold.game;

import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;

public class GameDetailsFragment extends Fragment {
	public static final String EXTRA_GAME_NAME = "de.fau.cs.mad.gamekobold.game.gamename";
	// private List<Charakter> charakterList;
	// private List<String> tagList;

	private Game game;
	private Template template;

	private TextView gameName;
	private TextView date;
	private TextView description;
	private TextView templateName;
	private ImageView gameIcon;
	private Button buttonInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_game_details, parent,
				false);

		String gName = (String) getActivity().getIntent().getSerializableExtra(
				EXTRA_GAME_NAME);
		game = GameLab.get(getActivity()).getGame(gName);
		
		// TODO change Layout ids!!!
		gameName = (TextView) view.findViewById(R.id.gameName);
		date = (TextView) view.findViewById(R.id.textView4);
		description = (TextView) view.findViewById(R.id.editTextDescription);
		templateName = (TextView) view.findViewById(R.id.textView4);
		gameIcon = (ImageView) view.findViewById(R.id.iconGame);
		buttonInfo = (Button) view.findViewById(R.id.buttonInfoPopup);

		gameName.setText(game.getGameName());
		// templateName.setText(game.getTemplate().getTemplateName());
		//templateName.setText(game.getTemplate().getTemplateName());
		date.setText(game.getDate());
		description.setText(game.getDescription());

		return view;
	}
}
