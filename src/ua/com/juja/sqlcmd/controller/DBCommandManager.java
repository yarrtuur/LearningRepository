package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.JdbcBridge;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;
import ua.com.juja.sqlcmd.viewer.Console;
import ua.com.juja.sqlcmd.viewer.View;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBCommandManager {

    private JdbcBridge jdbcBridge = new JdbcBridge();
    private View view = new Console();
    private PreparedStatement preparedStatement;
    private String tableName;
    private ResultSet resultSet;

    public View getView() {
        return view;
    }

    public JdbcBridge getJdbcBridge() {
        return jdbcBridge;
    }

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
    public DBFeedBack toConnect(String dbSidLine,String login ,String passwd) {

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

    public DBFeedBack toCreate(String tableName, DataSet dataSet){
        if( this.chkTableByName().equals(DBFeedBack.OK){

            return createTableWithParams(sql);
        }
        return DBFeedBack.REFUSE;
    }

    public String makeSqlCreateLine(DataSet dataSet) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName)
                .append(" ( rid serial CONSTRAINT id_").append(tableName).append("_pk PRIMARY KEY ");
        for (DataSet step : dataSet) {
            sb.append(",").append(aListColumnData).append(" varchar(200) ");
        }
        sb.append(");");
        return sb.toString();
    }

    private DBFeedBack createTableWithParams(String sql) throws SQLException {
        view.write("Creating table in given database...");
        preparedStatement = jdbcBridge.getConnection().prepareStatement(sql);
        stmtResult = preparedStatement.executeUpdate();
        view.write("CREATE TABLE Query returned successfully");
        preparedStatement.close();
        return DBFeedBack.OK;
    }

    private DBFeedBack chkTableByName() {
        String sqlStr = String.format("SELECT 1 FROM information_schema.tables WHERE table_name =  \'%s\'", tableName);
        try {
            resultSet = getPrepareStatement(sqlStr).executeQuery();
        } catch (SQLException e) {
            view.write( String.format( "%s",e.getCause( ) ) );
        }
        try {
            if (resultSet.next()) {
                closePrepareStatement(preparedStatement);
                return DBFeedBack.REFUSE;
            } else {
                return DBFeedBack.OK;
            }
        } catch (SQLException e) {
            view.write( String.format( "%s",e.getCause( ) ) );
        }
        return DBFeedBack.REFUSE;
    }



}
