package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

public class Exit implements CommandProcess {
    private DBCommandManager dbManager;
    View view;

    public Exit(DBCommandManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return (singleCommand.equals("exit") && dbManager.getConnection().isConnected());
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        FeedBack lineState;
        view.write("Closing connection...");
        try {
            lineState = dbManager.toExit();
            view.write("Connection closed.");
        } catch (SQLException e) {
            view.write("Close connection interrupted.");
            e.printStackTrace();
            lineState = FeedBack.REFUSE;
        }
        return lineState.equals(FeedBack.OK) ? CmdLineState.EXIT : CmdLineState.WAIT;
    }
}
