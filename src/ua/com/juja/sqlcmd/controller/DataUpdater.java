package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.types_enums.ActionResult;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;

public class DataUpdater implements CommandProcessable, Preparable {
    private DBCommandManager dbManager;
    private String tableName;
    private DataSet dataSetSet;
    private DataSet dataSetWhere;

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals ( "update" );
    }

    @Override
    public CmdLineState process(DBCommandManager dbManager, String[] commandLine) {
        this.dbManager = dbManager;

        if( prepareCmdData( commandLine ).equals( ActionResult.ACTION_RESULT_OK ) ) {
            this.dbManager.toUpdate(this.tableName, this.dataSetSet, this.dataSetWhere );
        }

        return CmdLineState.WAIT;
    }

    @Override
    public ActionResult prepareCmdData(String[] commandLine) {
        try {
            tableName = commandLine[1];
        } catch (IndexOutOfBoundsException ex) {
            dbManager.getView ().write ( "There isn`t tablename at string. Try again." );
            return ActionResult.ACTION_RESULT_WRONG;
        }

        if (commandLine.length % 2 != 0 && commandLine.length > 2) {
            dbManager.getView ().write ( "String format is wrong. Must be even count of data. Try again." );
            return ActionResult.ACTION_RESULT_WRONG;
        } else {
            int where = 0, set = 0;
            for (int i = 0; i < commandLine.length; i++) {
                if( commandLine[i].equals ( "set" )) {set = i;}
                if( commandLine[i].equals ( "where" )) {where = i; break;}
            }

            dataSetSet = new DataSet ();
            for (int i = set+1; i < where; i += 2) {
                dataSetSet.add ( commandLine[i], commandLine[i + 1] );
            }
            dataSetWhere = new DataSet ();
            for (int i = where + 1; i < commandLine.length; i += 2) {
                dataSetWhere.add ( commandLine[i], commandLine[i + 1] );
            }
        }
        return ActionResult.ACTION_RESULT_OK;
    }
}
