package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.DataSet;
import ua.com.juja.yar_tur.sqlcmd.model.PrepareCmdLine;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.PrepareResult;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DataFinder implements CommandProcess, PrepareCmdLine {
    private DBCommandManager dbManager;
    private View view;
    private String tableName;
    private DataSet dataSet;
    private boolean isDetails;
    private ResultSet resultSet;
    private ResultSetMetaData resultSetMeta;

    public DataFinder(DBCommandManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return (singleCommand.equals("find") && dbManager.getConnection().isConnected());
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        if (prepareCmdData(commandLine).equals(PrepareResult.PREPARE_RESULT_OK)) {
            try {
                resultSet = dbManager.toFind(tableName, isDetails, dataSet);
                resultSetMeta = resultSet.getMetaData();
                printFoundData();
            } catch (SQLException e) {
                view.write("No data in the result of query.");
                view.write(e.getCause().toString());
            }
        }
        return CmdLineState.WAIT;
    }

    private void printFoundData() throws SQLException {
        List<String> columnsList = new LinkedList<>();
        StringBuilder sb;
        for (int i = 1; i <= resultSetMeta.getColumnCount(); i++) {
            columnsList.add(resultSetMeta.getColumnName(i));
        }
        sb = new StringBuilder();
        sb.append(" | ");
        for (String aColumnsList : columnsList) {
            sb.append(aColumnsList).append(" | ");
        }
        view.write( sb.toString() );
        while (resultSet.next()) {
            sb = new StringBuilder();
            sb.append(" | ");
            for (String aColumnsList : columnsList) {
                sb.append(resultSet.getString(aColumnsList)).append(" | ");
            }
            view.write(sb.toString());
        }
    }

    @Override
    public PrepareResult prepareCmdData(String[] commandLine) {
        try {
            tableName = commandLine[1];
        } catch (IndexOutOfBoundsException ex) {
            view.write("There isn`t tablename at string. Try again.");
            return PrepareResult.PREPARE_RESULT_WRONG;
        }
        if (commandLine.length % 2 != 0 && commandLine.length > 2) {
            view.write("String format is wrong. Must be even count of data. Try again.");
            return PrepareResult.PREPARE_RESULT_WRONG;
        } else {
            isDetails = true;
            dataSet = new DataSet();
            for (int i = 2; i < commandLine.length; i += 2) {
                dataSet.add(commandLine[i], commandLine[i + 1]);
            }
        }
        return PrepareResult.PREPARE_RESULT_OK;
    }
}
