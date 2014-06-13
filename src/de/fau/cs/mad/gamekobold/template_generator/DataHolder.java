package de.fau.cs.mad.gamekobold.template_generator;

import de.fau.cs.mad.gamekobold.*;
import de.fau.cs.mad.gamekobold.jackson.AbstractTable;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

//atm not needed parcelable -> don't know if needed to hold data
//public class DataHolder implements Parcelable{
public class DataHolder{
	/*
	 * JACKSON START
	 */
	public AbstractTable jacksonTable;
	public boolean jacksonSkipNextSave = false;
	/*
	 * JACKSON END
	 */
	
	protected int selected;
	protected ArrayAdapter<CharSequence> adapter;
	protected EditText text;
	protected EditText invisibleTextField;
	protected Spinner spin;
	protected Activity myContext;
	protected int ID;
	protected FolderFragment childFragment;
	protected TableFragment table;
	
	static int idCounter = 0;
//    private DataHolder(Parcel parcel) {
//        this.selected = parcel.readInt();
//    }
    
	public DataHolder(Context parent) {
		myContext = (Activity) parent;
        adapter = ArrayAdapter.createFromResource(parent, R.array.choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ID = idCounter++;
//        LayoutInflater inflator = myContext.getLayoutInflater();
//        View view = inflator.inflate(R.layout.initialrow, null);
//        text = (EditText) view.findViewById(R.id.text);
//        invisibleTextField = (EditText) view.findViewById(R.id.text2);
//        spin = (Spinner) view.findViewById(R.id.spin);
        
        text = new EditText(myContext);
        invisibleTextField = new EditText(myContext);
        spin = new Spinner(myContext);
        
//        spin.setAdapter(getAdapter());
//        spin.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                setSelected(arg2);
//                if(getSelectedText().equals("Ordner")){
//                	myContext.findViewById(R.id.listView_items);
//                	View row = ((View) arg1.getParent().getParent());
//                	Toast.makeText(myContext, myContext.getResources().getResourceEntryName(((View) arg1.getParent().getParent()).getId()), Toast.LENGTH_LONG).show();
//                	row.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							Toast.makeText(myContext, "ONCLICK!!!", Toast.LENGTH_LONG).show();
//							//next 2 lines old code; now change to another fragment, not to another activity
////							Intent intent = new Intent(myContext, MainTemplateGenerator.class);
////                			myContext.startActivity(intent);
//
//							FragmentTransaction fragmentTransaction = ((MainTemplateGenerator) myContext).getFragmentManager().beginTransaction();
//							TemplateGeneratorFragment newFragment = new TemplateGeneratorFragment();
//							fragmentTransaction.add(R.id.main_view_empty, newFragment);
////							fragmentTransaction.remove(((MainTemplateGenerator) myContext).fragment);
//							TemplateGeneratorFragment oldFragment = ((MainTemplateGenerator) myContext).fragment;
//							fragmentTransaction.hide(oldFragment);
//							fragmentTransaction.commit();
//							//reset references in "Main":
//							//new main fragment = new created one
//							newFragment.fragment_parent = oldFragment;
//							((MainTemplateGenerator) myContext).fragment = newFragment;
//						}
//                	});
//                }
//                else if(arg0.getItemAtPosition(arg2).toString().equals("Text")){
//                	invisibleTextField.setVisibility(View.VISIBLE);
//                	//TODO: whole listview field must get larger if we show this text
//                	//doesn't seem to work with the following line (tried to invalidate listview to request rebuild)
//                	((ListView) arg1.getParent().getParent().getParent()).invalidate();
//                }
//                //if any other spinner item is selected
//                else{
//                	invisibleTextField.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//            }
//
//        });

    }

    public ArrayAdapter<CharSequence> getAdapter() {
        return adapter;
    }
    
    public void setAdapter(ArrayAdapter<CharSequence> newAdapter) {
        adapter = newAdapter;
    }

    public int getSelected() {
        return selected;
    }
    
    public String getSelectedText() {
        return adapter.getItem(selected).toString();
    }
    
    protected void setVisibility(int visibility){
    	invisibleTextField.setVisibility(visibility);
    }
    
    public int isInvisible(){
    	return invisibleTextField.getVisibility();
    }

    public void setSelected(int selectedNow) {
        this.selected = selectedNow;
    }
    
    public void setSelected(String type) {
    	for(int i=0 ; i<adapter.getCount() ; ++i){
    		if(adapter.getItem(i).equals(type)){
    			this.selected = i;
    			break;
    		}
    	}
    }

    //following would be needed if we want this class implementing Parcelable
//	@Override
//	public int describeContents() {
//		// Auto-generated method stub
//		return 0;
//	}

//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		// adapt if really need to parcel class!
//		dest.writeInt(selected);
//	}
	
//	public static final Creator<DataHolder> CREATOR = new Creator<DataHolder>() {
//        @Override
//        public DataHolder createFromParcel(Parcel parcel) {
//            return new DataHolder(parcel);
//        }
//        @Override
//        public DataHolder[] newArray(int size) {
//            return new DataHolder[size];
//        }
//    };
}
