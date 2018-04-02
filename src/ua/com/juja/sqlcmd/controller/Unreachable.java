package ua.com.juja.sqlcmd.controller;


import ua.com.juja.sqlcmd.controller.jdbcOperations.DBCommand;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.viewer.View;

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
