package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.types_enums.CmdLineState;

public class DBDataDeleter implements CommandProcessable {
    public DBDataDeleter(String[] command) {
        //TODO
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return false;
    }

    @Override
    public CmdLineState process(DBCommandManager dbManager, String[] commandLine) {
        return null;
    }


}
