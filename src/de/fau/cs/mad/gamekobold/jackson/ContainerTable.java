package de.fau.cs.mad.gamekobold.jackson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

import android.util.Log;

@JsonTypeName("folder")
public class ContainerTable extends AbstractTable{
	public List<AbstractTable> subTables;
	
	
	/**
	 * Creates a new empty ContainerTable and adds it to this ContainerTable.
	 * @return The new created ContainerTable.
	 */
	public ContainerTable createAndAddNewContainerTable() {
		if(subTables == null) {
			subTables = new ArrayList<AbstractTable>();
		}
		ContainerTable table = new ContainerTable();
		subTables.add(table);
		return table;
	}
	
	
	/**
	 * Creates a new empty Table and adds it to this ContainerTable.
	 * @return The new created Table.
	 */
	public Table createAndAddNewTable() {
		if(subTables == null) {
			subTables = new ArrayList<AbstractTable>();
		}
		Table table = new Table();
		subTables.add(table);
		return table;
	}
	
	
	/**
	 * Creates a new Table with the given informations and adds it to this ContainerTable.
	 * @return The new created Table.
	 */
	public Table createAndAddNewTable(String name, ColumnHeader[] headers) {
		if(subTables == null) {
			subTables = new ArrayList<AbstractTable>();
		}
		Table table = new Table(name, new ArrayList<ColumnHeader>(Arrays.asList(headers)));
		subTables.add(table);
		return table;
	}
	
	/**
	 * Creates a new empty MatrixTable and adds it to this ContainerTable.
	 * @return The new created MatrixTable.
	 */
	public MatrixTable createAndAddNewMatrixTable() {
		if(subTables == null) {
			subTables = new ArrayList<AbstractTable>();
		}
		MatrixTable table = new MatrixTable();
		subTables.add(table);
		return table;		
	}
			
	// needs to be null pointer safe!
	/**
	 * Removes the given table from this ContainerTable.
	 * @param table The table to remove
	 * @return True if the table has been removed, false if table is null or had not been removed.
	 */
	public boolean removeTable(AbstractTable table) {
		if(table == null) {
			return false;
		}
		if(subTables != null) {
			return subTables.remove(table);
		}
		return false;
	}
	
	/**
	 * Adds the given table to this ContainerTable
	 * @param table The table to add
	 * @return False if the table has already been added, true otherwise.
	 */
	public boolean addTable(AbstractTable table) {
		if(subTables == null) {
			subTables = new ArrayList<AbstractTable>();
		}
		// only add it if it is not already in list
		if(subTables.contains(table)) {
			return false;
		}
		return subTables.add(table);
	}
	
	public void print() {
		Log.d("CONTAINER_TABLE","ContainerTabler Name:" + tableName);
		if(subTables == null){
			return;
		}
		for(int i = 0; i < subTables.size(); i++) {
			subTables.get(i).print();
		}
	}
}
