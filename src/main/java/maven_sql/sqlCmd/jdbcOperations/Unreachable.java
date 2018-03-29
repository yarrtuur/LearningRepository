package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.controller.JdbcDbBridge;
import maven_sql.sqlCmd.types_enums.CmdLineState;

public class Unreachable extends DBCommand {
    @Override
    public boolean canProcess(String singleCommand) {
        return true;
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcDbBridge jdbcDbBridge) {
        System.out.println("Not available command. Please type `help` to list all commands ");
        return CmdLineState.WAIT;
    }
}
