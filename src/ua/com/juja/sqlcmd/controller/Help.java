package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.types_enums.DBFeedBack;

public class Help implements CommandProcessable {

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("help");
    }

    @Override
    public CmdLineState process(DBCommandManager dbManager, String[] commandLine) {

        if( dbManager.toHelp().equals ( DBFeedBack.OK ) ) {
            return CmdLineState.WAIT;
        }else{
            dbManager.getView ().write ( "Oops!" );
        }
            return CmdLineState.WAIT;
    }

}
