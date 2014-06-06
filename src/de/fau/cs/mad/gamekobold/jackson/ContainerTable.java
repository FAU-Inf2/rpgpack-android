package de.fau.cs.mad.gamekobold.jackson;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;


public class ContainerTable extends AbstractTable{
	public List<AbstractTable> subTables;
	
	public ContainerTable createAndAddNewContainerTable() {
		if(subTables == null) {
			subTables = new ArrayList<AbstractTable>();
		}
		ContainerTable table = new ContainerTable();
		subTables.add(table);
		return table;
	}
		
	public Table createAndAddNewTable() {
		if(subTables == null) {
			subTables = new ArrayList<AbstractTable>();
		}
		Table table = new Table();
		subTables.add(table);
		return table;
	}
	
	public Table createAndAddNewTable(String name, ColumnHeader[] headers) {
		if(subTables == null) {
			subTables = new ArrayList<AbstractTable>();
		}
		Table table = new Table(name, headers);
		subTables.add(table);
		return table;
	}
	
	// not nec needed
	public ContainerTable replaceAndCreateNewContainerTable(AbstractTable table) {
		if(removeTable(table)) {
			return createAndAddNewContainerTable();
		}
		return null;
	}
	
	public Table replaceAndCreateNewTable(AbstractTable table) {
		if(removeTable(table)) {
			return createAndAddNewTable();
		}
		return null;
	}
		
	// needs to be null pointer safe!
	public boolean removeTable(AbstractTable table) {
		if(table == null) {
			return false;
		}
		if(subTables != null) {
			return subTables.remove(table);
		}
		return false;
	}
	
	public boolean addTable(AbstractTable table) {
		if(subTables == null) {
			subTables = new ArrayList<AbstractTable>();
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
