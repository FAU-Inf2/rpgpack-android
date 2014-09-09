package de.fau.cs.mad.gamekobold.jackson;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.matrix.MatrixItem;

@JsonTypeName("matrix")
/**
 * You have to set the appContext field!
 *
 */
public class MatrixTable extends AbstractTable{
	// we need a static app context here for accessing the app's resources.
	// you have to set it manually. preferably when starting the app.
	public static Context appContext = null;
	@JsonIgnore
	public List<MatrixItem> entries;

	public MatrixTable() {
		entries = new LinkedList<MatrixItem>();
	}

	public List<MatrixItem> getEntries() {
		return entries;
	}
	
	@JsonProperty("entries")
	public void setEntries(List<MatrixItem> entries) {
		this.entries = entries;
	}
	
	/**
	 * DO NOT USE! Used for jackson serialization. Excludes the "add new item" entry
	 * @return
	 */
	@JsonProperty("entries")
	private List<MatrixItem> getEntriesForJackson() {
		if(entries.isEmpty()) {
			return entries;
		}
		// check if the last entry is the fake "new matrix item". if so we exclude it.
		if(appContext == null) {
			return entries;
		}
		if (entries.get(entries.size() - 1).getItemName().equals(
				appContext.getResources().getString(R.string.new_matrix_item))) {
			return entries.subList(0, entries.size()-1);	
		}
		return entries;
	}
}
