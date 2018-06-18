package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

public class ChkConnection implements CommandProcess {
    private DBCommandManager dbManager;
    private View view;

    public ChkConnection(DBCommandManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return (singleCommand.startsWith("chkconn"));
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        if(dbManager.getConnection().isConnected()){
            view.write("Connection has already done");
        }else{
            view.write("Connection hasnn`t done yet");
        }
        return CmdLineState.WAIT;
    }
}
