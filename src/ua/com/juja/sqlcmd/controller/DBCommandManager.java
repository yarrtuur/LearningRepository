package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.model.JdbcBridge;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;
import ua.com.juja.sqlcmd.types_enums.EnumCmdsList;
import ua.com.juja.sqlcmd.viewer.Console;
import ua.com.juja.sqlcmd.viewer.View;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DBCommandManager {

    private JdbcBridge jdbcBridge = new JdbcBridge();
    private View view = new Console();
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private ResultSetMetaData resultSetMeta;

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
            preparedStatement = getJdbcBridge ().getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            //e.printStackTrace();
        }catch (NullPointerException ex){
            view.write("PrepareStatement not resolved...");
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

    // update table
    public DBFeedBack toUpdate(String tableName, DataSet dataSet) {
        if( this.chkTableByName(tableName).equals(DBFeedBack.OK)) {
            return getUpdateData(makeSqlUpdateData(tableName, dataSet));
        }else {
            return DBFeedBack.REFUSE;
        }
    }

    private DBFeedBack getUpdateData(String sql) {
        view.write("Updating data in database...");
        try {
            resultSet = getPrepareStatement(sql).executeQuery();
            view.write("Update data successfully");
            closePrepareStatement();
            return DBFeedBack.OK;
        } catch (SQLException ex) {
            view.write("Update data is interrupted...");
            closePrepareStatement();
            return DBFeedBack.REFUSE;
        }catch (NullPointerException ex){
            view.write("PrepareStatement not resolved...");
            return DBFeedBack.REFUSE;
        }
    }

    private String makeSqlUpdateData(String tableName, DataSet dataSet) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("UPDATE public.%s ", tableName));
        sb.append ( " SET " );
        for(DataSet.Data step : dataSet.getData () ){
            sb.append ( String.format ( " %s = %s , ", step.getName(), step.getValue() ) );
        }
        sb.replace(sb.length() - 3, sb.length(), " ");
        return sb.toString();
    }

    // delete data
    public DBFeedBack toDelete(String tableName, DataSet dataSet) {
        if( this.chkTableByName(tableName).equals(DBFeedBack.OK)) {
            return getDeleteData(makeSqlDeleteData(tableName, dataSet));
        }
        return DBFeedBack.REFUSE;
    }

    private DBFeedBack getDeleteData(String sql) {
        try {
            resultSet = getPrepareStatement(sql).executeQuery();
            view.write("Operation successfull.");
            closePrepareStatement();
            return DBFeedBack.OK;
        } catch (SQLException e) {
            view.write("Delete data interrupted.");
            closePrepareStatement();
            return DBFeedBack.REFUSE;
        }
    }

    private String makeSqlDeleteData(String tableName, DataSet dataSet) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("DELETE from public.%s ", tableName));

        sb.append(" WHERE ");
        for( DataSet.Data step : dataSet.getData() ){
            sb.append(String.format(" %s = \'%s\' AND", step.getName(), step.getValue() ) );
        }
        sb.replace(sb.length() - 3, sb.length(), " ");

        return sb.toString();
    }

    //help
    public DBFeedBack toHelp() {
        view.write ( "List of commands:" );
        for (EnumCmdsList enumCmdsList : EnumCmdsList.values()) {
            view.write ( String.format ( " %s : %s", enumCmdsList, enumCmdsList.getDescription () ) );
        }
        return DBFeedBack.OK;
    }

    // exit
    public DBFeedBack toExit() {
        view.write ( "Closing connection..." );
        if( jdbcBridge.isConnected () ){
            try {
                if( jdbcBridge.getConnection () != null) {
                    jdbcBridge.getConnection ().close ();
                }
                view.write ( "Connection closed." ); //TODO
            } catch (SQLException e) {
                view.write ( "Close connection interrupted.." );
                return DBFeedBack.REFUSE;
            }
            jdbcBridge.setConnection ( null );
        }else{
            view.write ( "Connection closed." );
        }

        return DBFeedBack.OK;
    }

    // drop table
    public DBFeedBack toDrop(String tableName) {
        if( this.chkTableByName(tableName).equals(DBFeedBack.OK)) {
            return getDropTable(makeSqlDropTable( tableName ));
        }
        return DBFeedBack.OK;
    }

    private DBFeedBack getDropTable( String sql ){
        int resultCode = -1;
        view.write("Droping table from database...");

        try {
            resultCode = getPrepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            view.write("Drop table is interrupted...");
            closePrepareStatement();
        }

        if( resultCode == 0 ) {
            view.write("Drop table successfully");
            closePrepareStatement();
            return DBFeedBack.OK;
        } else {
            view.write("Something wrong with drop table...");
            closePrepareStatement();
            return DBFeedBack.REFUSE;
        }
    }

    private String makeSqlDropTable( String tableName ){
        return String.format("DROP TABLE IF EXISTS %s CASCADE ", tableName);
    }

    // clean table
    public DBFeedBack toClean(String tableName) {
        if( this.chkTableByName(tableName).equals(DBFeedBack.OK)) {
            return getClearData(makeSqlClearTable( tableName ));
        }
        return DBFeedBack.REFUSE;
    }

    private DBFeedBack getClearData( String sql ){
            view.write("Deleting data from table...");
        try {
            resultSet = getPrepareStatement(sql).executeQuery();
            view.write("Data deleted successfully");
            closePrepareStatement();
            return DBFeedBack.OK;
        } catch (SQLException ex) {
            view.write("Clear table is interrupted...");
            closePrepareStatement();
            return DBFeedBack.REFUSE;
        }
    }

    private String makeSqlClearTable( String tableName ){
        return String.format("DELETE FROM %s", tableName);
    }

    // find data
    public DBFeedBack toFind(String tableName, boolean isDetails, DataSet dataSet) {
        if( this.chkTableByName(tableName).equals(DBFeedBack.OK)) {
            return getFoundData(makeSqlFindData(tableName, isDetails, dataSet));
        }
        return DBFeedBack.REFUSE;
    }

    private DBFeedBack getFoundData( String sql ){
        try {
            resultSet = getPrepareStatement(sql).executeQuery();
            resultSetMeta = resultSet.getMetaData();
        } catch (SQLException ex) {
            view.write("No meta data in the result of query.");
            closePrepareStatement();
            return DBFeedBack.REFUSE;
        }

        try {
            return printFoundData();
        } catch (SQLException e) {
            e.printStackTrace();
            closePrepareStatement();
        }
        closePrepareStatement();
        return DBFeedBack.REFUSE;
    }

    private DBFeedBack printFoundData() throws SQLException {
        List<String> columnsList = new LinkedList<>();
        StringBuilder sb;

        for (int i = 1; i <= resultSetMeta.getColumnCount(); i++) {
            columnsList.add(resultSetMeta.getColumnName(i));
        }

        sb = new StringBuilder();
        sb.append(" | ");
        for (String aColumnsList : columnsList) {
            sb.append(aColumnsList).append(" | ");
        }

        view.write( sb.toString() );

        while (resultSet.next()) {
            sb = new StringBuilder();
            sb.append(" | ");
            for (String aColumnsList : columnsList) {
                sb.append(resultSet.getString(aColumnsList)).append(" | ");
            }
            view.write(sb.toString());
        }

        return DBFeedBack.OK;
    }

    private String makeSqlFindData( String tableName, boolean isDetails, DataSet dataSet ){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("SELECT * from public.%s ", tableName));

        if( isDetails ){
            sb.append(" WHERE ");
            for( DataSet.Data step : dataSet.getData() ){
                sb.append(String.format(" %s = %s AND", step.getName(), step.getValue() ) );
            }
            sb.replace(sb.length() - 3, sb.length(), " ");
        }
        return sb.toString();
    }

    // view table
    public DBFeedBack toView( boolean isDetails, boolean isOne, String tableName ) {
        if (isDetails && isOne) {
            return printOneTableDetails( tableName );
        } else if( isDetails && !isOne ){
            return  printTablesDetails();
        } else {
            return printTableList();
        }
    }

    private DBFeedBack printTablesDetails() {
        List<String> tblList = new LinkedList<>();
        String sql = "SELECT t.table_name FROM information_schema.tables t " +
                "WHERE t.table_schema = 'public'";
        try {
            tblList = getTablesList(sql);
        } catch (SQLException e) {
            view.write("Select data about table interrupted...");
            closePrepareStatement();
            return DBFeedBack.REFUSE;
        } catch (NullPointerException e) {
            view.write("getTablesList() not resolved...");
            return DBFeedBack.REFUSE;
        }

        for( String step : tblList){
            printOneTableDetails(step);
        }

        return DBFeedBack.OK;
    }

    private DBFeedBack printOneTableDetails( String tableName ) {
        StringBuilder columnString = new StringBuilder();
        String sql = String.format("SELECT c.column_name FROM information_schema.columns c " +
                "WHERE c.table_schema = 'public' AND c.table_name = \'%s\' ", tableName);
        try {
            resultSet = getPrepareStatement(sql).executeQuery();
            while (resultSet.next()) {
                columnString.append(" ").append(resultSet.getString("column_name")).append(",");
            }
        }catch( SQLException ex ){
            view.write("Select data about table interrupted...");
            closePrepareStatement();
            return DBFeedBack.REFUSE;
        }
        closePrepareStatement();
        view.write( String.format( "Table: %s , Columns: %s", tableName,
                columnString.replace(columnString.length() - 1, columnString.length(), " ").toString() ) );
        closePrepareStatement();
        return DBFeedBack.OK;
    }

    private DBFeedBack printTableList() {
        List<String> tblList = new LinkedList<>();
        String sql = "SELECT t.table_name FROM information_schema.tables t " +
                "WHERE t.table_schema = 'public'";
        try {
            tblList = getTablesList(sql);
        } catch (SQLException e) {
            view.write("Select data about table interrupted...");
            closePrepareStatement();
            return DBFeedBack.REFUSE;
        } catch (NullPointerException e) {
            view.write("getTablesList() not resolved...");
            return DBFeedBack.REFUSE;
        }
        StringBuilder tableString = new StringBuilder();
        for (String step : tblList) {
            tableString.append(" ").append(step).append(",");
        }
        view.write(String.format("Tables: %s ",
                tableString.replace(tableString.length() - 1, tableString.length(), " ").toString()));
        closePrepareStatement();
        return DBFeedBack.OK;
    }

    private List getTablesList(String sql) throws SQLException, NullPointerException {
        resultSet = getPrepareStatement(sql).executeQuery();
        List<String> tblList = new LinkedList<>();

        while (resultSet.next()) {
            tblList.add(resultSet.getString("table_name"));
        }

        closePrepareStatement();
        return tblList;
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
        if( this.chkTableByName(tableName).equals(DBFeedBack.REFUSE)){
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
            sb.append(" , ").append(step.getName()).append(" ").append(step.getValue()).append(" ");
        }
        sb.append(");");

        return sb.toString();
    }

    private DBFeedBack createTableWithParams(String sql)  {
        int resultCode = -1;
        view.write("Creating table in given database...");
        try {
            resultCode = getPrepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            view.write("CREATE TABLE Query refused.");
            view.write( e.getCause().toString() );
            return DBFeedBack.REFUSE;
        }
        if( resultCode == 0) {
            view.write("CREATE TABLE Query returned successfully.");
            closePrepareStatement();
            return DBFeedBack.OK;
        } else {
            view.write("Something wrong with Create table");
            return DBFeedBack.REFUSE;
        }
    }

    private DBFeedBack chkTableByName(String tableName) {
        String sqlStr = String.format("SELECT 1 FROM information_schema.tables WHERE table_name =  \'%s\'", tableName);
        try {
            resultSet = getPrepareStatement(sqlStr).executeQuery();
        } catch (SQLException e) {
            view.write( String.format( "%s",e.getCause( ) ) );
        }catch (NullPointerException ex){
            view.write("prepareStatement at the chkTableByName() not resolved...");
            return DBFeedBack.REFUSE;
        }

        try {
            if (resultSet.next()) {
                closePrepareStatement();
                view.write("A table has already exists");
                return DBFeedBack.OK;
            } else {
                view.write("No table found");
                return DBFeedBack.REFUSE;
            }
        } catch (SQLException e) {
            view.write( String.format( "%s",e.getCause( ) ) );
        }
        return DBFeedBack.REFUSE;
    }

    // connect to DB
    public DBFeedBack toConnect(String dbSidLine, String login ,String passwd) {

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
