package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.DataSet;
import ua.com.juja.yar_tur.sqlcmd.model.PrepareCmdLine;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.ExitException;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

public class DataInserter implements CommandProcess, PrepareCmdLine, PrepareCommandData {
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
        return (singleCommand.startsWith("insert") && dbManager.getConnection().isConnected());
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        try {
            prepareCmdData(commandLine);
            dbManager.toInsert(tableName, dataSet);
        } catch (SQLException | ExitException ex) {
            view.write(ex.getMessage());
        }
        return CmdLineState.WAIT;
    }

    @Override
    public void prepareCmdData(String[] commandLine) throws ExitException {
        tableName = chkAndGetTableName(commandLine);
        dataSet = chkAndGetFieldsParams(commandLine);
    }

}
