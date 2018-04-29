package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.types_enums.ActionResult;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;

public class TableViewer implements CommandProcessable, Preparable {
    private DBCommandManager dbManager;
    private String tableName;
    private boolean isDetails;
    private boolean isOne;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("tables");
    }

    @Override
    public CmdLineState process(DBCommandManager dbManager, String[] commandLine) {

        this.dbManager = dbManager;

        if( prepareCmdData( commandLine ).equals( ActionResult.ACTION_RESULT_OK ) ) {
            dbManager.toView( isDetails, isOne, tableName);
        }

        return CmdLineState.WAIT;
    }

    @Override
    public ActionResult prepareCmdData(String[] commandLine) {
        if (commandLine.length > 1 && commandLine[1].equals("fields")) {
            isDetails = true;
            isOne = false;
        } else if(commandLine.length > 1 && !commandLine[1].equals("fields")){
            isDetails = true;
            isOne = true;
            this.tableName = commandLine[1];
        } else if ( commandLine.length == 1 ) {
            isDetails = false;
            isOne = false;
        }

        return ActionResult.ACTION_RESULT_OK;
    }

}
