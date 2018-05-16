package ua.com.juja.yar_tur.sqlcmd.controller;

import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.DataSet;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.PrepareResult;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

public class DataInserter implements CommandProcess, PrepareCmdLine {
    private DBCommandManager dbManager;
    private View view;
    private String tableName;
    private DataSet dataSet;

    public DataInserter(DBCommandManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("insert");
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        FeedBack resultCode = FeedBack.REFUSE;
        if (prepareCmdData(commandLine).equals(PrepareResult.PREPARE_RESULT_OK)) {
            try {
                view.write("Inserting data.");
                dbManager.toInsert(tableName, dataSet);
            } catch (SQLException ex) {
                view.write("Insert data into table interrupted.");
                view.write(ex.getCause().toString());
            }
        }
        if( resultCode.equals(FeedBack.OK) ) {
            view.write("Insert data into table successfull");
            dbManager.closePrepareStatement();
        } else {
            view.write("Something wrong with insert data into table.");
            dbManager.closePrepareStatement();
        }
        return CmdLineState.WAIT;
    }

    @Override
    public PrepareResult prepareCmdData(String[] commandLine) {
        try {
            tableName = commandLine[1];
        } catch (IndexOutOfBoundsException ex) {
            view.write("There isn`t tablename at string. Try again.");
            return PrepareResult.PREPARE_RESULT_WRONG;
        }

        if (commandLine.length % 2 != 0) {
            view.write("String format is wrong. Must be even count of data. Try again.");
            return PrepareResult.PREPARE_RESULT_WRONG;
        } else {
            dataSet = new DataSet();
            for (int i = 2; i < commandLine.length; i += 2) {
                dataSet.add(commandLine[i], commandLine[i + 1]);
            }
        }
        return PrepareResult.PREPARE_RESULT_OK;
    }

}
