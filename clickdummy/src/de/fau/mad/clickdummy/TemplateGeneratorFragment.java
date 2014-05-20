package de.fau.mad.clickdummy;

import java.util.ArrayList;

import com.example.kobold.R;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class TemplateGeneratorFragment extends Fragment {
	
	View view;
	ListView lView;
	protected ArrayList<DataHolder> allData;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
        view = inflater.inflate(R.layout.initialrow, null);
        RelativeLayout pasteLayout=(RelativeLayout)view.findViewById(R.id.template_generated_element);
//        pasteLayout.setVisibility(RelativeLayout.GONE);
        Context context = getActivity();
        lView = (ListView) view.findViewById(R.id.listView_items);
        allData = new ArrayList<DataHolder>();
        DataAdapter dataAdapter = new DataAdapter(getActivity(), R.layout.initialrow, allData.toArray(new DataHolder[allData.size()]), allData);
        
        //setUpView
        EditText textIn = (EditText)getView().findViewById(R.id.textin);
        Button buttonAdd = (Button)getView().findViewById(R.id.add);
        ListView lvItem = (ListView)getView().findViewById(R.id.listView_items);
        if(lvItem == null){
        	System.exit(0);
        }
//        ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>();
//        lvItem.setAdapter(itemAdapter);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	((MainActivity)getActivity()).addItemList();
            }
        });
        
        lView.setAdapter(dataAdapter);
        return view;
    }
}
