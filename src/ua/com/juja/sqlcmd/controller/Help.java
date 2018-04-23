package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.types_enums.CmdLineState;

public class Help implements CommandProcessable {

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("help");
    }

    @Override
    public CmdLineState process(DBCommandManager dbManager, String[] commandLine) {
        dbManager.toHelp();
        return CmdLineState.WAIT;
    }
}
