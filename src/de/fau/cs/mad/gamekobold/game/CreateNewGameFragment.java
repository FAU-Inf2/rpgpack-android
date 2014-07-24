package de.fau.cs.mad.gamekobold.game;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.templatebrowser.Template;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class CreateNewGameFragment extends Fragment {
//	private List<Charakter> charakterList;
//	private List<String> tagList;
	
	private Game game;
	private Template template;
	
	private TextView gameName;
	private TextView date;
	private TextView description;
	private TextView templateName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_create_new_game, parent,
				false);

		gameName = (EditText) view.findViewById(R.id.gameName);
		gameName.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence c, int start, int before,
					int count) {
				//TODO speichern in objekt
				//game.setGameName(c.toString());
			}

			public void beforeTextChanged(CharSequence c, int start, int count,
					int after) {
				// This space intentionally left blank
			}

			public void afterTextChanged(Editable c) {
				// This one too
			}

		});

		return view;
	}
}
