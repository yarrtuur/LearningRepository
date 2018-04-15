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
import java.util.List;


public class DBCommandManager {

    private JdbcBridge jdbcBridge = new JdbcBridge();
    private View view = new Console();
    private PreparedStatement preparedStatement;
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
    public void closePrepareStatement() {
        if (preparedStatement != null) {
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
        if( this.chkTableByName(tableName).equals(DBFeedBack.OK)){
            return createTableWithParams( makeSqlCreateLine( tableName, dataSet ) );
        }
        return DBFeedBack.REFUSE;
    }

    private String makeSqlCreateLine(String tableName, DataSet dataSet) {
        StringBuilder sb = new StringBuilder();
        List<DataSet.Data> dataList = dataSet.getData();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName)
                .append(" ( rid serial CONSTRAINT id_").append(tableName).append("_pk PRIMARY KEY ");
        for (DataSet.Data step : dataList) {
            sb.append(",").append(step.getName()).append(" ").append(step.getValue()).append(" ");
        }
        sb.append(");");
        return sb.toString();
    }

    private DBFeedBack createTableWithParams(String sql)  {
        view.write("Creating table in given database...");
        try {
            resultSet = getPrepareStatement(sql).executeQuery();
        } catch (SQLException e) {
            view.write("CREATE TABLE Query refused.");
            return DBFeedBack.REFUSE;
        }
        view.write("CREATE TABLE Query returned successfully.");
        closePrepareStatement();
        return DBFeedBack.OK;
    }

    private DBFeedBack chkTableByName(String tableName) {
        String sqlStr = String.format("SELECT 1 FROM information_schema.tables WHERE table_name =  \'%s\'", tableName);
        try {
            resultSet = getPrepareStatement(sqlStr).executeQuery();
        } catch (SQLException e) {
            view.write( String.format( "%s",e.getCause( ) ) );
        }
        try {
            if (resultSet.next()) {
                closePrepareStatement();
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
