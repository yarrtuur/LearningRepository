package ua.com.juja.sqlcmd.controller.jdbcOperations;


import ua.com.juja.sqlcmd.controller.JdbcDbBridge;
import ua.com.juja.sqlcmd.types_enums.ActionResult;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;
import ua.com.juja.sqlcmd.viewer.View;

abstract public class DBCommand implements DBCommandable{

    @Override
    public boolean canProcess(String singleCommand) {
        return false;
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcDbBridge jdbcDbBridge, View view) {
        return null;
    }

    @Override
    public String makeSqlLine() {
        return null;
    }

    @Override
    public ActionResult getActionResult() {
        return null;
    }

    @Override
    public DBFeedBack startSqlAction(String sql) {
        return null;
    }

    @Override
    public void chkCmdData(String[] command) {

    }

}
