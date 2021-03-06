package de.fau.cs.mad.rpgpack.template_generator;

import de.fau.cs.mad.rpgpack.SlideoutNavigationActivity;
import de.fau.cs.mad.rpgpack.jackson.AbstractTable;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

//atm not needed parcelable -> don't know if needed to hold data
//public class DataHolder implements Parcelable{
public class FolderElementData{
	/*
	 * JACKSON START
	 */
	public AbstractTable jacksonTable;
	public boolean jacksonDoSaveOnNextChance = false;
	public TextWatcher nameChangeWatcher = null;
	/*
	 * JACKSON END
	 */
	
	protected TextView text;
	public GeneralFragment childFragment;
	protected element_type type;
	protected boolean checked = true;
	protected boolean favorite = false;
	
	enum element_type{
		table, matrix, folder;
	}
	
	public FolderElementData(Context parent, boolean editable, element_type typeOfElement) {
		type = typeOfElement;
		if(editable){
			Log.d("NOTICE", "editable is TRUE!");
			text = new EditText(SlideoutNavigationActivity.theActiveActivity);
		}
		else{
			text = new TextView(SlideoutNavigationActivity.theActiveActivity);
		}
        // create new TextWatcher for this data element.
        // it is added to the view representing this element
        // if the name is changed by the user we set the data element name and jackson table name
        nameChangeWatcher = new TextWatcher() {			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {
				if(jacksonTable == null) {
					return;
				}
				Log.d("FolderElementData", "oldName:\""+jacksonTable.tableName+"\" newName:\""+s.toString()+"\"");
				if(!jacksonTable.tableName.equals(s.toString())) {
					jacksonTable.tableName = s.toString();
					text.setText(s.toString());
				}
			}
		};
    }

    public element_type getType() {
        return type;
    }
}
