package ua.com.juja.sqlcmd.controller.jdbcOperations;


import ua.com.juja.sqlcmd.types_enums.ActionResult;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;

public class DBUpdater extends DBCommand {
    public DBUpdater(String[] command) {
        //TODO
    }

    @Override
    public ActionResult getActionResult() {
        return null;
    }

    @Override
    public void chkCmdData(String[] command) {
        super.chkCmdData ( command );
    }

    @Override
    public String makeSqlLine() {
        return null;
    }

    @Override
    public DBFeedBack startSqlAction(String sql) {
        return null;
    }
}
