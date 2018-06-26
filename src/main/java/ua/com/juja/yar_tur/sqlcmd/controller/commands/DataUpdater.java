package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.DataSet;
import ua.com.juja.yar_tur.sqlcmd.utils.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.utils.DataContainer;
import ua.com.juja.yar_tur.sqlcmd.utils.ExitException;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataUpdater implements CommandProcess, PrepareCmdLine, PrepareCommandData {
    private DBCommandManager dbManager;
    private View view;
    private DataSet dataSetSet;
    private DataSet dataSetWhere;
    private DataContainer dataContainer;

    public DataUpdater(DBCommandManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
        dataContainer = new DataContainer();
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return (singleCommand.startsWith("update") && dbManager.getConnection().isConnected());
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        try {
            prepareCmdData(commandLine);
            dbManager.toUpdate(dataContainer);
            view.write("Update data successfull");
        } catch (SQLException | ExitException ex) {
            view.write(ex.getMessage());
        }
        return CmdLineState.WAIT;
    }

    @Override
    public void prepareCmdData(String[] commandLine) throws ExitException {
        dataContainer.setTableName(getTableName(commandLine));
        getFieldsUpdateParams(commandLine);
        dataContainer.setDataSetSet(dataSetSet);
        dataContainer.setDataSetWhere(dataSetWhere);
    }


    private void getFieldsUpdateParams(String[] commandLine) throws ExitException {
        if (commandLine.length % 2 != 0 && commandLine.length > 2) {
            throw new ExitException("String format is wrong. Must be even count of data. Try again.");
        } else {
            fillDataSets(commandLine);
        }
    }

    private void fillDataSets(String[] commandLine) throws ExitException {
        List<String> cmdList = new ArrayList<>(Arrays.asList(commandLine));
        int set = cmdList.indexOf("set");
        int where = cmdList.indexOf("where");

        String[] setCommandLine = Arrays.copyOfRange(commandLine, set, where);
        dataSetSet = getFieldsParams(setCommandLine);

        String[] whereCommandLine = Arrays.copyOfRange(commandLine, where, commandLine.length);
        dataSetWhere = getFieldsParams(whereCommandLine);
    }


}
