package ua.com.juja.sqlcmd.controller.jdbcOperations;


import ua.com.juja.sqlcmd.types_enums.ActionResult;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;

public class DBDataDeleter extends DBCommand {
    public DBDataDeleter(String[] command) {
        //TODO
    }

    @Override
    public void chkCmdData(String[] command) {
        super.chkCmdData ( command );
    }

    @Override
    public ActionResult getActionResult() {
        return null;
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
