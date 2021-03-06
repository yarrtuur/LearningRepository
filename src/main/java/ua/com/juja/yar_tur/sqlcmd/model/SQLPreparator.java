package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.utils.DataContainer;
import ua.com.juja.yar_tur.sqlcmd.utils.DataContainerUpdate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

interface SQLPreparator {

	String makeFindQuery(DataContainer dataContainer);

	String makeDeleteQuery(DataContainer dataContainer);

	String makeInsertQuery(DataContainer dataContainer);

	String makeSqlDropTable( String tableName );

	String makeSqlUpdateData(DataContainerUpdate dataContainerUpdate);

	String makeSqlClearTable( String tableName );

	String makeSqlGetOneTableDetails(String tableName);

	String makeSqlGetTablesList();

	String makeSqlCreateTable(DataContainer dataContainer);

	Map<String, String> getColumnsNamesWithDataType(ResultSet resultSet) throws SQLException;

}