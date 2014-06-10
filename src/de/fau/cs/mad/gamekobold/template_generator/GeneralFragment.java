package de.fau.cs.mad.gamekobold.template_generator;

import java.util.ArrayList;

import android.app.Fragment;

public abstract class GeneralFragment extends Fragment {

	protected GeneralFragment fragment_parent = null;
	protected ArrayList<TableFragment> tables;
	
	public GeneralFragment() {
		super();
        setRetainInstance(true);
	}
	
	protected abstract void addItemList();
	protected abstract void addItemList(int selected);

}