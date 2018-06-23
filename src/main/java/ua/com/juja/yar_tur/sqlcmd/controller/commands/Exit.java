package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

public class Exit implements CommandProcess {
    private DBCommandManager dbManager;
    private View view;

    public Exit(DBCommandManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.startsWith("exit");
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        try {
            dbManager.toExit();
            view.write("Connection closed.");
        } catch (SQLException ex) {
            view.write(ex.getMessage() );
        }
        return CmdLineState.EXIT;
    }
}
