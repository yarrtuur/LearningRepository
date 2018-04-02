package maven_sql.sqlCmd.controller.jdbcOperations;

import maven_sql.sqlCmd.controller.JdbcDbBridge;
import maven_sql.sqlCmd.types_enums.CmdLineState;
import maven_sql.sqlCmd.viewer.View;

public class Unreachable extends DBCommand {
    @Override
    public boolean canProcess(String singleCommand) {
        return true;
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcDbBridge jdbcDbBridge, View view) {
        view.write ( "Not available command. Please type `help` to list all commands ");
        return CmdLineState.WAIT;
    }
}
