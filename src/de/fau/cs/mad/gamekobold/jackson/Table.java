package de.fau.cs.mad.gamekobold.jackson;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import android.util.Log;

@JsonTypeName("table")
public class Table extends AbstractTable{
	private List<Row> rows;
	private int numberOfColumns;

	private ArrayList<ColumnHeader> columnHeaders;
	
	@JsonIgnore
	public boolean writeOnly;

	public Table() {
		tableName = "";
		columnHeaders = new ArrayList<ColumnHeader>();
		numberOfColumns = 0;
		writeOnly = false;
		rows = new ArrayList<Row>();
	}
	
	public Table(String name, ArrayList<ColumnHeader> headers){
		tableName = name;
		numberOfColumns = headers.size();
		columnHeaders = headers;
		writeOnly = false;
		rows = new ArrayList<Row>();
	}
	
	@JsonCreator
	public Table(@JsonProperty("name") String name,
				@JsonProperty("columns") ArrayList<ColumnHeader> headers,
				@JsonProperty("rows") ArrayList<ArrayList<String>> loadedRows,
				@JsonProperty("selection") ArrayList<Boolean> selection,
				@JsonProperty("favlist") ArrayList<Integer> favlist) {
		tableName = name;
		numberOfColumns = headers.size();
		columnHeaders = headers;
		writeOnly = false;
		rows = new ArrayList<Row>();
		// parse rows
		for(int i = 0; i < loadedRows.size(); ++i) {
			final ArrayList<String> rawRow = loadedRows.get(i);
			// create new row
			Row newRow = new Row();
			// inflate
			newRow.inflate(headers, rawRow);
			if(selection != null) {
				newRow.setSelected(selection.get(i));
				if(favlist.get(i).intValue() == 0) {
					newRow.setFavorite(false);	
				}
				else {
					newRow.setFavorite(true);
				}
			}
			// add
			rows.add(newRow);
		}
	}
	
	/**
	 * Used for saving to json by jackson library.
	 * @return
	 */
	@JsonProperty("rows")
	public List<List<String>> getRows() {
		List<List<String>> ret = new LinkedList<List<String>>();
		for(final Row row : rows) {
			ret.add(row.getEntries());
		}
		return ret;
	}
	
	/**
	 * Used for saving to json by jackson library.
	 * @return
	 */
	@JsonProperty("selection")
	public List<Boolean> getSelection() {
		List<Boolean> ret = new LinkedList<Boolean>();
		for(final Row row : rows) {
			ret.add(row.isSelected());
		}
		return ret;
	}
	
	@JsonProperty("favlist")
	private List<Integer> getFavList() {
		List<Integer> ret = new LinkedList<Integer>();
		for(final Row row : rows) {
			if(row.isFavorite()) {
				ret.add(1);
			}
			else {
				ret.add(0);
			}
		}				
		return ret;
	}
	
	public Row getRow(int index) {
		if(index < 0 || index >= rows.size()) {
			return null;
		}
		return rows.get(index);
	}
	
	@JsonProperty(value="columns")
	public ArrayList<ColumnHeader> getColumnHeaders() {
		return columnHeaders;
	}
	
	public ColumnHeader getColumnHeader(int index) {
		if(index < 0 || index >= columnHeaders.size()) {
			return null;
		}
		return columnHeaders.get(index);
	}
	
	@JsonIgnore
	public int getNumberOfColumns() {
		return numberOfColumns;
	}
	
	/**
	 * Returns the number of selected rows.
	 * @return Number of selected rows.
	 */
	@JsonIgnore
	public int getNumberOfSelectedRows() {
		int counter = 0;
		for(final Row row : rows) {
			if(row.isSelected()) {
				counter++;
			}
		}
		return counter;
	}
	
	/**
	 * Returns a list containing only the currently selected rows.
	 * @return A List containing only the selected rows.
	 */
	@JsonIgnore
	public List<Row> getOnlySelectedRows() {
		LinkedList<Row> selRows = new LinkedList<Row>();
		for(final Row row : rows) {
			if(row.isSelected()) {
				selRows.add(row);
			}
		}
		return selRows;
	}
	
	public void print() {
		StringBuilder builder = new StringBuilder();
		builder.append("Table Name:"+tableName+"\n");
		for(int i = 0; i < numberOfColumns; i++) {
			builder.append(columnHeaders.get(i).name+"("+columnHeaders.get(i).type+")");
			if(columnHeaders.get(i).hidden) {
				builder.append("[hidden]");
			}
			if( i < numberOfColumns-1) {
				builder.append(" | ");
			}
		}
		Log.d("TABLE-print", builder.toString());
		for(int i = 0; i < rows.size(); i++) {
			rows.get(i).print();
		}
	}

