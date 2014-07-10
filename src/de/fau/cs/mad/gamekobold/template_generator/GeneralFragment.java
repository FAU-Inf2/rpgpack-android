package de.fau.cs.mad.gamekobold.template_generator;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class GeneralFragment extends Fragment {

	protected GeneralFragment fragment_parent = null;
	protected ArrayList<TableFragment> tables; //tables that are below this fragment
	protected GeneralFragment backStackElement = null;
	protected boolean isATopFragment = false;


	String elementName;
	
	public GeneralFragment() {
		super();
        setRetainInstance(true);
	}
	
	protected abstract void addItemList();

	public abstract void showDialog();
	
	@Override
	public void onAttach(Activity activity) {
		((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment = this;
		super.onAttach(activity);
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
		((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).currentFragment = this;
		if(savedInstanceState != null){
			backStackElement = (GeneralFragment) getFragmentManager().getFragment(savedInstanceState,"backStackElement");
			fragment_parent = (GeneralFragment) getFragmentManager().getFragment(savedInstanceState,"fragment_parent");
		}
		GeneralFragment searchedTopFragment = this;
		while(!searchedTopFragment.isATopFragment){
			searchedTopFragment = searchedTopFragment.fragment_parent;
		}
		((TemplateGeneratorActivity) TemplateGeneratorActivity.theActiveActivity).topFragment = searchedTopFragment;
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
//	@Override
//	public void onRestoreInstanceState(Bundle inState){
//		backStackElement = (GeneralFragment) getFragmentManager().getFragment(inState,"backStackElement");
//		fragment_parent = (GeneralFragment) getFragmentManager().getFragment(inState,"fragment_parent");
//	}
}