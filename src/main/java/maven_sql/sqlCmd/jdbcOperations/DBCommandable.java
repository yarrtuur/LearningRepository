package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.types_enums.ActionResult;
import maven_sql.sqlCmd.types_enums.CmdLineState;
import maven_sql.sqlCmd.types_enums.DBFeedBack;

public interface DBCommandable {

    String makeSqlLine();

    ActionResult getActionResult();

    DBFeedBack startSqlAction(String sql);

    void chkCmdData(String[] command);

    boolean canProcess(String singleCommand);

    CmdLineState process(String[] commandLine);

}
