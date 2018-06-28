package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.utils.DataContainer;
import ua.com.juja.yar_tur.sqlcmd.utils.DataContainerUpdate;
import ua.com.juja.yar_tur.sqlcmd.viewer.Printable;

import java.sql.*;
import java.util.Map;

public class JDBCDatabaseManager implements DBCommandManager {
	private SQLPreparator prepareForQuery = new PgSQLPreparator();
	private ConnectionKeeper connectionKeeper = new ConnectionKeeper();
	private Printable printer;
	private PreparedStatement preparedStatement;

	public JDBCDatabaseManager(Printable printer) {
		this.printer = printer;
	}

	@Override
	public ConnectionKeeper getConnection() {
		return connectionKeeper;
	}

	@Override
	public PreparedStatement getPreparedStatement(String sql) throws SQLException {
		return preparedStatement = connectionKeeper.getConnection().prepareStatement(sql);
	}

	@Override
	public void closePreparedStatement() throws SQLException {
		if (preparedStatement != null) {
			preparedStatement.close();
		}
	}

	@Override
	public void toExit() throws SQLException {
		connectionKeeper.close();
		closePreparedStatement();
	}

	@Override
	public int toDrop(String tableName) throws SQLException {
		String queryString = prepareForQuery.makeSqlDropTable(tableName);
		return getExecuteUpdate(queryString);

	}

	@Override
	public int toClean(String tableName) throws SQLException {
		String queryString = prepareForQuery.makeSqlClearTable(tableName);
		return getExecuteUpdate(queryString);
	}

	@Override
	public void toView(String tableName) throws SQLException {
		if (tableName == null) {
			toView();
		} else {
			ResultSet resultSet = getOneTableDetails(tableName);
			printer.printOneTableDetails(resultSet, tableName);
		}
	}

	private ResultSet getOneTableDetails(String tableName) throws SQLException {
		return getExecuteQuery(prepareForQuery.makeSqlGetOneTableDetails(tableName));
	}

	@Override
	public void toView() throws SQLException {
		ResultSet resultSet = getTablesList();
		printer.printTablesList(resultSet);
	}

	private ResultSet getTablesList() throws SQLException {
		return getExecuteQuery(prepareForQuery.makeSqlGetTablesList());
	}

	@Override
	public void toConnect(String dbSidLine, String login, String passwd) throws SQLException {
		Connection connection = DriverManager.getConnection(dbSidLine, login, passwd);
		connectionKeeper.setConnection(connection);
	}

	@Override
	public void toCreate(DataContainer dataContainer) throws SQLException {
		String queryString = prepareForQuery.makeSqlCreateTable(dataContainer);
		getExecuteUpdate(queryString);
	}

	@Override
	public void toFind(DataContainer dataContainer) throws SQLException {
		ResultSet resultSet = getOneTableDetails(dataContainer.getTableName());
		Map<String, String> tableFieldsMap = prepareForQuery.getColumnsNamesWithDataType(resultSet);
		dataContainer.setTableFieldsMap(tableFieldsMap);

		String queryString = prepareForQuery.makeFindQuery(dataContainer);
		resultSet = getExecuteQuery(queryString);
		printer.printFoundData(resultSet);
	}

	@Override
	public int toDelete(DataContainer dataContainer) throws SQLException {
		ResultSet resultSet = getOneTableDetails(dataContainer.getTableName());
		Map<String, String> tableFieldsMap = prepareForQuery.getColumnsNamesWithDataType(resultSet);
		dataContainer.setTableFieldsMap(tableFieldsMap);

		String queryString = prepareForQuery.makeDeleteQuery(dataContainer);
		return getExecuteUpdate(queryString);
	}

	@Override
	public int toInsert(DataContainer dataContainer) throws SQLException {
		ResultSet resultSet = getOneTableDetails(dataContainer.getTableName());
		Map<String, String> tableFieldsMap = prepareForQuery.getColumnsNamesWithDataType(resultSet);
		dataContainer.setTableFieldsMap(tableFieldsMap);

		String queryString = prepareForQuery.makeInsertQuery(dataContainer);
		return getExecuteUpdate(queryString);
	}

	@Override
	public int toUpdate(DataContainerUpdate dataContainerUpdate) throws SQLException {
		ResultSet resultSet = getOneTableDetails(dataContainerUpdate.getTableName());
		Map<String, String> tableFieldsMap = prepareForQuery.getColumnsNamesWithDataType(resultSet);
		dataContainerUpdate.setTableFieldsMap(tableFieldsMap);

		String queryString = prepareForQuery.makeSqlUpdateData(dataContainerUpdate);
		return getExecuteUpdate(queryString);
	}

	private int getExecuteUpdate(String sql) throws SQLException {
		return getPreparedStatement(sql).executeUpdate();
	}

	private ResultSet getExecuteQuery(String sql) throws SQLException {
		return getPreparedStatement(sql).executeQuery();
	}

}
