package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.utils.DataContainer;
import ua.com.juja.yar_tur.sqlcmd.utils.DataContainerUpdate;
import ua.com.juja.yar_tur.sqlcmd.utils.ExitException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface DBCommandManager {

	ConnectionKeeper getConnection();

	PreparedStatement getPreparedStatement(String sql) throws SQLException;

	void closePreparedStatement() throws SQLException;

	void toExit() throws SQLException;

    void toFind(DataContainer dataContainer) throws SQLException;

    int toDelete(DataContainer dataContainer) throws SQLException;

    int toInsert(DataContainer dataContainer) throws SQLException;

    int toDrop(String tableName) throws SQLException;

    int toClean(String tableName) throws SQLException;

	int toUpdate(DataContainerUpdate dataContainerUpdate) throws SQLException;

    void toView() throws SQLException;

    void toView(String tableName) throws SQLException;

	void toCreate(DataContainer dataContainer) throws SQLException, ExitException;

    void toConnect(String dbSidLine, String login, String passwd) throws SQLException;
}
