package ua.com.juja.yar_tur.sqlcmd.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public interface SQLPreparator {

	String makeSqlUpdateData(String tableName, DataSet dataSetSet, DataSet dataSetWhere, Map tableFields);

	String makeSqlDeleteData(String tableName, DataSet dataSet, Map tableFields);

	String makeSqlDropTable( String tableName );

	String makeSqlClearTable( String tableName );

	String makeSqlFindData( String tableName, boolean isDetails, DataSet dataSet, Map tableFields);

	String makeSqlGetOneTableDetails(String tableName);

	String makeSqlChkTableAvailable(String tableName);

	String makeSqlGetTablesList();

	String makeSqlInsertData(String tableName, DataSet dataSet, Map tableFields);

	String makeSqlCreateTable(String tableName, DataSet dataSet);

	Map<String, String> getColumnsWithDataType(ResultSet resultSet) throws SQLException;

}