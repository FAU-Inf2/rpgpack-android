package de.fau.cs.mad.gamekobold.template_generator;

import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.jackson.AbstractTable;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

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
	
	protected EditText text;
	public GeneralFragment childFragment;
	protected element_type type;
	
	enum element_type{
		table, matrix, folder;
	}
	
	public FolderElementData(Context parent, element_type typeOfElement) {
		type = typeOfElement;
        text = new EditText(SlideoutNavigationActivity.theActiveActivity);
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
					//TemplateGeneratorActivity.saveTemplateAsync();
				}
			}
		};
    }

    public element_type getType() {
        return type;
    }
}
