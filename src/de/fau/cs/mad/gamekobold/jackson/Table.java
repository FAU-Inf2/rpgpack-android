package de.fau.cs.mad.gamekobold.jackson;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class Table extends AbstractTable{
	public List<Row> rows;
	public int numberOfColumns;
	//public String[] columnNames;
	//public String[] columnTypes;
	public ArrayList<ColumnHeader> columnHeaders;
	//public ColumnHeader[] columnHeaders;
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
	
	/*public Table(String name, ColumnHeader[] headers){
		tableName = name;
		numberOfColumns = headers.length;
		columnHeaders = headers;
		writeOnly = false;
	}*/
	
	/*public Table(int numberOfCol, String[] types, String[] names) {
		numberOfColumns = numberOfCol;
		columnNames = names;
		columnTypes = types;
		hiddenColumns = new boolean[numberOfColumns];
	}*/
	
	public void print() {
		StringBuilder builder = new StringBuilder();
		builder.append("Table Name:"+tableName+"\n");
		for(int i = 0; i < numberOfColumns; i++) {
		//	builder.append(columnNames[i]+"("+columnTypes[i]+")");
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
	
	public Row createAndAddNewRow() {
		if(rows == null) {
			rows = new ArrayList<Row>();
		}
		//TODO numberOfColumns == columnHeaders.size()
		//Row ret = new Row(numberOfColumns);
		Row ret = new Row();
		rows.add(ret);
		return ret;
	}
	
	
	 public void addColumn(ColumnHeader header) {
		columnHeaders.add(header);
	 	if(rows != null) {
	 		for(final Row row : rows) {
	 			row.addColumn(header);
			}
		}
		numberOfColumns++;
	 }
	 
	 // TODO choose column id
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
	 	}
	 }
	 
	 /**
	  * 
	  * @param index index of column
	  * @param title new title
	  * @return true if title changed, false otherwise
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
	 
	/*public Table createAndAddNewTable() {
		return createAndAddNewTable(0, null, null);
	}
	
	public Table createAndAddNewTable(int numberOfColumns, String[] columnTypes, String[] nameOfColums) {
		if(rows == null) {
			rows = new ArrayList<AbstractRow>();
		}
		TableRow ret = new TableRow();
		ret.table = new Table(numberOfColumns, columnTypes, nameOfColums);
		rows.add(ret);
		return ret.table;		
	}*/
}
