package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.jdbcOperations.DBCommand;
import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

public class DBDataInserter extends DBCommand {
    public DBDataInserter(String[] command) {
        System.out.println("Make me!");
    }
    @Override
    public ActionResult getActionResult() {
        return null;
    }
    @Override
    String makeSqlLine(String[] command) {
        return null;
    }
    @Override
    DBFeedBack sqlAction(String sql){return null;};
}
