package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.utils.DataContainer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PgSQLPreparator implements SQLPreparator {

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
	public String makeSqlUpdateData(String tableName, DataSet dataSetSet, DataSet dataSetWhere, Map tableFields) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("UPDATE public.%s ", tableName));
		sb.append(" SET ");
		for (DataSet.Data step : dataSetSet.getData()) {
			chkQuoterUrgency(tableFields, sb, step);
			sb.append(" , ");
		}
		sb.replace(sb.length() - 3, sb.length(), " ");
		sb.append(" WHERE ");
		for (DataSet.Data step : dataSetWhere.getData()) {
			chkQuoterUrgency(tableFields, sb, step);
			sb.append(" AND ");
		}
		sb.replace(sb.length() - 4, sb.length(), " ");

		return sb.toString();
	}

	@Override
	public String makeDeleteQuery(DataContainer dataContainer) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("DELETE from public.%s ", dataContainer.getTableName()));
		gatherDeleteString(sb, dataContainer);
		return sb.toString();
	}

	private void gatherDeleteString(StringBuilder sb, DataContainer dataContainer) {
		if (dataContainer.getDataSet().getSize() > 0) {
			sb.append(" WHERE ");
			for (DataSet.Data step : dataContainer.getDataSet().getData()) {
				String columnName = step.getName();
				String columnValue = step.getValue().toString();
				String typeColumn = dataContainer.getTableFieldsMap().get(columnName);
				sb.append(" ").append(columnName).append(" = ");
				ifQuotesNeed(sb, columnValue, typeColumn);
			}
			sb.replace(sb.length() - 4, sb.length(), " ");
		}
	}

	private void ifQuotesNeed(StringBuilder sb, String columnValue, String typeColumn) {
		if (typeColumn.startsWith("char") ||
				typeColumn.startsWith("varchar") ||
				typeColumn.startsWith("text")) {
			sb.append("'").append(columnValue).append("'").append(" AND ");
		} else {
			sb.append(columnValue).append(" AND ");
		}
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
				chkQuoterUrgency(tableFields, sb, step);
				sb.append(" AND ");
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
        return "SELECT table_name FROM information_schema.tables t " +
                "WHERE t.table_schema = 'public'" +
                "ORDER BY 1";
	}

	@Override
	public String makeSqlChkTableAvailable(String tableName) {
		return String.format("SELECT 1 FROM information_schema.tables t " +
				"WHERE t.table_schema = 'public' AND t.table_name = \'%s\' ", tableName);
	}

	@Override
	public String makeSqlInsertData(String tableName, DataSet dataSet, Map tableFields) {
		StringBuilder sb = new StringBuilder();
		List<DataSet.Data> dataList = dataSet.getData();
		StringBuilder columns = new StringBuilder();
		StringBuilder values = new StringBuilder();

		sb.append("INSERT INTO ").append(tableName).append(" ( ");
		for (DataSet.Data step : dataList) {
			chkQuoterUrgency(tableFields, columns, values, step);
			values.append(", ");
		}
		columns.replace(columns.length() - 2, columns.length(), ")");
		values.replace(values.length() - 2, values.length(), ")");
		sb.append(columns).append(" values ( ").append(values).append(";");
		return sb.toString();
	}

	private void chkQuoterUrgency(Map tableFields, StringBuilder sb, DataSet.Data step) {
		String columnName = step.getName();
		String columnValue = step.getValue().toString();
		sb.append(" ").append(columnName).append(" = ");
		if (tableFields.get(columnName).toString().startsWith("char") ||
				tableFields.get(columnName).toString().startsWith("varchar") ||
				tableFields.get(columnName).toString().startsWith("text")) {
			sb.append("'").append(columnValue).append("'");
		} else {
			sb.append(columnValue);
		}
	}

	private void chkQuoterUrgency(Map tableFields, StringBuilder columns, StringBuilder values, DataSet.Data step) {
		String columnName = step.getName();
		String columnValue = step.getValue().toString();
		columns.append(columnName).append(", ");
		if (tableFields.get(columnName).toString().startsWith("char") ||
				tableFields.get(columnName).toString().startsWith("varchar") ||
				tableFields.get(columnName).toString().startsWith("text")) {
			values.append("'").append(columnValue).append("'");
		} else {
			values.append(columnValue);
		}
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
