package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.utils.DataContainer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
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
        if (typeColumn.startsWith("char") ||
                typeColumn.startsWith("varchar") ||
                typeColumn.startsWith("text")) {
            sb.append("'").append(columnValue).append("'").append(" AND ");
        } else {
            sb.append(columnValue).append(" AND ");
        }
    }

    @Override
    public String makeSqlUpdateData(DataContainer dataContainer) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("UPDATE public.%s ", dataContainer.getTableName()));
        sb.append(" SET ");

        gatherConditionSet(sb, dataContainer);
        gatherCondition(sb, dataContainer);

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

    private void gatherConditionSet(StringBuilder sb, DataContainer dataContainer) {
        for (DataSet.Data step : dataContainer.getDataSetSet().getData()) {
            String columnName = step.getName();
            String columnValue = step.getValue().toString();
            String typeColumn = dataContainer.getTableFieldsMap().get(columnName);
            sb.append(" ").append(columnName).append(" = ");
            ifQuotesNeedWithComma(sb, columnValue, typeColumn);
            sb.append(" , ");
        }
        sb.replace(sb.length() - 3, sb.length(), " ");
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
        if (typeColumn.startsWith("char") ||
                typeColumn.startsWith("varchar") ||
                typeColumn.startsWith("text")) {
            sb.append("'").append(columnValue).append("'").append(", ");
        } else {
            sb.append(columnValue).append(", ");
        }
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
    public String makeSqlChkTableAvailable(String tableName) {
        return String.format("SELECT 1 FROM information_schema.tables t " +
                "WHERE t.table_schema = 'public' AND t.table_name = \'%s\' ", tableName);
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
    public Map<String, String> getColumnsNamesWithDataType(ResultSet resultSet) throws SQLException {
        Map<String, String> columnsMap = new LinkedHashMap<>();
        while (resultSet.next()) {
            columnsMap.put(resultSet.getString("column_name"), resultSet.getString("data_type"));
        }
        return columnsMap;
    }
}
