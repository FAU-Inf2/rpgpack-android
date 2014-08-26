package de.fau.cs.mad.gamekobold.template_generator;

import de.fau.cs.mad.gamekobold.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WelcomePlayCharacterFragment extends GeneralFragment{

	public WelcomePlayCharacterFragment() {
		super();
        setRetainInstance(true);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		super.onCreateView(inflater, container, savedInstanceState);
		View view = (CustomDrawerLayout) inflater.inflate(R.layout.activity_template_generator_welcome, null);
        return view;
    }

	@Override
	protected void addItemList() {
		//nothing to add in WelcomeFragment
	}

	@Override
	public void showDialog() {
		//no dialog to show
	}
}
