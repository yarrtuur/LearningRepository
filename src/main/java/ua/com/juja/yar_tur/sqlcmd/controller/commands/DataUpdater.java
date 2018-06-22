package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.DataSet;
import ua.com.juja.yar_tur.sqlcmd.model.PrepareCmdLine;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.ExitException;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.PrepareResult;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        return (singleCommand.startsWith("update") && dbManager.getConnection().isConnected());
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
    public void prepareCmdData(String[] commandLine) throws ExitException {
        chkAndGetTableName(commandLine);
        chkAndGetFieldsParams(commandLine);
    }

    private void chkAndGetFieldsParams(String[] commandLine) throws ExitException {
        //todo make collection instead array
        List<String> cmdList = new ArrayList<String> (Arrays.asList(commandLine));
        if (cmdList.size() % 2 != 0 && cmdList.size() > 2) {
            throw new ExitException("String format is wrong. Must be even count of data. Try again.");
        } else {
            int set = cmdList.indexOf("set");
            int where = cmdList.indexOf("where");
            dataSetSet = new DataSet();
            for (int i = set + 1; i < where; i += 2) {
                dataSetSet.add(cmdList.get(i), cmdList.get(i + 1));
            }
            dataSetWhere = new DataSet();
            for (int i = where + 1; i < commandLine.length; i += 2) {
                dataSetWhere.add(cmdList.get(i), cmdList.get(i));
            }
        }
    }

    private void chkAndGetTableName(String[] commandLine) throws ExitException {
        if(commandLine.length > 1){
            tableName = commandLine[1];
        } else {
            throw new ExitException("There isn`t tablename at string. Try again.");
        }
    }
}
