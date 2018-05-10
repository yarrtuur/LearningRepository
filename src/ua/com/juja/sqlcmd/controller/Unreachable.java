package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.types_enums_except.CmdLineState;

public class Unreachable implements CommandProcessable {
    @Override
    public boolean canProcess(String singleCommand) {
        return true;
    }

    @Override
    public CmdLineState process(DBCommandManager dbManager, String[] commandLine) {
        dbManager.getView().write("Not available command. Please type `help` to list all commands ");
        return CmdLineState.WAIT;
    }
}
