package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;
import ua.com.juja.yar_tur.sqlcmd.viewer.Printable;

import java.sql.*;

public class JDBCDatabaseManager implements DBCommandManager {
	private SQLPreparator query = new PgSQLPreparator();
	private ConnectionKeeper connectionKeeper = new ConnectionKeeper();
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	private ResultSetMetaData resultSetMeta;
	private Printable printer;

	public JDBCDatabaseManager(Printable printer) {
		this.printer = printer;
	}

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
		resultSet = null;
		resultSet = chkTableByName(tableName);
		if(resultSet.next()) {
			int rs = resultSet.getInt(1);
			if (rs == 1) {
				return (getExecuteUpdate(query.makeSqlDropTable(tableName)) == 0) ? FeedBack.OK : FeedBack.REFUSE;
			}
		} else {
			throw new SQLException(String.format("table %s hasn`t exist", tableName));
		}
		return FeedBack.REFUSE;
	}

	@Override
	public FeedBack toClean(String tableName) throws SQLException {
		resultSet = chkTableByName(tableName);
		if ( resultSet != null) {
			return (getExecuteUpdate(query.makeSqlClearTable(tableName)) == 1) ? FeedBack.OK : FeedBack.REFUSE;
		} else {
			return FeedBack.REFUSE;
		}
	}

	@Override
	public FeedBack toView(String tableName) throws SQLException {
		resultSet = getOneTableDetails(tableName);
		if(resultSet != null){
			printer.printOneTableDetails(resultSet, tableName);
			closePrepareStatement();
			return FeedBack.OK;
		}else {
			closePrepareStatement();
			return FeedBack.REFUSE;
		}
	}

	@Override
	public FeedBack toView() throws SQLException {
		resultSet = getTablesList();
		if(resultSet != null){
			printer.printTablesList(resultSet);
			closePrepareStatement();
			return FeedBack.OK;
		}else {
			closePrepareStatement();
			return FeedBack.REFUSE;
		}
	}

	private ResultSet getOneTableDetails(String tableName) throws SQLException {
		return getExecuteQuery(query.makeSqlGetOneTableDetails(tableName));
	}

	private ResultSet getTablesList() throws SQLException {
		return getExecuteQuery(query.makeSqlGetTablesList());
	}

	@Override
	public FeedBack toCreate(String tableName, DataSet dataSet) throws SQLException {
		resultSet = null;
		resultSet = chkTableByName(tableName);
		if(resultSet.next()) {
			int rs = resultSet.getInt(1);
			if (rs == 1) {
				throw new SQLException(String.format("table %s has already exist", tableName));
			}
		} else {
			return (getExecuteUpdate(query.makeSqlCreateTable(tableName, dataSet)) == 0) ? FeedBack.OK : FeedBack.REFUSE;
		}
		return FeedBack.REFUSE;
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
		resultSet = null;
		resultSet = chkTableByName(tableName);
		if(resultSet.next()) {
			int rs = resultSet.getInt(1);
			if (rs == 1) {
				resultSet = getOneTableDetails(tableName);
				return (getExecuteUpdate(query.makeSqlDeleteData(tableName, dataSet,
				query.getColumnsWithDataType(resultSet))) > 0) ? FeedBack.OK : FeedBack.REFUSE;
			}
		}
		return FeedBack.REFUSE;
	}

	@Override
	public FeedBack toFind(String tableName, boolean isDetails, DataSet dataSet) throws SQLException {
		resultSet = null;
		resultSet = chkTableByName(tableName);
		if(resultSet.next()) {
			int rs = resultSet.getInt(1);
			if (rs == 1) {
				resultSet = getExecuteQuery(query.makeSqlFindData(tableName, isDetails, dataSet,
						query.getColumnsWithDataType(resultSet)));
				if (resultSet != null) {
					printer.printFoundData(resultSet);
					return FeedBack.OK;
				}
			}
		}
		return FeedBack.REFUSE;
	}

	@Override
	public FeedBack toInsert(String tableName, DataSet dataSet) throws SQLException {
		resultSet = null;
		resultSet = chkTableByName(tableName);
		if(resultSet.next()) {
			int rs = resultSet.getInt(1);
			if (rs == 1) {
				resultSet = getOneTableDetails(tableName);
				return (getExecuteUpdate(query.makeSqlInsertData(tableName, dataSet,
						query.getColumnsWithDataType(resultSet))) == 1) ? FeedBack.OK : FeedBack.REFUSE;
			}
		}else{
			throw new SQLException(String.format("table %s hasn`t exist", tableName));
		}
		return FeedBack.REFUSE;
	}

	private ResultSet chkTableByName(String tableName) throws SQLException {
		return getExecuteQuery(query.makeSqlChkTableAvailable(tableName));
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
