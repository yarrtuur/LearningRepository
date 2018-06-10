package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.PrepareCmdLine;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.PrepareResult;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

public class TableCleaner implements CommandProcess, PrepareCmdLine {
    private DBCommandManager dbManager;
    private View view;
    private String tableName;

    public TableCleaner(DBCommandManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return (singleCommand.equals("clear") && dbManager.getConnection().isConnected());
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        FeedBack resultCode = FeedBack.REFUSE;
        if (prepareCmdData(commandLine).equals(PrepareResult.PREPARE_RESULT_OK)) {
            try {
                view.write("Clearing table...");
                resultCode = dbManager.toClean(tableName);
                dbManager.closePrepareStatement();
            } catch (SQLException ex) {
                view.write("Clear table is interrupted.");
                view.write(ex.getMessage());
            }
        }
        if( resultCode.equals(FeedBack.OK)) {
            view.write("Clear table successfull");
        } else {
            view.write("Something wrong with Clear data");
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
        return PrepareResult.PREPARE_RESULT_OK;
    }
}
