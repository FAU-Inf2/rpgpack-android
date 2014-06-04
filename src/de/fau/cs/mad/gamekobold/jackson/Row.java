package de.fau.cs.mad.gamekobold.jackson;

import android.util.Log;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Row /*extends AbstractRow*/{	
	//public ArrayList<AbstractColumnEntry> entries;
	public AbstractColumnEntry[] entries;
	
	@JsonCreator
	public Row(@JsonProperty("entries") AbstractColumnEntry[] entries){
		this.entries = entries;
	}
	
	public Row(int numberOfColumns) {
		entries = new AbstractColumnEntry[numberOfColumns];
	}
	
	public void print() {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < entries.length; i++) {
			builder.append(entries[i]);
			if(i < entries.length-1) {
				builder.append(" | ");
			}
		}
		Log.d("ROW-print",builder.toString());
	}
	
	/*
	 public void addColumn(ColumnHeader header) {
	 	if(header.isInt()) {
	 		entries.add(new IntegerClass());
	 	}
	 	else if(header.isString()) {
	 		entries.add(new StringClass());
	 	}
	 }
	 
	 public void removeColumn() {
	 	if(!entries.isEmpty()) {
	 		entries.remove(entries.size()-1);
	 	}
	 }
	 */
}
