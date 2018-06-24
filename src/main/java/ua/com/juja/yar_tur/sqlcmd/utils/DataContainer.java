package ua.com.juja.yar_tur.sqlcmd.utils;

import ua.com.juja.yar_tur.sqlcmd.model.DataSet;

import java.util.Map;

public class DataContainer {
	private String tableName;
	private DataSet dataSet;
	private Map<String, String> tableFieldsMap;

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

	public DataSet getDataSet() {
		return dataSet;
	}

	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}
}
