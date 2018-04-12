package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.types_enums.CmdLineState;

import java.sql.SQLException;

public class DBExit  implements CommandProcessable {

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("exit");
    }

    @Override
    public CmdLineState process(DBCommandManager dbManager, String[] commandLine) {

        try {
            if (dbManager.getJdbcBridge().isConnected()) {
                dbManager.getJdbcBridge().getConnection().close();
                dbManager.getJdbcBridge().setConnection(null);
            }
        } catch (SQLException ex) {
            /*NOP*/
        }
        dbManager.getView().write("You are disconnected now. Bye...");
        return CmdLineState.EXIT;
    }
}
