package maven_sql.sqlCmd.jdbcOperations;

import maven_sql.sqlCmd.controller.JdbcDbBridge;
import maven_sql.sqlCmd.types_enums.CmdLineState;
import maven_sql.sqlCmd.types_enums.EnumCmdsList;

public class DBHelp extends DBCommand{
    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("help");
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcDbBridge jdbcDbBridge) {
        System.out.println("List of commands:");
        for (EnumCmdsList enumCmdsList : EnumCmdsList.values())
            System.out.println(enumCmdsList + ": " + enumCmdsList.getDescription());
        return CmdLineState.WAIT;
    }
}
