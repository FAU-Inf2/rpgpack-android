package de.fau.cs.mad.gamekobold.jackson;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Row extends SelectAndFavorableItem {
	private ArrayList<AbstractColumnEntry> entries;

	public Row(ArrayList<AbstractColumnEntry> entries) {
		this.entries = entries;
	}

	public Row() {
		entries = new ArrayList<AbstractColumnEntry>();
		isSelected = true;
	}

	/**
	 * 
	 * @param index Index of the entry
	 * @return The entry at index index
	 */
	public IEditableContent getEntry(int index) {
		if(index < 0 || index >= entries.size()) {
			return null;
		}
		return entries.get(index);
	}
	/**
	 * Used to save to json by jackson library.
	 * @return
	 */
	public List<String> getEntries() {
		List<String> ret = new LinkedList<String>();
		for(final AbstractColumnEntry entry : entries) {
			ret.add(entry.toString());
		}
		return ret;
	}

	/**
	 * Inflates this row with the given data.
	 * @param headers
	 * @param entries
	 */
	public void inflate(ArrayList<ColumnHeader> headers, ArrayList<String> entries) {
		// add all columns
		for(int i = 0; i < headers.size(); i++) {
			addColumn(headers.get(i), entries.get(i));
		}
	}

	/**
	 * Adds a new column to this row. This should only be called from the Table class.
	 * @param header The column information
	 * @param value The value for the new column
	 */
	public void addColumn(ColumnHeader header, String value) {
	 	if(header.isString()) {
	 		entries.add(new StringClass(value));
	 	}
	 	else if(header.isCheckBox()) {
	 		entries.add(new CheckBoxClass(value));
	 	}
	 	else if(header.isPopup()) {
	 		entries.add(new PopupClass(value));
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

	 /**
	  * 
	  * @param index Index for the Entry.
	  * @param newEntry the new entry to be set.
	  */
	 public void setColumnValue(int index, AbstractColumnEntry newEntry) {
		 if(index < 0 || index >= entries.size() || newEntry == null) {
			 return;
		 }
		 entries.set(index, newEntry);
	 }
}
