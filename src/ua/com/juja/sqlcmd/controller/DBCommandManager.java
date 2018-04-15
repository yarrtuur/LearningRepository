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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


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
    private PreparedStatement getPrepareStatement(String sql) {
        preparedStatement = null;
        try {
            preparedStatement = jdbcBridge.getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return preparedStatement;
    }

    // close PrepareStatement
    private void closePrepareStatement() {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // view table
    public DBFeedBack toView(boolean isDetails, boolean isOne) {
        if (isDetails && isOne) {
            printOneTableDetails();
        } else if( isDetails && !isOne ){
            printTablesDetails();
        } else {
            printTableList();
        }
        return null;
    }

    private void printOneTableDetails() throws SQLException {
        for (String step : tblList) {
            columnString = new StringBuilder();
            PreparedStatement preparedStatement = jdbcBridge.getConnection().prepareStatement(makeSqlLine(step));
            stmtResultSet = preparedStatement.executeQuery();
            while (stmtResultSet.next()) {
                columnString.append(" ").append(stmtResultSet.getString("column_name")).append(",");
            }
            preparedStatement.close();
            view.write(String.format("Table: %s , Columns: %s", step,
                    columnString.replace(columnString.length() - 1, columnString.length(), " ").toString()));
        }
    }

    private int getTablesList(String sql) throws SQLException {
        PreparedStatement preparedStatement = jdbcBridge.getConnection().prepareStatement(sql);
        stmtResultSet = preparedStatement.executeQuery();
        tblList = new LinkedList<>();
        while (stmtResultSet.next()) {
            tblList.add(stmtResultSet.getString("table_name"));
        }
        preparedStatement.close();
        return tblList.size();
    }

    // insert into  table
    public DBFeedBack toInsert(String tableName, DataSet dataSet){
        if( this.chkTableByName(tableName).equals(DBFeedBack.OK)){
            return insertIntoTable( makeSqlInsertData( tableName, dataSet ) );
        }
        return DBFeedBack.REFUSE;
    }

    private DBFeedBack insertIntoTable(String sql) {
        view.write("Inserting data into table ...");
        try {
            resultSet = getPrepareStatement(sql).executeQuery();
        } catch (SQLException e) {
            view.write("INSERT INTO TABLE Query refused.");
            return DBFeedBack.REFUSE;
        }
        view.write("INSERT INTO TABLE Query returned successfully.");
        closePrepareStatement();
        return DBFeedBack.OK;
    }

    private String makeSqlInsertData(String tableName, DataSet dataSet) {
        StringBuilder sb = new StringBuilder();
        List<DataSet.Data> dataList = dataSet.getData();
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        sb.append("INSERT INTO ").append(tableName).append(" ( ");
        for ( DataSet.Data step : dataList ) {
            columns.append(step.getName()).append(", ");
            values.append(step.getValue()).append(", ");
        }
        columns.replace(columns.length() - 1, columns.length(), ")");
        values.replace(values.length() - 1, values.length(), ")");
        sb.append(columns).append(" values ( ").append(values).append(";");

        return sb.toString();
    }

    //create table
    public DBFeedBack toCreate(String tableName, DataSet dataSet){
        if( this.chkTableByName(tableName).equals(DBFeedBack.OK)){
            return createTableWithParams( makeSqlCreateTable( tableName, dataSet ) );
        }
        return DBFeedBack.REFUSE;
    }

    private String makeSqlCreateTable(String tableName, DataSet dataSet) {
        StringBuilder sb = new StringBuilder();
        List<DataSet.Data> dataList = dataSet.getData();

        sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName)
                .append(" ( rid serial CONSTRAINT id_").append(tableName).append("_pk PRIMARY KEY ");

        for ( DataSet.Data step : dataList ) {
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

}
