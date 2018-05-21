package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCDatabaseManager implements DBCommandManager {
	private SQLPreparator query = new PgSQLPreparator();
	private ConnectionKeeper connectionKeeper = new ConnectionKeeper();
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	@Override
	public ConnectionKeeper getConnection(){
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
		if (chkTableByName(tableName) != null) {
			return (getExecuteUpdate(query.makeSqlDropTable(tableName)) == 1) ? FeedBack.OK : FeedBack.REFUSE;
		} else {
			return FeedBack.REFUSE;
		}
	}

	@Override
	public FeedBack toClean(String tableName) throws SQLException {
		if (chkTableByName(tableName) != null) {
			return (getExecuteUpdate(query.makeSqlClearTable(tableName)) == 1) ? FeedBack.OK : FeedBack.REFUSE;
		} else {
			return FeedBack.REFUSE;
		}
	}

	@Override
	public ResultSet toView(String tableName) throws SQLException {
		return getOneTableDetails(tableName);
	}

	@Override
	public ResultSet toView() throws SQLException {
		return getTablesList();
	}

	private ResultSet getOneTableDetails(String tableName) throws SQLException {
		return getExecuteQuery(query.makeSqlGetOneTableDetails(tableName));
	}

	private ResultSet getTablesList() throws SQLException {
		return getExecuteQuery(query.makeSqlGetTablesList());
	}

	@Override
	public FeedBack toCreate(String tableName, DataSet dataSet) throws SQLException {
		if (chkTableByName(tableName) == null) {
			return (getExecuteUpdate(query.makeSqlCreateTable(tableName, dataSet)) == 1) ? FeedBack.OK : FeedBack.REFUSE;
		} else {
			return FeedBack.REFUSE;
		}
	}

	@Override
	public FeedBack toConnect(String dbSidLine, String login, String passwd) throws SQLException {
		connectionKeeper.setConnection(DriverManager.getConnection(dbSidLine, login, passwd));
		return (connectionKeeper.isConnected()) ? FeedBack.OK : FeedBack.REFUSE;
	}


	@Override
	public FeedBack toUpdate(String tableName, DataSet dataSetSet, DataSet dataSetWhere) throws SQLException {
		resultSet = chkTableByName(tableName);
			return (getExecuteUpdate(query.makeSqlUpdateData(tableName, dataSetSet, dataSetWhere,
					query.getColumnsWithDataType(resultSet))) == 1)
					? FeedBack.OK : FeedBack.REFUSE;
	}

	@Override
	public FeedBack toDelete(String tableName, DataSet dataSet) throws SQLException {
		resultSet = chkTableByName(tableName);
		return (getExecuteUpdate(query.makeSqlDeleteData(tableName, dataSet,
				query.getColumnsWithDataType(resultSet))) == 1) ? FeedBack.OK : FeedBack.REFUSE;
	}

	@Override
	public ResultSet toFind(String tableName, boolean isDetails, DataSet dataSet) throws SQLException {
		resultSet = chkTableByName(tableName);
		return getExecuteQuery(query.makeSqlFindData(tableName, isDetails, dataSet,
				query.getColumnsWithDataType(resultSet)));
	}

	@Override
	public FeedBack toInsert(String tableName, DataSet dataSet) throws SQLException {
		resultSet = chkTableByName(tableName);
		return (getExecuteUpdate(query.makeSqlInsertData(tableName, dataSet,
				query.getColumnsWithDataType(resultSet))) == 1) ? FeedBack.OK : FeedBack.REFUSE;
	}

	private ResultSet chkTableByName(String tableName) throws SQLException {
		return getExecuteQuery(query.makeSqlGetOneTableDetails(tableName));
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
