package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.utils.DataContainer;
import ua.com.juja.yar_tur.sqlcmd.utils.ExitException;
import ua.com.juja.yar_tur.sqlcmd.utils.FeedBack;
import ua.com.juja.yar_tur.sqlcmd.viewer.Printable;

import java.sql.*;
import java.util.Map;

public class JDBCDatabaseManager implements DBCommandManager {
	private SQLPreparator prepareForQuery = new PgSQLPreparator();
	private ConnectionKeeper connectionKeeper = new ConnectionKeeper();
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private Printable printer;

	public JDBCDatabaseManager(Printable printer) {
		this.printer = printer;
	}

	@Override
	public ConnectionKeeper getConnection() {
		return connectionKeeper;
	}

	@Override
	public PreparedStatement getPrepareStatement(String sql) throws SQLException {
		preparedStatement = null;
		preparedStatement = connectionKeeper.getConnection().prepareStatement(sql);
		return preparedStatement;
	}

	@Override
	public void closePrepareStatement() throws SQLException {
		if (preparedStatement != null) {
			preparedStatement.close();
		}
	}

	@Override
	public FeedBack toExit() throws SQLException {
		if (connectionKeeper.isConnected()) {
			connectionKeeper.close();
		}
		return FeedBack.OK;
	}

	@Override
	public FeedBack toDrop(String tableName) throws SQLException {
		resultSet = null;
		resultSet = chkTableByName(tableName);
		if (resultSet.next()) {
			int rs = resultSet.getInt(1);
			if (rs == 1) {
				return (getExecuteUpdate(prepareForQuery.makeSqlDropTable(tableName)) == 0) ? FeedBack.OK : FeedBack.REFUSE;
			}
		} else {
			throw new SQLException(String.format("table %s hasn`t exist", tableName));
		}
		return FeedBack.REFUSE;
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
	public int toCreate(DataContainer dataContainer) throws SQLException, ExitException {
		ResultSet resultSet = getOneTableDetails(dataContainer.getTableName());
		String columnFilling = resultSet.getString("table_name");
		if (columnFilling.equals(dataContainer.getTableName())) {
			throw new ExitException(String.format("table %s has already exist", dataContainer.getTableName()));
		} else {
			String queryString = prepareForQuery.makeSqlCreateTable(dataContainer);
			return getExecuteUpdate(queryString);
		}
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
	public int toUpdate(DataContainer dataContainer) throws SQLException {
		ResultSet resultSet = getOneTableDetails(dataContainer.getTableName());
		Map<String, String> tableFieldsMap = prepareForQuery.getColumnsNamesWithDataType(resultSet);
		dataContainer.setTableFieldsMap(tableFieldsMap);

		String queryString = prepareForQuery.makeSqlUpdateData(dataContainer);
		return getExecuteUpdate(queryString);
	}

	private ResultSet chkTableByName(String tableName) throws SQLException {
		return getExecuteQuery(prepareForQuery.makeSqlChkTableAvailable(tableName));
	}

	/**
	 * universal method for exequteUpdate sql all same commands
	 */
	private int getExecuteUpdate(String sql) throws SQLException {
		return getPrepareStatement(sql).executeUpdate();
	}

	/**
	 * universal method for executeQuery sql all same commands
	 */
	private ResultSet getExecuteQuery(String sql) throws SQLException {
		return getPrepareStatement(sql).executeQuery();
	}

}
