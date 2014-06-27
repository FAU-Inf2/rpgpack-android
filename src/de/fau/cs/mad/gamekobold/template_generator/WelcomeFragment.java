package de.fau.cs.mad.gamekobold.template_generator;

import de.fau.cs.mad.gamekobold.R;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class WelcomeFragment extends GeneralFragment{

	public WelcomeFragment() {
		super();
        setRetainInstance(true);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
		View view = (CustomDrawerLayout) inflater.inflate(R.layout.activity_template_generator_welcome, null);
        
        return view;
    }

	@Override
	protected void addItemList() {
		
	}

	@Override
	public void showDialog() {
		
	}
}
