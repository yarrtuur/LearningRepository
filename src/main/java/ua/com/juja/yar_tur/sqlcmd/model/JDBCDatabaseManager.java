package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.types_enums_except.ExitException;
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

	// open PreparedStatement TODO
	public PreparedStatement getPrepareStatement(String sql) {
		preparedStatement = null;
		try {
			preparedStatement = connectionKeeper.getConnection().prepareStatement(sql);
		} catch (SQLException | NullPointerException e) {
			//e.printStackTrace();
		}
		return preparedStatement;
	}

	public void closePrepareStatement() {
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public FeedBack toExit() throws SQLException {
		if (connectionKeeper.isConnected()) {
			connectionKeeper.close();
			connectionKeeper.setConnection(null);
		} else {
			connectionKeeper.setConnection(null);
		}
		return FeedBack.OK;
	}

	@Override
	public FeedBack toUpdate(String tableName, DataSet dataSetSet, DataSet dataSetWhere) throws SQLException {
		if (this.chkTableByName(tableName).equals(FeedBack.OK) && connectionKeeper.isConnected()) {
			return (getExecuteUpdate(query.makeSqlUpdateData(tableName, dataSetSet, dataSetWhere)) == 1)
					? FeedBack.OK : FeedBack.REFUSE;
		} else {
			return FeedBack.REFUSE;
		}
	}

	@Override
	public FeedBack toDelete(String tableName, DataSet dataSet) throws SQLException {
		if (this.chkTableByName(tableName).equals(FeedBack.OK) && connectionKeeper.isConnected()) {
			return (getExecuteUpdate(query.makeSqlDeleteData(tableName, dataSet)) == 1) ? FeedBack.OK : FeedBack.REFUSE;
		} else {
			return FeedBack.REFUSE;
		}
	}

	@Override
	public FeedBack toDrop(String tableName) throws SQLException {
		if (this.chkTableByName(tableName).equals(FeedBack.OK) && connectionKeeper.isConnected()) {
			return (getExecuteUpdate(query.makeSqlDropTable(tableName)) == 1) ? FeedBack.OK : FeedBack.REFUSE;
		} else {
			return FeedBack.REFUSE;
		}
	}

	@Override
	public FeedBack toClean(String tableName) throws SQLException {
		if (this.chkTableByName(tableName).equals(FeedBack.OK) && connectionKeeper.isConnected()) {
			return (getExecuteUpdate(query.makeSqlClearTable(tableName)) == 1) ? FeedBack.OK : FeedBack.REFUSE;
		} else {
			return FeedBack.REFUSE;
		}
	}

	@Override
	public ResultSet toFind(String tableName, boolean isDetails, DataSet dataSet) throws SQLException {
		if (this.chkTableByName(tableName).equals(FeedBack.OK) && connectionKeeper.isConnected()) {
			return getExecuteQuery(query.makeSqlFindData(tableName, isDetails, dataSet));
		} else {
			throw new ExitException("Something wrong with found table");
		}
	}

	@Override
	public ResultSet toView(String tableName) throws SQLException {
		if (connectionKeeper.isConnected()) {
			return getOneTableDetails(tableName);
		} else {
			throw new ExitException("Something wrong with view table");
		}
	}

	@Override
	public ResultSet toView() throws SQLException {
		if (connectionKeeper.isConnected()) {
			return getTablesList();
		} else {
			throw new ExitException("Something wrong with view table");
		}
	}

	private ResultSet getOneTableDetails(String tableName) throws SQLException {
		return getExecuteQuery(query.makeSqlGetOneTableDetails(tableName));
	}

	private ResultSet getTablesList() throws SQLException {
		return getExecuteQuery(query.makeSqlGetTablesList());
	}

	@Override
	public FeedBack toInsert(String tableName, DataSet dataSet) throws SQLException {
		if (this.chkTableByName(tableName).equals(FeedBack.OK) && connectionKeeper.isConnected()) {
			return (getExecuteUpdate(query.makeSqlInsertData(tableName, dataSet)) == 1) ? FeedBack.OK : FeedBack.REFUSE;
		} else {
			return FeedBack.REFUSE;
		}
	}

	@Override
	public FeedBack toCreate(String tableName, DataSet dataSet) throws SQLException {
		if (this.chkTableByName(tableName).equals(FeedBack.REFUSE) && connectionKeeper.isConnected()) {
			return (getExecuteUpdate(query.makeSqlCreateTable(tableName, dataSet)) == 1) ? FeedBack.OK : FeedBack.REFUSE;
		} else {
			return FeedBack.REFUSE;
		}
	}

	private FeedBack chkTableByName(String tableName) throws SQLException {
		resultSet = getExecuteQuery(query.makeSqlChkTableByName(tableName));
		if (resultSet.next()) {
			closePrepareStatement();
			return FeedBack.OK;
		} else {
			closePrepareStatement();
			return FeedBack.REFUSE;
		}
	}/*todo
    remake for prepare sql`s : to update, to insert
    */

	@Override
	public FeedBack toConnect(String dbSidLine, String login, String passwd) throws SQLException {
		connectionKeeper.setConnection(DriverManager.getConnection(dbSidLine, login, passwd));
		return (connectionKeeper.isConnected()) ? FeedBack.OK : FeedBack.REFUSE;
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
