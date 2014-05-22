package de.fau.cs.mad.gamekobold.template_generator;

import de.fau.cs.mad.gamekobold.*;
import android.content.Context;
import android.widget.ArrayAdapter;

//atm not needed parcelable -> don't know if needed to hold data
//public class DataHolder implements Parcelable{
public class DataHolder{

	private int selected;
    private ArrayAdapter<CharSequence> adapter;

//    private DataHolder(Parcel parcel) {
//        this.selected = parcel.readInt();
//    }
    
	public DataHolder(Context parent) {
        adapter = ArrayAdapter.createFromResource(parent, R.array.choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

    public void setSelected(int selected) {
        this.selected = selected;
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
