package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.DataSet;
import ua.com.juja.yar_tur.sqlcmd.model.PrepareCmdLine;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.ExitException;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

public class TableCreater implements CommandProcess, PrepareCmdLine {
    private DBCommandManager dbManager;
    private View view;
    private String tableName;
    private DataSet dataSet;

    public TableCreater(DBCommandManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return (singleCommand.startsWith("create") && dbManager.getConnection().isConnected());
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        try {
            prepareCmdData(commandLine);
            dbManager.toCreate(tableName, dataSet);
        } catch (SQLException | ExitException ex) {
            view.write(ex.getMessage());
        }
        return CmdLineState.WAIT;
    }


    @Override
    public void prepareCmdData(String[] commandLine) throws ExitException {
        chkAndGetTableName(commandLine);
        chkAndGetFieldsParams(commandLine);
    }

    private void chkAndGetFieldsParams(String[] commandLine) throws ExitException {
        if (commandLine.length % 2 != 0) {
            throw new ExitException("String format is wrong. Try again.");
        } else {
            dataSet = new DataSet();
            for (int i = 2; i < commandLine.length; i += 2) {
                dataSet.add(commandLine[i], commandLine[i + 1]);
            }
        }
    }

    private void chkAndGetTableName(String[] commandLine) throws ExitException {
        if (commandLine.length > 1) {
            tableName = commandLine[1];
        } else {
            throw new ExitException("There isn`t tablename at string. Try again.");
        }
    }

}
