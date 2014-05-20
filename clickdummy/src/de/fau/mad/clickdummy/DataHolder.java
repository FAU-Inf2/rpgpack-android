package de.fau.mad.clickdummy;

import com.example.kobold.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

@SuppressLint("ValidFragment")
public class DataHolder extends Fragment implements Parcelable{
	private int selected;
    private ArrayAdapter<CharSequence> adapter;
//	private EditText editText;
//    private Context myContext;
	private int id;
	static int countID = 0;

    private DataHolder(Parcel parcel) {
        this.selected = parcel.readInt();
//        adapter = ArrayAdapter.createFromResource(parent, R.array.choices, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
    
    //XXX: ok to just declare it valid?
    @SuppressLint("ValidFragment")
	public DataHolder(Context parent) {
    	id = countID++;
//    	setRetainInstance(true);
        adapter = ArrayAdapter.createFromResource(parent, R.array.choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//    	Activity activity = (Activity) parent;
//    	LayoutInflater inflator = activity.getLayoutInflater();
//    	View view = inflator.inflate(R.layout.initialrow, null);
//    	editText = (EditText) view.findViewById(R.id.text);
//        this.myContext = parent;
    }

    public ArrayAdapter<CharSequence> getAdapter() {
        return adapter;
    }
    
    public void setAdapter(ArrayAdapter<CharSequence> newAdapter) {
        adapter = newAdapter;
    }

    public int getID(){
    	return id;
    }
    
    public String getText() {
//        return editText.getText().toString();
    	return "aa";
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
//    	editText.setText(type);
    	
//    	int index = 0;
//    	for(String oneType: myContext.getResources().getStringArray(R.array.choices)){
//    		Log.d("PIEP","AAAA: " + oneType);
//    		if(oneType.equals(type)){
//    			this.selected = index;
//    			return;
//    		}
//    		index++;
//    	}
//    	Log.d("PIEP","critical error in DataHolder.setSelected(String " + type +  " ), " + type + " does not exist!");
//    	System.exit(0);
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
//		int amount = adapter.getCount();
//		dest.writeInt(amount);
//		for(int i=0; i<amount; i++){
//			//dest.writeString(adapter.getItem(i).toString());
//			TextUtils.writeToParcel(adapter.getItem(i), dest, 0);
//		}
//		dest.writeList(val);
		
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
