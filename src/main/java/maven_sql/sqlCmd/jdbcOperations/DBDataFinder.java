package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.jdbcOperations.DBCommand;
import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

public class DBDataFinder extends DBCommand {
    public DBDataFinder(String[] command) {
        System.out.println("Make me!");
    }

    @Override
    String makeSqlLine(String[] command) {
        return null;
    }

    @Override
    public ActionResult getActionResult() {
        return null;
    }
    @Override
    DBFeedBack sqlAction(String sql){return null;};

}
