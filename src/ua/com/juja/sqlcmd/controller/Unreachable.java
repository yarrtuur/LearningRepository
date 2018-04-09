package ua.com.juja.sqlcmd.controller;


import ua.com.juja.sqlcmd.model.JdbcBridge;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.viewer.View;

public class Unreachable  implements CommandProcessable {
    @Override
    public boolean canProcess(String singleCommand) {
        return true;
    }

    @Override
    public CmdLineState process(String[] commandLine, JdbcBridge jdbcBridge, View view) {
        view.write("Not available command. Please type `help` to list all commands ");
        return CmdLineState.WAIT;
    }
}
