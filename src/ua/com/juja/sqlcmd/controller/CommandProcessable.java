package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.types_enums_except.CmdLineState;

public interface CommandProcessable {

    boolean canProcess(String singleCommand);

    CmdLineState process(DBCommandManager dbManager, String[] commandLine);


}
