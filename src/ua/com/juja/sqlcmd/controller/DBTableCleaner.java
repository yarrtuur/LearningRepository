package ua.com.juja.sqlcmd.controller;


import ua.com.juja.sqlcmd.model.JdbcBridge;
import ua.com.juja.sqlcmd.types_enums.ActionResult;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;
import ua.com.juja.sqlcmd.viewer.View;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBTableCleaner  implements CommandProcessable {
    private int stmtResult;
    private String tblName;
    private JdbcBridge jdbcBridge;
    private View view;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("clear");
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcBridge jdbcBridge, View view) {
        this.stmtResult = -1;
        this.tblName = null;
        this.jdbcBridge = jdbcBridge;
        this.view = view;
        chkCmdData(commandLine);
        view.write(this.startSqlAction(this.makeSqlLine()).toString());
        return CmdLineState.WAIT;
    }

    @Override
    public DBFeedBack startSqlAction(String sql) {
        if (jdbcBridge.isConnected()) {
            view.write("Not connected to DB.");
            return DBFeedBack.REFUSE;
        }
        try (
                PreparedStatement preparedStatement = jdbcBridge.getConnection().prepareStatement(sql)
        ) {
            view.write("Deleting data from table in given database...");
            stmtResult = preparedStatement.executeUpdate();
            view.write("data deleted successfully");
            return DBFeedBack.OK;
        } catch (SQLException ex) {
            view.write("Create table is lost in given database...");
            ex.printStackTrace();
            return DBFeedBack.REFUSE;
        }
    }

    @Override
    public String makeSqlLine() {
        return String.format("DELETE FROM %s", tblName);
    }

    @Override
    public void chkCmdData(String[] command) {
        try {
            tblName = command[1];
        } catch (IndexOutOfBoundsException ex) {
            view.write("Command string format is wrong. Try again.");
        }
    }

    @Override
    public ActionResult getActionResult() {
        return (stmtResult == 0) ? ActionResult.ACTION_RESULT_OK : ActionResult.ACTION_RESULT_WRONG;
    }
}
