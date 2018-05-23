package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.DataSet;
import ua.com.juja.yar_tur.sqlcmd.model.PrepareCmdLine;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.PrepareResult;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

public class DataUpdater implements CommandProcess, PrepareCmdLine {
    private DBCommandManager dbManager;
    private View view;
    private String tableName;
    private DataSet dataSetSet;
    private DataSet dataSetWhere;

    public DataUpdater(DBCommandManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return (singleCommand.equals("update") && dbManager.getConnection().isConnected());
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        FeedBack resultCode = FeedBack.REFUSE;
        if (prepareCmdData(commandLine).equals(PrepareResult.PREPARE_RESULT_OK)) {
            try {
                view.write("Updating data.");
                resultCode = this.dbManager.toUpdate(this.tableName, this.dataSetSet, this.dataSetWhere);
                dbManager.closePrepareStatement();
            } catch (SQLException ex) {
                view.write("Update data is interrupted...");
                view.write(ex.getMessage());
            }
        }
        if( resultCode.equals(FeedBack.OK) ) {
            view.write("Update data successfull");
        } else {
            view.write("Something wrong with update data...");
        }
        return CmdLineState.WAIT;
    }

    @Override
    public PrepareResult prepareCmdData(String[] commandLine) {
        if(commandLine.length > 1){
            tableName = commandLine[1];
        } else {
            view.write("There isn`t tablename at string. Try again.");
            return PrepareResult.PREPARE_RESULT_WRONG;
        }
        if (commandLine.length % 2 != 0 && commandLine.length > 2) {
            view.write("String format is wrong. Must be even count of data. Try again.");
            return PrepareResult.PREPARE_RESULT_WRONG;
        } else {
            int where = 0, set = 0;
            for (int i = 0; i < commandLine.length; i++) {
                if (commandLine[i].toLowerCase().equals("set")) {
                    set = i;
                }
                if (commandLine[i].toLowerCase().equals("where")) {
                    where = i;
                    break;
                }
            }
            dataSetSet = new DataSet();
            for (int i = set + 1; i < where; i += 2) {
                dataSetSet.add(commandLine[i], commandLine[i + 1]);
            }
            dataSetWhere = new DataSet();
            for (int i = where + 1; i < commandLine.length; i += 2) {
                dataSetWhere.add(commandLine[i], commandLine[i + 1]);
            }
        }
        return PrepareResult.PREPARE_RESULT_OK;
    }
}
