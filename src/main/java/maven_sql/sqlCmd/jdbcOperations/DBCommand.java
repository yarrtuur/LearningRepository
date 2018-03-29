package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.controller.JdbcDbBridge;
import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.CmdLineState;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

abstract public class DBCommand implements DBCommandable{

    @Override
    public boolean canProcess(String singleCommand) {
        return false;
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcDbBridge jdbcDbBridge) {
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
