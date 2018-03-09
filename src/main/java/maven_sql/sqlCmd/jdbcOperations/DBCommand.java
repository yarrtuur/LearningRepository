package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.types_enums.ActionResult;

import java.sql.Connection;

abstract public class DBCommand {
    static Connection connection = null;

    abstract String makeSqlLine();

    public abstract ActionResult getActionResult();
}
