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
		table.parentTable = this;
		subTables.add(table);
		return table;
	}
		
	public Table createAndAddNewTable() {
		if(subTables == null) {
			subTables = new ArrayList<AbstractTable>();
		}
		Table table = new Table();
		table.parentTable = this;
		subTables.add(table);
		return table;
	}
	
	public Table createAndAddNewTable(String name, ColumnHeader[] headers) {
		if(subTables == null) {
			subTables = new ArrayList<AbstractTable>();
		}
		Table table = new Table(name, headers);
		table.parentTable = this;
		subTables.add(table);
		return table;
	}
	
	// not nec needed
	public ContainerTable replaceTableWithContainerTable(AbstractTable table) {
		if(removeTable(table)) {
			return createAndAddNewContainerTable();
		}
		return null;
	}
	
	public Table replaceTableWithTable(AbstractTable table) {
		if(removeTable(table)) {
			return createAndAddNewTable();
		}
		return null;
	}
	
	public boolean removeTable(AbstractTable table) {
		if(subTables != null) {
			if(subTables.remove(table)) {
				table.parentTable = null;
				return true;
			}
		}
		return false;
	}
	
	public boolean addTalbe(AbstractTable table) {
		if(table.parentTable != null) {
			return false;
		}
		if(subTables == null) {
			subTables = new ArrayList<AbstractTable>();
		}
		if(subTables.add(table)) {
			table.parentTable = this;
			return true;
		}
		return false;
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
