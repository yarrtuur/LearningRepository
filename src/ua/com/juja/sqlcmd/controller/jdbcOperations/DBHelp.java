package ua.com.juja.sqlcmd.controller.jdbcOperations;


import ua.com.juja.sqlcmd.controller.JdbcDbBridge;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.types_enums.EnumCmdsList;
import ua.com.juja.sqlcmd.viewer.View;

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
