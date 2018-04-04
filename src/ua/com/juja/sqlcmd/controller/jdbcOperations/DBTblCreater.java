package ua.com.juja.sqlcmd.controller.jdbcOperations;


import ua.com.juja.sqlcmd.controller.JdbcDbBridge;
import ua.com.juja.sqlcmd.types_enums.ActionResult;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;
import ua.com.juja.sqlcmd.viewer.View;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DBTblCreater extends DBCommand {
    private JdbcDbBridge jdbcDbBridge;
    private int stmtResult;
    private List<String> listColumn;
    private String tblName;
    private PreparedStatement preparedStatement;
    private View view;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals ( "create" );
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcDbBridge jdbcDbBridge, View view) {
        this.jdbcDbBridge = jdbcDbBridge;
        this.stmtResult = -1;
        this.listColumn = new LinkedList<> ();
        this.tblName = null;
        this.view = view;

        chkCmdData ( commandLine );
        view.write ( this.startSqlAction ( this.makeSqlLine () ).toString () );
        return CmdLineState.WAIT;
    }

    @Override
    public void chkCmdData(String[] commandLine) {
        try {
            tblName = commandLine[1];
        } catch (IndexOutOfBoundsException ex) {
            view.write ( "Command string format is wrong. Try again." );
            return;
        }
        try {
            listColumn.addAll ( Arrays.asList ( commandLine ).subList ( 2, commandLine.length ) );
        } catch (IndexOutOfBoundsException ex) {
            view.write ( "There is no column to create table. Try again." );
        }
    }

    @Override
    public DBFeedBack startSqlAction(String sql) {
        if (!jdbcDbBridge.isConnected ()) {
            view.write ( "Not connected to DB." );
            return DBFeedBack.REFUSE;
        }
        try {
            DBFeedBack chkTableAvailable = searchTableByName ();
            if (chkTableAvailable.equals ( DBFeedBack.REFUSE )) {
                view.write ( String.format ( "Table %s already exists.", tblName ) );
                return chkTableAvailable;
            } else {
                return createTableWithParams ( sql );
            }
        } catch (SQLException ex) {
            view.write ( "Create table is interrupted in given database..." );
            ex.printStackTrace ();
            return DBFeedBack.REFUSE;
        }
    }

    private DBFeedBack createTableWithParams(String sql) throws SQLException {
        view.write ( "Creating table in given database..." );
        preparedStatement = jdbcDbBridge.getConnection ().prepareStatement ( sql );
        stmtResult = preparedStatement.executeUpdate ();
        view.write ( "CREATE TABLE Query returned successfully" );
        preparedStatement.close ();
        return DBFeedBack.OK;
    }

    private DBFeedBack searchTableByName() throws SQLException {
        String sqlstr = String.format ( "SELECT 1 FROM information_schema.tables WHERE table_name =  \'%s\'", tblName );
        preparedStatement = jdbcDbBridge.getConnection ().prepareStatement ( sqlstr );
        ResultSet resultSet = preparedStatement.executeQuery ();
        if (resultSet.next ()) {
            preparedStatement.close ();
            return DBFeedBack.REFUSE;
        } else {
            return DBFeedBack.OK;
        }
    }

    @Override
    public String makeSqlLine() {
        StringBuilder sb = new StringBuilder ();
        sb.append ( "CREATE TABLE IF NOT EXISTS " ).append ( tblName )
                .append ( " ( rid serial CONSTRAINT id_" ).append ( tblName ).append ( "_pk PRIMARY KEY " );
        for (String aListColumnData : listColumn) {
            sb.append ( "," ).append ( aListColumnData ).append ( " varchar(200) " );
        }
        sb.append ( ");" );
        return sb.toString ();
    }

    @Override
    public ActionResult getActionResult() {
        return (stmtResult == 0) ? ActionResult.ACTION_RESULT_OK : ActionResult.ACTION_RESULT_WRONG;
    }

}
