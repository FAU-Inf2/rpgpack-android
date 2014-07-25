package de.fau.cs.mad.gamekobold.jackson;

import java.util.ArrayList;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Row {
	public ArrayList<AbstractColumnEntry> entries;

	@JsonCreator
	public Row(@JsonProperty("entries") ArrayList<AbstractColumnEntry> entries) {
		this.entries = entries;
	}

	public Row() {
		entries = new ArrayList<AbstractColumnEntry>();
	}

	public void print() {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < entries.size(); i++) {
			builder.append(entries.get(i));
			if(i < entries.size()-1) {
				builder.append(" | ");
			}
		}
		Log.d("ROW-print",builder.toString());
	}

	/**
	 * Adds a new column to this row. This should only be called from the Table class.
	 * @param header The column information
	 */
	public void addColumn(ColumnHeader header) {
	 	if(header.isString()) {
	 		entries.add(new StringClass());
	 	}
	 	else if(header.isCheckBox()) {
	 		entries.add(new CheckBoxClass());
	 	}
	 	else if(header.isPopup()) {
	 		entries.add(new PopupClass());
	 	}
	 }

	/**
	 * Removes the last column from this row. This should only be called from the Table class.
	 */
	 public void removeColumn() {
	 	if(!entries.isEmpty()) {
	 		entries.remove(entries.size()-1);
	 	}
	 }

//	 /**
//	  * Adds a column with string content.
//	  * @param value The string to be used.
//	  */
//	 public void addStringColumn(String value) {
//		 entries.add(new StringClass(value));
//	 }
//	 
//	 /**
//	  * Adds a column with checkbox content.
//	  * @param value The value to be used.
//	  */
//	 public void addCheckBoxColumn(boolean value) {
//		 entries.add(new CheckBoxClass(value));
//	 }
//	 
//	 /**
//	  * Adds a column with popup content.
//	  * @param content The content for the popup.
//	  */
//	 public void addPopupColumn(String content) {
//		 entries.add(new PopupClass(content));
//	 }
}
