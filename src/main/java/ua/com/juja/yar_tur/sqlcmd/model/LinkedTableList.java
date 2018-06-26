package ua.com.juja.yar_tur.sqlcmd.model;

import java.util.Iterator;

class LinkedTableList implements Iterator {
	//todo
	private TableNode currentTableNode;

	public boolean add(){
		TableNode insertNewTableNode = new TableNode();

		return false;
	}

	@Override
	public boolean hasNext() {
		return currentTableNode != null;
	}

	@Override
	public Object next() {
		TableNode tableNodeForReturn;
		tableNodeForReturn = currentTableNode;
		currentTableNode = currentTableNode.getNextTableNode();
		return tableNodeForReturn;
	}

	@Override
	public void remove() {

	}


}

class TableNode {
	private TableNode nextTableNode;
	private String tableName;
	private MetaDataSet dataSet;

	public String getTableName(){
		return this.tableName;
	}

	public void setTableName(String tableName){
		this.tableName = tableName;
	}

	public TableNode getNextTableNode(){
		return this.nextTableNode;
	}

}

class MetaDataSet {
	private String columnName;
	private String data;
	private String dataType;
	private String columnRange;
}