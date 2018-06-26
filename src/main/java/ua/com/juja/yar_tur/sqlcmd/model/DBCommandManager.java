package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.utils.DataContainer;
import ua.com.juja.yar_tur.sqlcmd.utils.FeedBack;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DBCommandManager {

	ConnectionKeeper getConnection();

    PreparedStatement getPrepareStatement(String sql) throws SQLException;

    void closePrepareStatement() throws SQLException;

    FeedBack toExit() throws SQLException;

    void toFind(DataContainer dataContainer) throws SQLException;

    int toDelete(DataContainer dataContainer) throws SQLException;

    int toInsert(DataContainer dataContainer) throws SQLException;

    FeedBack toDrop(String tableName)throws SQLException;

    FeedBack toClean(String tableName)throws SQLException;

    FeedBack toUpdate(String tableName, DataSet dataSetSet, DataSet dataSetWhere) throws SQLException;

    void toView() throws SQLException;

    void toView(String tableName) throws SQLException;

    void toCreate(String tableName, DataSet dataSet) throws SQLException;

    void toConnect(String dbSidLine, String login, String passwd) throws SQLException;
}
