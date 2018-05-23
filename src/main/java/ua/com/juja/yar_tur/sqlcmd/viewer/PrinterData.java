package ua.com.juja.yar_tur.sqlcmd.viewer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PrinterData implements Printable {
	private View view;

	public PrinterData(View view) {
		this.view = view;
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
		if (resultSet != null) {
			while (resultSet.next()) {
				columnString.append(" ").append(resultSet.getString("column_name")).append(":")
						.append(resultSet.getString("data_type")).append(",");
			}
		}
		String columnView = columnString.replace(columnString.length() - 1, columnString.length(), " ").toString();
		view.write(String.format("Table: %s , Columns: %s", tableName, columnView));
	}
}
