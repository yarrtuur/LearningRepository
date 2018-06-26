package ua.com.juja.yar_tur.sqlcmd.utils;

import ua.com.juja.yar_tur.sqlcmd.model.DataSet;

import java.util.Map;

public class DataContainer {
	private String tableName;
	private DataSet dataSetWhere;
	private DataSet dataSetSet;
	private Map<String, String> tableFieldsMap;

	public DataSet getDataSetSet() {
		return dataSetSet;
	}

	public void setDataSetSet(DataSet dataSetSet) {
		this.dataSetSet = dataSetSet;
	}

	public Map<String, String> getTableFieldsMap() {
		return tableFieldsMap;
	}

	public void setTableFieldsMap(Map<String, String> tableFieldsMap) {
		this.tableFieldsMap = tableFieldsMap;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public DataSet getDataSetWhere() {
		return dataSetWhere;
	}

	public void setDataSetWhere(DataSet dataSetWhere) {
		this.dataSetWhere = dataSetWhere;
	}
}
