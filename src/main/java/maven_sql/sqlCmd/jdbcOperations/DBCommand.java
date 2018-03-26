package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

import java.sql.Connection;
import java.sql.PreparedStatement;

abstract public class DBCommand implements DBCommandable{
    static Connection connection = null;

    PreparedStatement preparedStatement = null;

    public String makeSqlLine(){return null;};

    public ActionResult getActionResult(){return null;};

    public DBFeedBack startSqlAction(String sql){return null;};

    public void chkCmdData(String[] command){};
}
