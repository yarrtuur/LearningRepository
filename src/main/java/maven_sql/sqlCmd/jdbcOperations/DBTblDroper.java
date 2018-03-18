package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.jdbcOperations.DBCommand;
import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

public class DBTblDroper extends DBCommand {
    public DBTblDroper(String[] command) {
        System.out.println("Make me!");
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
