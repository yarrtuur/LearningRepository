package ua.com.juja.yar_tur.sqlcmd.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PgSQLPreparator implements SQLPreparator {

	@Override
	public String makeSqlUpdateData(String tableName, DataSet dataSetSet, DataSet dataSetWhere) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("UPDATE public.%s ", tableName));
		sb.append(" SET ");
		for (DataSet.Data step : dataSetSet.getData()) {
			sb.append(String.format(" %s = %s , ", step.getName(), step.getValue()));
			//todo
			/* check columns data type for make right sql query */
		}
		sb.replace(sb.length() - 3, sb.length(), " ");
		sb.append(" WHERE ");
		for (DataSet.Data step : dataSetWhere.getData()) {
			sb.append(String.format(" %s = %s AND ", step.getName(), step.getValue()));
		}
		sb.replace(sb.length() - 4, sb.length(), " ");

		return sb.toString();
	}

	@Override
	public String makeSqlDeleteData(String tableName, DataSet dataSet) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("DELETE from public.%s ", tableName));
		sb.append(" WHERE ");
		for (DataSet.Data step : dataSet.getData()) {
			sb.append(String.format(" %s = \'%s\' AND", step.getName(), step.getValue()));
			//todo
			/* check columns data type for make right sql query */
		}
		sb.replace(sb.length() - 3, sb.length(), " ");
		return sb.toString();
	}

	@Override
	public String makeSqlDropTable(String tableName) {
		return String.format("DROP TABLE IF EXISTS %s CASCADE ", tableName);
	}

	@Override
	public String makeSqlClearTable(String tableName) {
		return String.format("DELETE FROM %s ", tableName);
	}

	@Override
	public String makeSqlFindData(String tableName, boolean isDetails, DataSet dataSet, Map tableFields) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("SELECT * from public.%s ", tableName));
		if (isDetails) {
			sb.append(" WHERE ");
			for (DataSet.Data step : dataSet.getData()) {
				String columnName = step.getName();
				String columnValue = step.getValue().toString();
				sb.append(columnName).append(" = ");
				if (tableFields.get(columnName).toString().startsWith("char") ||
						tableFields.get(columnName).toString().startsWith("varchar") ||
						tableFields.get(columnName).toString().startsWith("text")) {
					sb.append("'").append(columnValue).append("'").append(" AND ");
				} else {
					sb.append(columnValue).append(", ");
				}
			}
			sb.replace(sb.length() - 4, sb.length(), " ");
		}
		return sb.toString();
	}

	@Override
	public String makeSqlGetOneTableDetails(String tableName) {
		return String.format("SELECT c.column_name,c.data_type FROM information_schema.columns c " +
				"WHERE c.table_schema = 'public' AND c.table_name = \'%s\' ", tableName);
	}

	@Override
	public String makeSqlGetTablesList() {
		return "SELECT t.table_name FROM information_schema.tables t " +
				"WHERE t.table_schema = 'public'";
	}

	@Override
	public String makeSqlInsertData(String tableName, DataSet dataSet, Map tableFields) {
		StringBuilder sb = new StringBuilder();
		List<DataSet.Data> dataList = dataSet.getData();
		StringBuilder columns = new StringBuilder();
		StringBuilder values = new StringBuilder();

		sb.append("INSERT INTO ").append(tableName).append(" ( ");
		for (DataSet.Data step : dataList) {
			String columnName = step.getName();
			columns.append(columnName).append(", ");
			if (tableFields.get(columnName).toString().startsWith("char") ||
					tableFields.get(columnName).toString().startsWith("varchar") ||
					tableFields.get(columnName).toString().startsWith("text")) {
				values.append("'").append(step.getValue()).append("'").append(", ");
			} else {
				values.append(step.getValue()).append(", ");
			}
		}
		columns.replace(columns.length() - 2, columns.length(), ")");
		values.replace(values.length() - 2, values.length(), ")");
		sb.append(columns).append(" values ( ").append(values).append(";");
		return sb.toString();
	}

	@Override
	public String makeSqlCreateTable(String tableName, DataSet dataSet) {
		StringBuilder sb = new StringBuilder();
		List<DataSet.Data> dataList = dataSet.getData();
		sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName)
				.append(" ( rid serial CONSTRAINT id_").append(tableName).append("_pk PRIMARY KEY ");
		for (DataSet.Data step : dataList) {
			sb.append(" , ").append(step.getName()).append(" ").append(step.getValue()).append(" ");
		}
		sb.append(");");
		return sb.toString();
	}

	@Override
	public Map<String, String> getColumnsWithDataType(ResultSet resultSet) throws SQLException {
		Map<String, String> columnsMap = new LinkedHashMap<>();
		while (resultSet.next()) {
			columnsMap.put(resultSet.getString("column_name"), resultSet.getString("data_type"));
		}
		return columnsMap;
	}
}
