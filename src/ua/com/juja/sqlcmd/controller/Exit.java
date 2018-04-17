package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;

public class Exit implements CommandProcessable {

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("exit");
    }

    @Override
    public CmdLineState process(DBCommandManager dbManager, String[] commandLine) {
        if( dbManager.toExit().equals ( DBFeedBack.OK ) ) {
            return CmdLineState.EXIT;
        }else{
            return CmdLineState.WAIT;
        }
    }
}
