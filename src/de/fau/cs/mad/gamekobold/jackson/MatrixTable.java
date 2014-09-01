package de.fau.cs.mad.gamekobold.jackson;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.SlideoutNavigationActivity;
import de.fau.cs.mad.gamekobold.matrix.MatrixItem;

@JsonTypeName("matrix")
public class MatrixTable extends AbstractTable{
	@JsonIgnore
	public List<MatrixItem> entries;

	public MatrixTable() {
		entries = new LinkedList<MatrixItem>();
	}

	@JsonCreator
	public MatrixTable(@JsonProperty("entries") List<MatrixItem> entries) {
		this.entries = entries;
	}

	public List<MatrixItem> getEntries() {
		return entries;
	}

	/**
	 * DO NOT USE! Used for jackson serialization. Excludes the "add new item" entry
	 * @return
	 */
	@JsonProperty("entries")
	@Deprecated
	private List<MatrixItem> getEntriesForJackson() {
		if(entries.isEmpty()) {
			return entries;
		}
		if (entries.get(entries.size() - 1).getItemName().equals(
				SlideoutNavigationActivity.theActiveActivity.getResources().getString(R.string.new_matrix_item))) {
			return entries.subList(0, entries.size()-1);	
		}
		return entries;
	}
}
