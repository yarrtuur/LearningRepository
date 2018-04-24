package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.types_enums.ActionResult;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;

public class TableCreater implements CommandProcessable ,Preparable{
    private DBCommandManager dbManager;
    private String tableName;
    private DataSet dataSet;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("create");
    }

    @Override
    public CmdLineState process(DBCommandManager dbManager, String[] commandLine) {

        this.dbManager = dbManager;

        if( prepareCmdData( commandLine ).equals( ActionResult.ACTION_RESULT_OK ) ) {
            dbManager.toCreate(this.tableName, this.dataSet);
        }

        return CmdLineState.WAIT;
    }

    @Override
    public ActionResult prepareCmdData(String[] commandLine) {
        if( commandLine.length > 1 ){
            if(commandLine[1] != null) {
                tableName = commandLine[1];
            }
        }else {
            dbManager.getView ().write ( "There isn`t tablename at string. Try again." );
            return ActionResult.ACTION_RESULT_WRONG;
        }
        if(commandLine.length % 2 != 0){
            dbManager.getView().write("String format is wrong. Try again.");
            return ActionResult.ACTION_RESULT_WRONG;
        }else{
            dataSet = new DataSet();
            for (int i = 2; i < commandLine.length; i +=2 ) {
                dataSet.add(commandLine[i], commandLine[i+1]);
            }
        }
        return ActionResult.ACTION_RESULT_OK;
    }

}
