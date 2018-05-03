package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.types_enums_except.ActionResult;
import ua.com.juja.sqlcmd.types_enums_except.CmdLineState;

public class DataFinder implements CommandProcessable, Preparable {
    private DBCommandManager dbManager;
    private String tableName;
    private DataSet dataSet;
    private boolean isDetails;


    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("find");
    }

    @Override
    public CmdLineState process(DBCommandManager dbManager, String[] commandLine) {
        this.dbManager = dbManager;

        if( prepareCmdData( commandLine ).equals( ActionResult.ACTION_RESULT_OK ) ) {
            dbManager.toFind(this.tableName, this.isDetails, this.dataSet);
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

        if(commandLine.length % 2 != 0 && commandLine.length > 2 ){
            dbManager.getView().write("String format is wrong. Must be even count of data. Try again.");
            return ActionResult.ACTION_RESULT_WRONG;
        }else{
            isDetails = true;
            dataSet = new DataSet();
            for (int i = 2; i < commandLine.length; i +=2 ) {
                dataSet.add(commandLine[i], commandLine[i+1]);
            }
        }
        return ActionResult.ACTION_RESULT_OK;
    }
}
