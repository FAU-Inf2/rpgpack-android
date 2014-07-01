package de.fau.cs.mad.gamekobold.jackson;

import java.util.ArrayList;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Row /*extends AbstractRow*/{	
	public ArrayList<AbstractColumnEntry> entries;
	
	@JsonCreator
	public Row(@JsonProperty("entries") ArrayList<AbstractColumnEntry> entries){
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
	
	public void addColumn(ColumnHeader header) {
	 	if(header.isInt()) {
	 		entries.add(new IntegerClass(0));
	 	}
	 	else if(header.isString()) {
	 		entries.add(new StringClass(""));
	 	}
	 }
	 
	 public void removeColumn() {
	 	if(!entries.isEmpty()) {
	 		entries.remove(entries.size()-1);
	 	}
	 } 
}
