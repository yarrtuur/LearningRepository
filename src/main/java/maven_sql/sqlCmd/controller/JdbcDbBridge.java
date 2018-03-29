package maven_sql.sqlCmd.controller;

import java.sql.Connection;

public class JdbcDbBridge {
    private Connection connection ;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean isConnected(){
        return this.connection != null;
    }
}
