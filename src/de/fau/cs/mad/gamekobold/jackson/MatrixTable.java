package de.fau.cs.mad.gamekobold.jackson;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import de.fau.cs.mad.gamekobold.matrix.MatrixItem;

@JsonTypeName("matrix")
public class MatrixTable extends AbstractTable{
	public List<MatrixItem> entries;
	
	public MatrixTable() {
		entries = new LinkedList<MatrixItem>();
	}
	
	@JsonCreator
	public MatrixTable(@JsonProperty("entries") List<MatrixItem> entries) {
		this.entries = entries;
	}
	
	/**
	 * Used for jackson serialization. Excludes the "add new item" entry
	 * @return
	 */
	@JsonProperty("entries")
	public List<MatrixItem> getEntries() {
		// need this check if a matrix table is created but never visited.
		if(entries.isEmpty()) {
			return entries;
		}
		return entries.subList(0, entries.size()-1);
	}
	
	@Override
	public void print() {
		Log.d("MatrixTable","print");
	}
}
