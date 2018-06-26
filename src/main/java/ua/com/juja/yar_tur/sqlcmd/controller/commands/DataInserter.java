package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.utils.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.utils.DataContainer;
import ua.com.juja.yar_tur.sqlcmd.utils.ExitException;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

public class DataInserter implements CommandProcess, PrepareCmdLine, PrepareCommandData {
    private DBCommandManager dbManager;
    private View view;
    private DataContainer dataContainer;

    public DataInserter(DBCommandManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
        this.dataContainer = new DataContainer();
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return (singleCommand.startsWith("insert") && dbManager.getConnection().isConnected());
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        try {
            prepareCmdData(commandLine);
            int insertCount = dbManager.toInsert(dataContainer);
            view.write(String.format("Insert data into table successfull for %d rows", insertCount));
        } catch (SQLException | ExitException ex) {
            view.write(ex.getMessage());
        }
        return CmdLineState.WAIT;
    }

    @Override
    public void prepareCmdData(String[] commandLine) throws ExitException {
        dataContainer.setTableName(getTableName(commandLine));
        dataContainer.setDataSet(getFieldsParams(commandLine));
    }

}
