package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DBCommandManager {

	//get connection
	ConnectionKeeper getConnection();

    // open PreparedStatement
    PreparedStatement getPrepareStatement(String sql) throws SQLException;

    // close PrepareStatement
    void closePrepareStatement() throws SQLException;

    /**
     * close connection and exit from programm
     * */
    FeedBack toExit() throws SQLException;

    // update table
    FeedBack toUpdate(String tableName, DataSet dataSetSet, DataSet dataSetWhere) throws SQLException;

    // delete data
    FeedBack toDelete(String tableName, DataSet dataSet) throws SQLException;

    // drop table
    FeedBack toDrop(String tableName)throws SQLException;

    // clean table
    FeedBack toClean(String tableName)throws SQLException;

    // find data
    FeedBack toFind(String tableName, boolean isDetails, DataSet dataSet)throws SQLException;

    // view table
    /**
     * to view list of all tables
     * */
    FeedBack toView() throws SQLException;

    /**
     * to view one table with details
     * */
    void toView(String tableName) throws SQLException;

    // insert into  table
    FeedBack toInsert(String tableName, DataSet dataSet)throws SQLException;

    //create table
    void toCreate(String tableName, DataSet dataSet) throws SQLException;

    // connect to DB
    void toConnect(String dbSidLine, String login, String passwd) throws SQLException;
}
