package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.JdbcBridge;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;
import ua.com.juja.sqlcmd.viewer.Console;
import ua.com.juja.sqlcmd.viewer.View;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DBCommandManager {
    JdbcBridge jdbcBridge = new JdbcBridge();
    View view = new Console();
    PreparedStatement preparedStatement;

    // open PreparedStatement
    public PreparedStatement getPrepareStatement(String sql) {
        preparedStatement = null;
        try {
            preparedStatement = jdbcBridge.getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return preparedStatement;
    }

    // close PrepareStatement
    public void closePrepareStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // connect to DB
    public DBFeedBack connect(String dbSidLine,String login ,String passwd) {

        view.write("-------- PostgreSQL JDBC Connection Testing ------------");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            view.write("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            return DBFeedBack.REFUSE;
        }

        view.write("PostgreSQL JDBC Driver Registered!");

        try {
            jdbcBridge.setConnection(DriverManager.getConnection(
                    dbSidLine, login, passwd));
        } catch (SQLException e) {
            view.write("Connection Failed! Check output console");
            return DBFeedBack.REFUSE;
        }

        if (jdbcBridge.isConnected()) {
            view.write("You made it, take control your database now!");
            return DBFeedBack.OK;
        } else {
            view.write("Failed to make connection!");
        }

        return DBFeedBack.REFUSE;
    }

    public DBFeedBack finder(){



        return DBFeedBack.REFUSE;

    }



}
