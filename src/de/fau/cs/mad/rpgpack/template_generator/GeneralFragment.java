package de.fau.cs.mad.rpgpack.template_generator;

import java.util.ArrayList;

import de.fau.cs.mad.rpgpack.SlideoutNavigationActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class GeneralFragment extends Fragment {

	protected GeneralFragment fragment_parent = null;
	protected ArrayList<TableFragment> tables; //tables that are below this fragment
	protected GeneralFragment backStackElement = null;
	public boolean isATopFragment = false;


	public String elementName;
	
	public GeneralFragment() {
		super();
        setRetainInstance(true);
	}
	
	protected abstract void addItemList();

	public abstract void showDialog();
	
	public GeneralFragment getParent(){
		return fragment_parent;
	}
	
	public GeneralFragment getBackStackElement(){
		return backStackElement;
	}
	
	public void setBackStackElement(GeneralFragment newValue){
		backStackElement = newValue;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		if(backStackElement!=null){
			getFragmentManager().putFragment(outState,"backStackElement",backStackElement);
		}
		if(fragment_parent!=null){
			getFragmentManager().putFragment(outState,"fragment_parent",fragment_parent);
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SlideoutNavigationActivity.getAc().setCurrentFragment(this);
		if(savedInstanceState != null){
			backStackElement = (GeneralFragment) getFragmentManager().getFragment(savedInstanceState,"backStackElement");
			fragment_parent = (GeneralFragment) getFragmentManager().getFragment(savedInstanceState,"fragment_parent");
		}
		GeneralFragment searchedTopFragment = this;
		while(!searchedTopFragment.isATopFragment){
			searchedTopFragment = searchedTopFragment.fragment_parent;
		}
		SlideoutNavigationActivity.getAc().setTopFragment(searchedTopFragment);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
//	@Override
//	public void onRestoreInstanceState(Bundle inState){
//		backStackElement = (GeneralFragment) getFragmentManager().getFragment(inState,"backStackElement");
//		fragment_parent = (GeneralFragment) getFragmentManager().getFragment(inState,"fragment_parent");
//	}
}