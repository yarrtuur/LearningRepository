package ua.com.juja.sqlcmd.model;

import java.sql.Connection;

/**
 * this class hold a connection with DB
 */
public class JdbcBridge {
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
}
