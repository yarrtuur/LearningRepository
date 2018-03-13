package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

public class DBTblCleaner extends DBCommand {
    public DBTblCleaner(String[] args) {
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
    public DBFeedBack sqlAction(String sql){return null;};
}