	/**
	 * Creates a new row and adds it to the table.
	 * @return The newly created row.
	 */
	public Row addNewRow() {
		Row ret = new Row();
		// add cells
		for(final ColumnHeader header : columnHeaders) {
			ret.addColumn(header, "");
		}
		rows.add(ret);
		return ret;
	}

	/**
	 * Removes the last row.
	 */
	public void removeRow() {
		if(!rows.isEmpty()) {
			rows.remove(rows.size()-1);
		}
	}

	/**
	 * Removes the row at given index.
	 * @param index Index for the row.
	 */
	public void removeRow(int index) {
		Log.d("TABLE", "removeRow:"+index);
		if(index >= 0 && index < rows.size()) {
			rows.remove(index);
		}
	}
	
	/**
	 * Appends a new column to the table and every row.
	 * @param header The ColumnHeader with the information for the new Column.
	 */
	 public void addColumn(ColumnHeader header) {
		 // add header
		 columnHeaders.add(header);
		 // add column to every row
		 for(final Row row : rows) {
	 		row.addColumn(header, "");
		 }
		 // inc column count
		 numberOfColumns++;
		 // log
		 Log.d("JACKSON_TABLE", "added column");
	 }
	 
	 /**
	  * Appends a new column to the table and every row with the default type.
	  */
	 public void addColumn() {
		addColumn(new ColumnHeader("", StringClass.TYPE_STRING));
	 }

	 /**
	  * Removes the last Column from every row and the table.
	  */
	 public void removeColumn() {
	 	if(!columnHeaders.isEmpty()) {
	 		columnHeaders.remove(columnHeaders.size()-1);
	 		//rows
	 		for(final Row row : rows) {
	 			row.removeColumn();
	 		}
	 		numberOfColumns--;
			Log.d("JACKSON_TABLE", "removed column");
	 	}
	 }
	 
	 /**
	  * Sets the title for the given column at the given index.
	  * @param index Index of column.
	  * @param title New title.
	  * @return True if title changed, false otherwise.
	  */
	 public boolean setColumnTitle(int index, String title) {
		if(index >= numberOfColumns || index < 0) {
			return false;
	 	}
	 	ColumnHeader header = columnHeaders.get(index);
		Log.d("Table-setColumnTitle", "index:"+index);
		Log.d("Table-setColumnTitle", "old:"+header.name);
		Log.d("Table-setColumnTitle", "new:"+title);
	 	if(!header.name.equals(title)) {
		 	header.name = title;
		 	return true;
		}
	 	return false;
	 }
	 
	 @JsonIgnore
	 public int getRowCount() {
		 return rows.size();
	 }
	 
	 public IEditableContent getEntry(int columnIndex, int rowIndex) {
		 if(columnIndex < 0 || columnIndex >= numberOfColumns) {
			 return null;
		 }
		 if(rowIndex < 0 || rowIndex >= getRowCount()) {
			 return null;
		 }
		 return rows.get(rowIndex).getEntry(columnIndex);
	 }

	 /**
	  * Changes the type of the column identified by index.
	  * All old values of the rows will be deleted and replaced with default values.
	  * Only Changes the type if it is a new one.
	  * @param index
	  * @param newType
	  */
	 public void changeColumnType(int index, final String newType) {
		 if(index < 0 || index >= numberOfColumns || newType == null) {
			 return;
		 }
		 // change header
		 final ColumnHeader oldHeader = columnHeaders.get(index);
		 // check if old type == new type
		 if(oldHeader.type.equals(newType)) {
			 return;
		 }
		 final ColumnHeader newHeader = new ColumnHeader(oldHeader.name, newType, oldHeader.hidden);
		 columnHeaders.set(index, newHeader);
		 // create new entry according to type
		 // checkbox
		 if(newHeader.isCheckBox()) {
			 // set new type for all rows
			 for(final Row row : rows) {
				 row.setColumnValue(index, new CheckBoxClass());
			 }
		 }
		 // Popup
		 else if(newHeader.isPopup()) {
			 // set new type for all rows 
			 for(final Row row : rows) {
				 row.setColumnValue(index, new PopupClass());
			 }
		 }
		 // Text
		 else {
			 // set new type for all rows
			 for(final Row row : rows) {
				 row.setColumnValue(index, new StringClass());
			 }
		 }
	 }
}
