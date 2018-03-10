package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

import java.sql.Connection;
import java.sql.Statement;

abstract public class DBCommand {
    static Connection connection = null;
    Statement stmt = null;

    abstract String makeSqlLine(String[] command);

    public abstract ActionResult getActionResult();

    abstract DBFeedBack sqlAction(String sql);
}
