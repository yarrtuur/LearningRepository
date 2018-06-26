package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.utils.DataContainer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

interface SQLPreparator {

	String makeSqlUpdateData(String tableName, DataSet dataSetSet, DataSet dataSetWhere, Map tableFields);

	String makeDeleteQuery(DataContainer dataContainer);

	String makeSqlDropTable( String tableName );

	String makeSqlClearTable( String tableName );

	String makeFindQuery(DataContainer dataContainer);

	String makeSqlGetOneTableDetails(String tableName);

	String makeSqlChkTableAvailable(String tableName);

	String makeSqlGetTablesList();

	String makeSqlInsertData(String tableName, DataSet dataSet, Map tableFields);

	String makeSqlCreateTable(String tableName, DataSet dataSet);

	Map<String, String> getColumnsNamesWithDataType(ResultSet resultSet) throws SQLException;

}