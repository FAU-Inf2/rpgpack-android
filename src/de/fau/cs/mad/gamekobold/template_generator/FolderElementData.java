package de.fau.cs.mad.gamekobold.template_generator;

import de.fau.cs.mad.gamekobold.jackson.AbstractTable;
import android.content.Context;
import android.widget.EditText;

//atm not needed parcelable -> don't know if needed to hold data
//public class DataHolder implements Parcelable{
public class FolderElementData{
	/*
	 * JACKSON START
	 */
	public AbstractTable jacksonTable;
	public boolean jacksonDoSaveOnNextChance = false;
	/*
	 * JACKSON END
	 */
	
	protected EditText text;
	protected GeneralFragment childFragment;
	protected element_type type;
	
	enum element_type{
		table, matrix, folder;
	}
	
	public FolderElementData(Context parent, element_type typeOfElement) {
		type = typeOfElement;
        text = new EditText(TemplateGeneratorActivity.theActiveActivity);
    }

    public element_type getType() {
        return type;
    }
    
}
