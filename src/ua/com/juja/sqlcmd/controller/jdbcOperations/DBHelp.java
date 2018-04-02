package maven_sql.sqlCmd.controller.jdbcOperations;

import maven_sql.sqlCmd.controller.JdbcDbBridge;
import maven_sql.sqlCmd.types_enums.CmdLineState;
import maven_sql.sqlCmd.types_enums.EnumCmdsList;
import maven_sql.sqlCmd.viewer.View;

public class DBHelp extends DBCommand{

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("help");
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcDbBridge jdbcDbBridge, View view) {
        System.out.println("List of commands:");
        for (EnumCmdsList enumCmdsList : EnumCmdsList.values())
            view.write (String.format ( " %s : %s" ,enumCmdsList, enumCmdsList.getDescription()));
        return CmdLineState.WAIT;
    }
}
