package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.types_enums.ActionResult;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;

public class TableCleaner implements CommandProcessable,Preparable {
    private DBCommandManager dbManager;
    private String tableName;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("clear");
    }

    @Override
    public CmdLineState process(DBCommandManager dbManager, String[] commandLine) {

        this.dbManager = dbManager;

        if( prepareCmdData( commandLine ).equals( ActionResult.ACTION_RESULT_OK ) ) {
            dbManager.toClean( tableName);
        }

        return CmdLineState.WAIT;
    }

    @Override
    public ActionResult prepareCmdData(String[] commandLine) {
        try {
            tableName = commandLine[1];
        } catch (IndexOutOfBoundsException ex) {
            dbManager.getView().write("There isn`t tablename at string. Try again.");
            return ActionResult.ACTION_RESULT_WRONG;
        }
        return ActionResult.ACTION_RESULT_OK;
    }

}
