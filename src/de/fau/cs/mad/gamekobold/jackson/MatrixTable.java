package de.fau.cs.mad.gamekobold.jackson;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MatrixTable extends AbstractTable{
	public List<AbstractColumnEntry> entries;
	
	public MatrixTable() {
		entries = new LinkedList<AbstractColumnEntry>();
	}
	
	@JsonCreator
	public MatrixTable(@JsonProperty("entries") List<AbstractColumnEntry> entries) {
		this.entries = entries;
	}
	
	@Override
	public void print() {
		Log.d("MatrixTable","print");
	}
}
