package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.utils.DataContainer;
import ua.com.juja.yar_tur.sqlcmd.utils.DataContainerUpdate;
import ua.com.juja.yar_tur.sqlcmd.utils.DataSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class PgSQLPreparator implements SQLPreparator {

    @Override
    public String makeFindQuery(DataContainer dataContainer) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("SELECT * from public.%s ", dataContainer.getTableName()));
        gatherCondition(sb, dataContainer);
        return sb.toString();
    }

    @Override
    public String makeDeleteQuery(DataContainer dataContainer) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("DELETE from public.%s ", dataContainer.getTableName()));
        gatherCondition(sb, dataContainer);
        return sb.toString();
    }

    private void ifQuotesNeedWithAnd(StringBuilder sb, String columnValue, String typeColumn) {
        if (typeColumn.startsWith("char") || typeColumn.startsWith("varchar") || typeColumn.startsWith("text")) {
            sb.append("'").append(columnValue).append("'").append(" AND ");
        } else {
            sb.append(columnValue).append(" AND ");
        }
    }

    @Override
	public String makeSqlUpdateData(DataContainerUpdate dataContainerUpdate) {
        StringBuilder sb = new StringBuilder();
		sb.append(String.format("UPDATE public.%s ", dataContainerUpdate.getTableName()));
        sb.append(" SET ");

		gatherConditionSet(sb, dataContainerUpdate);
		gatherCondition(sb, dataContainerUpdate);

        return sb.toString();
    }

    @Override
    public String makeInsertQuery(DataContainer dataContainer) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(dataContainer.getTableName()).append(" ( ");
        gatherInsertConditions(sb, dataContainer);
        return sb.toString();
    }

    private void gatherInsertConditions(StringBuilder sb, DataContainer dataContainer) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (DataSet.Data step : dataContainer.getDataSetWhere().getData()) {
            String columnName = step.getName();
            String columnValue = step.getValue().toString();
            String typeColumn = dataContainer.getTableFieldsMap().get(columnName);
            columns.append(step.getName()).append(", ");
            ifQuotesNeedWithComma(values, columnValue, typeColumn);
        }

        columns.replace(columns.length() - 2, columns.length(), ")");
        values.replace(values.length() - 2, values.length(), ")");
        sb.append(columns).append(" values ( ").append(values).append(";");
    }

	private void gatherConditionSet(StringBuilder sb, DataContainerUpdate dataContainerUpdate) {
		for (DataSet.Data step : dataContainerUpdate.getDataSetSet().getData()) {
            String columnName = step.getName();
            String columnValue = step.getValue().toString();
			String typeColumn = dataContainerUpdate.getTableFieldsMap().get(columnName);
            sb.append(" ").append(columnName).append(" = ");
            ifQuotesNeedWithComma(sb, columnValue, typeColumn);
        }
        sb.replace(sb.length() - 2, sb.length(), " ");
    }

    private void gatherCondition(StringBuilder sb, DataContainer dataContainer) {
        if (dataContainer.getDataSetWhere().getSize() > 0) {
            sb.append(" WHERE ");
            for (DataSet.Data step : dataContainer.getDataSetWhere().getData()) {
                String columnName = step.getName();
                String columnValue = step.getValue().toString();
                String typeColumn = dataContainer.getTableFieldsMap().get(columnName);
                sb.append(" ").append(columnName).append(" = ");
                ifQuotesNeedWithAnd(sb, columnValue, typeColumn);
            }
            sb.replace(sb.length() - 4, sb.length(), " ");
        }
    }

    private void ifQuotesNeedWithComma(StringBuilder sb, String columnValue, String typeColumn) {
        if (typeColumn.startsWith("char") || typeColumn.startsWith("varchar") || typeColumn.startsWith("text")) {
            sb.append("'").append(columnValue).append("'").append(", ");
        } else {
            sb.append(columnValue).append(", ");
        }
    }

    @Override
    public String makeSqlCreateTable(DataContainer dataContainer) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(dataContainer.getTableName())
                .append(" ( rid serial CONSTRAINT id_").append(dataContainer.getTableName()).append("_pk PRIMARY KEY ");
        for (DataSet.Data step : dataContainer.getDataSetWhere().getData()) {
            sb.append(" , ").append(step.getName()).append(" ").append(step.getValue()).append(" ");
        }
        sb.append(");");
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
    public Map<String, String> getColumnsNamesWithDataType(ResultSet resultSet) throws SQLException {
        Map<String, String> columnsMap = new LinkedHashMap<>();
        while (resultSet.next()) {
            columnsMap.put(resultSet.getString("column_name"), resultSet.getString("data_type"));
        }
        return columnsMap;
    }
}
