package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.utils.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.utils.DataContainer;
import ua.com.juja.yar_tur.sqlcmd.utils.ExitException;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

public class DataFind implements CommandProcess, PrepareCmdLine, PrepareCommandData {
    private DBCommandManager dbManager;
    private View view;
    private DataContainer dataContainer;

    public DataFind(DBCommandManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
        dataContainer = new DataContainer();
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return (singleCommand.startsWith("find") && dbManager.getConnection().isConnected());
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        try {
            prepareCmdData(commandLine);
            dbManager.toFind(dataContainer);
            view.write("Find data successfull");
        } catch (SQLException | ExitException e) {
            view.write(e.getMessage());
        }
        return CmdLineState.WAIT;
    }

    @Override
    public void prepareCmdData(String[] commandLine) throws ExitException {
        dataContainer.setTableName(getTableName(commandLine));
        dataContainer.setDataSetWhere(getFieldsParams(commandLine));
    }

}
