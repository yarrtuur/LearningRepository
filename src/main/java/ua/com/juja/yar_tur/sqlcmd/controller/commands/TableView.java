package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.utils.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

public class TableView implements CommandProcess, PrepareCmdLine, PrepareCommandData {
    private DBCommandManager dbManager;
    private View view;
    private String tableName;

    public TableView(DBCommandManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return (singleCommand.startsWith("tables") && dbManager.getConnection().isConnected());
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        try {
            prepareCmdData(commandLine);
            dbManager.toView(tableName);
        } catch (SQLException ex) {
            view.write(ex.getMessage());
        }
        return CmdLineState.WAIT;
    }

    public void prepareCmdData(String[] commandLine) {
        tableName = getTableName(commandLine);
    }

    @Override
    public String getTableName(String[] commandLine) {
        if (commandLine.length > 1) {
            return commandLine[1];
        }
        return null;
    }

}
