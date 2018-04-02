package maven_sql.sqlCmd.controller.jdbcOperations;

import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

public class DBDataDeleter extends DBCommand {
    public DBDataDeleter(String[] command) {
        System.out.println("Make me!");
    }

    @Override
    public void chkCmdData(String[] command) {
        super.chkCmdData(command);
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
    public DBFeedBack startSqlAction(String sql){return null;};
}
