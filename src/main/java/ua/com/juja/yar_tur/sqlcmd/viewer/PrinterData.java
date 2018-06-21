package ua.com.juja.yar_tur.sqlcmd.viewer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PrinterData implements Printable {
	private View view;

	public PrinterData(View view) {
		this.view = view;
	}

	@Override
	public void printFoundData(ResultSet resultSet) throws SQLException {
		ResultSetMetaData resultSetMeta = resultSet.getMetaData();
		List<String> columnsList = new LinkedList<>();
		StringBuilder sb;
		for (int i = 1; i <= resultSetMeta.getColumnCount(); i++) {
			columnsList.add(resultSetMeta.getColumnName(i));
		}
		sb = new StringBuilder();
		sb.append(" | ");
		for (String aColumnsList : columnsList) {
			sb.append(aColumnsList).append(" | ");
		}
		view.write( sb.toString() );
		while (resultSet.next()) {
			sb = new StringBuilder();
			sb.append(" | ");
			for (String aColumnsList : columnsList) {
				sb.append(resultSet.getString(aColumnsList)).append(" | ");
			}
			view.write(sb.toString());
		}
	}

	@Override
	public void printTablesList(ResultSet resultSet) throws SQLException {
		List<String> tblList = new LinkedList<>();
		while (resultSet.next()) {
			tblList.add(resultSet.getString("table_name"));
		}
		StringBuilder tableString = new StringBuilder();
		for (String step : tblList) {
			tableString.append(" ").append(step).append(",");
		}
		view.write(String.format("Tables: %s ",
				tableString.replace(tableString.length() - 1, tableString.length(), " ").toString()));
	}

	@Override
	public void printOneTableDetails(ResultSet resultSet, String tableName) throws SQLException {
		StringBuilder columnString = new StringBuilder();
		while (resultSet.next()) {
			columnString.append(" ").append(resultSet.getString("column_name")).append(":")
					.append(resultSet.getString("data_type")).append(",");
		}
		String columnView = columnString.replace(columnString.length() - 1, columnString.length(), " ").toString();
		view.write(String.format("Table: %s , Columns: %s", tableName, columnView));
	}
}
