package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.types_enums_except.ExitException;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;

import java.sql.*;
import java.util.List;

public class JDBCDatabaseManager implements DBCommandManager {

    private ConnectionKeeper connectionKeeper = new ConnectionKeeper();
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public ConnectionKeeper getConnectionKeeper() {
        return connectionKeeper;
    }

    // open PreparedStatement TODO
    public PreparedStatement getPrepareStatement(String sql) {
        preparedStatement = null;
        try {
            preparedStatement = getConnectionKeeper().getConnection().prepareStatement(sql);
        } catch (SQLException | NullPointerException e) {
            //e.printStackTrace();
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

    // exit
    @Override
    public FeedBack toExit() throws SQLException{
        if( connectionKeeper.isConnected () ){
            connectionKeeper.getConnection ().close ();
            connectionKeeper.setConnection ( null );
        }else{
            connectionKeeper.setConnection ( null );
        }
        return FeedBack.OK;
    }

    // update table
    @Override
    public FeedBack toUpdate(String tableName, DataSet dataSetSet, DataSet dataSetWhere) throws SQLException {
        if( this.chkTableByName(tableName).equals(FeedBack.OK)  && connectionKeeper.isConnected() ){
            return (getExecuteUpdateData(makeSqlUpdateData(tableName, dataSetSet, dataSetWhere)) == 1)
                    ? FeedBack.OK : FeedBack.REFUSE;
        }else {
            return FeedBack.REFUSE;
        }
    }

    private String makeSqlUpdateData(String tableName, DataSet dataSetSet, DataSet dataSetWhere) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("UPDATE public.%s ", tableName));
        sb.append ( " SET " );
        for(DataSet.Data step : dataSetSet.getData () ){
            sb.append ( String.format ( " %s = %s , ", step.getName(), step.getValue() ) );
        }
        sb.replace(sb.length() - 3, sb.length(), " ");
        sb.append ( " WHERE " );
        for(DataSet.Data step : dataSetWhere.getData () ){
            sb.append ( String.format ( " %s = %s AND ", step.getName(), step.getValue() ) );
        }
        sb.replace(sb.length() - 4, sb.length(), " ");

        return sb.toString();
    }

    // delete data
    @Override
    public FeedBack toDelete(String tableName, DataSet dataSet)throws SQLException {
        if( this.chkTableByName(tableName).equals(FeedBack.OK)  && connectionKeeper.isConnected()) {
            return (getExecuteUpdateData(makeSqlDeleteData(tableName, dataSet)) == 1) ? FeedBack.OK : FeedBack.REFUSE;
        }else{
            return FeedBack.REFUSE;
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

    // drop table
    @Override
    public FeedBack toDrop(String tableName)throws SQLException {
        if( this.chkTableByName(tableName).equals(FeedBack.OK) && connectionKeeper.isConnected()) {
            return (getExecuteUpdateData(makeSqlDropTable( tableName )) == 1) ? FeedBack.OK : FeedBack.REFUSE;
        }else {
            return FeedBack.REFUSE;
        }
    }

    private String makeSqlDropTable( String tableName ){
        return String.format("DROP TABLE IF EXISTS %s CASCADE ", tableName);
    }

    // clean table
    @Override
    public FeedBack toClean(String tableName)throws SQLException {
        if( this.chkTableByName(tableName).equals(FeedBack.OK) && connectionKeeper.isConnected()) {
            return (getExecuteUpdateData(makeSqlClearTable( tableName )) == 1) ? FeedBack.OK : FeedBack.REFUSE;
        }else {
            return FeedBack.REFUSE;
        }
    }

    private String makeSqlClearTable( String tableName ){
        return String.format("DELETE FROM %s ", tableName);
    }

    // find data
    @Override
    public ResultSet toFind(String tableName, boolean isDetails, DataSet dataSet)throws SQLException{
        if( this.chkTableByName(tableName).equals(FeedBack.OK) && connectionKeeper.isConnected()) {
             getFoundData(makeSqlFindData(tableName, isDetails, dataSet));
        }else{
            throw new ExitException("Something wrong with found table");
        }
        return resultSet;
    }

    private FeedBack getFoundData(String sql )throws SQLException{
        resultSet = getPrepareStatement(sql).executeQuery();
        if(resultSet != null){
            return FeedBack.OK;
        }
        return FeedBack.REFUSE;
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
    @Override
    public ResultSet toView(String tableName) throws SQLException{
        if( connectionKeeper.isConnected()) {
            return getOneTableDetails(tableName);
        }else{
            throw new ExitException("Something wrong with view table");
        }
    }

    @Override
    public ResultSet toView() throws SQLException{
        if( connectionKeeper.isConnected()) {
            return getTablesList();
        }else{
            throw new ExitException("Something wrong with view table");
        }
    }

    private ResultSet getOneTableDetails(String tableName)throws SQLException{
        String sql = String.format("SELECT c.column_name FROM information_schema.columns c " +
                "WHERE c.table_schema = 'public' AND c.table_name = \'%s\' ", tableName);
        return getPrepareStatement(sql).executeQuery();
    }

    private ResultSet getTablesList() throws SQLException{
        String sql = "SELECT t.table_name FROM information_schema.tables t " +
                "WHERE t.table_schema = 'public'";
        return getPrepareStatement(sql).executeQuery();
    }

    // insert into  table
    @Override
    public FeedBack toInsert(String tableName, DataSet dataSet) throws SQLException {
        if (this.chkTableByName(tableName).equals(FeedBack.OK) && connectionKeeper.isConnected()) {
            return (getExecuteUpdateData(makeSqlInsertData(tableName, dataSet)) == 1) ? FeedBack.OK : FeedBack.REFUSE;
        }else {
            return FeedBack.REFUSE;
        }
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
        columns.replace(columns.length() - 2, columns.length(), ")");
        values.replace(values.length() - 2, values.length(), ")");
        sb.append(columns).append(" values ( ").append(values).append(";");

        return sb.toString();
    }

    //create table
    @Override
    public FeedBack toCreate(String tableName, DataSet dataSet) throws SQLException{
        if( this.chkTableByName(tableName).equals(FeedBack.REFUSE ) && connectionKeeper.isConnected() ){
            return (getExecuteUpdateData(makeSqlCreateTable(tableName, dataSet)) == 1) ? FeedBack.OK : FeedBack.REFUSE;
        }else {
            return FeedBack.REFUSE;
        }
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

    private FeedBack chkTableByName(String tableName)throws SQLException {
        String sqlStr = String.format("SELECT 1 FROM information_schema.tables WHERE table_name =  \'%s\'", tableName);
        resultSet = getPrepareStatement(sqlStr).executeQuery();
        if (resultSet.next()) {
            closePrepareStatement();
            return FeedBack.OK;
        } else {
            closePrepareStatement();
            return FeedBack.REFUSE;
        }
    }

    // connect to DB
    @Override
    public FeedBack toConnect(String dbSidLine, String login, String passwd) throws SQLException {
        connectionKeeper.setConnection(DriverManager.getConnection(dbSidLine, login, passwd));
        return (connectionKeeper.isConnected()) ? FeedBack.OK : FeedBack.REFUSE;
    }

    /**
     * universal method for exequteUpdate sql all same commands
     * */
    private int getExecuteUpdateData(String sql) throws SQLException{
        return getPrepareStatement(sql).executeUpdate();
    }

}
