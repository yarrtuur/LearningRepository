package ua.com.juja.sqlcmd.controller.jdbcOperations;


import ua.com.juja.sqlcmd.controller.JdbcDbBridge;
import ua.com.juja.sqlcmd.types_enums.ActionResult;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;
import ua.com.juja.sqlcmd.viewer.View;

public interface DBCommandable {

    String makeSqlLine();

    ActionResult getActionResult();

    DBFeedBack startSqlAction(String sql);

    void chkCmdData(String[] command);

    boolean canProcess(String singleCommand);

    CmdLineState process(String[] commandLine, JdbcDbBridge jdbcDbBridge, View view);

}
