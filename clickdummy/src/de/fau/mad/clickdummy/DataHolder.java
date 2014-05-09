package de.fau.mad.clickdummy;

import com.example.kobold.R;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

public class DataHolder implements Parcelable{
	private int selected;
    private ArrayAdapter<CharSequence> adapter;

    private DataHolder(Parcel parcel) {
        this.selected = parcel.readInt();
//        adapter = ArrayAdapter.createFromResource(parent, R.array.choices, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
    
    public DataHolder(Context parent) {
        adapter = ArrayAdapter.createFromResource(parent, R.array.choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public ArrayAdapter<CharSequence> getAdapter() {
        return adapter;
    }

    public String getText() {
        return (String) adapter.getItem(selected);
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO does that work?
		dest.writeInt(selected);
		int amount = adapter.getCount();
		dest.writeInt(amount);
		for(int i=0; i<amount; i++){
			dest.writeString(adapter.getItem(i).toString());
		}
		
	}
	
	public static final Creator<DataHolder> CREATOR = new Creator<DataHolder>() {

        // And here you create a new instance from a parcel using the first constructor
        @Override
        public DataHolder createFromParcel(Parcel parcel) {
            return new DataHolder(parcel);
        }

        @Override
        public DataHolder[] newArray(int size) {
            return new DataHolder[size];
        }

    };
}
