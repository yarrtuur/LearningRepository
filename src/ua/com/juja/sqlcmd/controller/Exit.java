package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.types_enums.CmdLineState;

public class Exit implements CommandProcessable {

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("exit");
    }

    @Override
    public CmdLineState process(DBCommandManager dbManager, String[] commandLine) {
        dbManager.toExit();
        return CmdLineState.EXIT;
    }
}
