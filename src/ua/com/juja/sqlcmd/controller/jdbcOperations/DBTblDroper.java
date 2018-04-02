package ua.com.juja.sqlcmd.controller.jdbcOperations;


import ua.com.juja.sqlcmd.controller.JdbcDbBridge;
import ua.com.juja.sqlcmd.types_enums.ActionResult;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;
import ua.com.juja.sqlcmd.viewer.View;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBTblDroper extends DBCommand {
    private int stmtResult ;
    private String tblName ;
    private JdbcDbBridge jdbcDbBridge;
    private View view;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("drop");
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcDbBridge jdbcDbBridge, View view) {
        this.stmtResult = -1;
        this.tblName = null;
        this.jdbcDbBridge = jdbcDbBridge;
        this.view = view;
        chkCmdData(commandLine);
        view.write(this.startSqlAction(this.makeSqlLine()).toString ());
        return CmdLineState.WAIT;
    }

    @Override
    public DBFeedBack startSqlAction(String sql){
        if ( !jdbcDbBridge.isConnected() ){
            view.write("Not connected to DB.");
            return DBFeedBack.REFUSE;
        }
        try (
            PreparedStatement preparedStatement = jdbcDbBridge.getConnection().prepareStatement(sql)
                ){
            view.write("Droping table from given database...");
            stmtResult = preparedStatement.executeUpdate();
            view.write("table deleted successfully");
            return DBFeedBack.OK;
        }catch(SQLException ex){
            view.write("Drop table is lost in given database...");
            ex.printStackTrace();
            return DBFeedBack.REFUSE;
        }
    }

    @Override
    public String makeSqlLine() {
        return String.format("DROP TABLE %s",tblName);
    }

    @Override
    public void chkCmdData(String[] command) {
        try{
            tblName = command[1];
        }catch(IndexOutOfBoundsException ex){
            view.write("Command string format is wrong. Try again.");
        }
    }

    @Override
    public ActionResult getActionResult() {
        return ( stmtResult == 0 ) ?  ActionResult.ACTION_RESULT_OK : ActionResult.ACTION_RESULT_WRONG;
    }
}
