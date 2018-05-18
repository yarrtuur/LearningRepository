package ua.com.juja.yar_tur.sqlcmd.model;

public interface SQLPreparator {

	String makeSqlUpdateData(String tableName, DataSet dataSetSet, DataSet dataSetWhere);

	String makeSqlDeleteData(String tableName, DataSet dataSet);

	String makeSqlDropTable( String tableName );

	String makeSqlClearTable( String tableName );

	String makeSqlFindData( String tableName, boolean isDetails, DataSet dataSet );

	String makeSqlGetOneTableDetails(String tableName);

	String makeSqlGetTablesList();

	String makeSqlInsertData(String tableName, DataSet dataSet);

	String makeSqlCreateTable(String tableName, DataSet dataSet);

	String makeSqlChkTableByName(String tableName);

}