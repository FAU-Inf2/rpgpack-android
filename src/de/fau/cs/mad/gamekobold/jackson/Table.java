package de.fau.cs.mad.gamekobold.jackson;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import android.util.Log;

public class Table extends AbstractTable{
	public List<Row> rows;
	public int numberOfColumns;
	public ArrayList<ColumnHeader> columnHeaders;
	public boolean writeOnly;
	
	public Table() {
		tableName = "";
		columnHeaders = new ArrayList<ColumnHeader>();
		numberOfColumns = 0;
		writeOnly = false;
	}
	
	public Table(String name, ArrayList<ColumnHeader> headers){
		tableName = name;
		numberOfColumns = headers.size();
		columnHeaders = headers;
		writeOnly = false;
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
		if(rows == null) {
			return;
		}
		for(int i = 0; i < rows.size(); i++) {
			rows.get(i).print();
		}
	}

	/**
	 * Creates a new row and adds it to the table.
	 * @return The newly created row.
	 */
	public Row addNewRow() {
		if(rows == null) {
			rows = new ArrayList<Row>();
		}
		Row ret = new Row();
		// add cells
		for(final ColumnHeader header : columnHeaders) {
			ret.addColumn(header);
		}
		rows.add(ret);
		return ret;
	}

	/**
	 * Removes the last row.
	 */
	public void removeRow() {
		if(rows != null) {
			rows.remove(rows.size()-1);
		}		
	}

	/**
	 * Removes the row at given index.
	 * @param index Index for the row.
	 */
	public void removeRow(int index) {
		Log.d("TABLE", "removeRow:"+index);
		if(rows != null) {
			if(index >= 0 && index < rows.size()) {
				rows.remove(index);	
			}
		}		
	}
	
	/**
	 * Appends a new column to the table and every row.
	 * @param header The ColumnHeader with the information for the new Column.
	 */
	 public void addColumn(ColumnHeader header) {
		columnHeaders.add(header);
	 	if(rows != null) {
	 		for(final Row row : rows) {
	 			row.addColumn(header);
			}
		}
		numberOfColumns++;
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
	 		if(rows != null) {
	 			for(final Row row : rows) {
	 				row.removeColumn();
	 			}
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
		if(index >= numberOfColumns) {
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
		 if(rows == null) {
			 return 0;
		 }
		 return rows.size();
	 }
	 
	 public AbstractColumnEntry getEntry(int columnIndex, int rowIndex) {
		 if(columnIndex < 0 || columnIndex >= numberOfColumns) {
			 return null;
		 }
		 if(rowIndex < 0 || rowIndex >= getRowCount()) {
			 return null;
		 }
		 return rows.get(rowIndex).entries.get(columnIndex);
	 }
//	 /**
//	  * Changes the type of the column identified by index.
//	  * All old values of the rows will be deleted and replaced with default values.
//	  * @param index
//	  * @param newType
//	  */
//	 public void changeColumnType(int index, ColumnHeader newType) {
//		 if(index < 0 || index >= numberOfColumns || newType == null) {
//			 return;
//		 }
//		 columnHeaders.set(index, newType);
//		 AbstractColumnEntry newEntry;
//		 if(newType.isCheckBox()) {
//			 newEntry = new CheckBoxClass();
//		 }
//		 else if(newType.isPopup()) {
//			 newEntry = new PopupClass();
//		 }
//		 else {
//			 newEntry = new StringClass();
//		 }
//		 for(final Row row : rows) {
//			 
//			 row.entries.set(index, newEntry);
//		 }
//	 }
}
