package ua.com.juja.yar_tur.sqlcmd.model;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * this class hold a connection with DB
 */
public class ConnectionKeeper {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isConnected() {
        return this.connection != null;
    }

    public void close() throws SQLException {
        if(connection != null)
        connection.close();
    }
}
