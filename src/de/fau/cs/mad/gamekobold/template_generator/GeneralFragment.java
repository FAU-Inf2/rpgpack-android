package de.fau.cs.mad.gamekobold.template_generator;

import java.util.ArrayList;

import de.fau.cs.mad.gamekobold.jackson.Table;
import android.app.Fragment;

public abstract class GeneralFragment extends Fragment {

	protected GeneralFragment fragment_parent = null;
	protected ArrayList<TableFragment> tables; //tables that are below this fragment

	String elementName;
	
	public GeneralFragment() {
		super();
        setRetainInstance(true);
	}
	
	protected abstract void addItemList();

	public abstract void showDialog();

}