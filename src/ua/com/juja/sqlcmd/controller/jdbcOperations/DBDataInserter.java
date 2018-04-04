package ua.com.juja.sqlcmd.controller.jdbcOperations;

import ua.com.juja.sqlcmd.controller.JdbcDbBridge;
import ua.com.juja.sqlcmd.types_enums.ActionResult;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;
import ua.com.juja.sqlcmd.viewer.View;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBDataInserter extends DBCommand {
    private Map<String, String> mapColumnsData;
    private int stmtResult;
    private String tblName;
    private JdbcDbBridge jdbcDbBridge;
    private View view;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals ( "insert" );
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcDbBridge jdbcDbBridge, View view) {
        this.mapColumnsData = null;
        this.stmtResult = -1;
        this.tblName = null;
        this.jdbcDbBridge = jdbcDbBridge;
        this.view = view;
        chkCmdData ( commandLine );
        view.write ( this.startSqlAction ( this.makeSqlLine () ).toString () );
        return CmdLineState.WAIT;
    }

    @Override
    public DBFeedBack startSqlAction(String sql) {
        if (!jdbcDbBridge.isConnected ()) {
            view.write ( "Not connected to DB." );
            return DBFeedBack.REFUSE;
        }
        try (
                PreparedStatement preparedStatement = jdbcDbBridge.getConnection ().prepareStatement ( sql )
        ) {
            view.write ( "Inserting data to table in given database..." );
            stmtResult = preparedStatement.executeUpdate ();
            view.write ( "insert data into table is done " );
            return DBFeedBack.OK;
        } catch (SQLException ex) {
            view.write ( "insert data into  table is interrupted in given database..." );
            //ex.printStackTrace();
            return DBFeedBack.REFUSE;
        }

    }

    @Override
    public ActionResult getActionResult() {
        return (stmtResult == 0) ? ActionResult.ACTION_RESULT_OK : ActionResult.ACTION_RESULT_WRONG;
    }

    @Override
    public String makeSqlLine() {
        StringBuilder sb = new StringBuilder ();
        sb.append ( "INSERT INTO " ).append ( tblName ).append ( " ( " );
        StringBuilder columns = new StringBuilder ();
        StringBuilder values = new StringBuilder ();
        for (Map.Entry<String, String> step : mapColumnsData.entrySet ()) {
            columns.append ( step.getKey () ).append ( "," );
            values.append ( "\'" ).append ( step.getValue () ).append ( "\'," );
        }

        columns.replace ( columns.length () - 1, columns.length (), ")" );
        values.replace ( values.length () - 1, values.length (), ")" );
        sb.append ( columns ).append ( " values ( " ).append ( values ).append ( ";" );
        return sb.toString ();
    }

    @Override
    public void chkCmdData(String[] command) {
        mapColumnsData = new HashMap<> ();
        try {
            tblName = command[1];
        } catch (IndexOutOfBoundsException ex) {
            view.write ( "Command string format is wrong. Try again." );
        }
        try {
            for (int i = 2; i < command.length; i = i + 2) {
                mapColumnsData.put ( command[i], command[i + 1] );
            }
        } catch (IndexOutOfBoundsException ex) {
            view.write ( "There is no column to insert. Try again." );
        }
    }
}
